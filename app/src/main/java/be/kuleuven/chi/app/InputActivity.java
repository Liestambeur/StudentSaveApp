package be.kuleuven.chi.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.ExpenseCategory;
import be.kuleuven.chi.backend.categories.Goal;
import be.kuleuven.chi.backend.categories.IncomeCategory;
import be.kuleuven.chi.backend.historyElements.ExpenseElement;
import be.kuleuven.chi.backend.historyElements.HistoryElement;
import be.kuleuven.chi.backend.historyElements.IncomeElement;
import be.kuleuven.chi.backend.historyElements.SavingElement;

/**
 * Created by joren on 4/4/14.
 */
public class InputActivity extends BaseActivity {

    String inputActivityType;
    private String selectedItem;
    private String inputName;
    private Double inputAmount;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //Gewoon om die INPUT_ACTIVITY_TYPE string niet overal te hardcoden staat hij in strings.xml
        inputActivityType = intent.getStringExtra(
                getResources().getText(R.string.input_activity_type).toString());
        int categories = InputActivityType.valueOf(inputActivityType).getCategories();
        setContentView(R.layout.activity_input);

        //Listview stuff
        ListView categoryList = (ListView) findViewById(R.id.categoryListView);
        CategoryElementAdapter adapter =
                new CategoryElementAdapter(inputActivityType,this,R.layout.category_list_row);
        categoryList.setAdapter(adapter);
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                TextView categoryTextView = ((TextView) view.findViewById(R.id.categoryTextView));
                String listItemText = categoryTextView.getText().toString();
                setSelectedCategory(listItemText);
            }
        });

        //EditText stuff
        EditText nameText = (EditText) findViewById(R.id.enter_name_et);
        EditText amountText = (EditText) findViewById(R.id.enter_amount_et);
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setInputName(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setInputAmount(Double.parseDouble(s.toString()));
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    public void setSelectedCategory(String listItemText) {
        this.selectedItem = listItemText;
    }

    public void setInputName(String name) {
        this.inputName = name;
    }

    public void setInputAmount(Double amount) {
        this.inputAmount = amount;
    }



    public void cancelButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void confirmInput(View view) {
        // TODO Minder vuil maken
        HistoryElement he;
        if(inputActivityType.equals(InputActivityType.INCOME.name())){
            he = new IncomeElement(inputAmount, new IncomeCategory(selectedItem),inputName);
            AppContent.getInstance(this).addToHistory(he);
        } else if(inputActivityType.equals(InputActivityType.EXPENSE.name())){
            he = new ExpenseElement(inputAmount, new ExpenseCategory(selectedItem), inputName);
            AppContent.getInstance(this).addToHistory(he);
        } else {
            Goal goal = AppContent.getInstance(this).getGoal();
            if(goal != null){
                he = new SavingElement(inputAmount, AppContent.getInstance(this).getGoal());
                AppContent.getInstance(this).addToHistory(he);
            } else {
                TextView alertView = new TextView(this);
                alertView.setText(getResources().getString(R.string.goal_not_initialized));
                AlertDialog ad = new AlertDialog.Builder(this).
                        setView(alertView).setTitle("Warning!").show();
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}