package ca.choremanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChoreSchedule extends Activity {

    Button buttonAddChore;
    ListView listViewChores;
    User activeUser;
    String userId;
    List<Chore> chores;

    DatabaseReference choreReference, userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_schedule);
        Intent ii = getIntent();
        userId = (String) ii.getExtras().get("userId");

        choreReference = FirebaseDatabase.getInstance().getReference("chore");
        userReference = FirebaseDatabase.getInstance().getReference("user");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activeUser = dataSnapshot.child(userId).getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewChores = findViewById(R.id.listViewChores);
        buttonAddChore = findViewById(R.id.addButton);

        chores = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activeUser.getParent()) {
                    Intent addIntent = new Intent(ChoreSchedule.this, ChoreAddView.class);
                    startActivity(addIntent);
                }
            }
        });

        listViewChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chore chore = chores.get(i); // Intent to view chore details
                Intent viewIntent = new Intent(ChoreSchedule.this, ChoreView.class);
                viewIntent.putExtra("choreId", chore.getId());
                viewIntent.putExtra("userId", userId);
                startActivity(viewIntent);
            }
        });
        listViewChores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (activeUser.getParent()) {
                    final Chore c = chores.get(i);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChoreSchedule.this);
                    builder.setMessage(R.string.dialog_message)
                            .setTitle(R.string.dialog_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DatabaseReference dR = FirebaseDatabase.getInstance().getReference("chore").child(c.getId());
                            dR.removeValue();
                            Toast.makeText(getApplicationContext(), "Chore Deleted", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        choreReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                chores.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting product
                    Chore chore = postSnapshot.getValue(Chore.class);
                    //adding product to the list
                    chores.add(chore);
                }

                //creating adapter
                ChoreList choresAdapter = new ChoreList(ChoreSchedule.this, chores);
                //attaching adapter to the listview
                listViewChores.setAdapter(choresAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


