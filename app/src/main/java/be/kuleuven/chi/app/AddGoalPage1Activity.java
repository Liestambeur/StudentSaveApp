package be.kuleuven.chi.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;
import be.kuleuven.chi.backend.RemindType;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage1Activity extends BaseActivity {

    Goal goal;
    int goalActivityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page1);

        Intent intent = getIntent();
        this.goalActivityType = intent.getIntExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);

        if(this.goalActivityType == GoalActivityType.ADD) {
            goal = new Goal();
            this.enableOK(false);
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            findViewById(R.id.delete).setVisibility(View.VISIBLE);

            AppContent.getInstance(this).backupCurrentGoal();
            this.goal = AppContent.getInstance(this).getCurrentGoal();
            initGoalValues();
            this.enableOK(true);

            if(((CheckBox) findViewById(R.id.date_switch_goal)).isChecked()) {
                findViewById(R.id.due_date_field).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.due_date_field).setVisibility(View.GONE);
                goal.resetDueDate();
            }
            if(((CheckBox) findViewById(R.id.remind_switch_goal)).isChecked()) {
                findViewById(R.id.radio_remind).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.radio_remind).setVisibility(View.GONE);
                ((RadioGroup) findViewById(R.id.radio_remind)).clearCheck();
            }
        }

        initTextWatchers();
    }

    /** TEXT WATCHERS **/
    private void initTextWatchers() {
        EditText nameOfGoalText = (EditText) findViewById(R.id.nameOfGoal);
        nameOfGoalText.addTextChangedListener(getTextWatcherNameOfGoal());

        EditText amountOfGoalText = (EditText) findViewById(R.id.amountToSave);
        amountOfGoalText.addTextChangedListener(getTextWatcherAmountOfGoal());

        EditText dueDateText = (EditText) findViewById(R.id.due_date_field);
        dueDateText.addTextChangedListener(getTextWatcherDueDate());
    }
    private TextWatcher getTextWatcherNameOfGoal() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    // not used
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    goal.setName(charSequence.toString());
                    setOkButton();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // not used
                }
            };
    }
    private TextWatcher getTextWatcherAmountOfGoal() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    // not used
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    try {
                        goal.setAmountTotalNeeded(Double.parseDouble(charSequence.toString()));
                    }
                    catch (NumberFormatException e) {
                        goal.setAmountTotalNeeded(Double.parseDouble("0"));
                        // do nothing
                    }
                    setOkButton();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // not used
                }
            };
    }
    private TextWatcher getTextWatcherDueDate() {
        return new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    // not used
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    findViewById(R.id.date_no_past_text).setVisibility(View.GONE);
                    findViewById(R.id.date_format_text).setVisibility(View.GONE);
                    findViewById(R.id.date_not_valid_text).setVisibility(View.GONE);
                    String date = charSequence.toString();

                    if(date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                        findViewById(R.id.date_format_text).setVisibility(View.GONE);

                        String[] dateParts = date.split("/");
                        try {
                            // note: the calendar counts months starting from 0 (e.g. January = 0, February = 1)
                            // so decrease the user input with one as the user will start counting from 1.
                            // days of month start from 1, as the user will expect so no change needed here.
                            Calendar calendar = new GregorianCalendar(Integer.valueOf(dateParts[2]),
                                    Integer.valueOf(dateParts[1]) - 1, Integer.valueOf(dateParts[0]));
                            if(calendar.after(new GregorianCalendar())) {
                                findViewById(R.id.date_no_past_text).setVisibility(View.GONE);
                                Calendar backupDueDate = goal.getDueDate();
                                goal.setDueDate(calendar);

                                // note: if you fill in 80/5/2014, Calendar will recalculate the date to 19/7/2014,
                                // adding all the days that are to many in the month.
                                // checking whether the stored date equals the date in the text editor allows to check for this event.
                                if(!charSequence.toString().equals(goal.getDueDateString())) {
                                    findViewById(R.id.date_not_valid_text).setVisibility(View.VISIBLE);
                                    if(goalActivityType == GoalActivityType.EDIT) {
                                        goal.setDueDate(backupDueDate);
                                    }
                                    else {
                                        goal.resetDueDate();
                                    }
                                }
                            }
                            else {
                                findViewById(R.id.date_no_past_text).setVisibility(View.VISIBLE);
                            }
                        }
                        catch (NumberFormatException e) {
                            findViewById(R.id.date_not_valid_text).setVisibility(View.VISIBLE);
                            goal.resetDueDate();
                            // do nothing
                        }
                    }
                    else {
                        findViewById(R.id.date_format_text).setVisibility(View.VISIBLE);
                    }
                    setOkButton();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // not used
                }
            };
    }

    /** EDIT GOAL: FILL IN RIGHT VALUES **/
    private void initGoalValues() {
        // name
        TextView nameOfGoal = (TextView) findViewById(R.id.nameOfGoal);
        nameOfGoal.setText(this.goal.getName());

        // amount
        TextView amountToSave = (TextView) findViewById(R.id.amountToSave);
        amountToSave.setText(this.goal.getAmountTotalNeededString());

        // due date
        CheckBox dateSwitch = (CheckBox) findViewById(R.id.date_switch_goal);
        if(this.goal.hasDueDate()) {
            dateSwitch.setChecked(true);

            TextView dueDateField = (TextView) findViewById(R.id.due_date_field);
            dueDateField.setText(this.goal.getDueDateString());
        }
        else {
            dateSwitch.setChecked(false);
        }

        // reminder
        CheckBox remindSwitch = (CheckBox) findViewById(R.id.remind_switch_goal);
        if(this.goal.hasRemindSetting()) {
            remindSwitch.setChecked(true);

            int radioID = goal.getRemindType().getRadioID();
            ((RadioGroup) findViewById(R.id.radio_remind)).check(radioID);
        }
        else {
            remindSwitch.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_goal, menu);
        return true;
    }

    public void dateSwitch(View dateSwitch) {
        if(((CheckBox) dateSwitch).isChecked()) {
            findViewById(R.id.due_date_field).setVisibility(View.VISIBLE);
            findViewById(R.id.date_format_text).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.due_date_field).setVisibility(View.GONE);
            findViewById(R.id.date_format_text).setVisibility(View.GONE);
            goal.resetDueDate();
        }
        setOkButton();
    }

    public void remindSwitch(View remindSwitch) {
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_remind);
        radioGroup.clearCheck();

        if(((CheckBox) remindSwitch).isChecked()) {
            radioGroup.setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.radio_remind).setVisibility(View.GONE);
            goal.resetRemind();
        }

        setOkButton();
    }

    public void remindRadio(View radioButton) {
        RadioGroup group = (RadioGroup) findViewById(R.id.radio_remind);
        int id = group.getCheckedRadioButtonId();

        for(RemindType remindType: RemindType.values()) {
            if(remindType.hasRadioButton() && remindType.getRadioID() == id) {
                goal.setRemindType(remindType);
                goal.updateNextRemindDate();
            }
            // if the remindType is not found, the goal will have the 'NEVER' default.
        }
        setOkButton();
    }

    public void okButton(View okButton) {
        if(this.goalActivityType == GoalActivityType.ADD) {
            AppContent.getInstance(this).addGoal(this.goal);
        }

        Intent intent = new Intent(this, AddGoalPage2Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), this.goalActivityType);

        startActivity(intent);
        finish();
    }

    public void deleteButton(View deleteButton) {
        // the goal is removed from the AppContent
        AppContent.getInstance(this).deleteCurrentGoal();
        AppContent.getInstance(this).deleteBackUpCurrentGoal();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        // the goal is not stored in the AppContent
        if(this.goalActivityType == GoalActivityType.EDIT) {
            // change the state of the goal back to its former state
            AppContent.getInstance(this).resetCurrentGoalToBackup();
            AppContent.getInstance(this).deleteBackUpCurrentGoal();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setOkButton(){
        boolean dueDateRequired = ((CheckBox) findViewById(R.id.date_switch_goal)).isChecked();
        boolean reminderRequired = ((CheckBox) findViewById(R.id.remind_switch_goal)).isChecked();

        if(this.goal.isValid(dueDateRequired, reminderRequired)){
            this.enableOK(true);
        } else{
            this.enableOK(false);
        }
    }

    private void enableOK(Boolean enable) {
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }

}
