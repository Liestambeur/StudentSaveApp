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
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage1Activity extends BaseActivity {

    Goal goal;
    Goal oldGoal;
    int goalActivityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page1);

        Intent intent = getIntent();
        this.goalActivityType = intent.getIntExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);

        // TODO zorg dat focus niet op 'name of goal' ligt: tekst mag niet geselecteerd zijn bij opstarten

        if(this.goalActivityType == GoalActivityType.ADD) {
            goal = new Goal();
            this.enableOK(false);
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            findViewById(R.id.delete).setVisibility(View.VISIBLE);

            this.goal = AppContent.getInstance(this).getCurrentGoal();
            this.oldGoal = AppContent.getInstance(this).getCurrentGoal().getCopy();
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
                //TODO reset remind optie van goal
            }
        }

        initTextWatchers();
    }

    private void initTextWatchers() {
        EditText nameOfGoalText = (EditText) findViewById(R.id.nameOfGoal);
        EditText amountOfGoalText = (EditText) findViewById(R.id.amountToSave);
        EditText dueDateText = (EditText) findViewById(R.id.due_date_field);

        TextWatcher nameOfGoalWatcher = new TextWatcher() {
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
        TextWatcher amountOfGoalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                try {
                    goal.setAmount(Double.parseDouble(charSequence.toString()));
                }
                catch (NumberFormatException e) {
                    goal.setAmount(Double.parseDouble("0"));
                    // do nothing
                }
                setOkButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // not used
            }
        };
        TextWatcher dueDateWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                findViewById(R.id.date_no_past_text).setVisibility(View.GONE);
                String date = charSequence.toString();

                if(date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    findViewById(R.id.date_format_text).setVisibility(View.GONE);

                    String[] dateParts = date.split("/");
                    try {
                        Calendar calendar = new GregorianCalendar(Integer.valueOf(dateParts[2]),
                                Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[0]));
                        System.out.println(dateParts);
                        if(calendar.after(new GregorianCalendar())) {
                            findViewById(R.id.date_no_past_text).setVisibility(View.GONE);
                            goal.setDueDate(calendar);
                        }
                        else {
                            findViewById(R.id.date_no_past_text).setVisibility(View.VISIBLE);
                        }
                    }
                    catch (NumberFormatException e) {
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

        nameOfGoalText.addTextChangedListener(nameOfGoalWatcher);
        amountOfGoalText.addTextChangedListener(amountOfGoalWatcher);
        dueDateText.addTextChangedListener(dueDateWatcher);
    }

    private void initGoalValues() {
        TextView nameOfGoal = (TextView) findViewById(R.id.nameOfGoal);
        nameOfGoal.setText(this.goal.getName());

        TextView amountToSave = (TextView) findViewById(R.id.amountToSave);
        amountToSave.setText(Double.toString(this.goal.getAmount()));

        CheckBox date_switch_goal = (CheckBox) findViewById(R.id.date_switch_goal);
        if(this.goal.getDueDate() != null) {
            date_switch_goal.setChecked(true);

            TextView due_date_field = (TextView) findViewById(R.id.due_date_field);
            due_date_field.setText(this.goal.getDueDateString());
        }
        else {
            date_switch_goal.setChecked(false);
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

    private boolean reminder = false;

    public void remindSwitch(View remindSwitch) {
        if(((CheckBox) remindSwitch).isChecked()) {
            findViewById(R.id.radio_remind).setVisibility(View.VISIBLE);
            reminder = true;
        }
        else {
            findViewById(R.id.radio_remind).setVisibility(View.GONE);
            ((RadioGroup) findViewById(R.id.radio_remind)).clearCheck();
            //TODO reset remind optie van goal
            goal.resetRemind();
            reminder = false;
        }
        setOkButton();
    }

    // TODO registreer optie van RadioButtons en implementeer het herinneren

    public void okButton(View okButton) {
        if(reminder){
            RadioGroup group = (RadioGroup) findViewById(R.id.radio_remind);
            int id = group.getCheckedRadioButtonId();
            long time = 0;
            if(id==R.id.radio_day){
                time = TimeUnit.DAYS.toMillis(1);
            }else if(id==R.id.radio_week){
                time = TimeUnit.DAYS.toMillis(7);
            }else if(id==R.id.radio_month){
                time = TimeUnit.DAYS.toMillis(30);
            }
           // time = TimeUnit.MINUTES.toMillis(1);
            goal.setMillisecondsToBeReminded(time);
        }


        // TODO de oude goal gaat hier verloren, alle wijzigingen die je hebt aangebracht zijn permanent
        // TODO is dit wat de gebruiker verwacht? of wil hij op volgende pagina nog volledig kunnen rollbacken?
        AppContent.getInstance(this).addGoal(this.goal);

        Intent intent = new Intent(this, AddGoalPage2Activity.class);
        intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), this.goalActivityType);

        startActivity(intent);
        finish();
    }

    public void deleteButton(View deleteButton) {
         // the goal is removed from the AppContent
         AppContent.getInstance(this).deleteGoal(AppContent.getInstance(this).getCurrentGoal());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        // the goal is not stored in the AppContent
        if(this.goalActivityType == GoalActivityType.EDIT) {
            // change the state of the goal back to its former state
            this.goal.copyState(this.oldGoal);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setOkButton(){
        if(this.goal.isValid(((CheckBox) findViewById(R.id.date_switch_goal)).isChecked())){
            this.enableOK(true);
        } else{
            this.enableOK(false);
        }
    }

    public void enableOK(Boolean enable) {
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }

}
