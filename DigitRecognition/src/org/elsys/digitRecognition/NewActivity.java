package org.elsys.digitRecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import com.example.drawing.R;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class NewActivity extends Activity {
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
            AssetManager assetManager = getAssets();
            String[] files = null;
                try {
					files = assetManager.list("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            for(String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                  in = assetManager.open(filename);
                 
                  File outFile = new File(Environment.getExternalStorageDirectory()
            				+ File.separator + "SaveDir", filename);
                  out = new FileOutputStream(outFile);
                  copyFile(in, out);
                } catch(IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                }     
                finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }  
            }
        }
        private void copyFile(InputStream in, OutputStream out) throws IOException {
            byte[] buffer = new byte[1024];
            int read;
            while((read = in.read(buffer)) != -1){
              out.write(buffer, 0, read);
            }
        

       
        Button start = (Button) findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Intent myIntent = new Intent(view.getContext(),MainActivity.class);
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
