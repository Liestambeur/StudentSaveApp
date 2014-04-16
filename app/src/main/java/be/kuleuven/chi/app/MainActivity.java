package be.kuleuven.chi.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.Goal;

public class MainActivity extends BaseActivity {

    AppContent appContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appContent = AppContent.getInstance(this);

        if(appContent.hasGoal()){
            //TODO tekst niet hardcode, maar via String-file
            Goal goal = appContent.getCurrentGoal();

            View goalview = findViewById(R.id.goal);
            goalview.setVisibility(1);

            TextView goalName = (TextView) findViewById(R.id.goalName);
            goalName.setText(goal.getName());

            TextView goalAmount = (TextView) findViewById(R.id.goalAmount);
            goalAmount.setText("€ "+(goal.getAmount()- goal.getAmountSaved())+" to go");

            TextView goalDone = (TextView) findViewById(R.id.goalDone);
            goalDone.setText("€ "+goal.getAmountSaved()+" done");

            TextView goalDue = (TextView) findViewById(R.id.goalDue);
            if(goal.getDueDate() != null) {
                goalDue.setText("Due " + goal.getDueDateString());
                //TODO moet eigenlijk zijn: nog zoveel dagen te gaan
            }
            else {
                goalDue.setVisibility(View.INVISIBLE);
            }

            TextView goalPercent = (TextView) findViewById(R.id.goalProcent);
            goalPercent.setText(goal.getPercent() + " %");

            ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
            progress.setProgress(goal.getPercent());

            ImageView im = (ImageView) findViewById(R.id.imageView);
            im.setImageDrawable(goal.getPicture());
        }else{
            View addgoal = findViewById(R.id.addgoal);
            addgoal.setVisibility(1);
            this.enableButtonSave(false);
        }

        if(appContent.getWalletTotalAmount()<=0){
            this.enableButtonSave(false);
            this.enableButtonExpense(false);
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
                v.setClickable(true);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toHistory(view);
                    }
                });
              //  listPreviewHistory.addView(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }else{
            View noHistory = findViewById(R.id.no_history);
            noHistory.setVisibility(1);
            View historyFrame = findViewById(R.id.historyFrame);
            historyFrame.setClickable(false);
        }




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public void addGoal(View view){
        //Intent intent = new Intent(this, AddGoalActivity.class);
        Intent intent = new Intent(this, AddGoalPage1Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);
        startActivity(intent);
        finish();
    }

    public void editGoal(View view){
        //Intent intent = new Intent(this, AddGoalActivity.class);
        Intent intent = new Intent(this, AddGoalPage1Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.EDIT);
        startActivity(intent);
        finish();
    }

    public void toHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void income(View view){
        View income = findViewById(R.id.income);
        income.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),InputActivityType.INCOME.name());
        startActivity(intent);
        finish();
    }

    public void expense(View view){
        View expense = findViewById(R.id.expense);
        expense.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),InputActivityType.EXPENSE.name());
        startActivity(intent);
        finish();
    }

    public void save(View view){
        View save = findViewById(R.id.save);
        save.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),InputActivityType.SAVE.name());
        startActivity(intent);
        finish();
    }

    private void enableButtonSave(Boolean enable){
        LinearLayout save = (LinearLayout) findViewById(R.id.save);
        save.setEnabled(enable);
        Button saveb = (Button) findViewById(R.id.button_save);
        saveb.setEnabled(enable);
        ImageView savei = (ImageView) findViewById(R.id.image_save);
        savei.setEnabled(enable);
        if(!enable){
            //savei.setColorFilter(Color.GRAY);
            save.setAlpha(new Float(0.6));
        }

    }

    private void enableButtonExpense(Boolean enable){
        LinearLayout save = (LinearLayout) findViewById(R.id.expense);
        save.setEnabled(enable);
        Button saveb = (Button) findViewById(R.id.button_expense);
        saveb.setEnabled(enable);
        ImageView savei = (ImageView) findViewById(R.id.image_expense);
        savei.setEnabled(enable);
        if(!enable){
            //savei.setColorFilter(Color.GRAY);
            save.setAlpha(new Float(0.6));
        }

    }

}
