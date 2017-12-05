package ca.uottawa.farnaz.user3;
// With reference to https://www.tutorialspoint.com/android/android_login_screen.htm

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity  {
    Button button1,button2;
    EditText edit1,edit2;

    // Counter for the number of login attempts
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Buttons and text box initializations
        button1 = (Button)findViewById(R.id.button);
        edit1 = (EditText)findViewById(R.id.editText);
        edit2 = (EditText)findViewById(R.id.editText2);
        button2 = (Button)findViewById(R.id.button2);


// Username and Password Authentication
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((edit1.getText().toString().equals("user1")) && (edit2.getText().toString().equals("user1"))||
                        (edit1.getText().toString().equals("user2")) && (edit2.getText().toString().equals("user2")) ||
                        (edit1.getText().toString().equals("user3")) && (edit2.getText().toString().equals("user3"))||
                        (edit1.getText().toString().equals("user4")) && (edit2.getText().toString().equals("user4"))))

                //Informs if the authentication was successful or not
                {
                    Toast.makeText(getApplicationContext(),
                            "Login Successful...",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Incorrect Username or Password",Toast.LENGTH_SHORT).show();

                    counter--;

                    if (counter == 0) {
                        button1.setEnabled(false);
                    }
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}