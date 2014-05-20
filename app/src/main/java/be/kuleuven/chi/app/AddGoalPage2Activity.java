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
import java.util.Map;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.GoalActivityType;

/**
 * Created by NeleR on 16/04/2014.
 */
public class AddGoalPage2Activity extends AddGoalPageActivity {

    private Map<Integer, String> paths;
    private Map<String, ImageButton> views;
    private int goalActivityType;
    private String oldPicture;

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

        this.goal = AppContent.getInstance(this).getCurrentGoal();

        if(this.goalActivityType == GoalActivityType.EDIT) {
            findViewById(R.id.delete).setVisibility(View.VISIBLE);
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
        if(views.containsKey(this.goal.getPictureUrl())) {
            views.get(this.goal.getPictureUrl()).setActivated(true);
        }
        else {
            views.get(getResources().getString(R.drawable.dotdotdot));
        }
    }

    public void chooseGoalPicture(View picture) {
        // unselect a selected picture
        if(picture.isActivated()) {
            setAllPicturesUnactivated();
            AppContent.getInstance(this).resetPicture();
        }
        // select an unselected picture
        else{
            setAllPicturesUnactivated();
            picture.setActivated(true);
            AppContent.getInstance(this).setPictureCurrentGoal(paths.get(picture.getId()));
        }
    }

    public void okButton(View okButton) {
        if(this.goalActivityType == GoalActivityType.EDIT) {
            // the backup is no longer needed and can be deleted
            AppContent.getInstance(this).deleteBackUpCurrentGoal();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on ok in second goalpage");

    }

    public void deleteButton(View deleteButton) {
        showDeleteConfirmation();
        sendTracking("clicked on delete in second goalpage");
    }

    public void cancelButton(View cancelButton) {
        if(this.goalActivityType == GoalActivityType.ADD) {
            // the goal is not stored in the AppContent
            AppContent.getInstance(this).deleteCurrentGoal();
        }
        else if(this.goalActivityType == GoalActivityType.EDIT) {
            // the picture of the goal is restored to its older value
            AppContent.getInstance(this).resetCurrentGoalToBackup();
            AppContent.getInstance(this).deleteBackUpCurrentGoal();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //finish(); verwijderd voor back button gedrag
        sendTracking("clicked on cancel in second goalpage");

    }

    private static int RESULT_LOAD_IMAGE = 1;

    public void externalGoalPicture(View dotdotdotPicture) {
//        Intent intent = new Intent(this, ExternalPicutureActivity.class);
 //       startActivity(intent);
  //      finish();

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i,RESULT_LOAD_IMAGE);
        sendTracking("external goal clicked");

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
