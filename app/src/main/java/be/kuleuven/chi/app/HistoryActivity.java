package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.categories.ExpenseCategory;
import be.kuleuven.chi.backend.historyElements.ExpenseElement;
import be.kuleuven.chi.backend.categories.Goal;
import be.kuleuven.chi.backend.categories.IncomeCategory;
import be.kuleuven.chi.backend.historyElements.IncomeElement;
import be.kuleuven.chi.backend.historyElements.SavingElement;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryActivity extends BaseActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_history);



            // TODO verwijder volgende methodeaanroep!!! enkel gebruikt voor het testen!!
            instantiateHardCodeHistoryElements();

            TextView walletTotal = (TextView) findViewById(R.id.walletTotal);
            walletTotal.append(": " + AppContent.getInstance().getWalletTotal());

            ListView historyListView = (ListView) findViewById(R.id.fullHistoryList);
            HistoryElementAdapter adapter = new HistoryElementAdapter(this,R.layout.history_row);
            historyListView.setAdapter(adapter);

        }


    /**
     * TODO - DELETE THIS METHOD WHEN THE CREATING OF HISTORY ELEMENTS WORKS AS INTENDED!
     */
    private void instantiateHardCodeHistoryElements() {
        IncomeElement income = new IncomeElement(50.00, new IncomeCategory("Pocket Money"),"Pocket Money 4/apr");
        ExpenseElement expense = new ExpenseElement(20.00, new ExpenseCategory("Food"),"Groceries");

        Goal paris = new Goal();
        paris.setName("Paris");
        paris.setAmount(100.00);
        SavingElement saving = new SavingElement(20.00, paris);

        AppContent.getInstance().addToHistory(income);
        AppContent.getInstance().addToHistory(expense);
        AppContent.getInstance().addToHistory(saving);
    }


    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.add_goal, menu);
            return true;
        }


}
