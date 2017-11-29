package ca.choremanager;

import java.util.Collections;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class User {
    private String _name, _email;
    private boolean _parent;
    private int _points;
    private List<Chore> _chores;
    private Family _family;

    public User (String name, boolean parent, String email, Family family){
        _family = family;
        _name = name;
        _parent = parent;
        _email = email;
        _points = 0;
        _chores = Collections.emptyList();
        family.addUser(this);
    }
    public String getName () {return _name;}
    public void setName (String name){_name = name;}
    public boolean isParent () {return _parent;}
    public void setParent (boolean parent){_parent = parent;}
    public String getEmail() {return _email;}
    public void setEmail(String email){_email=email;}
    public int getPoints () {return _points;}
    public void setPoints (int points){_points = points;}

    public void assignChore (Chore chore){
        _chores.add(chore);
    }

    public void removeChore(Chore chore){
        _chores.remove(chore);
    }

    public void addPoints (int pointsToAdd){
        _points += pointsToAdd;
    }
}