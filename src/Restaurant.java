import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Restaurant {
    private Menu menu;
    private Kitchen kitchen;
    private Waitress waiter;
    private HashMap<Integer,Table> allTables;
    private Scanner scanner;
    private int numberOfChefs;
    private int numberOfWaitresses;
    private BlockingQueue<Order> orderQueue;
    private BlockingQueue<Order> completedOrders;
    private ExecutorService waitressesExecutor;
    private int totalCashEarned;

    public Restaurant(int numberOfChefs,int numberOfWaitresses){
        this.numberOfChefs = numberOfChefs;
        this.numberOfWaitresses = numberOfWaitresses;
        menu = new Menu();
        scanner = new Scanner(System.in);
        orderQueue = new LinkedBlockingQueue<>();
        completedOrders = new LinkedBlockingQueue<>();
        kitchen = new Kitchen(numberOfChefs, orderQueue, completedOrders);
        waiter = new Waitress(completedOrders);
        waitressesExecutor= Executors.newFixedThreadPool(numberOfWaitresses);
        waitressesExecutor.execute(waiter);
        allTables = new HashMap<>();
        totalCashEarned = 0;

    }



    public void start(){
        boolean finished = false;
        System.out.println("Welcome to the restaurant!!! \n");
        do {
            showOptionsForUser();
            try {
                int choice = getUserChoice();
                boolean result = handleUserInput(choice);
                if (result) {
                    finished = true;
                }
            } catch (InputMismatchException e) {
                handleInvalidInput();
            }

        } while (!finished);
        exitTheApp();
    }
    private void showOptionsForUser() {
        System.out.println("1. Show the menu");
        System.out.println("2. Add a meal to the menu");
        System.out.println("3. Take an order");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }
    private int getUserChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (choice < 1 || choice > 4) {
            System.out.println("Please enter a choice between 1-4");
            choice = scanner.nextInt();
            scanner.nextLine();
        }
        return choice;
    }

    private boolean handleUserInput(int choice) {
        switch (choice) {
            case 1:
                menu.showMenu();
                break;

            case 2:
                addDishToMenu();
                break;

            case 3:
                takeOrder();
                break;

            case 4:
                System.out.println("Exiting the Restaurant Management System. Goodbye!");
                return true;

            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return false;
    }

    private void takeOrder() {

        System.out.print("Enter table number: ");
        int currentTable = scanner.nextInt();
        scanner.nextLine();
        int tableNumber=checkIfTableIsFree(currentTable);
        List<Dish> selectedDishes = new ArrayList<>();
        menu.showMenu();
        System.out.print("Enter the dish numbers (comma-separated) for the order (or '0' to go back): ");
        String dishNumbersInput = scanner.nextLine();

        if (dishNumbersInput.equals("0")) {
            return; // Go back to main menu
        }

        String[] dishNumbers = dishNumbersInput.split(",");
        for (String dishNumber : dishNumbers) {
            int index = Integer.parseInt(dishNumber.trim()) - 1;
            if (index >= 0 && index < menu.getAllDishes().size()) {
                selectedDishes.add(menu.getAllDishes().get(index));
            }
        }

        if (!selectedDishes.isEmpty()) {
            System.out.print("Enter any special comments: ");
            String specialComments = scanner.nextLine();
            Order order = new Order(selectedDishes, tableNumber, specialComments);
            kitchen.submitOrder(order);
            //waiter.placeOrder(order);
            System.out.println("Order placed for Table " + tableNumber);
        } else {
            System.out.println("Invalid dish selection.");
        }
    }

    private int checkIfTableIsFree(int tableNumber) {

        while(allTables.containsKey(tableNumber)){
            System.out.print("This table is occupied.\nEnter table number: ");
            tableNumber = scanner.nextInt();
            scanner.nextLine();
        }
        allTables.put(tableNumber,new Table(tableNumber,0));
        return tableNumber;
    }

    private void addDishToMenu() {
        System.out.print("Enter the name of the dish: ");
        String dishName = scanner.nextLine();
        System.out.print("Enter the price of the dish: ");
        double dishPrice = scanner.nextDouble();
        System.out.print("Enter the preparation time (in minutes) for the dish: ");
        int preparationTime = scanner.nextInt();
        scanner.nextLine();
        menu.addNewDish(dishName, dishPrice,preparationTime);
        System.out.println("Dish added to the menu.");
    }

    private void handleInvalidInput(){
        System.out.println("Invalid input. Please enter a number between 1-4.");
        scanner.nextLine();
    }
    private void exitTheApp(){
        waitressesExecutor.shutdown();
        kitchen.shutdown();
        scanner.close();
        System.exit(0);
    }
}
