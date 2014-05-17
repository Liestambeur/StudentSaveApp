package be.kuleuven.chi.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage2Activity extends BaseActivity {

    HashMap<Integer, String> paths;
    HashMap<String, ImageButton> views;
    int goalActivityType;
    String oldPicture;
   // Drawable oldPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_page2);

        // define what type of page this is: add a new goal or edit an existing one
        Intent intent = getIntent();
        this.goalActivityType = intent.getIntExtra(getResources().getText(R.string.goal_activity_type).toString(), GoalActivityType.ADD);

        fillPicturesList();
        setAllPicturesUnactivated();

        if(this.goalActivityType == GoalActivityType.EDIT) {
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
            this.oldPicture = AppContent.getInstance(this).getPictureCurrentGoal();
            initActivePicture();
        }
        // else  if(this.goalActivityType == GoalActivityType.ADD) { // no initialising necessary }

        this.enableOK(true);
    }

    private void fillPicturesList() {
        // TODO kan het met 1 lijst?
        paths = new HashMap<Integer, String>();
        paths.put(findViewById(R.id.goalPicture1).getId(), getResources().getString(R.drawable.auto));
        paths.put(findViewById(R.id.goalPicture2).getId(), getResources().getString(R.drawable.shopping));
        paths.put(findViewById(R.id.goalPicture3).getId(), getResources().getString(R.drawable.bier));
        paths.put(findViewById(R.id.goalPicture4).getId(), getResources().getString(R.drawable.valies));
        paths.put(findViewById(R.id.goalPicture5).getId(), getResources().getString(R.drawable.spaarvarken));
        paths.put(findViewById(R.id.goalPicture6).getId(), getResources().getString(R.drawable.cadeaus));
        paths.put(findViewById(R.id.goalPicture7).getId(), getResources().getString(R.drawable.multimedia));
        paths.put(findViewById(R.id.goalPicture8).getId(), getResources().getString(R.drawable.gitaar));
        paths.put(findViewById(R.id.goalPicture9).getId(), getResources().getString(R.drawable.dotdotdot));

        views = new HashMap<String, ImageButton>();
        views.put(getResources().getString(R.drawable.auto), (ImageButton) findViewById(R.id.goalPicture1));
        views.put(getResources().getString(R.drawable.shopping), (ImageButton) findViewById(R.id.goalPicture2));
        views.put(getResources().getString(R.drawable.bier), (ImageButton) findViewById(R.id.goalPicture3));
        views.put(getResources().getString(R.drawable.valies), (ImageButton) findViewById(R.id.goalPicture4));
        views.put(getResources().getString(R.drawable.spaarvarken), (ImageButton) findViewById(R.id.goalPicture5));
        views.put(getResources().getString(R.drawable.cadeaus), (ImageButton) findViewById(R.id.goalPicture6));
        views.put(getResources().getString(R.drawable.multimedia), (ImageButton) findViewById(R.id.goalPicture7));
        views.put(getResources().getString(R.drawable.gitaar), (ImageButton) findViewById(R.id.goalPicture8));
        views.put(getResources().getString(R.drawable.dotdotdot), (ImageButton) findViewById(R.id.goalPicture9));
    }

    private void setAllPicturesUnactivated() {
        for(ImageButton picture: views.values()) {
            picture.setActivated(false);
        }
    }

    private void initActivePicture() {
        if(views.containsKey(oldPicture)) {
            views.get(oldPicture).setActivated(true);
        }
        else {
            views.get(getResources().getString(R.drawable.dotdotdot));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_goal, menu);
        return true;
    }

    public void chooseGoalPicture(View picture) {
        // unselect a selected picture
        if(picture.isActivated()) {
            setAllPicturesUnactivated();
            AppContent.getInstance(this).resetPictureCurrentGoal();
        }
        // select an unselected picture
        else{
            setAllPicturesUnactivated();
            picture.setActivated(true);
            AppContent.getInstance(this).setPictureCurrentGoal(paths.get(picture.getId()));

            //NULLPOINTER
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
        // TODO ask for confirmation of delete
        AppContent.getInstance(this).deleteCurrentGoal();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cancelButton(View cancelButton) {
        if(this.goalActivityType == GoalActivityType.ADD) {
            // the goal is not stored in the AppContent
            AppContent.getInstance(this).deleteCurrentGoal();
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            // the picture of the goal is restored to its older value
            AppContent.getInstance(this).setPictureCurrentGoal(oldPicture);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void externalGoalPicture(View dotdotdotPicture) {
        Intent intent = new Intent(this, ExternalPicutureActivity.class);
        startActivity(intent);
        finish();

//        Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_LOAD_IMAGE = 1;
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            AppContent.getInstance(this).setPictureCurrentGoal(picturePath);

        }


    }

    private void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}
