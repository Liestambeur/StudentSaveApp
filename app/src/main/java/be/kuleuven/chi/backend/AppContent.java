package be.kuleuven.chi.backend;

/**
 * Created by Lies on 3/04/14.
 */
public class AppContent {

    private Goal goal;
    private History history;

    private static AppContent singleton = new AppContent( );

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private AppContent(){ }

    /* Static 'instance' method */
    public static AppContent getInstance( ) {
        return singleton;
    }

    public Goal getGoal(){
        return goal;
    }

    public void addGoal(Goal goal){
        this.goal=goal; // later a list of goals will be included
    }

    public boolean hasGoal(){
        return this.goal!=null;
    }

    public void addToHistory(HistoryElement element){
        if(this.history==null){
            this.history = new History();
        }
        this.history.addToHistory(element);
    }







}
