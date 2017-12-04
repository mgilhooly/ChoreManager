package ca.choremanager;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by michaelgilhooly on 2017-12-01.
 */

public class UserList extends ArrayAdapter<User> {

    List<User> users;
    private Activity context;

    public UserList(Activity context, List<User> users) {
        super(context, R.layout.layout_chore_list, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewParent = listViewItem.findViewById(R.id.textViewParent);
        TextView textViewPoints = listViewItem.findViewById(R.id.textViewPoints);

        User user = users.get(position);
        textViewName.setText(user.getName());
        if (user.getParent()) {
            textViewParent.setText("Parent");
        } else {
            textViewParent.setText("Child");
        }
        textViewPoints.setText(String.valueOf(user.getPoints()));
        return listViewItem;
    }
}

