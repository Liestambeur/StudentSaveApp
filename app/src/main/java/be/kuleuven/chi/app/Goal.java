package be.kuleuven.chi.app;

/**
 * Created by Lies on 3/04/14.
 */
public class Goal {

    private String name;
    private double amount;
    private double done;
    private int percent;

    public Goal(String name, double amount){
        this.name=name;
        this.amount=amount;
        this.done=0;
        this.percent=0;
    }

    public void addAmount(double amount){
        if(percent==100){

        }else if((done + amount)>this.amount ){

        }else if((done + amount)<0){

        }

        done = done + amount;
        this.setPercent();
    }

    public void setPercent(){
        int percent = (int) (amount/done);
        this.percent = percent;
    }

}
