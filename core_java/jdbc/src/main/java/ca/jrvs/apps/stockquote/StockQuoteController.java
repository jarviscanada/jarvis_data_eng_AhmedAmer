package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dao.Position;
import ca.jrvs.apps.stockquote.dao.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
        boolean validEntry = false;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.print("Buy Menu: \n");
            System.out.println("Enter a ticker symbol - this is the symbol for the stock you wish to view.");
            System.out.println("To return to the main menu, type back");
            input = scanner.nextLine();
            if (input.equals("back")) {
                validEntry = true;
                System.out.print("\nHeading back to main menu!");
                infoLogger.info("User navigated to main menu.");
            }
            quoteOptional = quoteService.fetchQuoteDataFromAPI(input);
            if (quoteOptional.isPresent()) {
                Quote quote = quoteOptional.get();
                buyMenuStockFound(quote);
            } else {
                System.out.print("\n\nYou need to enter a valid stock symbol. Try again!");
            }
        } while (!validEntry);
    }

    public void buyMenuStockFound(Quote quote) {
        double price = quote.getPrice();
        String input;
        boolean exit = false;
        boolean validNumOfShares = false;
        do {
            System.out.print("\n\n\n\n\n");
            System.out.printf("Quote for %s: \n", quote.getTicker());
            System.out.println(quote);
            System.out.print("\nWould you like to purchase this stock? Type yes or no.");
            input = scanner.nextLine();
            if (input.equals("yes")) {
                do {
                    System.out.print("\n\nHow many shares of this stock do you want to purchase? enter the amount below:");
                    try {
                        int amount = scanner.nextInt();
                        System.out.print("\nPurchasing...");
                        positionService.buy(quote.getTicker(), amount, price);
                        System.out.printf("\nStock purchased at the amount %d for the price: %f", amount, price);
                        System.out.print("\nReturning to buy menu.");
                        validNumOfShares = true;
                        exit = true;
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("\nInvalid entry. Please enter a valid integer smaller than 10 digits.");
                        errorLogger.error("Valid integer required for inputting number of shares to buy", e);
                    }
                } while (!validNumOfShares);
            } else if (input.equals("no")) {
                System.out.println("\nReturning to buy menu.");
                exit = true;
            } else {
                System.out.println("\nPlease enter 'yes' or 'no'!");
            }
        } while (!exit);
    }

    public void sellMenu() {
        String input;
        Optional<Position> posOptional;
        boolean exit = false;
        do {
            System.out.print("\n\n\n\n\n");
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
                System.out.println("\nInvalid entry. Please enter the ticker symbol of the stock you wish to sell.");
                System.out.println("Note that you must own shares of this stock to sell it!");
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
                System.out.print("\n\n\nIf you sold your shares now your profits would be " + netProfit + "!");
                System.out.print("\nWould you like to sell this stock? Type yes or no.");
                input = scanner.nextLine();
                if (input.equals("yes")) {
                    System.out.println("Selling stock...");
                    positionService.sell(position.getTicker());
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
        System.out.print("\n\n\n\n\n");
        System.out.print("Here is the list of all the stock you own currently: \n");
        for (Position position : positions) {
            System.out.println(position + "\n");
        }
        System.out.print("Returning to main menu!");
    }
}
