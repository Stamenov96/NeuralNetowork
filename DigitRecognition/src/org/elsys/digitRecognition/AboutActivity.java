package org.elsys.digitRecognition;

import org.elsys.digitrecognition.R;

import android.app.Activity;

import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AboutActivity extends Activity implements  OnTouchListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	

	}

	public boolean onTouch(View v, MotionEvent event) {
		finish();
		return true;
	}

	
}