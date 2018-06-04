package scrapper.ing.userInterface;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import scrapper.ing.client.IngClientDataExtractorService;
import scrapper.ing.client.ResponseData;
import scrapper.ing.utils.PasswordUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConsoleUserInterface {
    private final IngClientDataExtractorService clientDataExtractor;

    private boolean isFinished;

    public ConsoleUserInterface() {
        this.isFinished = false;
        this.clientDataExtractor = new IngClientDataExtractorService();
    }

    private void displayWelcomeMessage() {
        System.out.println("Hello, we will try to get your account data here!");
        System.out.println("(ps. we only support ING Bank... for now)");
    }

    private String getUserLogin() throws IOException {
        System.out.println("Give me Your login please:");
        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        return bufferedReader.readLine();
    }


    // TODO: Move hashing logic outside
    public void interactWithUser() throws JSONException, IOException {
        this.displayWelcomeMessage();
        String login = this.getUserLogin();

        ResponseData responseData = this.clientDataExtractor.getResponseForLoginCheck(login);
        JSONObject data = (JSONObject) responseData.getJsonBody().get("data");
        String sessionId = this.extractSessionIdToSet(responseData);

        String salt = data.getString("salt");
        String mask = data.getString("mask");
        String key = data.getString("key");

        List<Integer> revealedPasswordCharactersIndexes = PasswordUtils.extractStarIndexesFromMask(mask);
        List<Character> password = askUserForNeededPasswordCharacters(revealedPasswordCharactersIndexes);

        String saltWithPassword = PasswordUtils.mixSaltAndPasswordAsInJS(PasswordUtils.putMaskOnSalt(mask, salt), password.iterator());
        String passwordHash = HmacUtils.hmacSha1Hex(key, saltWithPassword);

        ResponseData loggedInResponseData = this.clientDataExtractor.attemptLogIn(login, passwordHash, sessionId);
        String token = loggedInResponseData.getJsonBody().getJSONObject("data").getString("token");
        sessionId = this.extractSessionIdToSet(loggedInResponseData);

        ResponseData accountsInfoResponse = this.clientDataExtractor.getAccountsInfo(sessionId, token);
        JSONObject accounts = accountsInfoResponse.getJsonBody().getJSONObject("data");
        JSONArray cur = accounts.getJSONArray("cur");
        printAccounts(cur);
        JSONArray sav = accounts.getJSONArray("sav");
        printAccounts(sav);
        isFinished = true;

    }

    private void printAccounts(JSONArray cur) throws JSONException {
        for (int i = 0; i < cur.length(); i++) {
            JSONObject o = (JSONObject) cur.get(i);
            String accountNumber = o.getString("acct");
            Double availableBalance = o.getDouble("avbal");
            String currency = o.getString("curr");
            String name = o.getString("name");
            System.out.println("Account " + name + " no. " + accountNumber + "; available balance: " + availableBalance + " " + currency);
        }
    }

    private String extractSessionIdToSet(ResponseData responseData) {
        Optional<Header> sessionHeader = Arrays.stream(responseData.getHeaders()).filter(
                header -> header.getName().equals("Set-Cookie")).filter(cookieHeader -> cookieHeader.getValue().contains("JSESSIONID")).findFirst();
        if (!sessionHeader.isPresent()) {
            throw new RuntimeException();
        }
        String header = sessionHeader.get().getValue();
        int i = header.indexOf("=") + 1;
        int j = header.indexOf(";");
        return header.substring(i, j);
    }

    private List<Character> askUserForNeededPasswordCharacters(List<Integer> indexesOfRevealedCharacters) throws IOException {
        List<Character> passphrase = new ArrayList<>();

        for (Integer single : indexesOfRevealedCharacters) {
            System.out.println("Give me character no. " + (single + 1) + " of your password:");

            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            passphrase.add((char) bufferedReader.read());
        }
        return passphrase;
    }

    public boolean isNotDone() {
        return !this.isFinished;
    }
}
