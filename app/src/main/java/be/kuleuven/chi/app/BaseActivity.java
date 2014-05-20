package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Lies on 6/04/14.
 */
class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void enableLinear(LinearLayout view, Boolean enable){
        for(int i = 0; i<view.getChildCount();i++){
            view.getChildAt(i).setEnabled(enable);
        }
        view.setEnabled(enable);
        if(enable){
            view.setAlpha(1);
        } else{
            view.setAlpha(new Float(0.6));
        }
    }
}
