package ca.choremanager;

import java.util.Date;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Chore {
    private String _name, _description, _recurring;
    private Date _deadline;
    private int _points;
    private boolean _completed;
    List<Tool> _tools;
    private User _user;


    public Chore (String name, Date deadline, int points, String recurring){
        _name = name;
        _deadline = deadline;
        _points = points;
        _recurring =recurring;
        _completed = false;
        _description = "";
    }
    public void setName (String name){_name = name;}
    public String getName (){return _name;}
    public void setDescription (String description){_description = description;}
    public String getDescription (){return _description;}
    public void setDeadline (Date deadline){_deadline = deadline;}
    public Date getDeadline (){return _deadline;}
    public void setPoints (int points){_points = points;}
    public int getPoints() {return _points;}
    public void setCompleted (boolean completed){_completed = completed;}
    public boolean getCompleted () {return _completed;}
    public void setTools(List<Tool> tools) {_tools = tools;}
    public List<Tool> getTools() {return _tools;}
    public void setUser (User user){_user = user;}
    public User getUser () {return _user;}
    public void setRecurring (String recurring){_recurring = recurring;}
    public String getRecurring () {return _recurring;}
}
