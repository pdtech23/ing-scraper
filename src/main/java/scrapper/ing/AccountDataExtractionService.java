package scrapper.ing;

import scrapper.ing.client.DataDownloaderService;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.PasswordMetadata;
import scrapper.ing.user.experience.ConsoleUserInterface;

import java.util.List;

class AccountDataExtractionService {

    private final ConsoleUserInterface userInterface;
    private final DataDownloaderService dataProvider;

    AccountDataExtractionService(ConsoleUserInterface ui, DataDownloaderService dataProvider) {
        this.userInterface = ui;
        this.dataProvider = dataProvider;
    }

    void displayAccountDataWithUserInteraction() {
        this.userInterface.displayWelcomeMessage();

        String login = this.userInterface.askUserForLogin();

        if (login.isEmpty()) {
            this.userInterface.displayFailureMessage();
            return;
        }

        PasswordMetadata passwordMetadata = this.dataProvider.doFirstLogInStep(login);

        if (passwordMetadata.isEmpty()) {
            this.userInterface.displayFailureMessage();
            return;
        }

        List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
                (passwordMetadata.getMask());
        char[] password = this.userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);

        if (password.length == 0) {
            this.userInterface.displayFailureMessage();
            return;
        }

        AuthenticatedSession session = this.dataProvider.createAuthenticatedSession(login, password, passwordMetadata);

        if (session.isEmpty()) {
            this.userInterface.displayFailureMessage();
        } else {
            this.userInterface.printAccounts(this.dataProvider.getAccountsInfo(session));
        }
    }
}
