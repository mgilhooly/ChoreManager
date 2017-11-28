package ca.choremanager;

import java.util.Collections;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Family {
    private List<User> family;

    public Family (){
        family = Collections.emptyList();
    }

    public void addUser(User user){
        family.add(user);
    }
}
