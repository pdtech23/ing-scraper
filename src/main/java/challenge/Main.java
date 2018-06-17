package challenge;

import challenge.scraper.Scraper;
import challenge.scraper.ing.IngScraper;
import challenge.user.experience.ConsoleUI;
import challenge.user.experience.UI;

public class Main {

  public static void main(String[] args) {
    UI ui = new ConsoleUI();
    Scraper ingScraper = new IngScraper();
    FetchAccountsUseCase fetchAccounts = new FetchAccountsUseCase(ui, ingScraper);
    fetchAccounts.execute();
  }
}
