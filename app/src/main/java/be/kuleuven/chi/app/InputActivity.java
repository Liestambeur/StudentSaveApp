package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import be.kuleuven.chi.backend.InputActivityType;

/**
 * Created by joren on 4/4/14.
 */
public class InputActivity extends Activity {

    String inputActivityType;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //Gewoon om die INPUT_ACTIVITY_TYPE string niet overal te hardcoden staat hij in strings.xml
        inputActivityType = intent.getStringExtra(
                getResources().getText(R.string.input_activity_type).toString());
        int categories = InputActivityType.valueOf(inputActivityType).getCategories();
        setContentView(R.layout.activity_input);
        ListView categoryList = (ListView) findViewById(R.id.categoryListView);
        categoryList.setAdapter(new SimpleListAdapter());
    }

    private class SimpleArrayAdapter extends ArrayAdapter<String> {

        public SimpleArrayAdapter(Context context, int resource) {
            super(context, resource);
        }
    }

}