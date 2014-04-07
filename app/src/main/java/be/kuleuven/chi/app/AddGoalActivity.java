package be.kuleuven.chi.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.categories.Goal;

public class AddGoalActivity extends BaseActivity {

    Goal newGoal;
    List<ImageButton> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // TODO zorg dat focus niet op 'name of goal' ligt: tekst mag niet geselecteerd zijn bij opstarten

        newGoal = new Goal();

        initTextWatchers();
        fillPicturesList();
        setAllPicturesUnactivated();
    }

    private void initTextWatchers() {
        EditText nameOfGoalText = (EditText) findViewById(R.id.nameOfGoal);
        EditText amountOfGoalText = (EditText) findViewById(R.id.amountToSave);

        TextWatcher nameOfGoalWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                newGoal.setName(charSequence.toString());
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
                    // do nothing
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // not used
            }
        };

        nameOfGoalText.addTextChangedListener(nameOfGoalWatcher);
        amountOfGoalText.addTextChangedListener(amountOfGoalWatcher);
    }

    private void fillPicturesList() {
        pictures = new ArrayList<ImageButton>();
        pictures.add((ImageButton) findViewById(R.id.goalPicture1));
        pictures.add((ImageButton) findViewById(R.id.goalPicture2));
        pictures.add((ImageButton) findViewById(R.id.goalPicture3));
        pictures.add((ImageButton) findViewById(R.id.goalPicture4));
        pictures.add((ImageButton) findViewById(R.id.goalPicture5));
        pictures.add((ImageButton) findViewById(R.id.goalPicture6));
        pictures.add((ImageButton) findViewById(R.id.goalPicture7));
        pictures.add((ImageButton) findViewById(R.id.goalPicture8));
        pictures.add((ImageButton) findViewById(R.id.goalPicture9));
    }

    private void setAllPicturesUnactivated() {
        for(ImageButton picture: pictures) {
            picture.setActivated(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_goal, menu);
        return true;
    }


    public void chooseGoalPicture(View picture) {
        if(((ImageButton) picture).isActivated()) {
            setAllPicturesUnactivated();
            this.newGoal.resetPicture();
        }
        else{
            setAllPicturesUnactivated();
            ((ImageButton) picture).setActivated(true);

            Drawable drawable = ((ImageButton) picture).getDrawable();
            this.newGoal.setPicture(drawable);
        }
    }

    public void okButton(View okButton) {
        AppContent.getInstance().addGoal(this.newGoal);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        // the goal is not stored in the AppContent

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void externalGoalPicture(View dotdotdotPicture) {
        Intent intent = new Intent(this, ExternalPicutureActivity.class);
        startActivity(intent);
        finish();
    }
}
