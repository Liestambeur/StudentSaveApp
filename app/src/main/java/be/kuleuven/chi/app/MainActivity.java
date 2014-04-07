package be.kuleuven.chi.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.ExpenseCategory;
import be.kuleuven.chi.backend.categories.Goal;
import be.kuleuven.chi.backend.categories.IncomeCategory;
import be.kuleuven.chi.backend.historyElements.ExpenseElement;
import be.kuleuven.chi.backend.historyElements.IncomeElement;
import be.kuleuven.chi.backend.historyElements.SavingElement;

public class MainActivity extends BaseActivity {

    AppContent appContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContent = AppContent.getInstance(this);

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

        if(appContent.hasHistory()){
            View historyPreview = findViewById(R.id.history_preview);
            historyPreview.setVisibility(1);

            TextView walletTotal = (TextView) findViewById(R.id.previewWalletTotal);
            walletTotal.append(": " + AppContent.getInstance(this).getWalletTotal());

            LinearLayout listPreviewHistory = (LinearLayout) findViewById(R.id.listPreviewHistory);
            listPreviewHistory.setWeightSum(3);
            HistoryElementAdapterPreview adapter = new HistoryElementAdapterPreview(this,R.layout.history_rowb);
            for(int i=0;i<3;i++) {
                View v;
                if(appContent.getNumberOfHistoryElements()<=i){
                    v = new LinearLayout(this);
                } else{
                    v = adapter.getView(i, null, null);
                }
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,0, 1.0f);
                v.setLayoutParams(param);

                listPreviewHistory.addView(v);
              //  listPreviewHistory.addView(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }else{
            View noHistory = findViewById(R.id.no_history);
            noHistory.setVisibility(1);
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public void addGoal(View view){
        Intent intent = new Intent(this, AddGoalActivity.class);
        startActivity(intent);
        finish();
    }

    public void toHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void income(View view){
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),
                InputActivityType.INCOME.name());
        startActivity(intent);
        finish();
    }

    public void expense(View view){
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),
                InputActivityType.EXPENSE.name());
        startActivity(intent);
        finish();
    }

    public void save(View view){
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),
                InputActivityType.SAVE.name());
        startActivity(intent);
        finish();
    }

}
