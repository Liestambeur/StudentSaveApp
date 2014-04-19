package be.kuleuven.chi.backend.categories;

import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by Lies on 3/04/14.
 */
public class Goal implements Category {

    private String name;
    private double amount;
    private double amountSaved;
    private Drawable picture;
    private Calendar dueDate;
    //TODO when adding a variable, don't forget to change to copy methods!

    public Goal(){
        this.name = "Goal";
        this.amount = 0;
        this.amountSaved = 0;
        this.picture = null;
        this.dueDate = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("NAAM: "+name);
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void addAmount(double amount){
        if(getAmount() == getAmountSaved()){

        }else if((amountSaved + amount)>this.amount ){

        }else if((amountSaved + amount)<0){

        }

        amountSaved = amountSaved + amount;
    }

    public double getAmountSaved() {
        return amountSaved;
    }

    private void setAmountSaved(double amountSaved) {
        this.amountSaved = amountSaved;
    }


    public double getAmountToGo(){
        return getAmount() - getAmountSaved();
    }

    /**
     * The percentage saved, rounded to an integer
     */
    public int getPercent() {
        return (int) ((getAmountSaved()*100)/getAmount());
    }


    public Drawable getPicture() {
        return picture;
    }

    public void resetPicture() {
        this.picture = null;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public Calendar getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Calendar newDate) {
        if(newDate != null && newDate.after(new GregorianCalendar())) {
            // the given date is a valid dueDate
            this.dueDate = newDate;
        }
    }

    public void resetDueDate() {
        this.dueDate = null;
    }

    public String getDueDateString() {
        return this.dueDate.get(Calendar.DAY_OF_MONTH) + "/" + this.dueDate.get(Calendar.MONTH) + "/" + this.dueDate.get(Calendar.YEAR);
    }

    public Boolean isValid(boolean dueDateRequired) {
        boolean result = true;
        if(dueDateRequired) {
            result = result && getDueDate() != null;
        }
        return result && this.amount != 0 && this.name.length() > 0;
    }

    public Goal getCopy() {
        Goal copy = new Goal();
        copy.setName(this.name);
        copy.setAmount(this.amount);
        copy.setAmountSaved(this.amountSaved);
        copy.setPicture(this.picture);
        copy.setDueDate(this.dueDate);

        return copy;
    }

    public void copyState(Goal goal) {
        this.name = goal.getName();
        this.amount = goal.getAmount();
        this.amountSaved = goal.getAmountSaved();
        this.picture = goal.getPicture();
        this.dueDate = goal.getDueDate();
    }

    public boolean isDone(){
        return amountSaved>=amount;
    }
}
