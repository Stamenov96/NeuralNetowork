package com.example.drawing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
//import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.MotionEvent;

public class DrawingView extends View {

	static File file ;
	
	// drawing path
	private Path drawPath;
	// drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	// initial color
	private int paintColor = 0xFF2EC2FF;
	// canvas
	public Canvas drawCanvas;
	// canvas bitmap
	private Bitmap canvasBitmap;

	
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupDrawing();
	}

	private void setupDrawing() {
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(paintColor);
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(20);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		canvasPaint = new Paint(Paint.DITHER_FLAG);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
		canvas.drawPath(drawPath, drawPaint);
	}


	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		float touchX = event.getX();
		float touchY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://touch the screen 
			drawPath.moveTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE://move on the screen
			drawPath.lineTo(touchX, touchY);
			break;
		case MotionEvent.ACTION_UP://stop touching the screen 
			drawCanvas.drawPath(drawPath, drawPaint);
			drawPath.reset();
			
			String appDir = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "SaveDir").toString();
			OutputStream fOut = null;
			File file = new File(appDir, "newpic.jpg"); // the File to save to
			try {
				fOut = new FileOutputStream(file);

				setDrawingCacheEnabled(true);
				Bitmap pictureBitmap = getDrawingCache();
				pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			}catch (Exception e) {
				Log.e("PictureDemo", "Exception in photoCallback", e);
			}
		
			
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public static void main(String[] args,Context context){
		
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
			System.out.println("SAVED");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
