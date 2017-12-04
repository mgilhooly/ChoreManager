package ca.choremanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class SelectUser extends Activity {

    Button buttonAddUser;
    ListView listViewUsers;

    List<User> users;

    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        usersReference = FirebaseDatabase.getInstance().getReference("user");
        listViewUsers = findViewById(R.id.listViewUsers);
        buttonAddUser = findViewById(R.id.addButton);

        users = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent addIntent = new Intent(SelectUser.this, ChoreAddView.class);
                //startActivity(addIntent);
                Toast.makeText(getApplicationContext(), "You cannot add users yet", Toast.LENGTH_LONG).show();
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i); // Intent to view chore details
                Intent viewIntent = new Intent(SelectUser.this, UserProfileActivity.class);
                viewIntent.putExtra("userId", user.getId());
                startActivity(viewIntent);
            }
        });
        /*listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User u = users.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectUser.this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("user").child(u.getId());
                        dR.removeValue();
                        Toast.makeText(getApplicationContext(), "User Deleted", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous artist list
                users.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting product
                    User user = postSnapshot.getValue(User.class);
                    //adding product to the list
                    users.add(user);
                }

                //creating adapter
                UserList usersAdapter = new UserList(SelectUser.this, users);
                //attaching adapter to the listview
                listViewUsers.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}


