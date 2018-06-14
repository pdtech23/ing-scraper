package challange;

import challange.ing.scraper.IIngScraper;
import challange.ing.scraper.IngScraper;
import challange.user.experience.ConsoleUI;
import challange.user.experience.IConsoleUI;

public class Main {

  public static void main(String[] args) {
    IConsoleUI ui = new ConsoleUI();
    IIngScraper ingScraper = new IngScraper();
    FetchAccountsUseCase fetchAccounts = new FetchAccountsUseCase(ui, ingScraper);
    fetchAccounts.execute();
  }
}
