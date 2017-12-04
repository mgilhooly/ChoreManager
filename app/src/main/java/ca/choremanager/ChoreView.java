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

import static ca.choremanager.R.id.choreDate;
import static ca.choremanager.R.id.choreNotes;
import static ca.choremanager.R.id.chorePoints;
import static ca.choremanager.R.id.choreUserName;

public class ChoreView extends AppCompatActivity {
    DatabaseReference choreRef, userRef;
    String choreId, userId;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Chore chore;
    private TextView mChoreUserName, mChoreDate, mChoreNotes, mChoreRecurring;
    private ValueEventListener mListener;
    private User choreUser, activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Intent ii = getIntent();
        choreId = (String) ii.getExtras().get("choreId");
        userId = (String) ii.getExtras().get("userId");

        setContentView(R.layout.activity_chore_view);
        final Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        choreRef = FirebaseDatabase.getInstance().getReference("chore").child(choreId);
        choreRef.addValueEventListener(mListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chore = dataSnapshot.getValue(Chore.class);
                mChoreNotes = findViewById(choreNotes);
                mChoreUserName = findViewById(choreUserName);
                mChoreNotes.setText(chore.getDescription());
                myToolbar.setTitle(chore.getName());
                mChoreRecurring = findViewById(chorePoints);
                mChoreRecurring.setText(Integer.toString(chore.getPoints()));
                mChoreDate = findViewById(choreDate);
                String date = chore.getDeadline().getYear() + "-" + chore.getDeadline().getMonth() + "-" + chore.getDeadline().getDay();
                mChoreDate.setText(date);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                choreUser = dataSnapshot.child(chore.getUser()).getValue(User.class);
                activeUser = dataSnapshot.child(userId).getValue(User.class);
                mChoreUserName.setText(choreUser.getName());
                if (chore.getUser().equals("")) {
                    mChoreUserName.setText("Unassigned");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTitle = mDrawerTitle = getTitle();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_edit:
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "You can delete chores by long pressing from the schedule view", Toast.LENGTH_SHORT).show();
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
        if (!chore.getUser().equals("")) {
            if (!chore.getCompleted()) {
                choreRef.child("completed").setValue(true);
                userRef.child(choreUser.getId()).child("points")
                        .setValue(choreUser.getPoints() + chore.getPoints());
            } else {
                Toast.makeText(getApplicationContext(), "This chore has already been completed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "This chore needs to be assigned before it can be completed", Toast.LENGTH_LONG).show();
        }
    }

    public void changeToTools(View view){
        Intent toolsIntent = new Intent(this, Toolss.class);
        startActivity(toolsIntent);
    }

    public void changeToUser(View view) {
        Intent userIntent = new Intent(this, UserProfileActivity.class);
        startActivity(userIntent);
    }

    public void cancelChoreView(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPause() {

        // Remove post value event listener
        if (mListener != null && choreRef != null) {
            choreRef.removeEventListener(mListener);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        choreRef.addValueEventListener(mListener);
        super.onResume();
    }


}
