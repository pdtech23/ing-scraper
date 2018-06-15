package challange;

import challange.ing.scraper.Scraper;
import challange.ing.scraper.IngScraper;
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
