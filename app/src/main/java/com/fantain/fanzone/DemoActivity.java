package com.fantain.fanzone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by satya on 12/4/16.
 */
public class DemoActivity extends Activity {

    private GridView mygrid;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse responseee;

    HttpClient httpclient;
    private LayoutInflater inflater= null;
    Activity ac;
    ProgressDialog mProgressDialog;
    String URL;
    Context mcontext;
    ImageLoader imageLoader;

    private ArrayList<String> id = new ArrayList<String>();
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> email = new ArrayList<String>();
    private ArrayList<String> address = new ArrayList<String>();
    private ArrayList<String> gender = new ArrayList<String>();
    private ArrayList<String> profile_pics = new ArrayList<String>();

    private ArrayList<String> mobile = new ArrayList<>();
    private ArrayList<String> home = new ArrayList<>();
    private ArrayList<String> office = new ArrayList<>();

    private ArrayList<String> idlist = new ArrayList<>();

    private Handler handler = new Handler();
    SQLiteDatabase db;
    String myid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_acttivity);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            ac = this;
            mygrid = (GridView) findViewById(R.id.mygrid);
            handler.post( new Runnable() {

                @Override
                public void run() {

                    new DownloadJSON().execute();
                }
            });


            imageLoader = new ImageLoader(mcontext);

            try {
                Cursor c = db.rawQuery("SELECT * FROM items", null);
                if (c.getCount() == 0) {
                    Toast.makeText(DemoActivity.this,
                            "No data Found--- !!!", Toast.LENGTH_LONG).show();

                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    //view all
                    buffer.append("id: " + c.getString(0) + "\n");

                    idlist.add(c.getString(0));


                }

            } catch (Exception e) {
            }


        }
    }


    // asynctask for data fetching

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progress dialog
            mProgressDialog = new ProgressDialog(DemoActivity.this);
            // Set progress dialog title
            mProgressDialog.setTitle("Please Wait Data Loading");
            // Set progress dialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progress dialog
            mProgressDialog.show();
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                callServerToSaveData();


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {


           mygrid.setAdapter(new ImageAdapter(ac,name,email));

            mProgressDialog.dismiss();

        }

    }


    // parsing data from server


    public String callServerToSaveData() throws JSONException, UnsupportedEncodingException {

        String type = null;
        JSONObject json = new JSONObject();
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;

        URL =  "http://pratikbutani.x10.mx/json_data.json";



        try {
            HttpPost post = new HttpPost(URL);


            //   System.out.println("pppppppppppppppppppppp***********"+post);
            // Collect the response
            response = client.execute(post);
            // Checking response
            if (response != null) {
                // get response content
                HttpEntity entity = response.getEntity();
                // convert stream to String
                BufferedReader buf = new BufferedReader(new InputStreamReader(
                        entity.getContent()));
                // building string
                StringBuilder sb1 = new StringBuilder();
                String line = null;
                while ((line = buf.readLine()) != null) {
                    sb1.append(line);
                }
                //System.out.println("Recieve----from------server---doc------->" + sb1.toString());


                JSONObject list = new JSONObject(sb1.toString());


                JSONArray jsonarray = list.getJSONArray("contacts");
                // System.out.println("my json datas----------->    "+jsonarray.length());

                for (int i = 0; i < jsonarray.length(); i++) {
                    list = jsonarray.getJSONObject(i);

                    id.add(list.getString("id"));
                    name.add(list.getString("name"));
                    email.add(list.getString("email"));
                    address.add(list.getString("address"));
                    gender.add(list.getString("gender"));
                    profile_pics.add(list.getString("profile_pic"));

                    // Phone node is JSON Object
                    JSONObject phones = list.getJSONObject("phone");
                    mobile.add(phones.getString("mobile"));
                    home.add(phones.getString("home"));
                    office.add(phones.getString("office"));




                   // System.out.println("My name list--------->"+mobile);

                }
            }
        } catch (Exception e) {
        }
        return type;
    }


    //  set adapter

    public class ImageAdapter extends BaseAdapter {


        public ImageAdapter(Context context,ArrayList<String> name,
                            ArrayList<String> email) {
            // TODO Auto-generated constructor stub
        }
        public int getCount() {

            return id.size();



        }
        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }
        class ViewHolder
        {
            TextView cname;
            ImageView mypics;
            CheckBox checkbox;
            int postion;
            View view;
            ViewGroup viewgroup;


        }
        public View getView(final int position, View convertView, ViewGroup parent) {

                final ViewHolder vh;
                inflater = LayoutInflater.from(ac);
                vh = new ViewHolder();
                vh.postion = position;
                vh.view = convertView;
                vh.viewgroup = parent;
                // convertView = LayoutInflater.from(mcontext).inflate(R.layout.demo_list_view, parent, false);
                convertView = inflater.inflate(R.layout.demo_list_view, null);

                vh.mypics = (ImageView) convertView.findViewById(R.id.demo_image);
                vh.cname = (TextView) convertView.findViewById(R.id.name);
                vh.cname.setText(name.get(position));
                vh.checkbox = (CheckBox) convertView.findViewById(R.id.grid_item_checkbox);

                // Passes  images URL into ImageLoader.class
                imageLoader.DisplayImage(profile_pics.get(position), vh.mypics);



/*

            for(String s:id)
            {
                for(String s1:idlist )
                {
                    if(s.equals(s1))
                    {
                        Toast.makeText(DemoActivity.this,
                                "equal id store !!!", Toast.LENGTH_LONG).show();
                         vh.checkbox.setEnabled(true);

                    }
                }

            }
*/



            vh.checkbox.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {
                    //is checkbox checked?




                    if (((CheckBox) v).isChecked()) {
                        /*Toast.makeText(DemoActivity.this,
                                "I am Checked Boss !!!", Toast.LENGTH_LONG).show();*/
                    myid = id.get(position);

                        db=openOrCreateDatabase("idRecordsDb", Context.MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS items(id VARCHAR);");

                        db.execSQL("INSERT INTO items VALUES('"+myid+"');");

                        System.out.println("show my id  ----------->>"+myid);
                    } /*else {
                        //calling cursor object
                        Cursor c=db.rawQuery("SELECT * FROM items WHERE id='"+myid+"'", null);
                        if(c.moveToFirst())
                        {
                            // delete data from table
                            db.execSQL("DELETE FROM items WHERE id='"+myid+"'");
                            System.out.println("show my id  --delete--------->>"+myid);
                        }

                    }*/
                }
            });


            convertView.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent details = new Intent(getApplicationContext(),DetailActivity.class);

                    details.putExtra("id", id.get(position));
                    details.putExtra("name",name.get(position));
                    details.putExtra("email",email.get(position));
                    details.putExtra("address",address.get(position));
                    details.putExtra("gender",gender.get(position));
                    details.putExtra("pics",profile_pics.get(position));
                    details.putExtra("mobile",mobile.get(position));
                    details.putExtra("home",home.get(position));
                    details.putExtra("office",office.get(position));

                    startActivity(details);
                    overridePendingTransition(R.anim.slide_down, R.anim.slide_up);


                }
            });


            return convertView;
        }



    }
}
