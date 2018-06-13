package scrapper.ing;

import scrapper.ing.client.DataDownloaderService;
import scrapper.ing.security.AuthenticatedSession;
import scrapper.ing.security.PasswordBehaviorHandler;
import scrapper.ing.security.UnauthenticatedSession;
import scrapper.user.experience.ConsoleUserInterface;

import java.util.List;

public class AccountDataExtractionService {

    private final ConsoleUserInterface userInterface;
    private final DataDownloaderService dataDownloaderService;

    public AccountDataExtractionService(ConsoleUserInterface userInterface, DataDownloaderService dataDownloaderService) {
        this.userInterface = userInterface;
        this.dataDownloaderService = dataDownloaderService;
    }

    public void displayAccountDataWithUserInteraction() {
        userInterface.displayWelcomeMessage();

        String login = userInterface.askUserForLogin();

        if (login.isEmpty()) {
            userInterface.displayFailureMessage();
            return;
        }

        UnauthenticatedSession unauthenticatedSession = dataDownloaderService.createUnauthenticatedSession(login)
                .orElseThrow(() -> new RuntimeException("Cannot connect with bank"));

        List<Integer> positionsOfRevealedCharacters = PasswordBehaviorHandler.extractPositionsOfRevealedCharacters
                (unauthenticatedSession.mask);
        char[] password = userInterface.askUserForNeededPasswordCharacters(positionsOfRevealedCharacters);

        if (password.length == 0) {
            userInterface.displayFailureMessage();
            return;
        }

        AuthenticatedSession authenticatedSession = dataDownloaderService.createAuthenticatedSession(login, password,
                unauthenticatedSession).orElseThrow(() -> new RuntimeException("Invalid credentials"));

        userInterface.printAccounts(dataDownloaderService.getAccountsInfo(authenticatedSession));
    }
}
