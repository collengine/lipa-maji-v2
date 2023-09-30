package com.water.eldowas;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.water.eldowas.model.AccountObject;
import com.water.eldowas.ui.activity.AddAccountActivity;
import com.water.eldowas.ui.activity.MonthlyBillActivity;
import com.water.eldowas.ui.activity.SearchActivity;
import com.water.eldowas.util.Item_Model;
import com.water.eldowas.util.RecyclerClick_Listener;
import com.water.eldowas.util.RecyclerTouchListener;
import com.water.eldowas.util.RecyclerView_Adapter;
import com.water.eldowas.util.Toolbar_ActionMode_Callback;

import java.util.ArrayList;


import static com.water.eldowas.HomeFragment.USERS_ACCOUNTS;
import static com.water.eldowas.HomeFragment.USERS_CHILD;

/**
 * Created by SONU on 27/03/16.
 */
public class RecyclerView_Fragment extends Fragment {
    private static View view;
    private static RecyclerView recyclerView;
    private static ArrayList<Item_Model> item_models;
    private static RecyclerView_Adapter adapter;
    private ActionMode mActionMode;
    private DatabaseReference mFirebaseDatabaseReference;
    private String uiduserx;
    private String TAG = "RecyclerViewFragment";
    private static  RecyclerView_Fragment fragment;

    private static String UID = "USER_ID";

    private String user_name;
    private String firstname;
    private String lastname;
    public String accountNumber;
    private String uid;
    private String phoneNumber;

    public RecyclerView_Fragment() {
    }

    public static RecyclerView_Fragment createInstance(String uid, String uidz) {
        fragment = new RecyclerView_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(UID , uid);
        bundle.putString("PHONE", uidz);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

          //  uiduserx = fragment.getArguments().getString(UID);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        uiduserx = fragment.getArguments().getString(UID);
        phoneNumber =  fragment.getArguments().getString("PHONE");
       // Log.e(TAG, " User uid : " + uiduserx);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference().child(USERS_ACCOUNTS).child(uiduserx);


        return view;
    }

    @Override
    public void onViewCreated(View viewz, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // adapter.notifyDataSetChanged();
        FloatingActionButton fabSearch = (FloatingActionButton)viewz.findViewById(R.id.contact_fabButton);
        FloatingActionButton fabAddAccount = (FloatingActionButton)viewz.findViewById(R.id.fab_add_account);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                myIntent.putExtra("USER_NAME", user_name);
                myIntent.putExtra("USER_FIRST_NAME", firstname);
                myIntent.putExtra("USER_LAST_NAME", lastname);
                myIntent.putExtra("PHONE" , phoneNumber);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);



            }
        });

        fabAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), AddAccountActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                myIntent.putExtra("USER_NAME", user_name);
                myIntent.putExtra("USER_FIRST_NAME", firstname);
                myIntent.putExtra("USER_LAST_NAME", lastname);
                myIntent.putExtra("PHONE" , phoneNumber);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(myIntent);


            }
        });

        FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(uiduserx).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {// mFirebaseDatabaseReference.child(USERS_CHILD).child(uiduserx).child(FOLLOWING)

                user_name = dataSnapshot.child("userName").getValue(String.class);
                uid = dataSnapshot.child("uid").getValue(String.class);
                firstname = dataSnapshot.child("firstName").getValue(String.class);
                lastname = dataSnapshot.child("lastName").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        populateRecyclerView();
        implementRecyclerViewClickListeners();
    }

    //Populate ListView with dummy data
    private void populateRecyclerView() {
        item_models = new ArrayList<>();

        mFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "snapshot: " + dataSnapshot.toString());
                item_models.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    AccountObject friendlyMessageObject = postSnapshot.getValue(AccountObject.class);

                    item_models.add(new Item_Model(friendlyMessageObject.getSubTitle(),  friendlyMessageObject.getAccountNumber(), friendlyMessageObject.getAccountRoute(), friendlyMessageObject.getAccountIndex()));




                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new RecyclerView_Adapter(getActivity(), item_models, "AfterAccount");
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerClick_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                ConnectivityManager ConnectionManager=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()==true ){


                if (mActionMode != null){
                    onListItemSelect(position);
                }else if(mActionMode == null){

                    Button viewBtn = (Button) view.findViewById(R.id.item_bill_account_viewBtn);
                    viewBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Item_Model account = adapter.getAccount(position);
                            accountNumber = account.getSubTitle();
                            Intent mIntent2 = new Intent(getContext(), MonthlyBillActivity.class);
                            mIntent2.putExtra("ACCOUNT_NUMBER", accountNumber);
                            mIntent2.putExtra("USER_ID", uiduserx);
                            mIntent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//Revisit this

                            startActivity(mIntent2);
                            getActivity().finish();
                        }
                    });
                }
                }

                else
                {
                    Toast.makeText(getContext(), "No Internet connection", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        adapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = adapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(),adapter, null, item_models, false, uiduserx));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
            //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(adapter
                    .getSelectedCount()) + " selected");


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = adapter
                .getSelectedIds();//Get selected ids

        //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                //If current id is selected remove the item via key


                item_models.remove(selected.keyAt(i));
                adapter.notifyDataSetChanged();//notify adapter

            }
        }
        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use

    }

    public void itemRemoved(int position){


//        messagereference = FirebaseDatabase.getInstance().getReference().child(MESSAGES_CHILD);
//        DatabaseReference ref = mFirebaseAdapter.getRef(position);
//        String key = ref.getKey();
//        messagereference.child(key).removeValue();
//        Toast.makeText(getContext(),"Item deleted :", Toast.LENGTH_SHORT).show();
//        formAdapter.notifyDataSetChanged();

    }
}