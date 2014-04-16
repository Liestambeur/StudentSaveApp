package be.kuleuven.chi.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            this.goal = AppContent.getInstance(this).getCurrentGoal();
            this.oldGoal = AppContent.getInstance(this).getCurrentGoal().getCopy();
            initGoalValues();
        }

        initTextWatchers();
        this.enableOK(false);
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
                String date = charSequence.toString();
                if(date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                    String[] dateParts = date.split("/");
                    try {
                        Calendar calendar = new GregorianCalendar(Integer.valueOf(dateParts[2]),
                                Integer.valueOf(dateParts[1]), Integer.valueOf(dateParts[0]));
                        System.out.println(dateParts);
                        if(calendar.after(new GregorianCalendar())) {
                            goal.setDueDate(calendar);

                        }
                        else {
                            //TODO waarschuwing dat een datum in het verleden niet mag
                        }
                    }
                    catch (NumberFormatException e) {
                        goal.resetDueDate();
                        // do nothing
                    }
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

        Switch date_switch_goal = (Switch) findViewById(R.id.date_switch_goal);
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
        ((Switch) dateSwitch).isChecked();

        if(((Switch) dateSwitch).isChecked()) {
            findViewById(R.id.due_date_field).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.due_date_field).setVisibility(View.INVISIBLE);
            goal.resetDueDate();
        }
        setOkButton();
    }

    public void okButton(View okButton) {
        if(this.goalActivityType == GoalActivityType.EDIT) {
            //TODO when in edit-mode, you go now back to main, editing the picture is not possible yet
            //TODO oldGOal op een of andere manier meegeven aan pagina 2, zodat je rollback kan doen

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            AppContent.getInstance(this).addGoal(this.goal);

            Intent intent = new Intent(this, AddGoalPage2Activity.class);
            intent.putExtra(getResources().getText(R.string.goal_activity_type).toString(), this.goalActivityType);

            startActivity(intent);
            finish();
        }
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
        if(this.goal.isValid(((Switch) findViewById(R.id.date_switch_goal)).isChecked())){
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
