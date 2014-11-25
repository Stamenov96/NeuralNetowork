package com.example.drawing;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener {

	private DrawingView drawView;
	private ImageButton currPaint, saveBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		drawView = (DrawingView) findViewById(R.id.drawing);
		LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaint = (ImageButton) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));
		saveBtn = (ImageButton) findViewById(R.id.save_btn);
		saveBtn.setOnClickListener(this);

	}

	public void onClick(View view) {
		if (view.getId() == R.id.save_btn) {
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle("Save drawing");
			saveDialog.setMessage("Save drawing to device Gallery?");
			saveDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.setDrawingCacheEnabled(true);
							String imgSaved = MediaStore.Images.Media
									.insertImage(getContentResolver(), drawView
											.getDrawingCache(), UUID
											.randomUUID().toString() + ".png",
											"drawing");
							if (imgSaved != null) {
								Toast savedToast = Toast.makeText(
										getApplicationContext(),
										"Drawing saved to Gallery!",
										Toast.LENGTH_SHORT);
								savedToast.show();
							} else {
								Toast unsavedToast = Toast.makeText(
										getApplicationContext(),
										"Oops! Image could not be saved.",
										Toast.LENGTH_SHORT);
								unsavedToast.show();
							}
							drawView.destroyDrawingCache();
						}
						
					});
			saveDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			saveDialog.show();
		}
	}

	public void paintClicked(View view) {
		if (view != currPaint) {
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaint = (ImageButton) view;
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
