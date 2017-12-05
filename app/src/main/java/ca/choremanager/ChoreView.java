package ca.choremanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

import static ca.choremanager.R.id.choreDate;
import static ca.choremanager.R.id.choreNotes;
import static ca.choremanager.R.id.chorePoints;
import static ca.choremanager.R.id.choreUserName;

public class ChoreView extends AppCompatActivity {
    DatabaseReference dataRef;
    String choreId, userId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Chore chore;
    private TextView mChoreUserName, mChoreDate, mChoreNotes, mChorePoints;
    private ValueEventListener mListener;
    private User choreUser, activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        //Get the Ids of the chore being displayed and the active user from the last activity
        Intent ii = getIntent();
        choreId = (String) ii.getExtras().get("choreId");
        userId = (String) ii.getExtras().get("userId");


        setContentView(R.layout.activity_chore_view);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        //Find all objects that need to be filled in using chore details
        mChoreNotes = findViewById(choreNotes);
        mChoreUserName = findViewById(choreUserName);
        mChorePoints = findViewById(chorePoints);
        mChoreDate = findViewById(choreDate);

        // Create database reference and listener
        dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.addValueEventListener(mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get the chore, the user to whom the chore is assigned and the active user
                chore = dataSnapshot.child("chore").child(choreId).getValue(Chore.class);
                choreUser = dataSnapshot.child("user").child(chore.getUser()).getValue(User.class);
                activeUser = dataSnapshot.child("user").child(userId).getValue(User.class);

                // Set all of the items to their value
                myToolbar.setTitle(chore.getName());
                mChorePoints.setText(Integer.toString(chore.getPoints()));
                SimpleDateFormat dfDate_m = new SimpleDateFormat("dd/MM/yyyy");
                String date = dfDate_m.format(chore.getDeadline());
                mChoreDate.setText(date);
                mChoreNotes.setText(chore.getDescription());

                mChoreUserName.setText(choreUser.getName());
                if (chore.getUser().equals("")) {
                    mChoreUserName.setText("Unassigned");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // This exists to display the app drawer (It currently does nothing)
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    // When the activity is resumed, add the listener back to the database
    @Override
    public void onResume() {
        dataRef.addValueEventListener(mListener);
        super.onResume();
    }

    // Create the options menu on the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //Case for when edit chore is selected
            case R.id.action_edit:
                //Make sure active user is a parent then make a new intent, passing the active user and the chore
                if (activeUser.getParent()) {
                    Intent editIntent = new Intent(this, ChoreEditView.class);
                    editIntent.putExtra("choreId", choreId);
                    editIntent.putExtra("userId", userId);
                    startActivity(editIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "You need to be an adult before you can edit chores.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_delete:
                //If the use selects delete ** Not implemented from this page **, Does work from chore list
                // Creates a dialog asking if user wishes to delete chore,
                // If they do, then delete chore from database and send them back to the previous page
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "You can delete chores by long pressing from the schedule view", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            default:
                return false;
        }
    }

    public void completeChore(View view){
        // Makes sure that there is a user who has done the chore
        if (!chore.getUser().equals("")) {
            //Make sure that the chore has not been completed in the past
            if (!chore.getCompleted()) {
                // Set the chore to complete
                dataRef.child("chore").child(choreId).child("completed").setValue(true);
                // Add the points to the user to whom the chore was assigned
                dataRef.child("user").child(choreUser.getId()).child("points")
                        .setValue(choreUser.getPoints() + chore.getPoints());
                // Confirm to the user that the chore is completed
                Toast.makeText(getApplicationContext(), "This chore has been completed", Toast.LENGTH_SHORT).show();
            } else {
                // Inform the user that the chore is already complete
                Toast.makeText(getApplicationContext(), "This chore has already been completed", Toast.LENGTH_LONG).show();
            }
        } else {
            // Inform the user that the chore is unassigned
            Toast.makeText(getApplicationContext(), "This chore needs to be assigned before it can be completed", Toast.LENGTH_LONG).show();
        }
    }

    // When the user is done with this activity end it
    public void cancelChoreView(View view) {
        finish();
    }

    // Whenever this activity is paused remove the database listener
    @Override
    public void onPause() {

        // Remove post value event listener
        if (mListener != null && dataRef != null) {
            dataRef.removeEventListener(mListener);
        }
        super.onPause();
    }

    // Whenever this activity is stopped remove the database listener
    @Override
    public void onStop() {

        // Remove post value event listener
        if (mListener != null && dataRef != null) {
            dataRef.removeEventListener(mListener);
        }
        super.onStop();
    }

    /*These methods were designed for the non-functional app drawer (this could be implemented in the future)
    public void changeToTools(View view){
        Intent toolsIntent = new Intent(this, Toolss.class);
        startActivity(toolsIntent);
    }

    public void changeToUser(View view) {
        Intent userIntent = new Intent(this, UserProfileActivity.class);
        startActivity(userIntent);
    }
    */
}
