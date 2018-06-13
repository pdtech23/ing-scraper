package scrapper;

import scrapper.ing.AccountDataExtractionService;
import scrapper.ing.client.DataDownloaderService;
import scrapper.ing.client.response.ResponseDataExtractor;
import scrapper.user.experience.ConsoleUserInterface;

public class Main {

    public static void main(String[] args) {

        ConsoleUserInterface ui = new ConsoleUserInterface();

        ResponseDataExtractor extractor = new ResponseDataExtractor();
        DataDownloaderService dataProvider = new DataDownloaderService(extractor);

        AccountDataExtractionService scrapper = new AccountDataExtractionService(ui, dataProvider);

        scrapper.displayAccountDataWithUserInteraction();
    }

}
