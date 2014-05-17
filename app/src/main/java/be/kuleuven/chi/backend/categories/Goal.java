package be.kuleuven.chi.backend.categories;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.kuleuven.chi.backend.RemindType;

//import org.joda.time.DateTime;
//import org.joda.time.Days;


/**
 * Created by Lies on 3/04/14.
 */
public class Goal implements Category {

    private String name;
    private double amountTotalNeeded; // the total amountTotalNeeded to save
    private double amountSaved;
    private String urlPicture;
    private Calendar dueDate;
    private RemindType remindType;
    private Calendar nextRemindDate;
    //TODO when adding a variable, don't forget to change to copy methods!

    public Goal(){
        this.name = "Goal";
        this.amountTotalNeeded = 0;
        this.amountSaved = 0;
        this.urlPicture = "";
        this.dueDate = null;
        this.remindType = RemindType.NEVER;
        this.nextRemindDate = new GregorianCalendar();
    }

    /** NAME **/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /** THE TOTAL AMOUNT YOU NEED TO SAVE **/
    public double getAmountTotalNeeded() {
        return amountTotalNeeded;
    }
    public void setAmountTotalNeeded(double amountTotalNeeded) {
        this.amountTotalNeeded = amountTotalNeeded;
    }

    /** THE AMOUNT YOU HAVE SAVED SO FAR **/
    public double getAmountSaved() {
        return amountSaved;
    }
    public String getAmountSavedString() {
        return String.format("%.2f", getAmountSaved());
    }
    public void addAmountSaved(double amount){
        // TODO: waarom staan deze if-tests hier??
        if(getAmountTotalNeeded() == getAmountSaved()){

        }
        else if((amountSaved + amount)>this.amountTotalNeeded){

        }
        else if((amountSaved + amount)<0){

        }

        amountSaved = amountSaved + amount;
    }
    private void setAmountSaved(double amountSaved) {
        this.amountSaved = amountSaved;
    }

    /** THE AMOUNT YOU STILL HAVE TO SAVE **/
    public double getAmountToGo(){
        return getAmountTotalNeeded() - getAmountSaved();
    }
    public String getAmountToGoString() {
        return String.format("%.2f", getAmountToGo());
    }
    public int getPercent() {
        return (int) ((getAmountSaved()*100)/ getAmountTotalNeeded());
    }
    public boolean isDone(){
        return amountSaved>= amountTotalNeeded;
    }

    /** PICTURE **/
    public String getPictureUrl() {
        return urlPicture;
    }
    public void resetPicture() {
        this.urlPicture = "";
    }
    public void setPicture(String picture) {
        this.urlPicture = picture;
    }

    /** DEADLINE **/
    public boolean hasDueDate() {
        return this.dueDate != null;
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
        // note: the calendar counts months starting from 0 (e.g. January = 0, February = 1)
        // so decrease the user input with one as the user will start counting from 1.
        // days of month start from 1, as the user will expect so no change needed here.
        return this.dueDate.get(Calendar.DAY_OF_MONTH) + "/"
                + (this.dueDate.get(Calendar.MONTH) + 1) + "/"
                + this.dueDate.get(Calendar.YEAR);
    }
    public int daysLeft(){
        return this.daysBetween(new GregorianCalendar(), dueDate);
        //return Days.daysBetween(new DateTime(), new DateTime(dueDate.getTime())).getDays();
    }
    private int daysBetween(final Calendar startDate, final Calendar endDate) {
        if(startDate.after(endDate)){
            return 0;
        }
        else {
            //assert: startDate must be before endDate
            int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
            long endInstant = endDate.getTimeInMillis();
            return (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY) + 1;
            // + 1 because you count in today
        }
    }

    /** REMINDING **/
    public boolean hasRemindSetting() {
        return this.remindType != RemindType.NEVER;
    }
    public RemindType getRemindType() {
        return this.remindType;
    }
    public void setRemindType(RemindType remindType) {
        this.remindType = remindType;
    }
    public Calendar getNextRemindDate() {
        return this.nextRemindDate;
    }
    private void setNextRemindDate(Calendar nextRemindDate) {
        this.nextRemindDate = nextRemindDate;
    }
    public boolean shouldRemind(){
        return hasRemindSetting() && this.nextRemindDate.after(new GregorianCalendar());
    }
    public void resetRemind(){
        this.remindType = RemindType.NEVER;
    }
    public void updateNextRemindDate(){
        this.nextRemindDate = this.remindType.nextRemindDate(new GregorianCalendar());
    }

    /** OTHER **/
    public Boolean isValid(boolean dueDateRequired, boolean reminderRequired) {
        boolean result = true;
        if(dueDateRequired) {
            result = result && hasDueDate();
        }
        if(reminderRequired) {
            result = result && hasRemindSetting();
        }
        return result && this.amountTotalNeeded != 0 && this.name.length() > 0;
    }

    public Goal getCopy() {
        Goal copy = new Goal();
        copy.setName(this.name);
        copy.setAmountTotalNeeded(this.amountTotalNeeded);
        copy.setAmountSaved(this.amountSaved);
        copy.setPicture(this.urlPicture);
        copy.setDueDate(this.dueDate);
        copy.setRemindType(this.remindType);
        copy.setNextRemindDate(this.nextRemindDate);

        return copy;
    }
    public void copyState(Goal goal) {
        this.name = goal.getName();
        this.amountTotalNeeded = goal.getAmountTotalNeeded();
        this.amountSaved = goal.getAmountSaved();
        this.urlPicture = goal.getPictureUrl();
        this.dueDate = goal.getDueDate();
        this.remindType = goal.getRemindType();
        this.nextRemindDate = goal.getNextRemindDate();
    }

}
