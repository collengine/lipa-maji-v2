package com.water.eldowas.ui.activity;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.water.eldowas.R;
import com.water.eldowas.bill.BillFragment;
import com.water.eldowas.bill.BottomNavigationBehavior;
import com.water.eldowas.bill.DownloadFragment;
import com.water.eldowas.bill.MpesaFragment;
import com.water.eldowas.bill.StatementFragment;
import com.water.eldowas.util.TokenObject;

public class MonthlyBillActivity extends AppCompatActivity {


    private String month;
    private String billNumber;
    private String currentReading;
    private String previousReading;
    private String consumptionUnits;
    private String waterBill;
    private String sewerageBill;
    private String meterRent;
    private String billAmount;
    private String monthlyBill;
    private String dateRead;
    private String dueDate;
    private String dateUpdated;


    private TextView monthTv;
    private TextView billNumberTv;
    private TextView currentReadingTv;
    private TextView previousReadingTv;
    private TextView consumptionUnitsTv;
    private TextView waterBillTv;
    private TextView sewerageBillTv;
    private TextView meterRentTv;
    private TextView billAmountTv;
    private TextView monthlyBillTv;
    private TextView dateReadTv;
    private TextView dueDateTv;//
    private TextView dateUpdatedTv;
    private Button proceedBtn;

    private String uid;
    private RequestQueue queue;
    private String mpesacoded;
    private String TOKENS = "tokens";
    private String account_id,accBal,accMeter,accName,accDue;

    private TokenObject tokenObject;
    private ActionBar toolbar;

    private Fragment billFragment;
    private  Fragment mpesaFragment;
    private  Fragment statementFragment;
    private  Fragment downloadFragment;

    private final static String BACK_STACK_ROOT_TAG = "root_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_bill);

        toolbar = getSupportActionBar();
        Intent myintent = getIntent();
        uid = myintent.getStringExtra("USER_ID");
        account_id = myintent.getStringExtra("ACCOUNT_NUMBER");

        //for statement
        accBal = myintent.getStringExtra("ACCBALANCE");
        accMeter = myintent.getStringExtra("METERNO");
        accName = myintent.getStringExtra("NAME");
        accDue = myintent.getStringExtra("DUEDATE");

        billFragment = BillFragment.newInstance(uid, account_id);
        mpesaFragment = MpesaFragment.newInstance(uid, account_id);
        statementFragment = StatementFragment.newInstance(uid, account_id);
        downloadFragment = DownloadFragment.newInstance(uid, account_id, "" );

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        toolbar.setTitle("MonthlyBill");
        loadFragment(BillFragment.newInstance(uid, account_id));

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_bill:
                    toolbar.setTitle("MonthlyBill");
                    loadFragment(billFragment);
                    return true;
               /* case R.id.navigation_pay:
                    toolbar.setTitle("Pay Bill");
                    loadFragment(mpesaFragment);
                    return true;*/
                case R.id.navigation_statement:
                    toolbar.setTitle("Statements");
                    loadFragment(statementFragment);
                    return true;
                case R.id.navigation_download:
                    toolbar.setTitle("Downloads");
                    loadFragment(downloadFragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();

    }

    @Override
    public void onBackPressed(){
//        if(getSupportFragmentManager().getBackStackEntryCount()>1)getSupportFragmentManager().popBackStack();
//        else
            {
            Intent intent = new Intent(MonthlyBillActivity.this, MainActivity.class);
            intent.putExtra("USER_ID", uid );
            startActivity(intent);
            finish();
        }


    }

}
