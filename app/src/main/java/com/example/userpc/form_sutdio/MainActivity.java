package com.example.userpc.form_sutdio;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
		
	
	public static ArrayList<EditText> textstore;
	public static ArrayList<RadioGroup> radiostore;
	public static ArrayList<ArrayList<CheckBox>> checkstore;
	public static ArrayList<String> questions;
	public static ArrayList<Integer> map;
	public static ArrayList<String> answers;
	public static ArrayList<String> set;
	public static ArrayList<String> quesid;
	public static String village=null;

	public class MakeRequest extends AsyncTask<Void, Void, Void>{
	
		Context context;
		String query=null;
		ProgressDialog mProgressDialog;
		Httpcall req;
		List<NameValuePair> param=null;
		String response;
		
	
	
		public MakeRequest(Context c,String str) {
			// TODO Auto-generated constructor stub
			context=c;
			query=str;
		}
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Please wait...");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			req=new Httpcall();
			param=new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("pass", "M-8zd57x"));
			param.add(new BasicNameValuePair("query", query));
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			response=req.makeServiceCall("http://disarmproject.in/connection.php",Httpcall.POST, param); 
			//String dout = response;		
			//dout=HtmlStr;
			Log.e("Response", response+"");		    
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			ArrayList<NavItem> nav=new  ArrayList<NavItem>();
			try{
				JSONArray questions=new JSONArray(response);
				for(int i=0;i<questions.length();i++)
				{
					JSONObject question=questions.getJSONObject(i);
					String ques=question.getString("question");
					int type=question.getInt("type");
					int set=question.getInt("set_id");
					int qid=question.getInt("ques_id");
					nav.add(new NavItem(ques, type,set,qid));
				}
			
			}
			catch(Exception e)
			{
				Log.e("Request", "Internet Problem or request malformed.");
			}
			generateForm(nav);
			mProgressDialog.dismiss();
			super.onPostExecute(result);
		}

	}

	
	public class SubmitAnswer extends AsyncTask<Void, Void, Void>{
		
		Context context;
		String query;
		Httpcall req;
		List<NameValuePair> param=null;
		String response;
		String phn;
		String vi;
		ProgressDialog mProgressDialog;
	
	
		public SubmitAnswer(Context c) {
			// TODO Auto-generated constructor stub
			context=c;
		}
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();			
			mProgressDialog = new ProgressDialog(context);
			mProgressDialog.setMessage("Submitting Answers");
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			req=new Httpcall();
			param=new ArrayList<NameValuePair>();
			SharedPreferences prefs = getSharedPreferences("Preferences", MODE_PRIVATE);
			phn=prefs.getString("name", "null");
			//vi=prefs.getString("village", "null");
			
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			for(int i=0;i<answers.size();i++)
			{
				Log.e("Inside", "Submit");
				query="INSERT INTO `answers` (`phone`, `village`, `set_id`, `question_id`, `question`, `response`, `time`) VALUES ('"+phn+"', '"+village+"', '"+set.get(i)+"', '"+quesid.get(i)+"', '"+questions.get(i).split(";")[0]+"', '"+answers.get(i)+"', NOW());";
				//query="INSERT INTO `answers` (`phone`, `village`, `set_id`, `question_id`, `question`, `response`, `time`) VALUES ('1', 'asdjlksjd', '1', '1', 'হ্যালো', 'হ্যালো', NOW());";
				Log.e("query", query);
				param.add(new BasicNameValuePair("pass", "M-8zd57x"));
				param.add(new BasicNameValuePair("query", query));
				response=req.makeServiceCall("http://disarmproject.in/connection.php",Httpcall.POST, param); 
				param.clear();
				Log.e("Response", response+"");	
			}
				    
			return null;
		}
	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressDialog.dismiss();
			answers.clear();
			AlertDialog.Builder msg=new AlertDialog.Builder(context);
			msg.setMessage("উত্তর সাফল্যের সাথে দায়ের করা হয়েছে . আপনি  হোম স্কৃণে নেভিগেট করতে চান?");
			msg.setCancelable(true);
			msg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Intent i = new Intent(context, Choice.class);
					// set the new task and clear flags
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					i.putExtra("village", village);
					startActivity(i);
				}
			});
			
			msg.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			AlertDialog alert=msg.create();			
			alert.show();
			TextView textView = (TextView) alert.findViewById(android.R.id.message);
			textView.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
					
		}

	}

	

	void generateForm(ArrayList<NavItem> arr)
	{
		LinearLayout ll=(LinearLayout) findViewById(R.id.page);
		for(int i=0;i<arr.size();i++)
		{
			set.add(arr.get(i).set_id+"");
			quesid.add(arr.get(i).ques_id+"");
			LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			int type=arr.get(i).type;
			View view=null;
			if(type==1){
            	view = inflater.inflate(R.layout.textquestion, null);
            	TextView titleView = (TextView) view.findViewById(R.id.ques); 
            	titleView.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
                titleView.setText( arr.get(i).mTitle );
                EditText ans=(EditText) view.findViewById(R.id.ans);
                textstore.add(ans);
                map.add(1);
                questions.add(arr.get(i).mTitle);               
            }
            else if(type==2){
            	view = inflater.inflate(R.layout.singlecorrect, null);
            	TextView titleView = (TextView) view.findViewById(R.id.ques);
            	titleView.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
            	String str[]=arr.get(i).mTitle.split(";",-1);
                titleView.setText( str[0] );
                RadioGroup rg=(RadioGroup) view.findViewById(R.id.rg);
                radiostore.add(rg);
                for(int j=1;j<str.length-1;j++)
                {
                	RadioButton r= new RadioButton(this);
                	r.setText(str[j]);
                	r.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
                	rg.addView(r);
                }
                map.add(2);
                questions.add(arr.get(i).mTitle); 
            }
            else if(type==0){
            	view = inflater.inflate(R.layout.multicorrect, null);
            	TextView titleView = (TextView) view.findViewById(R.id.ques);
            	titleView.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
            	String str[]=arr.get(i).mTitle.split(";",-1);
                titleView.setText( str[0] );
                LinearLayout ll2=(LinearLayout) view.findViewById(R.id.alert);
                ArrayList<CheckBox> checkbox=new ArrayList<CheckBox>();
                for(int j=1;j<str.length-1;j++)
                {
                	CheckBox r= new CheckBox(this);
                	checkbox.add(r);
                	r.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
                	r.setText(str[j]);
                	ll2.addView(r);
                }       
                map.add(0);
                checkstore.add(checkbox);
                questions.add(arr.get(i).mTitle);                 
            }
			ll.addView(view);			
		}
		
		Button b=new Button(MainActivity.this);
		b.setText("সম্পন্ন");
		b.setBackgroundResource(R.drawable.button);
		b.setTextColor(Color.WHITE);
		b.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fetchAnswers();
				for(int i=0;i<answers.size();i++)
					Log.e("Answer "+(i+1), answers.get(i)+"");
				SubmitAnswer s=new SubmitAnswer(MainActivity.this);
				s.execute();
			}
		});
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.addView(b,lp);
	}
	
	void fetchAnswers()
	{
		int q,t,r,c;
		q=t=c=r=0;		
		while(q<map.size())
		{
			int type=map.get(q);
			if(type==0)
			{
				ArrayList<CheckBox> check=checkstore.get(c);
				String ans="";
				for(int j=0;j<check.size();j++)
				{
					if(check.get(j).isChecked())
						ans+=check.get(j).getText().toString()+",";
				}
				answers.add(ans);
				c++;
				
			}
			else if(type==1)
			{
				answers.add(textstore.get(t).getText().toString());
				t++;
			}
			else if(type==2)
			{
				int radioid=radiostore.get(r).getCheckedRadioButtonId();
				RadioButton rad=(RadioButton) radiostore.get(r).findViewById(radioid);
				if(rad==null)
					answers.add("");
				else
				{
					//int index=radiostore.get(r).indexOfChild(rad)+1;
					answers.add(rad.getText().toString());
				}	
				r++;
			}
			q++;
		}		
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setIcon(R.drawable.toplogo);
		Intent i=getIntent();
		Bundle b=i.getExtras();
		String clause=b.getString("query");
		village=b.getString("village");
		String cat=b.getString("categories");
		TextView t=(TextView) findViewById(R.id.cat_text);
		t.setTypeface(Typeface.createFromAsset(getAssets(),"bengali.ttf"));
		t.setText("এই বিভাগগুলি আপনার দ্বারা নির্বাচিত হয়েছে : "+cat);
		Log.e("Village", village);
		textstore=new ArrayList<EditText>();
		radiostore=new ArrayList<RadioGroup>();
		checkstore=new ArrayList<ArrayList<CheckBox>>();
		map=new ArrayList<Integer>();
		questions=new ArrayList<String>();
		answers=new ArrayList<String>();
		set=new ArrayList<String>();
		quesid=new ArrayList<String>();
		Log.e("Query", "SELECT * FROM questions where "+clause+" ORDER BY ques_id asc;");
		MakeRequest sel=new MakeRequest(this, "SELECT * FROM questions where "+clause+" ORDER BY ques_id asc;");
		sel.execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
			case R.id.done : 				
				fetchAnswers();
				for(int i=0;i<answers.size();i++)
					Log.e("Answer "+(i+1), answers.get(i)+"");
				SubmitAnswer s=new SubmitAnswer(this);
				s.execute();
				//answers.clear();				
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
