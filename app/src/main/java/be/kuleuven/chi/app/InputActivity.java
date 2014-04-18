package be.kuleuven.chi.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

    private boolean userClickedYet = true;
    AlertDialog ad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //Gewoon om die INPUT_ACTIVITY_TYPE string niet overal te hardcoden staat hij in strings.xml
        inputActivityType = intent.getStringExtra(getResources().getText(R.string.input_activity_type).toString());
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
                setSelected(view);
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
                try{
                    setInputAmount(Double.parseDouble(s.toString()));
                }catch(NumberFormatException e){
                    setInputAmount(Double.parseDouble("0"));
                }

            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Save Screen
        if(inputActivityType.equals(InputActivityType.SAVE.name())){
            categoryList.setVisibility(View.INVISIBLE);
            nameText.setVisibility(View.INVISIBLE);
            TextView pick = (TextView) findViewById(R.id.pick_category);
            if(pick!=null) pick.setText(R.string.dont_forget_piggybank);
        }
        this.enableOK(false);
    }

    /**
     * Selects the view (makes all other views in the list white and this one gray, sets the category accordingly)
     * @param view
     */
    public void setSelected(View view){
        ListView list = (ListView) findViewById(R.id.categoryListView);
        for(int i =0; i<list.getChildCount(); i++){
            list.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        view.setBackgroundColor(Color.LTGRAY);
        Context context = view.getContext();
        TextView categoryTextView = ((TextView) view.findViewById(R.id.categoryTextView));
        String listItemText = categoryTextView.getText().toString();
        setSelectedCategory(listItemText);
    }

    public void setSelectedCategory(String listItemText) {
        this.selectedItem = listItemText;
    }

    public void setInputName(String name) {
        this.inputName = name;
    }

    public void setInputAmount(Double amount) {
        System.out.println(amount);
        if(amount!=0){
            this.enableOK(true);
        } else{
            this.enableOK(false);
        }
        this.inputAmount = amount;
    }



    public void cancelButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void okButton(View view) {
        // TODO Minder vuil maken
        inputName = inputName==null?"Other":inputName;
        selectedItem = selectedItem==null?"Other":selectedItem;
        inputAmount = inputAmount==null?0.0:inputAmount;
        HistoryElement he;
        AppContent instance = AppContent.getInstance(this);
        userClickedYet = true;
        if(inputActivityType.equals(InputActivityType.INCOME.name())){
            he = new IncomeElement(inputAmount, new IncomeCategory(selectedItem),inputName);
            instance.addToHistory(he);
        } else if(inputActivityType.equals(InputActivityType.EXPENSE.name())){
            he = new ExpenseElement(inputAmount, new ExpenseCategory(selectedItem), inputName);
            instance.addToHistory(he);
        } else {
            Goal goal = instance.getCurrentGoal();
            if(goal != null){
                he = new SavingElement(inputAmount, instance.getCurrentGoal());
                instance.addToHistory(he);
                instance.getCurrentGoal().addAmount(inputAmount);
            } else {
                userClickedYet = false;
                TextView alertView = new TextView(this);
                alertView.setText(getResources().getString(R.string.goal_not_initialized));
                ad = new AlertDialog.Builder(this).
                        setView(alertView).setTitle("Warning!").show();
                alertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissAd();
                    }
                });
            }
        }

        if(userClickedYet){
            backToMain();
        }
    }

    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void dismissAd(){
        ad.dismiss();
        backToMain();
    }

    public void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}