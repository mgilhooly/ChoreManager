package ca.choremanager;

/**
 * Created by michaelgilhooly on 2017-11-28.
 */

public class Tool {
    private String _name;
    private boolean _available;

    public Tool(String name, boolean available) {
        _name = name;
        _available = available;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isAvailable() {
        return _available;
    }

    public void setAvailable(boolean available) {
        _available = available;
    }
}