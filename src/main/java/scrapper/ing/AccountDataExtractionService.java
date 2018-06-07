package scrapper.ing;

import scrapper.ing.client.DataDownloaderService;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.UnauthenticatedSession;
import scrapper.ing.user.experience.ConsoleUserInterface;

import java.util.List;

class AccountDataExtractionService {

    private final ConsoleUserInterface userInterface;
    private final DataDownloaderService dataDownloaderService;

    AccountDataExtractionService(ConsoleUserInterface ui, DataDownloaderService dataDownloaderService) {
        this.userInterface = ui;
        this.dataDownloaderService = dataDownloaderService;
    }

    void displayAccountDataWithUserInteraction() {
        this.userInterface.displayWelcomeMessage();

        String login = this.userInterface.askUserForLogin();

        if (login.isEmpty()) {
            this.userInterface.displayFailureMessage();
            return;
        }

        UnauthenticatedSession unauthenticatedSession = this.dataDownloaderService.createUnauthenticatedSession(login);

        if (unauthenticatedSession.isEmpty()) {
            this.userInterface.displayFailureMessage();
            return;
        }

        List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
                (unauthenticatedSession.getMask());
        char[] password = this.userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);

        if (password.length == 0) {
            this.userInterface.displayFailureMessage();
            return;
        }

        AuthenticatedSession session = this.dataDownloaderService.createAuthenticatedSession(login, password,
                unauthenticatedSession);

        if (session.isEmpty()) {
            this.userInterface.displayFailureMessage();
        } else {
            this.userInterface.printAccounts(this.dataDownloaderService.getAccountsInfo(session));
        }
    }
}
