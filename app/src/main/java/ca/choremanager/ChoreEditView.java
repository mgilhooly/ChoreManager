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
import static ca.choremanager.R.id.userSpinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreEditView extends AppCompatActivity {
    EditText mNotes, mName;
    Spinner mRecurring, mUsers;
    EditText dateEntry;
    private Chore chore;
    DatabaseReference dR;
    String choreId;
    List<User> list2 = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        List<String> list2 = new ArrayList<String>();
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
        addItemsOnUser();
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
        if (list2.get(mUsers.getSelectedItemPosition()) != chore.getUser()){chore.setUser(list2.get(mUsers.getSelectedItemPosition()));
        dR.setValue(chore);
        finish();
    }
    protected void addItemsOnUser(){
        final Spinner spinner = findViewById(userSpinner);
        final List<String> list = new ArrayList<String>();

        dR = FirebaseDatabase.getInstance().getReference("user");
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    list.add(user.getName());
                    list2.add(user);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    protected void cancelEdit(View view){
        finish();
    }

}
