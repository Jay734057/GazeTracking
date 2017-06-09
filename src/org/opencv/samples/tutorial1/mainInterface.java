package org.opencv.samples.tutorial1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class mainInterface extends Activity{
	private Button start = null;
    private Button quit = null;
    private View back = null;
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.maininterface);
		back = findViewById(R.id.Lay);
		start = (Button)findViewById(R.id.gazetracking);
		quit = (Button)findViewById(R.id.quit);
		back.setBackgroundColor(Color.WHITE);
		start.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
					{
						start.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						start.getBackground().clearColorFilter();
						break;
					}
				}
				
				return false;
			}

		});
		
		start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent mainIntent = new Intent();
				mainIntent.setClass(mainInterface.this, Tutorial1Activity.class);
				mainInterface.this.startActivity(mainIntent);
				mainInterface.this.finish();
			}
			
		});
		
		quit.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
					{
						quit.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFFF0000));
						break;
					}
					case MotionEvent.ACTION_MOVE:
					{
						break;
					}
					case MotionEvent.ACTION_UP:
					{
						quit.getBackground().clearColorFilter();
						break;
					}
				}
				
				return false;
			}

		});
		
		quit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainInterface.this.finish();
			}
			
		});
	}
}
