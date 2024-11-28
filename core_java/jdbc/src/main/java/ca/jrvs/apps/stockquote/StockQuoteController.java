package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {
    Scanner scanner = new Scanner(System.in);
    private final QuoteService quoteService;
    private final PositionService positionService;

    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        int option;
        System.out.print("Welcome to the Stock Quote App main menu!\n\n");
        do {
            mainMenu();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    buyMenu();
                    break;
                case 2:
                    sellMenu();
                    break;
                case 3:
                    allOwnedStock();
                    break;
                case 4:
                    System.out.println("Exiting. Thank you for using the Stock Quote Application.");
                    break;
                default:
                    System.out.println("Invalid option. Please input the number of the service you need!");
            }
        } while (option != 4);

        scanner.close();
    }

    public void mainMenu() {
        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("Please select an option by inputting its number: ");
        System.out.println("1 - Look up and buy new stock.");
        System.out.println("2 - View and sell owned stock.");
        System.out.println("3 - View all your stock positions.");
        System.out.println("4 - Exit the application.");

    }

    public void buyMenu() {
        String input;
        Optional<Quote> fetchedOptional;
        Double price = 0.0;
        do {
            System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            System.out.print("Quote Menu: \n\n");
            System.out.println("Enter a ticker symbol - this is the symbol for the stock you wish to view.\n");
            System.out.println("To return to the main menu, type quit");
            input = scanner.nextLine();
            if (input.equals("quit")) {
                break;
            }
            fetchedOptional = quoteService.fetchQuoteDataFromAPI(input);
            if (fetchedOptional.isPresent()) {
                Quote quote = fetchedOptional.get();
                price = quote.getPrice();
                System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n");
                System.out.printf("Quote for %s: \n\n", quote.getTicker());
                System.out.println(quote);
                System.out.print("\n\nWould you like to purchase this stock? Type yes or no.");
                input = scanner.nextLine();
                if (input.equals("yes")) {
                    System.out.print("\n\nHow many shares of this stock do you want to purchase? enter the amount below:");
                    int amount = scanner.nextInt();
                    System.out.print("\nPurchasing...");
                    positionService.buy(quote.getTicker(), amount, price);
                }
            }
        } while (fetchedOptional.isEmpty());
    }

    public void sellMenu() {}
    public void allOwnedStock() {}
}
