package be.kuleuven.chi.backend;

import java.util.Date;

/**
 * Created by Lies on 3/04/14.
 */
public abstract class HistoryElement {



    private double amount;
    private Category category;
    private String title;
    private Date date;


    public HistoryElement(double amount, Category category, String title){
        this.date = new Date();
        this.amount=amount;
        this.category=category;
        this.title=title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }





}
