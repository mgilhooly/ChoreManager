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
    EditText mNotes, mName, mPoints;
    Spinner mRecurring, mUsers;
    EditText dateEntry;
    DatabaseReference dR, userdR;
    String choreId;
    List<String> list2 = new ArrayList<>();
    private Chore chore;
    private ValueEventListener mListener;

    protected void onCreate(Bundle savedInstanceState) {
        List<String> list2 = new ArrayList<String>();
         setTheme(R.style.AppTheme);
         setContentView(R.layout.activity_chore_edit);
        super.onCreate(savedInstanceState);
        mName  = findViewById(editName);
        mNotes = findViewById(editNotes);
        mPoints = findViewById(editPoints);
        Intent ii = getIntent();
        choreId = (String) ii.getExtras().get("choreId");
        dR = FirebaseDatabase.getInstance().getReference("chore").child(choreId);
        mUsers = findViewById(userSpinner);
        dR.addValueEventListener(mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chore = dataSnapshot.getValue(Chore.class);
                mName.setText(chore.getName());
                mNotes.setText(chore.getDescription());
                mPoints.setText(Integer.toString(chore.getPoints()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addItemsOnUser();
    }

    protected void editChore(View view){
        chore.setName(mName.getText().toString());
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
        chore.setDescription(mNotes.getText().toString());
        if (list2.get(mUsers.getSelectedItemPosition()) != chore.getUser()) {
            chore.setUser(list2.get(mUsers.getSelectedItemPosition()));
        }
        if (mPoints.getText().toString().length() > 0) {
            chore.setPoints(Integer.parseInt(mPoints.getText().toString()));
        } else {
            Toast.makeText(getApplicationContext(), "Please set the point value of the chore", Toast.LENGTH_LONG).show();
            return;
        }

        dR.setValue(chore);
        finish();
    }
    protected void addItemsOnUser(){
        final Spinner mUser = findViewById(userSpinner);
        final List<String> list = new ArrayList<String>();
        list.add("Unassigned");
        list2.add("");

        userdR = FirebaseDatabase.getInstance().getReference("user");
        userdR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    list.add(user.getName());
                    list2.add(user.getId());
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mUser.setAdapter(dataAdapter);
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

    @Override
    public void onPause() {

        // Remove post value event listener
        if (mListener != null && dR != null) {
            dR.removeEventListener(mListener);
        }
        super.onPause();
    }

}
