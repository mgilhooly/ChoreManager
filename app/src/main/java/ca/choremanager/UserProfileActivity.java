package ca.choremanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static ca.choremanager.R.id.textUserName;
import static ca.choremanager.R.id.textUserPoint;

public class UserProfileActivity extends Activity {
    String userId;
    DatabaseReference userRef, choreRef;
    User activeUser;
    ValueEventListener userListener, choreListener;
    List<Chore> chores;
    ListView listViewChores;
    private TextView mUserName, mUserPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Intent ii = getIntent();
        userId = (String) ii.getExtras().get("userId");
        userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
        userRef.addValueEventListener(userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activeUser = dataSnapshot.getValue(User.class);
                mUserName = findViewById(textUserName);
                mUserPoints = findViewById(textUserPoint);
                mUserName.setText(activeUser.getName());
                mUserPoints.setText(String.valueOf(activeUser.getPoints()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        super.onStart();
        chores = new ArrayList<>();
        listViewChores = findViewById(R.id.choreList);
        //attaching value event listener
        choreRef = FirebaseDatabase.getInstance().getReference("chore");
        choreRef.addValueEventListener(choreListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                chores.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting product
                    Chore chore = postSnapshot.getValue(Chore.class);
                    //adding product to the list
                    if (chore.getUser().equals(userId)) {
                        chores.add(chore);
                    }
                }

                //creating adapter
                ChoreList choresAdapter = new ChoreList(UserProfileActivity.this, chores);
                //attaching adapter to the listview
                listViewChores.setAdapter(choresAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chore chore = chores.get(i); // Intent to view chore details
                Intent viewIntent = new Intent(UserProfileActivity.this, ChoreView.class);
                viewIntent.putExtra("choreId", chore.getId());
                viewIntent.putExtra("userId", userId);
                startActivity(viewIntent);
            }
        });

    }

    public void viewAllChores(View view) {
        Intent fullScheduleIntent = new Intent(UserProfileActivity.this, ChoreSchedule.class);
        fullScheduleIntent.putExtra("userId", userId);
        startActivity(fullScheduleIntent);
    }

}
