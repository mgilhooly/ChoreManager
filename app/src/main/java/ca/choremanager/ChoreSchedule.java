package ca.choremanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

    List<Chore> chores;

    DatabaseReference databaseChores, dR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_schedule);

        databaseChores = FirebaseDatabase.getInstance().getReference("chore");
        listViewChores = findViewById(R.id.listViewChores);
        buttonAddChore = findViewById(R.id.addButton);

        chores = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddChore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(ChoreSchedule.this, ChoreAddView.class);
                startActivity(addIntent);
            }
        });

        listViewChores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Chore chore = chores.get(i); // Intent to view chore details
                Intent viewIntent = new Intent(ChoreSchedule.this, ChoreView.class);
                viewIntent.putExtra("id", chore.getId());
                startActivity(viewIntent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseChores.addValueEventListener(new ValueEventListener() {
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


