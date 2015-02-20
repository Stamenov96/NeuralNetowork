package org.elsys.digitRecognition;

import org.elsys.digitrecognition.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewActivity extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);

        Button start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(),MainActivity.class);
                //setResult(RESULT_OK, intent);
                startActivityForResult(myIntent, 0);
            }

        });
        
        Button about = (Button) findViewById(R.id.About);
        about.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(), AboutActivity.class);
                //setResult(RESULT_OK, intent);
                startActivityForResult(myIntent, 0);
            }

        });
        
        Button quit = (Button) findViewById(R.id.Quit);
        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	finish();
            }

        });
        
    }
}