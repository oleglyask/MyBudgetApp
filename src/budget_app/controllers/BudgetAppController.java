package budget_app.controllers;

import budget_app.model.*;
import budget_app.services.Services;

import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class BudgetAppController {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to the Budget App");
        System.out.println("-------------------------");
        System.out.println();
        System.out.println("What is your email?");


        //Initialize user
        try {
            if (!Services.initUser("oleg@gmail.com")) {
            //if (!Services.initUser(scanner.nextLine())) {

                //no such user exists, (allow user to create user as a stretch project)
                System.out.println("You are not in the system, Please register");
                //no User exists and user does not want to create an account
                exitApp();
            }

            //show main menu while the user did not choose Exit
            int menuChoice;
            do {
                menuChoice = mainMenu();
                switch (menuChoice) {
                    case 1: {
                        quickStatsMenu();
                        break;
                    }
                    case 2: {
                        incomeExpenseMenu();
                        break;
                    }
                    case 3: {
                        reportsMenu();
                        break;
                    }
                    case 4: {
                        customGoalsMenu();
                        break;
                    }
                }

            } while (menuChoice != 5);

            //Exiting Application
            exitApp();

        } catch (SQLException exc) {
            exc.printStackTrace();
        } finally {
            try {
                //close connections
                Services.getDb().closeConnections();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }

    } // End of Main

    //Main Menu
    public static int mainMenu() {

        System.out.println("\nMain menu\n");
        System.out.println("1) Quick Stats");
        System.out.println("2) Add/Edit Income and Expenses");
        System.out.println("3) Customized Reports");
        System.out.println("4) Create/View Customized Goals");
        System.out.println("5) Exit");
        return mustBeInteger(scanner.nextLine());
    }

    //Quick Stats menu
    public static void quickStatsMenu() throws SQLException {

        int menuChoice;
        do {
            System.out.println("\nQuick Stats menu:");
            System.out.println("-----------------");
            System.out.println("1) See Stats");
            System.out.println("2) Main menu");
            System.out.println("3) Exit application");
            menuChoice = mustBeInteger(scanner.nextLine());

            switch (menuChoice) {
                case 1: {
                    showQuickStats();
                    break;
                }
                case 3: {
                    exitApp();
                }
            }
        } while (menuChoice != 2);
    }

    //Print out the quick stats
    public static void showQuickStats() {

        //get all account balances
        Services.getUser().getAccounts().stream().forEach((Account a)->
                System.out.println(a.getName() + " account has $" + NumberFormat.getInstance().format(a.getBalance())));

        //get total of all Income.amounts
        int sum = Services.getUser().getIncomes().stream().mapToInt(Income::getAmount).sum();
        System.out.println("Your total Income is: " + NumberFormat.getInstance().format(sum));

        //get total of all Expense.amounts
        sum = Services.getUser().getExpenses().stream().mapToInt(Expense::getAmount).sum();
        System.out.println("Your total Expenses are: " + NumberFormat.getInstance().format(sum));

        //get total of all Debt.amounts
        sum = Services.getUser().getDebts().stream().mapToInt(Debt::getAmount).sum();
        System.out.println("Your total Debt is: " + NumberFormat.getInstance().format(sum));


    }//end of showQuickStats

    //Income Expense menu
    public static void incomeExpenseMenu() throws SQLException {
        int menuChoice;
        do {
            System.out.println("\nIncome/Expense menu:");
            System.out.println("---------------------");
            System.out.println("1) Show All Income");
            System.out.println("2) Add Income");
            System.out.println("3) Edit Income");
            System.out.println("4) Show All Expenses");
            System.out.println("5) Add Expense");
            System.out.println("6) Edit Expense");
            System.out.println("7) Main menu");
            System.out.println("8) Exit application");
            menuChoice = mustBeInteger(scanner.nextLine());

            switch (menuChoice) {

                case 1: {
                    showIncome();
                    break;
                }
                case 2: {
                    addIncomeMenu();
                    break;
                }
                case 3: {
                    editIncomeMenu();
                    break;
                }
                case 4: {
                    showExpenses();
                    break;
                }
                case 5: {
                    addExpenseMenu();
                    break;
                }
                case 6: {
                    editExpenseMenu();
                    break;
                }
                case 8: {
                    exitApp();
                }
            }
        } while (menuChoice != 7);
    } //End incomeExpenseMenu

    //Shows all current expenses
    public static void showIncome(){

        System.out.println("Here are your current Income sources");
        Services.getUser().getIncomes().forEach((Income i) ->
                System.out.println(i.getId() + ") " + i.getName() + " " + i.getHowOften() + " " + i.getAmount())
        );
    }

    //Adds income to the database
    public static void addIncomeMenu() throws SQLException {

        String incomeName;
        String howOften;
        int incomeAmount;

        System.out.println("\nLets get some info to get started\n");
        System.out.println("What is the name of the income?");
        incomeName = scanner.nextLine().toLowerCase();

        howOften = getHowOften();

        System.out.println("What is the amount of the income?");
        incomeAmount = mustBeInteger(scanner.nextLine());

        if (Services.addIncome(incomeName, howOften, incomeAmount) == 1) {
            System.out.println("Income has been added");
        } else {
            System.out.println("Could not add income");
        }

    } //end addIncomeMenu

    //edit Income
    public static void editIncomeMenu() throws SQLException {

        //Figure out which income record the user wants to modify
        int modifyId;
        boolean exit = false;
        do {
            System.out.println("Which income would you like to modify? -> enter Id Number");
            showIncome();
            modifyId = mustBeInteger(scanner.nextLine());

            int finalId = modifyId;
            exit = Services.getUser().getIncomes().stream().anyMatch(x -> x.getId() == finalId);

        } while (!exit);

        //figure out which field of specified income record the user wants to modify
        System.out.println("Which field would you like to modify?");
        int finalModifyId = modifyId;
        //save the Income object from the Income array
        Income income = Services.getUser().getIncomes().stream().filter((Income i) -> i.getId() == finalModifyId).findAny().get();
        int incomeId = income.getId();
        String incomeName = income.getName();
        String howOften = income.getHowOften();
        int incomeAmount = income.getAmount();

        int menuChoice = 0;
        do {
            System.out.println("1) name -> " + incomeName);
            System.out.println("2) how often -> " + howOften);
            System.out.println("3) amount -> " + incomeAmount);
            System.out.println("4) Cancel");

            menuChoice = mustBeInteger(scanner.nextLine());

        } while (menuChoice < 1 || menuChoice > 4);

        switch (menuChoice) {
            case 1: {
                System.out.println("Enter new Name:");
                incomeName = scanner.nextLine().toLowerCase();
                break;
            }
            case 2: {
                howOften = getHowOften();
                break;
            }
            case 3: {
                System.out.println("Enter new Amount");
                incomeAmount = mustBeInteger(scanner.nextLine());
                break;
            }
        } //end switch

        //if the user did not cancel, proceed
        if (menuChoice != 4) {
            //Update database with new values
            if (Services.updateIncome(incomeId, incomeName, howOften, incomeAmount) == 1) {
                System.out.println("Income has been updated");
            } else {
                System.out.println("There was an error, Income has not been updated");
            }
        }
    }//End editIncomeMenu

    //shows all the expenses
    public static void showExpenses(){

        System.out.println("Here are your current Expense sources");
        Services.getUser().getExpenses().forEach((Expense e) ->
                System.out.println(e.getId() + ") " + e.getName() + " " + e.getHowOften() + " " + e.getAmount())
        );
    }

    //adds an Expense
    public static void addExpenseMenu() throws SQLException {

        String expenseName;
        String howOften;
        int expenseAmount;

        System.out.println("\nLets get some info to get started\n");
        System.out.println("What is the name of the expense?");
        expenseName = scanner.nextLine().toLowerCase();

        howOften = getHowOften();

        System.out.println("What is the amount of the expense?");
        expenseAmount = mustBeInteger(scanner.nextLine());

        if (Services.addExpense(expenseName, howOften, expenseAmount) == 1) {
            System.out.println("Expense has been added");
        } else {
            System.out.println("Could not add expense");
        }

    } //end addExpenseMenu

    //edit Expense
    public static void editExpenseMenu() throws SQLException {

        //Figure out which expense record the user wants to modify
        int modifyId;
        boolean exit = false;
        do {
            System.out.println("Which expense would you like to modify? -> enter Id Number");
            showExpenses();
            modifyId = mustBeInteger(scanner.nextLine());

            int finalId = modifyId;
            exit = Services.getUser().getExpenses().stream().anyMatch(x -> x.getId() == finalId);

        } while (!exit);

        //figure out which field of specified expense record the user wants to modify
        System.out.println("Which field would you like to modify?");
        int finalModifyId = modifyId;
        //save the Expense object from the Income array
        Expense expense = Services.getUser().getExpenses().stream().filter((Expense e) -> e.getId() == finalModifyId).findAny().get();
        int expenseId = expense.getId();
        String expenseName = expense.getName();
        String howOften = expense.getHowOften();
        int expenseAmount = expense.getAmount();

        int menuChoice = 0;
        do {
            System.out.println("1) name -> " + expenseName);
            System.out.println("2) how often -> " + howOften);
            System.out.println("3) amount -> " + expenseAmount);
            System.out.println("4) Cancel");

            menuChoice = mustBeInteger(scanner.nextLine());

        } while (menuChoice < 1 || menuChoice > 4);

        switch (menuChoice) {
            case 1: {
                System.out.println("Enter new Name:");
                expenseName = scanner.nextLine().toLowerCase();
                break;
            }
            case 2: {
                howOften = getHowOften();
                break;
            }
            case 3: {
                System.out.println("Enter new Amount");
                expenseAmount = mustBeInteger(scanner.nextLine());
                break;
            }
        } //end switch

        //if the user did not cancel, proceed
        if (menuChoice != 4) {
            //Update database with new values
            if (Services.updateExpense(expenseId, expenseName, howOften, expenseAmount) == 1) {
                System.out.println("Expense has been updated");
            } else {
                System.out.println("There was an error, Expense has not been updated");
            }
        }
    }//End editExpenseMenu

    //STOPPED HERE. CONTINUE TO ADD FUNCTIONALITY FOR THE FOLLOWING TWO MENUS AT A LATER DATE

    //Report menu
    public static void reportsMenu() {

        int menuChoice;
        do {
            System.out.println("\nReports menu:");
            System.out.println("--------------");
            System.out.println("1) Saved Year to Date");
            System.out.println("2) Time takes to save to $100,000");
            System.out.println("3) Percent of save per Month");
            System.out.println("4) Saving tip");
            System.out.println("5) Exit");
            menuChoice = mustBeInteger(scanner.nextLine());

            switch (menuChoice) {
//                case 1:
//                case 2:
//                case 3:
//                case 4:
            }
        } while (menuChoice != 5);

    }

    //Custom goal menu
    public static void customGoalsMenu() {

        int menuChoice;
        do {
            System.out.println("\nCustom goal menu:");
            System.out.println("-----------------");
            System.out.println("1) See Goals");
            System.out.println("2) Set Goals");
            System.out.println("3) Exit");
            menuChoice = mustBeInteger(scanner.nextLine());

            switch (menuChoice) {
//                case 1:
//                case 2:
            }
        } while (menuChoice != 3);

    }//end customGoalMenu

    public static void exitApp() throws SQLException {
        Services.getDb().closeConnections();
        System.out.println("Good Bye");
        System.exit(0);
    }

    public static String getHowOften() {

        String howOften;
        do {
            System.out.println("Is this a Monthly, Weekly, or a One time income? -> m/w/o");
            howOften = scanner.nextLine().toLowerCase();
        } while (!(howOften.equalsIgnoreCase("m") || howOften.equalsIgnoreCase("w") || howOften.equalsIgnoreCase("o")));
        return howOften;
    }

    public static int mustBeInteger(String s) {

        int returnInt=0;
        boolean exit = false;
        do {
            try {
                returnInt = Integer.parseInt(s);
                exit = true;
            } catch (NumberFormatException exc) {
                System.out.println("Must be a NUMBER:");
                s = scanner.nextLine();
            }
        } while (!exit);
        return returnInt;
    }//end of isInteger

}//end class

