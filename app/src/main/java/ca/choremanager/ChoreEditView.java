package ca.choremanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static ca.choremanager.R.id.editName;
import static ca.choremanager.R.id.editNotes;
import static ca.choremanager.R.id.recurring_spinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreEditView extends AppCompatActivity {
    EditText mNotes, mName;
    Spinner mRecurring;
    EditText dateEntry;
    private Chore chore;
    DatabaseReference dR;


    protected void onCreate(Bundle savedInstanceState) {
         setTheme(R.style.AppTheme);
         setContentView(R.layout.activity_chore_edit);

        super.onCreate(savedInstanceState);
        addItemsOnRecurring();
    }

    public void addItemsOnRecurring() {

        Spinner spinner = findViewById(recurring_spinner);
        List<String> list = new ArrayList<String>();
        list.add("Set Recurring");
        list.add("Not Recurring");
        list.add("Daily");
        list.add("Weekly");
        list.add("Monthly");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    protected void editChore(View view){
        mName  = findViewById(editName);
        chore.setName(mName.getText().toString());
        mNotes = findViewById(editNotes);
        mRecurring = findViewById(recurring_spinner);
        chore.setDescription(mNotes.getText().toString());
        if (mRecurring.toString() != "Set Recurring") {chore.setRecurring(mRecurring.toString());}
        dR.setValue(chore);
    }

    protected void cancelEdit(View view){
        finish();
    }
}
