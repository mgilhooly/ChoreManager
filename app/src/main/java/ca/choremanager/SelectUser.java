package ca.choremanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
        // Create reference to users
        usersReference = FirebaseDatabase.getInstance().getReference("user");
        // Get items from the view
        listViewUsers = findViewById(R.id.listViewUsers);
        buttonAddUser = findViewById(R.id.addButton);
        // Create the list of users
        users = new ArrayList<>();

        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SelectUser.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
                dialogBuilder.setView(dialogView);
                final EditText addUserName = dialogView.findViewById(R.id.addUserText);
                final Switch isParent = dialogView.findViewById(R.id.isParent);
                final Button buttonAdd = dialogView.findViewById(R.id.confirmAdd);
                final Button buttonCancel = dialogView.findViewById(R.id.cancelAdd);

                dialogBuilder.setTitle("Add New User");
                final AlertDialog b = dialogBuilder.create();
                b.show();

                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = addUserName.getText().toString().trim();
                        boolean parent = isParent.isChecked();
                        if (!TextUtils.isEmpty(name)) {
                            String id = usersReference.push().getKey();
                            User u = new User(id, name, parent, "");
                            usersReference.child(id).setValue(u);
                            b.dismiss();
                        }
                    }
                });

                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
            }
        });
        // When a chore is clicked on launch the users profile
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = users.get(i); // Intent to view chore details
                Intent viewIntent = new Intent(SelectUser.this, UserProfileActivity.class);
                viewIntent.putExtra("userId", user.getId());
                startActivity(viewIntent);
            }
        });
        listViewUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final User u = users.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectUser.this);
                builder.setMessage("Are you sure you want to continue? Continuing will permanently delete this user and all of their information. ")
                        .setTitle("Delete User");
                builder.setPositiveButton("Delete User", new DialogInterface.OnClickListener() {
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
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous user
                users.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting user
                    User user = postSnapshot.getValue(User.class);
                    //adding user to the list
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


