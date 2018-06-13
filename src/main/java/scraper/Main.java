package scraper;

import scraper.ing.DisplayAccountsUseCase;
import scraper.ing.client.IngScraper;
import scraper.user.experience.ConsoleUserInterface;

public class Main {

  public static void main(String[] args) {
    ConsoleUserInterface ui = new ConsoleUserInterface();
    IngScraper ingScraper = new IngScraper();
    DisplayAccountsUseCase useCase = new DisplayAccountsUseCase(ui, ingScraper);
    useCase.displayAccountsWithUserInteraction();
  }
}
