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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Selection extends Activity {
	
	ArrayList<CheckBox> category=null;
	String type;
	
	public void fetch(View v)
	{
		String checkquery="0";
		String categories="";
		for(int i=0;i<category.size();i++)
		{
			if(category.get(i).isChecked())
			{
				checkquery+=" or `category`='"+category.get(i).getText().toString()+"'";
				categories+=category.get(i).getText().toString()+", ";
			}
		}
		checkquery+=" and set_id="+type+"";
		if(!categories.equals(""))
			categories=categories.substring(0,categories.length()-2);
		Intent rec=getIntent();
		Bundle b=rec.getExtras();
		String village=b.getString("village");		
		Intent i=new Intent(this,Subcategory.class);
		i.putExtra("query", checkquery);
		i.putExtra("village", village);
		i.putExtra("type", type);
		i.putExtra("categories", categories);
		startActivity(i);
		finish();
	}
	
	public class fetchCatagories extends AsyncTask<Void, Void, Void>
	{
		Context context;
		private ProgressDialog mProgressDialog;
		Httpcall req;
		List<NameValuePair> param=null;
		private String response;
		
		fetchCatagories(Context c)
		{
			context=c;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Fetching Categories");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			req=new Httpcall();
			param=new ArrayList<NameValuePair>();
			String query="select distinct `category` from questions where set_id="+type;
			param.add(new BasicNameValuePair("pass", "M-8zd57x"));
			param.add(new BasicNameValuePair("query", query));
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			response=req.makeServiceCall("http://disarmproject.in/connection.php",Httpcall.POST, param); 
			Log.e("Categories", response+"");
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			LinearLayout ll=(LinearLayout) findViewById(R.id.select);
			try{
				JSONArray catarray=new JSONArray(response);
				for(int i=0;i<catarray.length();i++)
				{
					JSONObject cat=catarray.getJSONObject(i);
					String c=cat.getString("category");						
					CheckBox r= new CheckBox(context);
					//LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					//lp.gravity=Gravity.CENTER_HORIZONTAL;
					//r.setLayoutParams(lp);
                	category.add(r);
                	r.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
                	r.setText(c);
                	ll.addView(r);
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
		setContentView(R.layout.selection);
		getActionBar().setIcon(R.drawable.toplogo);
		category=new ArrayList<CheckBox>();
		
		Intent rec=getIntent();
		Bundle bun=rec.getExtras();
		type=bun.getString("type");
		
		fetchCatagories f=new fetchCatagories(this);		
		f.execute();
		
		
		Button b=(Button) findViewById(R.id.button1);
		TextView t=(TextView) findViewById(R.id.tv);
		t.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		b.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
	}

}
