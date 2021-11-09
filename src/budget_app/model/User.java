package budget_app.model;

import java.util.ArrayList;

public class User {

    private String first_name = null;
    private String last_name = null;
    private String email = null;
    private int userId;
    private ArrayList<Income> incomes = new ArrayList<>();
    private ArrayList<Expense> expenses = new ArrayList<>();
    private ArrayList<Debt> debts = new ArrayList<>();
    private ArrayList<Account> accounts = new ArrayList<>();


    public User(String first_name, String last_name, String email, int userId) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.userId = userId;
    }

    public void setIncomes(ArrayList<Income> incomes) {
        this.incomes = incomes;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setDebts(ArrayList<Debt> debts) {
        this.debts = debts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public int getUserId() {
        return userId;
    }

    public ArrayList<Income> getIncomes() {
        return incomes;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public ArrayList<Debt> getDebts() {
        return debts;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    @Override
    public String toString() {

        String toReturn;

        toReturn = "User {" +
                "first_name='" + first_name + '\'';

        for (Income income : incomes){
            toReturn += income.toString();
        }
        for (Expense expense : expenses){
            toReturn += expense.toString();
        }

        toReturn += '}';

        return toReturn;
    }
}//end Use class

