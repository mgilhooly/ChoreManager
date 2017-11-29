package ca.choremanager;

import java.util.Collections;
import java.util.List;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Family {
    private List<User> family;
    private List<Tool> tools;

    public Family (){
        family = Collections.emptyList();
        tools = Collections.emptyList();
    }

    public void addUser(User user){
        family.add(user);
    }
    public void removeUser(User user){ family.remove(user);}
    public void addTool (Tool tool) {tools.add(tool);}
    public void removeTool (Tool tool) {tools.remove(tool);}
}
