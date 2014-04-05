package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.History;
import be.kuleuven.chi.backend.HistoryElement;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

            TextView walletTotal = (TextView) findViewById(R.id.walletTotal);
            walletTotal.append(": " + AppContent.getInstance().getWalletTotal());

            ListView historyListView = (ListView) findViewById(R.id.fullHistoryList);
            HistoryElementAdapter adapter = new HistoryElementAdapter(this,1);//android.R.layout.history_row);
            historyListView.setAdapter(adapter);
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.add_goal, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

}
