package ca.choremanager;

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
import java.util.Date;
import java.util.List;

import static ca.choremanager.R.id.addPoints;
import static ca.choremanager.R.id.editName;
import static ca.choremanager.R.id.editNotes;
import static ca.choremanager.R.id.recurring_spinner;
import static ca.choremanager.R.id.userSpinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreAddView extends AppCompatActivity {
    EditText mNotes, mName, mPoints;
    Spinner mRecurring, mUsers;
    EditText dateEntry;
    DatabaseReference dR;
    String choreId;
    List<String> list2 = new ArrayList<>();
    private Chore chore;

    protected void onCreate(Bundle savedInstanceState) {
        List<String> list2 = new ArrayList<String>();
         setTheme(R.style.AppTheme);
         setContentView(R.layout.activity_chore_add);
        super.onCreate(savedInstanceState);
        mName  = findViewById(editName);
        mNotes = findViewById(editNotes);
        mUsers = findViewById(userSpinner);
        mPoints = findViewById(addPoints);
        addItemsOnUser();
    }

    protected void addChore(View view){
        dR = FirebaseDatabase.getInstance().getReference("chore");
        String name = mName.getText().toString();
        mRecurring = findViewById(recurring_spinner);
        String notes = mNotes.getText().toString();
        Integer points = Integer.parseInt(mPoints.getText().toString());
        String user = list2.get(mUsers.getSelectedItemPosition());

        String id = dR.push().getKey();
        chore = new Chore(id, name, new Date(0, 0, 0), points, "", notes, user);
        dR.child(id).setValue(chore);
        finish();
    }
    protected void addItemsOnUser(){
        final Spinner spinner = findViewById(userSpinner);
        final List<String> list = new ArrayList<String>();
        list.add("Unassigned");
        list2.add("");

        dR = FirebaseDatabase.getInstance().getReference("user");
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    list.add(user.getName());
                    list2.add(user.getId());
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
