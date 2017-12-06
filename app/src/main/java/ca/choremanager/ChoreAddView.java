package ca.choremanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.choremanager.R.id.addDate;
import static ca.choremanager.R.id.addPoints;
import static ca.choremanager.R.id.editName;
import static ca.choremanager.R.id.editNotes;
import static ca.choremanager.R.id.userSpinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreAddView extends AppCompatActivity {
    EditText mNotes, mName, mPoints;
    Spinner mUsers;
    EditText dateEntry;
    DatabaseReference dR;
    List<String> list2 = new ArrayList<>();
    private Chore chore;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_chore_add);
        super.onCreate(savedInstanceState);
        // Find all elements in the view
        mName  = findViewById(editName);
        mNotes = findViewById(editNotes);
        mUsers = findViewById(userSpinner);
        mPoints = findViewById(addPoints);
        dateEntry = findViewById(addDate);
        // Add the users to the users spinner
        addItemsOnUser();
    }

    protected void addChore(View view){
        Integer points;
        Date d = new Date();
        // Set database reference
        dR = FirebaseDatabase.getInstance().getReference("chore");
        // Get all of the chores details
        String name = mName.getText().toString();
        String notes = mNotes.getText().toString();
        // Get points, ensure that a point is empty and convert to integer
        if (mPoints.getText().toString().length() > 0) {
            points = Integer.parseInt(mPoints.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Please set the point value of the chore", Toast.LENGTH_LONG).show();
            return;
        }
        // Set the date format used
        SimpleDateFormat dfDate_m = new SimpleDateFormat("ddMMyyyy");
        // Convert the string to a data
        try {
            String str = dateEntry.getText().toString();
            if (str.length() != 8) {
                Toast.makeText(getApplicationContext(), "Please enter the deadline in the DDMMYYYY format", Toast.LENGTH_LONG).show();
                return;
            } else {
                d = dfDate_m.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Get the selected user
        String user = list2.get(mUsers.getSelectedItemPosition());
        // Create new id for chore
        String id = dR.push().getKey();
        // Create new chore
        chore = new Chore(id, name, d, points, "", notes, user);
        // Add new chore to the database
        dR.child(id).setValue(chore);
        // Done with adding the chore
        finish();
    }

    protected void addItemsOnUser(){
        // Create list to contain the names of all the users
        final List<String> list = new ArrayList<String>();
        // Add the unassigned user to the list, and empty user to list of user Ids
        list.add("Unassigned");
        list2.add("");
        // Set the database reference and a database listener
        dR = FirebaseDatabase.getInstance().getReference("user");
        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // For all user, add their names to the list of names and their ids to the list of ids
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    list.add(user.getName());
                    list2.add(user.getId());
                }
                // Create adapter linking names of users to the spinnee
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mUsers.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // When the user presses cancel, end the activity
    protected void cancelEdit(View view){
        finish();
    }

}
