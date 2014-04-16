package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Lies on 6/04/14.
 */
public class BaseActivity extends Activity{


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

    public void enableLinear(LinearLayout view, Boolean enable){
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
