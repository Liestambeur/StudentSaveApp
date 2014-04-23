package be.kuleuven.chi.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage2Activity extends BaseActivity {

    Map<ImageButton, Integer> pictures;
    int goalActivityType;
    Drawable oldPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page2);

        Intent intent = getIntent();
        this.goalActivityType = intent.getIntExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);

        fillPicturesList();
        setAllPicturesUnactivated();

        if(this.goalActivityType == GoalActivityType.ADD) {
            // do nothing in preperation
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
            this.oldPicture = getResources().getDrawable(AppContent.getInstance(this).getCurrentGoal().getPicture());
            // initGoalValues(); TODO - the current picture should be highlighted
        }

        this.enableOK(true);
    }

    private void fillPicturesList() {
        pictures = new HashMap<ImageButton, Integer>();
        pictures.put((ImageButton) findViewById(R.id.goalPicture1), R.id.goalPicture1);
        pictures.put((ImageButton) findViewById(R.id.goalPicture2), R.id.goalPicture2);
        pictures.put((ImageButton) findViewById(R.id.goalPicture3), R.id.goalPicture3);
        pictures.put((ImageButton) findViewById(R.id.goalPicture4), R.id.goalPicture4);
        pictures.put((ImageButton) findViewById(R.id.goalPicture5), R.id.goalPicture5);
        pictures.put((ImageButton) findViewById(R.id.goalPicture6), R.id.goalPicture6);
        pictures.put((ImageButton) findViewById(R.id.goalPicture7), R.id.goalPicture7);
        pictures.put((ImageButton) findViewById(R.id.goalPicture8), R.id.goalPicture8);
        pictures.put((ImageButton) findViewById(R.id.goalPicture9), R.id.goalPicture9);
    }

    private void setAllPicturesUnactivated() {
        for(ImageButton picture: pictures.keySet()) {
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
            AppContent.getInstance(this).getCurrentGoal().resetPicture();
        }
        else{
            setAllPicturesUnactivated();
            ((ImageButton) picture).setActivated(true);

            Drawable drawable = ((ImageButton) picture).getDrawable();
            //TODO FIX FIX FIX FIX FIX NULLPOINTER
            AppContent.getInstance(this).getCurrentGoal().setPicture(pictures.get(drawable));
        }
    }

    public void okButton(View okButton) {
        Intent intent = new Intent(this, MainActivity.class);
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

        if(this.goalActivityType == GoalActivityType.ADD) {
            // the goal is not stored in the AppContent
            AppContent.getInstance(this).deleteGoal(AppContent.getInstance(this).getCurrentGoal());
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            // the picture of the goal is restored to its older value
            AppContent.getInstance(this).getCurrentGoal().setPicture(pictures.get(this.oldPicture));
        }

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
        this.enableOK(true);
    }

    public void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}
