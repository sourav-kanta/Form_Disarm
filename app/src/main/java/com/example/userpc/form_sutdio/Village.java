package com.example.userpc.form_sutdio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Village extends Activity {
	
	RadioGroup rad;

	public void fetch(View v)
	{		
		String village=null;
		RadioButton sel=(RadioButton) rad.findViewById(rad.getCheckedRadioButtonId());
		if(sel==null) 
			return;
		village=sel.getText().toString();
		if(village.equals(null))
		{
			Toast.makeText(this, "Select a village first", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent i=new Intent(this,Choice.class);
		i.putExtra("village", village);
		startActivity(i);
		finish();
	}
	
	public class fetchVillage extends AsyncTask<Void, Void, Void>
	{
		Context context;
		private ProgressDialog mProgressDialog;
		Httpcall req;
		List<NameValuePair> param=null;
		private String response;
		
		fetchVillage(Context c)
		{
			context=c;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Fetching Villages");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			req=new Httpcall();
			param=new ArrayList<NameValuePair>();
			String query="select * from village;" ;
			Log.e("Query", query);
			param.add(new BasicNameValuePair("pass", "M-8zd57x"));
			param.add(new BasicNameValuePair("query", query));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			response=req.makeServiceCall("http://disarmproject.in/connection.php",Httpcall.POST, param); 
			Log.e("Village", response+"");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try{
				JSONArray catarray=new JSONArray(response);
				for(int i=0;i<catarray.length();i++)
				{
					JSONObject cat=catarray.getJSONObject(i);
					String c=cat.getString("vname");		
					if(c.isEmpty())
						continue;
					RadioButton r= new RadioButton(context);
                	r.setText(c);
                	r.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
                	rad.addView(r);
				}
			
			}
			catch(Exception e)
			{
				Log.e("Request", "Internet Problem or request malformed.");
			}
			mProgressDialog.dismiss();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.village);
		rad=(RadioGroup) findViewById(R.id.group);
		fetchVillage f=new fetchVillage(Village.this);
		f.execute();
		TextView t=(TextView) findViewById(R.id.tv);
		t.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
	} 
	
}




