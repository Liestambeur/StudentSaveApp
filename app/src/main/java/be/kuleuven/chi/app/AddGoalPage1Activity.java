package be.kuleuven.chi.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage1Activity extends BaseActivity {

    Goal newGoal;
    DatePickerFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page1);

        // TODO zorg dat focus niet op 'name of goal' ligt: tekst mag niet geselecteerd zijn bij opstarten

        newGoal = new Goal();

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
                newGoal.setName(charSequence.toString());
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
                    newGoal.setAmount(Double.parseDouble(charSequence.toString()));
                }
                catch (NumberFormatException e) {
                    newGoal.setAmount(Double.parseDouble("0"));
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
                            newGoal.setDueDate(calendar);

                        }
                        else {
                            //TODO waarschuwing dat een datum in het verleden niet mag
                        }
                    }
                    catch (NumberFormatException e) {
                        newGoal.resetDueDate();
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
            newGoal.resetDueDate();
        }
        setOkButton();
    }

    public void okButton(View okButton) {


        AppContent.getInstance(this).addGoal(this.newGoal);

        Intent intent = new Intent(this, AddGoalPage2Activity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        // the goal is not stored in the AppContent

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setOkButton(){
        if(this.newGoal.isValid(((Switch) findViewById(R.id.date_switch_goal)).isChecked())){
            this.enableOK(true);
        } else{
            this.enableOK(false);
        }
    }

    public void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        Calendar calendar;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default values for the picker
            final Calendar calendar = new GregorianCalendar();
            int year = calendar.YEAR;
            int month = calendar.MONTH;
            int day = calendar.DAY_OF_MONTH;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Called when the date picker is changed
            calendar.set(year, month, day);
            System.out.println("Date picked: " + year + "-" + month + "-" + day);
        }

        public Calendar getCalendar() {
            return this.calendar;
        }
    }

}
