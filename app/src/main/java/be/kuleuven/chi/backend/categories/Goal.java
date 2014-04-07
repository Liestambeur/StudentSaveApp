package be.kuleuven.chi.backend.categories;

import android.graphics.drawable.Drawable;

/**
 * Created by Lies on 3/04/14.
 */
public class Goal implements Category {

    private String name;
    private double amount;
    private double amountSaved;
    private Drawable picture;

    public Goal(){
        this.name="Goal";
        this.amount=0;
        this.amountSaved =0;
        this.picture =null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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



}
