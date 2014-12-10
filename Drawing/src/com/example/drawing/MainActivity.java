package com.example.drawing;

import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.provider.MediaStore;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private File appDir;
	private DrawingView drawView;
	// private ImageButton currPaint;
	private ImageButton saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawingView) findViewById(R.id.drawing);
		
		saveBtn = (ImageButton) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);

		appDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "SaveDir");

		if (!appDir.exists() && !appDir.isDirectory()) {
			appDir.mkdirs();
		}

	}
	
	
	
	

	public void onClick(View view) {

		if (view.getId() == R.id.save_btn) {
			// System.out.println(appDir+"APPP DIRRR");
			String path = appDir.toString();
			// System.out.println(path+"PATHHHHHHHH");
			OutputStream fOut;
			File file = new File(path, "newpic.jpeg"); // the File to save to
			try {
				fOut = new FileOutputStream(file);

				drawView.setDrawingCacheEnabled(true);
				
				Bitmap pictureBitmap = drawView.getDrawingCache();
				pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
				
			} catch (IOException e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
