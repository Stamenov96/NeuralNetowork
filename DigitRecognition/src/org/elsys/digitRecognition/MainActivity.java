package org.elsys.digitRecognition;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.elsys.NeuralNet.NeuralNetworksProgram;

import com.example.drawing.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View.OnClickListener;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	final Context context = this;
	static File appDir;
	private DrawingView drawView;
	private ImageButton saveBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		drawView = (DrawingView) findViewById(R.id.drawing);
		appDir = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "SaveDir");
	        
			saveBtn = (ImageButton) findViewById(R.id.save_btn);
			saveBtn.setOnClickListener(this);

	}
	
	
	
	

	public void onClick(final View view) {

		if (view.getId() == R.id.save_btn) {
	
			
			final String path = appDir.toString();
			@SuppressWarnings("unused")
			OutputStream fOut;
			System.gc();
			File file2 = new File(path,"greyscale.jpeg");
			try {
				
				drawView.setDrawingCacheEnabled(true);
				
				Bitmap pictureBitmap = drawView.getDrawingCache();
				final Bitmap resized = Bitmap.createScaledBitmap(pictureBitmap, 28, 28, true); // Save in SaveDir in 28x28px
				
				OutputStream fout2=new FileOutputStream(file2);
				  final int height = resized.getHeight();
			        final int width = resized.getWidth();
			        System.gc();
			        final Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			        final Canvas c = new Canvas(bmpGrayscale);
			        final Paint paint = new Paint();
			        final ColorMatrix cm = new ColorMatrix();
			        cm.setSaturation(0);
			        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			        paint.setColorFilter(f);//grayscale the pic
			        c.drawBitmap(resized, 0, 0, paint);
			       System.gc(); 
			        bmpGrayscale.compress(Bitmap.CompressFormat.JPEG, 100, fout2);
				fout2.flush();
				fout2.close();
				
				
			Runnable NN =new Runnable() {
				        public void run() {
								try {
								int result= NeuralNetworksProgram.main(view,resized,path);//start the main neural network program
								final TextView txtValue = (TextView) findViewById(R.id.rec_digit);
								txtValue.setText(Integer.toString(result));// set result from NN to answer field
								System.out.println("Setting text");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
				        }
				    };
				    
				    
				    
				    Thread neuralnet = new Thread(NN);
					neuralnet.start();
					try {
						neuralnet.join();
						System.out.println(Thread.currentThread().getName());
						System.out.println(neuralnet.getName());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Runnable mainmen = new Runnable() {
			public void run() {
				getMenuInflater().inflate(R.menu.main, menu);
				
			}
		};
		Thread anotherone=new Thread(mainmen);
		anotherone.start();
		try {
			anotherone.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
