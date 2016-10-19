package com.example.userpc.form_sutdio;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choice extends Activity {
	
	String village;
	
	public void live(View v)
	{
		Intent i=new Intent(this,Selection.class);
		i.putExtra("village",village);
		i.putExtra("type", "2");
		startActivity(i);
	}

	public void disarm(View v)
	{
		Intent i=new Intent(this,Selection.class);
		i.putExtra("type", "1");
		i.putExtra("village", village);
		startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choice);
		getActionBar().setIcon(R.drawable.toplogo);
		Intent i=getIntent();
		Bundle b=i.getExtras();
		village=b.getString("village");
		Button li=(Button) findViewById(R.id.live);
		Button dis=(Button) findViewById(R.id.disarm);
		li.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		dis.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
	} 
	
}
