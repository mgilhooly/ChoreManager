package ca.uottawa.farnaz.shopping;

// With reference to https://www.tutorialspoint.com/android/android_list_view.htm

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

   //An array witch stores the items of the list
    String[] shoppingArray = {"Broom","DietCoke","AA Batteries","Bacon",
            "Lettuce","Tomatoes","Flour","Lysol Disinfectanct"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The adapter retrieves data from the shoppingArray

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_list, shoppingArray);

        ListView listView = (ListView) findViewById(R.id.shoppingList);
        listView.setAdapter(adapter);
    }
    }

