package ca.choremanager;

import android.content.Intent;
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

import static ca.choremanager.R.id.editDate;
import static ca.choremanager.R.id.editName;
import static ca.choremanager.R.id.editNotes;
import static ca.choremanager.R.id.editPoints;
import static ca.choremanager.R.id.userSpinner;


/**
 * Created by michaelgilhooly on 2017-11-24.
 */

public class ChoreEditView extends AppCompatActivity {
    EditText mNotes, mName, mPoints, mDate;
    Spinner mUsers;
    EditText dateEntry;
    DatabaseReference dR, userdR;
    String choreId;
    List<String> idList = new ArrayList<>();
    private Chore chore;
    private ValueEventListener mListener;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_chore_edit);
        super.onCreate(savedInstanceState);

        Intent ii = getIntent();
        choreId = (String) ii.getExtras().get("choreId");
        // Get all fieldss
        mName  = findViewById(editName);
        mNotes = findViewById(editNotes);
        mPoints = findViewById(editPoints);
        mUsers = findViewById(userSpinner);
        mDate = findViewById(editDate);
        mUsers = findViewById(userSpinner);

        dR = FirebaseDatabase.getInstance().getReference("chore").child(choreId);
        dR.addValueEventListener(mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Set all fields using preexisting data
                chore = dataSnapshot.getValue(Chore.class);
                mName.setText(chore.getName());
                mNotes.setText(chore.getDescription());
                mPoints.setText(Integer.toString(chore.getPoints()));
                SimpleDateFormat dfDate_m = new SimpleDateFormat("ddMMyyyy");
                mDate.setText(dfDate_m.format(chore.getDeadline()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addItemsOnUser();
    }

    protected void editChore(View view){
        // set the name to whatever is in the text field
        chore.setName(mName.getText().toString());
        // Set the date to the contents of the date field using SimpleDateFormat
        dateEntry = findViewById(editDate);
        SimpleDateFormat dfDate_m = new SimpleDateFormat("ddMMyyyy");
        try {
            String str = dateEntry.getText().toString();
            if (str.length() != 8) {
                Toast.makeText(getApplicationContext(), "Please enter the deadline in the DDMMYYYY format", Toast.LENGTH_LONG).show();
                return;
            } else {
                Date d = dfDate_m.parse(str);
                chore.setDeadline(d);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Set description to the contents of the notes field
        chore.setDescription(mNotes.getText().toString());
        // Set the assigned user
        if (idList.get(mUsers.getSelectedItemPosition()) != chore.getUser()) {
            chore.setUser(idList.get(mUsers.getSelectedItemPosition()));
        }
        // Set the points to the contents of the text field
        if (mPoints.getText().toString().length() > 0) {
            chore.setPoints(Integer.parseInt(mPoints.getText().toString()));
        } else {
            Toast.makeText(getApplicationContext(), "Please set the point value of the chore", Toast.LENGTH_LONG).show();
            return;
        }
        // Set value of chore using the updated chore
        dR.setValue(chore);
        finish();
    }
    protected void addItemsOnUser(){
        // Add all the user names to the user selector spinner
        final List<String> nameList = new ArrayList<String>();
        // add the unassigned user and empty user id
        nameList.add("Unassigned");
        idList.add("");

        // Get all of the users
        userdR = FirebaseDatabase.getInstance().getReference("user");
        userdR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    // Get each user
                    User user = snapshot.getValue(User.class);
                    // Extract the name ans id
                    nameList.add(user.getName());
                    idList.add(user.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Set the adapter to addapt the names to the spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, nameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUsers.setAdapter(dataAdapter);
    }
    protected void cancelEdit(View view){
        finish();
    }

    @Override
    public void onPause() {

        // Remove post value event listener
        if (mListener != null && dR != null) {
            dR.removeEventListener(mListener);
        }
        super.onPause();
    }

}
