package scrapper.ing;

import scrapper.ing.client.ConnectionProxyService;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.ing.user.experience.ConsoleUserInterface;

public class AppEntryPoint {

    public static void main(String[] args) {

        ConsoleUserInterface ui = new ConsoleUserInterface();
        ResponseDataExtractor extractor = new ResponseDataExtractor();
        ConnectionProxyService dataProvider = new ConnectionProxyService(extractor);

        AccountDataExtractionService scrapper = new AccountDataExtractionService(ui, dataProvider);

        scrapper.downloadAccountDataWithUserInteraction();
    }

}
