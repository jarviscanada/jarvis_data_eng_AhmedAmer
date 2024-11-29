package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;

import java.util.List;
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
        System.out.print("\n\n\n\n\n");
        System.out.println("Please select an option by inputting its number: ");
        System.out.println("1 - Look up and buy new stock.");
        System.out.println("2 - View and sell owned stock.");
        System.out.println("3 - View all your stock positions.");
        System.out.println("4 - Exit the application.");

    }

    public void buyMenu() {
        String input;
        Optional<Quote> quoteOptional;
        Double price;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.print("Buy Menu: \n");
            System.out.println("Enter a ticker symbol - this is the symbol for the stock you wish to view.");
            System.out.println("To return to the main menu, type back");
            input = scanner.nextLine();
            if (input.equals("back")) {
                break;
            }
            quoteOptional = quoteService.fetchQuoteDataFromAPI(input);
            if (quoteOptional.isPresent()) {
                Quote quote = quoteOptional.get();
                price = quote.getPrice();
                System.out.print("\n\n\n\n\n");
                System.out.printf("Quote for %s: \n", quote.getTicker());
                System.out.println(quote);
                System.out.print("\nWould you like to purchase this stock? Type yes or no.");
                input = scanner.nextLine();
                if (input.equals("yes")) {
                    System.out.print("\n\nHow many shares of this stock do you want to purchase? enter the amount below:");
                    int amount = scanner.nextInt();
                    System.out.print("\nPurchasing...");
                    positionService.buy(quote.getTicker(), amount, price);
                }
            } else {
                System.out.print("\n\nYou need to enter a valid stock symbol. Try again!");
            }
        } while (quoteOptional.isEmpty());
    }

    public void sellMenu() {
        String input;
        Optional<Position> posOptional;
        Optional<Quote> quoteOptional;
        Double price;
        Double netProfit;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.print("Sell Menu: \n");
            System.out.println("Enter a ticker symbol for the stock you wish to sell.");
            System.out.println("To return to the main menu, type back");
            input = scanner.nextLine();
            if (input.equals("back")) {
                break;
            }
            posOptional = positionService.fetchPosition(input);
            if (posOptional.isPresent()) {
                Position position = posOptional.get();
                quoteOptional = quoteService.fetchQuoteDataFromAPI(position.getTicker());
                if (quoteOptional.isEmpty()) {
                    System.out.println("Could not retrieve updated stock information at this moment!");
                    System.out.println("Fetching latest information from database if available!");
                    quoteOptional = quoteService.fetchQuoteDataFromDB(position.getTicker());
                    if (quoteOptional.isEmpty()) {
                       System.out.println("\nCould not find any information in database!");
                       System.out.println("Returning to main menu!");
                       break;
                    }
                }
                price = quoteOptional.get().getPrice();
                netProfit = price*position.getNumOfShares() - position.getValuePaid();
                System.out.print("\n" + "You currently own some shares!\n" + position);
                System.out.print("\n\n\nIf you sold your shares now your profits would be " + netProfit + "!");
                System.out.print("\nWould you like to sell this stock? Type yes or no.");
                input = scanner.nextLine();
                if (input.equals("yes")) {
                    System.out.println("Selling stock...");
                    positionService.sell(position.getTicker());
                }
            } else {
                System.out.println("\nInvalid entry. Please enter the ticker symbol of the stock you wish to sell.");
                System.out.println("Note that you must own shares of this stock to sell it!");
            }
        } while (posOptional.isEmpty());
    }

    public void allOwnedStock() {
        List<Position> positions = positionService.fetchAllPositions();
        System.out.print("\n\n\n\n\n");
        System.out.print("Here is the list of all the stock you own currently: \n");
        for (Position position : positions) {
            System.out.println(position + "\n");
        }
        System.out.print("Returning to main menu!");
    }
}
