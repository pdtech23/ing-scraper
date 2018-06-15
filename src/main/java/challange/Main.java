package challange;

import challange.scraper.Scraper;
import challange.scraper.ing.IngScraper;
import challange.user.experience.ConsoleUI;
import challange.user.experience.UI;

public class Main {

  public static void main(String[] args) {
    UI ui = new ConsoleUI();
    Scraper ingScraper = new IngScraper();
    FetchAccountsUseCase fetchAccounts = new FetchAccountsUseCase(ui, ingScraper);
    fetchAccounts.execute();
  }
}
