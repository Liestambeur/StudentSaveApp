package be.kuleuven.chi.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.Goal;
import be.kuleuven.chi.backend.History;

public class MainActivity extends Activity {

    AppContent appContent = AppContent.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(appContent.hasGoal()){
            Goal goal = appContent.getGoal();
            View goalview = findViewById(R.id.goal);
            goalview.setVisibility(1);
            TextView goalName = (TextView) findViewById(R.id.goalName);
            goalName.setText(goal.getName());
            TextView goalAmount = (TextView) findViewById(R.id.goalAmount);
            goalAmount.setText("€ "+(goal.getAmount()- goal.getDone())+" to go.");
            TextView goalDone = (TextView) findViewById(R.id.goalDone);
            goalDone.setText("€ "+goal.getDone()+" done.");
            TextView goalPercent = (TextView) findViewById(R.id.goalProcent);
            goalPercent.setText(goal.getPercent() + " %");
            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
            progress.setProgress(goal.getPercent());
        }else{
            View addgoal = findViewById(R.id.addgoal);
            addgoal.setVisibility(1);
        }



        setContentView(R.layout.activity_main);


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
