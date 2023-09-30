/*
 *
 *  * Copyright (C) 2017 Safaricom, Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.water.eldowas.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.water.eldowas.ListView_Fragment;
import com.water.eldowas.R;
import com.water.eldowas.RecyclerView_Fragment;
import com.water.eldowas.login.LoginActivity;
import com.water.eldowas.login.PhoneAuthActivity;
import com.water.eldowas.ui.FragmentDrawer;
import com.water.eldowas.util.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener  {
    private static ViewPagerAdapter adapter;
    private Fragment recyclerViewFragment;
    private Fragment listViewragment;
    private String uid;
    private String token;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FragmentDrawer drawerFragment;
    private boolean isInitialized = false;
    private boolean doubleBackToExitPressedOnce = false;
    private String phoneNumber;
    private String mainIntent = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permissions" , Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }

      /*  ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
            Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(MainActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();

        }*/



          //  Toast.makeText(MainActivity.this, "Network Available", Toast.LENGTH_LONG).show();



                try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true;
            }else{
                //   Log.d(TAGs, "Already initialized");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
         mFirebaseUser = mFirebaseAuth.getCurrentUser();

        token = FirebaseInstanceId.getInstance().getToken();



        if(mFirebaseUser == null){
            Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        }

       //  uid = mFirebaseAuth.getCurrentUser().getUid();

        Intent intent3 = getIntent();
        uid =  intent3.getStringExtra("USER_ID");
        mainIntent = intent3.getStringExtra("MAIN");
        if(mainIntent!= null){

        }

        phoneNumber = mFirebaseUser.getPhoneNumber();
        if (null == uid) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            //    myIntent.putExtra("USER_ID", uid);
            finish();
            return;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setBackgroundColor(Color.parseColor("#1dcaff"));
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layoutDra), toolbar, uid );
        drawerFragment.setDrawerListener( this);

        recyclerViewFragment =  RecyclerView_Fragment.createInstance(uid, phoneNumber );
        listViewragment =  ListView_Fragment.createInstance(uid, phoneNumber );

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);//Set up View Pager

        if( mainIntent!= null){

        }
        if( mainIntent!= null && mainIntent.equalsIgnoreCase("MAIN")){

            viewPager.setCurrentItem(2);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager
        tabLayout.setBackgroundColor(Color.parseColor("#1dcaff"));
        tabLayout.setTabTextColors(Color.parseColor("#fdfaff"), Color.parseColor("#ffffff"));








    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(recyclerViewFragment, "My Accounts");
        adapter.addFrag(listViewragment, "My Notification");
       // adapter.addFrag(new ListView_Fragment(), "My Notification");

        viewPager.setAdapter(adapter);
    }


    //Return current fragment on basis of Position
    public Fragment getFragment(int pos) {
        return adapter.getItem(pos);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView( int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
     /*       case 0 :

                *//*Intent mIntent2 = new Intent(this, MainActivity.class);
                mIntent2.putExtra("USER_ID", uid);
                mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent2);
                finish();*//*
                Intent mIntentrep = new Intent(this, SendReportActivity.class);
                mIntentrep.putExtra("USER_ID", uid);
                mIntentrep.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntentrep);
                break ;*/


            case 0 :

                //Tambua Staff
                //

                Intent mIntent3 = new Intent(this, ContactActivity.class);
                mIntent3.putExtra("USER_ID", uid);
                mIntent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent3);


                break ;
            case 1 :

                mFirebaseAuth.signOut();
                startActivity(new Intent(this, PhoneAuthActivity.class));
                finish();
                break ;
            case 2 :
//

                Intent mIntent1 = new Intent(this, AboutActivity.class);
                mIntent1.putExtra("USER_ID", uid);
                mIntent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent1);
                break ;
            case 3 :
                //About Contents
                Intent mIntent4 = new Intent(this, AboutActivity.class);
                mIntent4.putExtra("USER_ID", uid);
                mIntent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this
                startActivity(mIntent4);
               // finish();
            default :
                break ;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewPager, fragment);
            fragmentTransaction.commit();
// set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }



    @Override
    public void onBackPressed(){
//        if(getSupportFragmentManager().getBackStackEntryCount()>0)getSupportFragmentManager().popBackStack();
//        else finish();

        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to Exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        /*
        if

         */
    }
}
