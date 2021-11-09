package budget_app.services;

import budget_app.data.JDBC_Controller;
import budget_app.model.*;
import com.sun.media.jfxmediaimpl.HostUtils;

import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class Services {

    private static JDBC_Controller db = new JDBC_Controller();
    private static Scanner scanner = new Scanner(System.in);
    private static User user;


    //adds income into the database
    public static int addIncome(String incomeName, String howOften, int incomeAmount) throws SQLException{

        String query = "INSERT INTO income (`user_id`, `income_name`, `how_often`, `income_amount`) VALUES ("
                + user.getUserId() + ", '" + incomeName + "', '" + howOften + "', " + incomeAmount + ")";
        int returnInt = db.updateSQL(query);
        loadIncome();
        return returnInt;
    } //end of addIncome

    //updates income table with new values
    public static int updateIncome(int incomeId, String incomeName, String howOften, int incomeAmount) throws SQLException {

        String query = "UPDATE income SET income_name = '" + incomeName + "', how_often = '" + howOften +
                "', income_amount = " + incomeAmount  + " WHERE (income_id = " + incomeId  + ") and (user_id = " + user.getUserId() +")";
        int returnInt = db.updateSQL(query);
        loadIncome();
        return returnInt;
    }//end of updateIncome

    //adds expense into the database
    public static int addExpense(String expenseName, String howOften, int expenseAmount) throws SQLException{

        String query = "INSERT INTO expense (`user_id`, `expense_name`, `how_often`, `expense_amount`) VALUES ("
                + user.getUserId() + ", '" + expenseName + "', '" + howOften + "', " + expenseAmount + ")";
        int returnInt = db.updateSQL(query);
        loadExpense();
        return returnInt;

    } //end of addExpense

    //updates expense table with new values
    public static int updateExpense(int expenseId, String expenseName, String howOften, int expenseAmount) throws SQLException {

        String query = "UPDATE expense SET expense_name = '" + expenseName + "', how_often = '" + howOften +
                "', expense_amount = " + expenseAmount  + " WHERE (expense_id = " + expenseId  + ") and (user_id = " + user.getUserId() +")";
        int returnInt = db.updateSQL(query);
        loadExpense();
        return returnInt;
    }//end of updateExpense

    //Checking if user is in the user table, will exit if not (stretch: let user create new user)
    public static boolean initUser(String email) throws SQLException {

        //checking if a user exists based on the email
        db.executeSQL("SELECT * FROM users WHERE email = '" + email + "'");
        if (db.getResultSet().next()) {
            user = (new User(
                    db.getResultSet().getString("first_name"),
                    db.getResultSet().getString("last_name"),
                    db.getResultSet().getString("email"),
                    db.getResultSet().getInt("user_id")
            ));

            //load all incomes into the user
            loadIncome();
            //load the expenses into the user
            loadExpense();
            //load the debt into the user
            loadDebt();
            //load the account into the user
            loadAccount();

            return true;
        } else {
            return false;
        }
    } //End of initUser

    //load the Income arrayList in the user class with all income
    public static void loadIncome() throws SQLException {

        ArrayList<Income> incomes = new ArrayList<>();
        db.executeSQL("SELECT * FROM income WHERE user_id = " + user.getUserId());
        while (db.getResultSet().next()){
            incomes.add (new Income(
                    db.getResultSet().getInt("income_id"),
                    db.getResultSet().getString("income_name"),
                    db.getResultSet().getString("how_often"),
                    db.getResultSet().getInt("income_amount")
            ));
            user.setIncomes(incomes);
        }
    }//End of loadIncome

    //load the Expense arrayList in the user class with all expense
    public static void loadExpense() throws SQLException {

        ArrayList<Expense> expenses = new ArrayList<>();
        db.executeSQL("SELECT * FROM expense WHERE user_id = " + user.getUserId());
        while (db.getResultSet().next()){
            expenses.add(new Expense(
                    db.getResultSet().getInt("expense_id"),
                    db.getResultSet().getString("expense_name"),
                    db.getResultSet().getString("how_often"),
                    db.getResultSet().getInt("expense_amount")
            ));
            user.setExpenses(expenses);
        }
    }//End loadExpense

    //will load the Debt arrayList in the user Object
    public static void loadDebt() throws SQLException{

        db.executeSQL("SELECT * FROM debts WHERE user_id=" + user.getUserId());
        ArrayList<Debt> debts = new ArrayList<>();
        while (db.getResultSet().next()){
            debts.add (new Debt(
                    db.getResultSet().getInt("debt_id"),
                    db.getResultSet().getString("debt_name"),
                    db.getResultSet().getInt("debt_amount"),
                    db.getResultSet().getInt("debt_interest"),
                    db.getResultSet().getInt("payment_date")
            ));
            user.setDebts(debts);
        }
    } //End of loadDebt

    //will load the Accounts arrayList in the user Object
    public static void loadAccount() throws SQLException {

        db.executeSQL("SELECT * FROM accounts WHERE user_id= " + user.getUserId());
        ArrayList<Account> accounts = new ArrayList<>();
        while (db.getResultSet().next()){
            accounts.add(new Account(
                    db.getResultSet().getInt("account_id"),
                    db.getResultSet().getString("account_name"),
                    db.getResultSet().getInt("account_balance")
            ));
        }
        user.setAccounts(accounts);
    }//end of loadAccount

    public static JDBC_Controller getDb() {
        return db;
    }

    public static User getUser() {
        return user;
    }
} //end of Class
