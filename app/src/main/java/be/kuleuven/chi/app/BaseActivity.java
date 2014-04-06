package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

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
}
