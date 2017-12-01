package ca.choremanager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class User {
    private String _name, _email, _id;
    private boolean _parent;
    private int _points;
    private List<Chore> _chores;
    private Family _family;
    private DatabaseReference dR;

    public User (String id, String name, boolean parent, String email, Family family){
        _id = id;
        _family = family;
        _name = name;
        _parent = parent;
        _email = email;
        _points = 0;
        _chores = new ArrayList<>();
        //family.addUser(this);
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
    public Family getFamily () {return _family;}
    public void setFamily (Family family){ _family = family;}
    public List<Chore> getChores() {return _chores;}
    public void setChores(List<Chore> chores){_chores = chores;}

    public void assignChore (Chore chore){
        dR = FirebaseDatabase.getInstance().getReference("user").child(_id);
        _chores.add(chore);
        dR.setValue(this);
    }

    public void removeChore(Chore chore){
        dR = FirebaseDatabase.getInstance().getReference("user").child(_id);
        _chores.remove(chore);
        dR.setValue(this);
    }

    public void addPoints (int pointsToAdd){
        dR = FirebaseDatabase.getInstance().getReference("user").child(_id);
        _points += pointsToAdd;
        dR.setValue(this);
    }
}