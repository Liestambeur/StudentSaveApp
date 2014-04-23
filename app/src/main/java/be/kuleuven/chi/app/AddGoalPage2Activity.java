package be.kuleuven.chi.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage2Activity extends BaseActivity {

    Map<ImageButton, String> paths;
    int goalActivityType;
    String oldPicture;
   // Drawable oldPicture;

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
            this.oldPicture = AppContent.getInstance(this).getCurrentGoal().getPictureUrl();
            // initGoalValues(); TODO - the current picture should be highlighted
        }

        this.enableOK(true);
    }

    private void fillPicturesList() {
        paths = new HashMap<ImageButton, String>();
        paths.put((ImageButton) findViewById(R.id.goalPicture1), getResources().getString(R.drawable.auto));
        paths.put((ImageButton) findViewById(R.id.goalPicture2), getResources().getString(R.drawable.shopping));
        paths.put((ImageButton) findViewById(R.id.goalPicture3), getResources().getString(R.drawable.bier));
        paths.put((ImageButton) findViewById(R.id.goalPicture4), getResources().getString(R.drawable.valies));
        paths.put((ImageButton) findViewById(R.id.goalPicture5), getResources().getString(R.drawable.spaarvarken));
        paths.put((ImageButton) findViewById(R.id.goalPicture6), getResources().getString(R.drawable.cadeaus));
        paths.put((ImageButton) findViewById(R.id.goalPicture7), getResources().getString(R.drawable.multimedia));
        paths.put((ImageButton) findViewById(R.id.goalPicture8), getResources().getString(R.drawable.gitaar));
        paths.put((ImageButton) findViewById(R.id.goalPicture9), getResources().getString(R.drawable.dotdotdot));
    }

    private void setAllPicturesUnactivated() {
        for(ImageButton picture: paths.keySet()) {
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
            String path = paths.get(((ImageButton) picture));
            AppContent.getInstance(this).getCurrentGoal().setPicture(path);

            //TODO FIX FIX FIX FIX FIX NULLPOINTER
           // AppContent.getInstance(this).getCurrentGoal().setPicture(pictures.get(drawable));
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
            AppContent.getInstance(this).getCurrentGoal().setPicture(oldPicture);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private static int RESULT_LOAD_IMAGE = 1;

    public void externalGoalPicture(View dotdotdotPicture) {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            AppContent.getInstance(this).getCurrentGoal().setPicture(picturePath);

        }


    }

    private void setOkButton(){
        this.enableOK(true);
    }

    public void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}
