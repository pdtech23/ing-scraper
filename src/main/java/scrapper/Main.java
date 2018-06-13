package scrapper;

import scrapper.ing.PresentAccounts;
import scrapper.ing.client.Connection;
import scrapper.ing.client.response.ResponseHandler;
import scrapper.user.experience.ConsoleUserInterface;

public class Main {

  public static void main(String[] args) {
    ConsoleUserInterface ui = new ConsoleUserInterface();
    ResponseHandler extractor = new ResponseHandler();
    Connection downloader = new Connection(extractor);
    PresentAccounts scrapper = new PresentAccounts(ui, downloader);
    scrapper.displayAccountsWithUserInteraction();
  }

}
