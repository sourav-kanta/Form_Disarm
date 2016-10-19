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

public class Subcategory extends Activity {
	
	ArrayList<CheckBox> category=null;
	String type;
	String checkquery,village,categories;
	
	public void fetch(View v)
	{
		String checksubquery="0";
		String subcategories="";
		for(int i=0;i<category.size();i++)
		{
			if(category.get(i).isChecked())
			{
				checksubquery+=" or `subcategory`='"+category.get(i).getText().toString()+"'";
				subcategories+=category.get(i).getText().toString()+", ";
			}
		}
		checksubquery+=" and set_id="+type+"";
		if(!subcategories.equals(""))
			subcategories=subcategories.substring(0,subcategories.length()-2);	
		Intent i=new Intent(this,MainActivity.class);
		i.putExtra("query", checksubquery);
		i.putExtra("village", village);
		i.putExtra("categories", subcategories);
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
			String query="select `subcategory` from questions where ( "+checkquery+") group by `subcategory`;" ;
			Log.e("Query", query);
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
					String c=cat.getString("subcategory");		
					if(c.isEmpty())
						continue;
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
		checkquery=bun.getString("query");
		village=bun.getString("village");
		categories=bun.getString("categories");
		fetchCatagories f=new fetchCatagories(this);		
		f.execute();
		
		
		Button b=(Button) findViewById(R.id.button1);
		TextView t=(TextView) findViewById(R.id.tv);
		t.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		b.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
	}

}
