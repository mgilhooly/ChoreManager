package ca.choremanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static ca.choremanager.R.id.recurring_spinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreEditView extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         setTheme(R.style.AppTheme);
         setContentView(R.layout.activity_chore_edit);

        super.onCreate(savedInstanceState);
        addItemsOnRecurring();
    }

    public void addItemsOnRecurring() {

        Spinner spinner = (Spinner) findViewById(recurring_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Daily");
        list.add("Weekly");
        list.add("Monthly");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
}
