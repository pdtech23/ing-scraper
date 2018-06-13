package scrapper;

import scrapper.ing.PresentAccounts;
import scrapper.ing.client.DownloadDataClient;
import scrapper.ing.client.response.ResponseHandler;
import scrapper.user.experience.ConsoleUserInterface;

public class Main {

    public static void main(String[] args) {

        ConsoleUserInterface ui = new ConsoleUserInterface();

        ResponseHandler extractor = new ResponseHandler();
        DownloadDataClient downloader = new DownloadDataClient(extractor);

        PresentAccounts scrapper = new PresentAccounts(ui, downloader);

        scrapper.displayAccountDataWithUserInteraction();
    }

}
