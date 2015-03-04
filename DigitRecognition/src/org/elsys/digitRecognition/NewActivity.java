package org.elsys.digitRecognition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.drawing.R;

import android.app.Activity;
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

		File appDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "SaveDir");

		if (!appDir.exists() && !appDir.isDirectory()) {
			appDir.mkdirs();
		}

		AssetManager assetManager = getAssets();
		String[] files = null;
		// extract Assets folder into "SaveDir" folder onto device
		
		try {
			files = assetManager.list("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (String filename : files) {
			if (filename.equals("weightsandbiases.txt")) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open("weightsandbiases.txt");
					File outFile = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator + "SaveDir", filename);
					out = new FileOutputStream(outFile);
					copyFile(in, out);
				} catch (IOException e) {
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				}

			}
		}

	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}

		Button start = (Button) findViewById(R.id.Start);
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(),
						MainActivity.class);
				startActivityForResult(myIntent, 0);
			}

		});

		Button about = (Button) findViewById(R.id.About);
		about.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent myIntent = new Intent(view.getContext(),
						AboutActivity.class);
				// setResult(RESULT_OK, intent);
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
