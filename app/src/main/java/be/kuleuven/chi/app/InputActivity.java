package be.kuleuven.chi.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

    private String walletTotal;
    private double walletAmount;
    private AppContent appContent;
    private String inputActivityType;
    private String selectedItem;
    private String inputName;
    private Double inputAmount;

    private boolean userClickedYet = true;
    private AlertDialog ad;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        appContent = AppContent.getInstance(getApplicationContext());
        walletTotal = appContent.getWalletTotal();
        walletAmount = appContent.getWalletTotalAmount();

        //Gewoon om die INPUT_ACTIVITY_TYPE string niet overal te hardcoden staat hij in strings.xml
        inputActivityType = intent.getStringExtra(getResources().getText(R.string.input_activity_type).toString());
        int categories = InputActivityType.valueOf(inputActivityType).getCategories();
        setContentView(R.layout.activity_input);

        //Textview
        TextView inWalletText = (TextView) findViewById(R.id.wallet_left);
        inWalletText.append(" " + walletTotal);

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
        TextWatcher amountWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    setInputAmount(Double.parseDouble(s.toString()));
                } catch (NumberFormatException e) {
                    setInputAmount(Double.parseDouble("0"));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        //It isn't added immediately, we check if this is the save screen first


        //Save Screen
        if(inputActivityType.equals(InputActivityType.SAVE.name())){
            categoryList.setVisibility(View.INVISIBLE);
            nameText.setVisibility(View.INVISIBLE);
            nameText.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 0));
            categoryList.setLayoutParams(new LinearLayout.LayoutParams(0,0, (float) 0.5));
            nameText.setImeOptions(EditorInfo.IME_ACTION_NONE);
            amountText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            TextView pick = (TextView) findViewById(R.id.pick_category);
            if(pick!=null) pick.setText(R.string.dont_forget_piggybank);

            EditText et = (EditText) findViewById(R.id.enter_amount_et);
            final double togo = appContent.getCurrentGoal().getAmountToGo();
            final double available = appContent.getWalletTotalAmount();

            amountWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    String amount = charSequence.toString();
                    double v;
                    try{
                        if(amount.equals("")){
                            v = 0;
                        } else {
                            v = Double.parseDouble(amount);
                        }
                    } catch (NumberFormatException e) {
                        v = -1;
                    }

                    TextView amount_warning = (TextView) findViewById(R.id.amount_warning);
                    if(v < 0) {
                        amount_warning.setText(R.string.amount_something_wrong);
                        amount_warning.setVisibility(View.VISIBLE);
                        enableOK(false);
                    } else if(v > available) {
                        amount_warning.setText(R.string.amount_too_much);
                        amount_warning.setVisibility(View.VISIBLE);
                        enableOK(false);
                    } else if(v > togo) {
                        amount_warning.setText(R.string.amount_over_left);
                        amount_warning.append(AppContent.getInstance(InputActivity.this).getCurrencySymbol() + " " + String.valueOf(togo));
                        amount_warning.setVisibility(View.VISIBLE);
                        enableOK(false);
                    } else {
                        // It should be OK then
                        amount_warning.setVisibility(View.GONE);
                        setInputAmount(v);
                        enableOK(true);
                    }
                }
                @Override
                public void afterTextChanged(Editable editable) {}
            };
            amountText.removeTextChangedListener(amountWatcher);

        }
        if(inputActivityType.equals(InputActivityType.EXPENSE.name())){
            EditText et = (EditText) findViewById(R.id.enter_amount_et);
            double available = appContent.getWalletTotalAmount();
            et.setFilters(new InputFilter[]{ new InputFilterMinMax(0, available)});
        }

        amountText.addTextChangedListener(amountWatcher);
        this.enableOK(false);
    }

    /**
     * Selects the view (makes all other views in the list white and this one gray, sets the category accordingly)
     * @param view
     */
    void setSelected(View view){
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

    void setSelectedCategory(String listItemText) {
        this.selectedItem = listItemText;
    }

    void setInputName(String name) {
        this.inputName = name;
    }

    void setInputAmount(Double amount) {
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
        inputName = (inputName==null || inputName=="")?"":inputName;
        selectedItem = selectedItem==null?"Other":selectedItem;
        inputAmount = inputAmount==null?0.0:inputAmount;
        if(inputAmount==null || inputAmount==0){
            alertUser(getResources().getString(R.string.fill_in_amount), false);
        } else if(!(inputActivityType.equals(InputActivityType.INCOME.name())) && inputAmount>walletAmount){
            alertUser(getResources().getString(R.string.not_enough_money), false);
        } else {
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
                    instance.getCurrentGoal().addAmountSaved(inputAmount);
                } else {
                    userClickedYet = false;
                    alertUser(getResources().getString(R.string.goal_not_initialized), true);
                }
            }
            if(userClickedYet){
                backToMain();
            }
        }
    }

    private void alertUser(String text, final boolean backToMain) {
        TextView alertView = new TextView(this);
        alertView.setText(text);
        ad = new AlertDialog.Builder(this).
                setView(alertView).setTitle("Warning!").show();
        alertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAd(backToMain);
            }
        });
    }

    void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void dismissAd(boolean backToMain){
        ad.dismiss();
        if(backToMain) backToMain();
    }

    void enableOK(Boolean enable){
        LinearLayout ok = (LinearLayout) findViewById(R.id.ok);
        this.enableLinear(ok, enable);
    }
}