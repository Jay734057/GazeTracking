package org.opencv.samples.tutorial1;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
public class welcome extends Activity {
	private TextView load = null;
	private String view = null;
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.starting);
		load = (TextView)findViewById(R.id.loading);
		load.setTextColor(android.graphics.Color.BLACK);
		String original  = (String) load.getText();
		
			view = original;
			load.setText(view);
			for (int i = 0; i < 3; i++){
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						view = (String) load.getText() + ".";
						load.setText(view);
					}
					
				}, 1000);
				
			}
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent();
				mainIntent.setClass(welcome.this, Tutorial1Activity.class);
				welcome.this.startActivity(mainIntent);
				welcome.this.finish();
			}
			
		}, 1000);
	}
}
