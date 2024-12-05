package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StockQuoteController {
    Scanner scanner = new Scanner(System.in);
    private final QuoteService quoteService;
    private final PositionService positionService;
    private final Logger infoLogger = LoggerFactory.getLogger("infoLogger");
    private final Logger errorLogger = LoggerFactory.getLogger("errorLogger");


    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }

    /**
     * User interface for our application
     */
    public void initClient() {
        int option;
        System.out.print("\n\n\nWelcome to the Stock Quote App main menu!");
        do {
            mainMenu();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    infoLogger.info("User navigated to buy menu.");
                    buyMenu();
                    break;
                case 2:
                    infoLogger.info("User navigated to sell menu.");
                    sellMenu();
                    break;
                case 3:
                    infoLogger.info("User asked to print all owned stock.");
                    allOwnedStock();
                    break;
                case 4:
                    infoLogger.info("User asked to exit application.");
                    System.out.println("Exiting. Thank you for using the Stock Quote Application.");
                    break;
                default:
                    System.out.println("Invalid option. Please input the number of the service you need!");
            }
        } while (option != 4);

        scanner.close();
    }

    public void mainMenu() {
        System.out.print("\n\n\n");
        System.out.println("Please select an option by inputting its number: ");
        System.out.println("1 - Look up and buy new stock.");
        System.out.println("2 - View and sell owned stock.");
        System.out.println("3 - View all your stock positions.");
        System.out.println("4 - Exit the application.");

    }

    public void buyMenu() {
        String input;
        Optional<Quote> quoteOptional;
        boolean exit = false;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.print("Buy Menu: \n");
            System.out.println("Enter a ticker symbol - this is the symbol for the stock you wish to view.");
            System.out.println("To return to the main menu, type back");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("back")) {
                exit = true;
                System.out.print("\nHeading back to main menu!");
                infoLogger.info("User navigated to main menu.");
            } else {
                infoLogger.info("User asked to fetch quote for {}", input);
                try {
                    quoteOptional = quoteService.fetchQuoteDataFromAPI(input);
                    if (quoteOptional.isPresent()) {
                        Quote quote = quoteOptional.get();
                        buyMenuStockFound(quote);
                    } else {
                        errorLogger.error("User entered invalid stock symbol");
                        System.out.print("\n\nYou need to enter a valid stock symbol. Try again!");
                    }
                } catch (NoSuchElementException e) {
                    System.out.print("\n\nAPI Error: Possibly due to API call limit. " +
                            "\nPlease try again in about 2 minutes when the call limit has subsided!");
                    errorLogger.error("User reached API call limit in buy menu.");
                    break;
                }
            }
        } while (!exit);
    }

    public void buyMenuStockFound(Quote quote) {
        String input;
        boolean exit = false;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.printf("Quote for %s: \n", quote.getTicker());
            System.out.println(quote);
            System.out.print("\nWould you like to purchase this stock? Type yes or no.");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) {
                processBuy(quote);
                exit = true;
            } else if (input.equals("no")) {
                System.out.println("\nReturning to buy menu.");
                exit = true;
            } else {
                System.out.println("\nPlease enter 'yes' or 'no'!");
            }
        } while (!exit);
    }

    public void processBuy(Quote quote) {
        boolean validNumOfShares = false;
        double price = quote.getPrice();
        do {
            System.out.print("\n\nHow many shares of this stock do you want to purchase? enter the amount below:");
            try {
                int amount = Integer.parseInt(scanner.nextLine().trim());
                if (amount < 0) {
                    System.out.println("\nPlease enter a positive integer!");
                    errorLogger.error("User entered invalid number of shares: {}", amount);
                    continue;
                }
                System.out.print("\nPurchasing...");
                positionService.buy(quote.getTicker(), amount, price);
                infoLogger.info("User purchased {} shares of {} at the price: {}", amount, quote.getTicker(), price);
                System.out.printf("\nYou have purchased %d shares of %s for the price: $%.2f per share", amount, quote.getTicker(), price);
                System.out.print("\nReturning to buy menu.");
                validNumOfShares = true;
            } catch (InputMismatchException e) {
                System.out.println("\nInvalid entry. Please enter a valid integer smaller than 10 digits.");
                errorLogger.error("Invalid integer entered by user", e);
            }
        } while (!validNumOfShares);
    }

    public void sellMenu() {
        String input;
        Optional<Position> posOptional;
        boolean exit = false;
        do {
            System.out.print("\n\n\n");
            System.out.print("Sell Menu: \n");
            System.out.println("Enter a ticker symbol for the stock you wish to sell.");
            System.out.println("To return to the main menu, type back");
            input = scanner.nextLine();
            if (input.equals("back")) {
                exit = true;
                System.out.print("\nHeading back to main menu!");
                infoLogger.info("User navigated to main menu.");
            }
            posOptional = positionService.fetchPosition(input);
            if (posOptional.isPresent()) {
                Position position = posOptional.get();
                sellMenuPositionFound(position);
            } else {
                System.out.println("\nInvalid entry or no shares available to sell. " +
                        "\nPlease enter the ticker symbol of the stock you wish to sell.");
                System.out.println("Note: case sensitive");
            }
        } while (!exit);
    }

    public void sellMenuPositionFound(Position position) {
        String input;
        boolean exit = false;
        Optional<Quote> quoteOptional;
        double price;
        double netProfit;
        quoteOptional = quoteService.fetchQuoteDataFromAPI(position.getTicker());
        if (quoteOptional.isEmpty()) {
            System.out.println("Could not retrieve updated stock information at this moment!");
            System.out.println("Fetching latest information from database if available!");
            quoteOptional = quoteService.fetchQuoteDataFromDB(position.getTicker());
            if (quoteOptional.isEmpty()) {
                System.out.println("\nCould not find any information in database!");
                System.out.println("Returning to main menu! Please wait a few minutes before using the application again.");
            }
        } else {
            price = quoteOptional.get().getPrice();
            netProfit = price*position.getNumOfShares() - position.getValuePaid();
            do {
                System.out.print("\n" + "You currently own some shares!\n" + position);
                System.out.print("\n\nIf you sold your shares now your profits would be " + netProfit + "!");
                System.out.print("\nWould you like to sell this stock? Type yes or no.");
                input = scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    System.out.println("Selling stock...");
                    try {
                        positionService.sell(position.getTicker());
                    } catch (NoSuchElementException e) {
                        System.out.print("\n\nAPI Error: Possibly due to API call limit. " +
                                "\nPlease try again in about 2 minutes when the call limit has subsided!");
                        errorLogger.error("User reached API call limit in sell menu.");
                        break;
                    }
                    System.out.printf("\nStock sold at the share amount %d for the total profit of: %f", position
                            .getNumOfShares(), netProfit);
                    System.out.print("\nReturning to sell menu.");
                    exit = true;
                } else if (input.equals("no")) {
                    System.out.println("\nReturning to sell menu.");
                    exit = true;
                } else {
                    System.out.println("\nPlease answer with either 'yes' or 'no'!");
                }
            } while (!exit);
        }
    }

    public void allOwnedStock() {
        List<Position> positions = positionService.fetchAllPositions();
        System.out.print("\n\n\n");
        System.out.print("Here is the list of all the stock you own currently: \n");
        for (Position position : positions) {
            System.out.println(position + "\n");
        }
        if (positions.isEmpty()) {
            System.out.print("You do not own any stocks!\n");
        }
        System.out.println("Returning to main menu!");
    }
}
