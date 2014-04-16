package be.kuleuven.chi.app;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chi.backend.AppContent;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage2Activity extends BaseActivity {

    List<ImageButton> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page2);

        fillPicturesList();
        setAllPicturesUnactivated();
        this.enableOK(true);
        ImageButton image = (ImageButton) findViewById(R.id.goalPicture5);
        this.chooseGoalPicture(image);
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
            AppContent.getInstance(this).getCurrentGoal().resetPicture();
        }
        else{
            setAllPicturesUnactivated();
            ((ImageButton) picture).setActivated(true);

            Drawable drawable = ((ImageButton) picture).getDrawable();
            AppContent.getInstance(this).getCurrentGoal().setPicture(drawable);
        }
    }

    public void okButton(View okButton) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        // the goal is not stored in the AppContent
        AppContent.getInstance(this).deleteGoal(AppContent.getInstance(this).getCurrentGoal());

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
