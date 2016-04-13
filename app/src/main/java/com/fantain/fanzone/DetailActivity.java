package com.fantain.fanzone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by satya on 12/4/16.
 */
public class DetailActivity extends Activity {


    private ImageView detail_image;
    private TextView  id,name,email,gender,address,mobile,home,office;

    private String mid,mname,memail,mgender,maddress,mimage,mmobile,mhome,moffice;

    ImageLoader imageLoader;
    Activity ac;
    Context mcontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);


        ac = this;
        detail_image = (ImageView) findViewById(R.id.image_detail);
        id = (TextView) findViewById(R.id.id);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        gender = (TextView) findViewById(R.id.gender);
        address = (TextView) findViewById(R.id.address);
        mobile = (TextView) findViewById(R.id.mobile);
        home = (TextView) findViewById(R.id.home);
        office = (TextView) findViewById(R.id.office);

        Intent intent = getIntent();

        mid =  intent.getStringExtra("id");
        mname =  intent.getStringExtra("name");
        memail =  intent.getStringExtra("email");
        mgender =  intent.getStringExtra("gender");
        maddress =  intent.getStringExtra("address");
        mimage =    intent.getStringExtra("pics");

        mmobile =    intent.getStringExtra("mobile");
        mhome =    intent.getStringExtra("home");
        moffice =    intent.getStringExtra("office");

        imageLoader = new ImageLoader(mcontext);

        imageLoader.DisplayImage(mimage,  detail_image);

        id.setText(mid);
        name.setText(mname);
        email.setText(memail);
        gender.setText(mgender);
        address.setText(maddress);
        mobile.setText(mmobile);
        home.setText(mhome);
        office.setText(moffice);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

       // Intent details = new Intent(getApplicationContext(),DemoActivity.class);
       // startActivity(details);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}
