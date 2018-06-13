package scrapper.ing;

import scrapper.ing.client.DownloadDataClient;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.UnauthenticatedSession;
import scrapper.user.experience.ConsoleUserInterface;

import java.util.List;

public class PresentAccounts {

    private final ConsoleUserInterface userInterface;
    private final DownloadDataClient downloadDataClient;

    public PresentAccounts(ConsoleUserInterface userInterface, DownloadDataClient downloadDataClient) {
        this.userInterface = userInterface;
        this.downloadDataClient = downloadDataClient;
    }

    public void displayAccountDataWithUserInteraction() {
        userInterface.displayWelcomeMessage();

        String login = userInterface.askUserForLogin();

        UnauthenticatedSession unauthenticatedSession = downloadDataClient.createUnauthenticatedSession(login)
                .orElseThrow(() -> new RuntimeException("Cannot connect with bank"));

        List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
                (unauthenticatedSession.mask);
        char[] password = userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);

        AuthenticatedSession authenticatedSession = downloadDataClient.createAuthenticatedSession(login, password,
                unauthenticatedSession).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        userInterface.printAccounts(downloadDataClient.getAccountsInfo(authenticatedSession));
    }
}
