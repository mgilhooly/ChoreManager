package ca.choremanager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Family {
    private ArrayList<User> _family;
    private ArrayList<Tool> _tools;
    private String _id;
    private DatabaseReference dR;

    public Family (String id){
        _id = id;
        _family = new ArrayList<User>();
        _tools = new ArrayList<Tool>();
    }
    public Family(){}
    public String getID (){return _id;}
    public void setId(String id){_id = id;}

    public List getFamily(){return _family;}

    public void setFamily(ArrayList<User> family) {
        _family = family;
    }

    public List getTools(){return _tools;}

    public void setTools(ArrayList<Tool> tools) {
        _tools = tools;
    }

    /*public void addUser(User user) {
        dR = FirebaseDatabase.getInstance().getReference("family").child(_id);
        _family.add(user);
        dR.setValue(this);
    }*/
    public void removeUser(User user){
        dR = FirebaseDatabase.getInstance().getReference("family").child(_id);
        _family.remove(user);
        dR.setValue(this);}
    public void addTool (Tool tool) {
        dR = FirebaseDatabase.getInstance().getReference("family").child(_id);
        _tools.add(tool);
        dR.setValue(this);}
    public void removeTool (Tool tool) {
        dR = FirebaseDatabase.getInstance().getReference("family").child(_id);
        _tools.remove(tool);
        dR.setValue(this);}
}
