package be.kuleuven.chi.app;

import android.app.Dialog;
import android.content.Context;
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

import java.io.InputStream;
import java.io.Serializable;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.Goal;

public class MainActivity extends BaseActivity implements Serializable {

    static final long serialVersionUID = 0L;

    AppContent appContent;
    Context context;
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        appContent = AppContent.getInstance(this);
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        appContent.saveState();
//    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        appContent.saveState();
//    }
//
//    @Override
//    public void onStart(){
//        super.onStart();
//        appContent = AppContent.getInstance(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        this.appContent = AppContent.getInstance(this);

        if(this.appContent.hasCurrentGoal()){
            Goal goal = appContent.getCurrentGoal();
            fillInGoalView(goal);
            checkGoalPopUps(goal);
        }
        else{
            View addgoal = findViewById(R.id.addgoal);
            addgoal.setVisibility(View.VISIBLE);
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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(goal.getPictureUrl());
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        im.setImageBitmap(bm);

        //im.setImageDrawable(getResources().getDrawable(goal.getPicture()));
        return goal;
    }
    private void checkGoalPopUps(Goal goal) {
        //REMIND
        if(goal.shouldRemind()){
            this.showReminder();
            goal.updateLastReminded();
        }

        //GOAL gedaan
        if(goal.isDone()){
            showDialogGoal();
           // showDialog(DIALOG_ALERT);
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

        for(int i=0;i<3;i++) {
            View v;
            if(appContent.getNumberOfHistoryElements()<=i){
                v = new LinearLayout(this);
            } else{
                v = adapter.getView(i, null, null);
            }
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0, 1.0f);
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
    }

    /** ON CLICKS **/
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
    public void toHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        finish();
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
        dialog.setTitle("Reminder");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.popuptext1);
        text.setText("Please remember to save");
        TextView text2 = (TextView) dialog.findViewById(R.id.popuptext2);
        text2.setText("for your goal: "+appContent.getCurrentGoal().getName());
        ImageView image = (ImageView) dialog.findViewById(R.id.popupimage);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appContent.getCurrentGoal().getPictureUrl());
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        image.setImageBitmap(bm);
        //Drawable draw = getResources().getDrawable(appContent.getCurrentGoal().getPicture());
        //if(draw==null){
        //    image.setImageResource(R.drawable.ic_launcher);
        //} else{
        //    image.setImageDrawable(draw);
        //}



        LinearLayout lin = (LinearLayout) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }
    public void showDialogGoal(){
        // custom dialog
        final Dialog dialog = new Dialog(this, R.style.myBackgroundStyle);
        dialog.setContentView(R.layout.popup);
        dialog.setTitle("Congratulations");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.popuptext1);
        text.setText("You have saved enough!");
        TextView text2 = (TextView) dialog.findViewById(R.id.popuptext2);
        text2.setText("Enjoy "+appContent.getCurrentGoal().getName()+"!");
        ImageView image = (ImageView) dialog.findViewById(R.id.popupimage);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appContent.getCurrentGoal().getPictureUrl());
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        image.setImageBitmap(bm);
//
//        Drawable draw = getResources().getDrawable(appContent.getCurrentGoal().getPicture());
//        if(draw==null){
//            image.setImageResource(R.drawable.ic_launcher);
//        } else{
//            image.setImageDrawable(draw);
//        }



        LinearLayout lin = (LinearLayout) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appContent.currentGoalDone();
                dialog.dismiss();
                View goalview = findViewById(R.id.goal);
                goalview.setVisibility(View.INVISIBLE);
                View addgoal = findViewById(R.id.addgoal);
                addgoal.setVisibility(View.VISIBLE);
                enableButtonSave(false);
            }
        });

        dialog.show();
    }
/*
    // constant for identifying the dialog
    private static final int DIALOG_ALERT = 10;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_ALERT:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This ends the activity");
                builder.setCancelable(false);
                builder.setPositiveButton("I agree", new OkOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            MainActivity.this.finish();
        }
    }*/

}
