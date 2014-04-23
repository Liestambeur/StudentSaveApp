package be.kuleuven.chi.backend.categories;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Days;


/**
 * Created by Lies on 3/04/14.
 */
public class Goal implements Category {

    private String name;
    private double amount;
    private double amountSaved;
    private int picture;
    private Calendar createdDate;
    private Calendar lastReminded;
    // -1 -> not reminded
    private long milisecondsToBeReminded;
    private Calendar dueDate;
    //TODO when adding a variable, don't forget to change to copy methods!

    public Goal(){
        this.name = "Goal";
        this.amount = 0;
        this.amountSaved = 0;
        this.picture = 0;
        this.dueDate = null;
        this.milisecondsToBeReminded = -1;
        this.createdDate = new GregorianCalendar();
        this.lastReminded = new GregorianCalendar();
    }

    public long daysLeft(){
        return this.daysBetween(new GregorianCalendar(), dueDate);
        //return Days.daysBetween(new DateTime(), new DateTime(dueDate.getTime())).getDays();
    }

    private int daysBetween(final Calendar startDate, final Calendar endDate)
    {
        if(startDate.after(endDate)){
            return 0;
        }
        //assert: startDate must be before endDate
        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        long endInstant = endDate.getTimeInMillis();
        return  (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY);
    }

    public boolean shouldRemind(){
        if(milisecondsToBeReminded<=0){
            return false;
        }
        Calendar now = new GregorianCalendar();
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(lastReminded.getTimeInMillis()+milisecondsToBeReminded);
        if(now.after(cal)){
            return true;
        }
        return false;
    }

    public void resetRemind(){
        this.milisecondsToBeReminded = -1;
    }

    public void setMilisecondsToBeReminded(long milisecondsToBeReminded){
        this.milisecondsToBeReminded = milisecondsToBeReminded;
    }

    public void updateLastReminded(){
        this.lastReminded = new GregorianCalendar();
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


    public int getPicture() {
        return picture;
    }

    public void resetPicture() {
        this.picture = 0;
    }

    public void setPicture(int picture) {
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
