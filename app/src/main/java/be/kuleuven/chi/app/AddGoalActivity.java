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
import android.widget.LinearLayout;

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

        this.enableOK(false);
        ImageButton image = (ImageButton) findViewById(R.id.goalPicture5);
        this.chooseGoalPicture(image);
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
                    newGoal.setAmountTotalNeeded(Double.parseDouble(charSequence.toString()));
                }
                catch (NumberFormatException e) {
                    newGoal.setAmountTotalNeeded(Double.parseDouble("0"));
                    // do nothing
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
        if(picture.isActivated()) {
            setAllPicturesUnactivated();
            this.newGoal.resetPicture();
        }
        else{
            setAllPicturesUnactivated();
            picture.setActivated(true);

            Drawable drawable = ((ImageButton) picture).getDrawable();
            System.out.println("Als ge deze print ziet check AddGoalActivity");
            //Uit luiheid uitgecomment omdat ik er van overtuigd ben dat deze klasse niet meer
            //gebruikt wordt en geen zin heb om dit helemaal naar het nieuwe systeem om te zetten.
            //this.newGoal.setPicture(drawable);
        }
    }

    public void okButton(View okButton) {
        AppContent.getInstance(this).addGoal(this.newGoal);

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

    private void setOkButton(){
        if(this.newGoal.isValid(false)){
            this.enableOK(true);
        } else{
            this.enableOK(false);
        }
    }

    public void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}
