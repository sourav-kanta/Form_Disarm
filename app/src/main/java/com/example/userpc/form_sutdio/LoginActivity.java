package com.example.userpc.form_sutdio;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	public void login(View v)
	{
		EditText t=(EditText) findViewById(R.id.editText1);
		EditText vill=(EditText) findViewById(R.id.EditText01);
		String name=t.getText().toString();
		String village=vill.getText().toString();
		if(name.isEmpty() || (!(name.length()==10))){
			t.setError("Enter valid phone number");
			return;
		}
		if(!village.equals("1947")){
			vill.setError("Password incorrect");
			return;
		}
		SharedPreferences.Editor editor = getSharedPreferences("Preferences", MODE_PRIVATE).edit();
		editor.putString("name", name)       //.putString("village", village)
		.commit();
		Intent i=new Intent(this,Village.class);
		startActivity(i);
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpg);
		getActionBar().setIcon(R.drawable.toplogo);
		EditText t=(EditText) findViewById(R.id.editText1);
		EditText vill=(EditText) findViewById(R.id.EditText01);
		Button b=(Button) findViewById(R.id.login);
		b.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		t.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		vill.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
		if (prefs.contains("name")) {
				 Intent i=new Intent(this,Village.class);
				 startActivity(i);
				 finish();
		}
	}

}
