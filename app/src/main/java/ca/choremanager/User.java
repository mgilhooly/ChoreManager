package ca.choremanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class User {
    // Basically all the attributes for a chore, the constructors and getters and setters.
    private String _name, _email, _id;
    private boolean _parent;
    private int _points;
    private List<Chore> _chores;

    public User(String id, String name, boolean parent, String email) {
        _id = id;
        _name = name;
        _parent = parent;
        _email = email;
        _points = 0;
        _chores = new ArrayList<>();
    }
    public User (){}
    public String getId(){return _id;}
    public void setId(String id){_id = id;}
    public String getName () {return _name;}
    public void setName (String name){_name = name;}
    public boolean getParent () {return _parent;}
    public void setParent (boolean parent){_parent = parent;}
    public String getEmail() {return _email;}
    public void setEmail(String email){_email=email;}
    public int getPoints () {return _points;}
    public void setPoints (int points){_points = points;}
    public List<Chore> getChores() {return _chores;}
    public void setChores(List<Chore> chores){_chores = chores;}
}