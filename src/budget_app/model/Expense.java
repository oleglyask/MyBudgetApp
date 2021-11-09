package budget_app.model;

public class Expense {

    private int id;
    private String name;
    private String howOften;
    private int amount;


    public Expense(int id, String name, String howOften, int amount) {
        this.id = id;
        this.name = name;
        this.howOften = howOften;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHowOften() {
        return howOften;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "name='" + name + '\'' +
                "amount=" + amount +
                "} ";
    }
}//end of Expense class
