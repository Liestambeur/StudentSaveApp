package be.kuleuven.chi.app;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryActivity extends BaseActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_history);

            TextView walletTotal = (TextView) findViewById(R.id.walletTotal);
            walletTotal.append(": " + AppContent.getInstance(this).getWalletTotal());

            ListView historyListView = (ListView) findViewById(R.id.fullHistoryList);
            HistoryElementAdapter adapter = new HistoryElementAdapter(this,R.layout.history_row);
            historyListView.setAdapter(adapter);

        }

    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.add_goal, menu);
            return true;
        }


}
