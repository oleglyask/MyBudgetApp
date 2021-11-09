package budget_app.model;

public class Income {

    private int id;
    private String name;
    private String howOften;
    private int amount;

    public Income(int id, String name, String howOften, int amount) {
        this.id = id;
        this.name = name;
        this.howOften = howOften;
        this.amount = amount;
    }//end of constructor

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHowOften() {
        return howOften;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Income{" +
                "name='" + name + '\'' +
                "amount=" + amount +
                '}';
    }
}//End of Income class
