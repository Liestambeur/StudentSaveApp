package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.Goal;

public class MainActivity extends Activity {

    AppContent appContent = AppContent.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_main);

        if(appContent.hasGoal()){
            Goal goal = appContent.getGoal();
            View goalview = findViewById(R.id.goal);
            goalview.setVisibility(1);
            TextView goalName = (TextView) findViewById(R.id.goalName);
            goalName.setText(goal.getName());
            TextView goalAmount = (TextView) findViewById(R.id.goalAmount);
            goalAmount.setText("€ "+(goal.getAmount()- goal.getAmountSaved())+" to go.");
            TextView goalDone = (TextView) findViewById(R.id.goalDone);
            goalDone.setText("€ "+goal.getAmountSaved()+" done.");
            TextView goalPercent = (TextView) findViewById(R.id.goalProcent);
            goalPercent.setText(goal.getPercent() + " %");
            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
            progress.setProgress(goal.getPercent());
        }else{
            View addgoal = findViewById(R.id.addgoal);
            addgoal.setVisibility(1);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addGoal(View view){
        Intent intent = new Intent(this, AddGoalActivity.class);
        startActivity(intent);
        finish();
    }

    public void removeGoal(View view){
        View a = findViewById(R.id.addgoal);
        a.setVisibility(1);
        View b = findViewById(R.id.goal);
        b.setVisibility(0);
    }

}
