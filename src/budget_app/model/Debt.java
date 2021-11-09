package budget_app.model;

public class Debt {

    private int id;
    private String name;
    private int amount;
    private int interest;
    private int paymentDate;

    public Debt(int debtId, String debtName, int debtAmount, int debtInterest, int debtPaymentDate) {
        this.id = debtId;
        this.name = debtName;
        this.amount = debtAmount;
        this.interest = debtInterest;
        this.paymentDate = debtPaymentDate;
    }

    public int getAmount() {
        return amount;
    }
}//end of Debt Class
