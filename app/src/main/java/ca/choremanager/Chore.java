package ca.choremanager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Chore {
    private String _name, _description, _recurring, _id;
    private Date _deadline;
    private int _points;
    private boolean _completed;
    List<Tool> _tools;
    private User _user;
    private DatabaseReference dR;


    public Chore (String id, String name, Date deadline, int points, String recurring, User user){
        _id = id;
        _name = name;
        _deadline = deadline;
        _points = points;
        _recurring = recurring;
        _completed = false;
        _description = "";
        _tools = new ArrayList<Tool>();
        assignToUser(user);
    }
    public Chore(){}
    public String getId () {return _id;}
    public void setId (String id){_id = id;}

    public String getName (){return _name;}
    public void setName (String name){_name = name;}

    public String getDescription (){return _description;}
    public void setDescription (String description){_description = description;}

    public Date getDeadline (){return _deadline;}
    public void setDeadline (Date deadline){_deadline = deadline;}

    public int getPoints() {return _points;}
    public void setPoints (int points){_points = points;}

    public boolean getCompleted () {return _completed;}
    public void setCompleted (boolean completed){_completed = completed;}

    public List<Tool> getTools() {return _tools;}
    public void setTools(List<Tool> tools) {_tools = tools;}

    public User getUser () {return _user;}
    public void setUser (User user){_user = user;}

    public String getRecurring () {return _recurring;}
    public void setRecurring (String recurring){_recurring = recurring;}

    public void assignToUser(User user){
        dR = FirebaseDatabase.getInstance().getReference("chore").child(_id);
        _user = user;
        user.assignChore(this);
        dR.setValue(this);
    }

    public void addTool (Tool tool){
        dR = FirebaseDatabase.getInstance().getReference("chore").child(_id);
        _tools.add(tool);
        dR.setValue(this);
    }
    public void removeTools (Tool tool){
        dR = FirebaseDatabase.getInstance().getReference("chore").child(_id);
        _tools.remove(tool);
        dR.setValue(this);
    }

    public void removeUser (User user){
        dR = FirebaseDatabase.getInstance().getReference("chore").child(_id);
        user.removeChore(this);
        _user = null;
        dR.setValue(this);
    }

    public void completeChore(){
        dR = FirebaseDatabase.getInstance().getReference("chore").child(_id);
        _completed = true;
        _user.addPoints(_points);
        dR.setValue(this);
    }
}
