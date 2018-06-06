package scrapper.ing;

import scrapper.ing.client.ConnectionProxyService;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.security.SessionData;
import scrapper.ing.user.experience.ConsoleUserInterface;

import java.util.List;

public class AccountDataExtractionService {

    private final ConsoleUserInterface userInterface;
    private final ConnectionProxyService dataProvider;

    public AccountDataExtractionService(ConsoleUserInterface ui, ConnectionProxyService dataProvider) {
        this.userInterface = ui;
        this.dataProvider = dataProvider;
    }

    public void downloadAccountDataWithUserInteraction() {
        this.userInterface.displayWelcomeMessage();

        String login = this.userInterface.askUserForLogin();

        if (login.equals("")) {
            return;
        }

        PasswordMetadata passwordMetadata = this.dataProvider.doFirstLogInStep(login);

        if (passwordMetadata.equals(PasswordMetadata.EMPTY)) {
            return;
        }

        List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
                (passwordMetadata.getMask());
        char[] password = this.userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);

        if (password.length == 0) {
            return;
        }

        SessionData session = this.dataProvider.createAuthenticatedSession(login, password, passwordMetadata);

        if (session == SessionData.EMPTY) {
            this.userInterface.displayFailureMessage();
        } else {
            this.userInterface.printAccounts(this.dataProvider.getAccountsInfo(session));
        }
    }
}
