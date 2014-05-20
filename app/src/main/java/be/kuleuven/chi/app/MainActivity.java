package be.kuleuven.chi.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.io.InputStream;
import java.io.Serializable;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.Goal;

public class MainActivity extends BaseActivity implements Serializable {

    static final long serialVersionUID = 0L;
    private AppContent appContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.appContent = AppContent.getInstance(this);

        System.out.println("a@@@@@@@@@@@@@@@@@@@@@@@ "+this.appContent);

        if(this.appContent.hasCurrentGoal()){
            fillInGoalView(appContent.getCurrentGoal());
            for(Goal goal: AppContent.getInstance(this).getGoalsBusy()) {
                checkGoalPopUps(goal);
            }
            if(this.appContent.getCurrentGoal().isDone()){
                this.showDialogGoal();
                sendTracking("Goal done");
            }
        }
        else{
            View addGoal = findViewById(R.id.addgoal);
            addGoal.setVisibility(View.VISIBLE);
            this.enableButtonSave(false);
        }

        if(appContent.getWalletTotalAmount()<=0){
            this.enableButtonSave(false);
            this.enableButtonExpense(false);
        }

        if(appContent.hasHistory()){
            fillInHistoryView();
        }
        else{
            View noHistory = findViewById(R.id.no_history);
            noHistory.setVisibility(View.VISIBLE);
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

    /** VIEWS **/
    private Goal fillInGoalView(Goal goal) {
        String currencySymbol = AppContent.getInstance(this).getCurrencySymbol();

        View goalview = findViewById(R.id.goal);
        goalview.setVisibility(View.VISIBLE);

        TextView goalName = (TextView) findViewById(R.id.goalName);
        goalName.setText(goal.getName());

        TextView goalAmount = (TextView) findViewById(R.id.goalAmount);
        goalAmount.setText(currencySymbol + " " + goal.getAmountToGoString() + " " + getResources().getString(R.string.to_go));

        TextView goalDone = (TextView) findViewById(R.id.goalDone);
        goalDone.setText(currencySymbol + " " + goal.getAmountSavedString() + " " + getResources().getString(R.string.done));

        TextView goalDue = (TextView) findViewById(R.id.goalDue);
        if(goal.hasDueDate()) {
            int numberOfDaysLeft = goal.daysLeft();
            goalDue.setText(numberOfDaysLeft + " " + getResources().getQuantityString(R.plurals.days_left, numberOfDaysLeft));
        }
        else {
            goalDue.setVisibility(View.GONE);
        }

        TextView goalPercent = (TextView) findViewById(R.id.goalProcent);
        goalPercent.setText(goal.getPercent() + " %");

        ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setProgress(goal.getPercent());

        ImageView im = (ImageView) findViewById(R.id.goalPicture);

        String path = goal.getPictureUrl();
        Bitmap bm;
        if(path.startsWith(String.valueOf('/'))){
            bm = BitmapFactory.decodeFile(path);
        } else{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            bm = BitmapFactory.decodeStream(inputStream);
        }

        im.setImageBitmap(bm);
        return goal;
    }
    private void checkGoalPopUps(Goal goal) {
        if(goal.shouldRemind()){
            this.showReminder();
            goal.updateNextRemindDate();
            sendTracking("Reminded");
        }

    }

    private void fillInHistoryView() {
        View historyPreview = findViewById(R.id.history_preview);
        historyPreview.setVisibility(View.VISIBLE);

        TextView walletTotal = (TextView) findViewById(R.id.previewWalletTotal);
        walletTotal.append(": " + AppContent.getInstance(this).getWalletTotal());

        LinearLayout listPreviewHistory = (LinearLayout) findViewById(R.id.listPreviewHistory);
        listPreviewHistory.setWeightSum(3);
        HistoryElementAdapterPreview adapter = new HistoryElementAdapterPreview(this,R.layout.history_row_preview);

        for(int i = 0; i < 3; i++) {
            View view;
            if(appContent.getNumberOfHistoryElements() <= i) {
                view = new LinearLayout(this);
            }
            else {
                view = adapter.getView(i, null, null);
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0, 1.0f);
            view.setLayoutParams(param);

            listPreviewHistory.addView(view);
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toHistory(view);
                }
            });
            //  listPreviewHistory.addView(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    /** ON CLICKS **/
    public void addGoal(View view){
        //Intent intent = new Intent(this, AddGoalActivity.class);
        Intent intent = new Intent(this, AddGoalPage1Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on add goal");
    }
    public void editGoal(View view){
        //Intent intent = new Intent(this, AddGoalActivity.class);
        Intent intent = new Intent(this, AddGoalPage1Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.EDIT);
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on edit goal");
    }

    public void income(View view){
        View income = findViewById(R.id.income);
        income.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),InputActivityType.INCOME.name());
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on income");
    }
    public void expense(View view){
        View expense = findViewById(R.id.expense);
        expense.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(), InputActivityType.EXPENSE.name());
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on expense");

    }
    public void save(View view){
        View save = findViewById(R.id.save);
        save.setPressed(true);

        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra(getResources().getText(R.string.input_activity_type).toString(),InputActivityType.SAVE.name());
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on save");

    }
    public void toHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on history");

    }

    /** ENABLE BUTTONS /**/
    private void enableButtonSave(Boolean enable){
        LinearLayout save = (LinearLayout) findViewById(R.id.save);
        save.setEnabled(enable);
        Button saveb = (Button) findViewById(R.id.button_save);
        saveb.setEnabled(enable);
        ImageView savei = (ImageView) findViewById(R.id.image_save);
        savei.setEnabled(enable);
        if(!enable){
            //savei.setColorFilter(Color.GRAY);
            save.setAlpha(0.6f);
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
            save.setAlpha(0.6f);
        }

    }

    /** POPUPS **/
    public void showReminder(){
        // custom dialog
        final Dialog dialog = new Dialog(this, R.style.myBackgroundStyle);
        dialog.setContentView(R.layout.popup);

        // set the dialog text and image
        dialog.setTitle(getResources().getString(R.string.goal_reminder_title));
        TextView text = (TextView) dialog.findViewById(R.id.popup_text);
        text.setText(getResources().getString(R.string.goal_reminder_text,appContent.getCurrentGoal().getName()));
        ImageView image = (ImageView) dialog.findViewById(R.id.popup_image);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appContent.getCurrentGoal().getPictureUrl());
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        image.setImageBitmap(bm);

        // buttons
        dialog.findViewById(R.id.popup_ok).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.popup_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(!MainActivity.this.isFinishing()){
            dialog.show();
        }
    }

    public void showDialogGoal(){
        // custom dialog
        final Dialog dialog = new Dialog(this, R.style.myBackgroundStyle);
        dialog.setContentView(R.layout.popup);
        dialog.setCanceledOnTouchOutside(false);

        // set the dialog text and image
        dialog.setTitle(getResources().getString(R.string.goal_done_title));
        TextView text = (TextView) dialog.findViewById(R.id.popup_text);
        text.setText(getResources().getString(R.string.goal_done_text,appContent.getCurrentGoal().getName()));
        ImageView image = (ImageView) dialog.findViewById(R.id.popup_image);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appContent.getCurrentGoal().getPictureUrl());
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        image.setImageBitmap(bm);

        // buttons
        dialog.findViewById(R.id.popup_ok).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.popup_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                appContent.currentGoalDone();

                View goalView = findViewById(R.id.goal);
                goalView.setVisibility(View.INVISIBLE);
                View addGoal = findViewById(R.id.addgoal);
                addGoal.setVisibility(View.VISIBLE);
                enableButtonSave(false);
            }
        });

        if(!MainActivity.this.isFinishing()){
            dialog.show();
        }
    }

}
