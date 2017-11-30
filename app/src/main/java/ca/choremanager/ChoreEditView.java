package ca.choremanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    String choreId;

    protected void onCreate(Bundle savedInstanceState) {
         setTheme(R.style.AppTheme);
         setContentView(R.layout.activity_chore_edit);
        super.onCreate(savedInstanceState);
        mName  = findViewById(editName);
        mNotes = findViewById(editNotes);
        Intent ii = getIntent();
        choreId = (String)ii.getExtras().get("id");
        dR = FirebaseDatabase.getInstance().getReference("chore").child(choreId);
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chore = dataSnapshot.getValue(Chore.class);
                mName.setText(chore.getName());
                mNotes.setText(chore.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
        chore.setName(mName.getText().toString());
        mRecurring = findViewById(recurring_spinner);
        chore.setDescription(mNotes.getText().toString());
        if (mRecurring.toString() != "Set Recurring") {chore.setRecurring(mRecurring.getSelectedItem().toString());}
        dR.setValue(chore);
        finish();
    }

    protected void cancelEdit(View view){
        finish();
    }
}
