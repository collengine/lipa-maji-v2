package com.water.eldowas.ui.adapter;


import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.water.eldowas.model.Contacts;

import com.water.eldowas.R;



/**
 * Created by Mako on 2/9/2017.
 */
public class ContactsAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder> {
    private static final String TAG = "MContact Adapter";
    public List<Contacts> contacts = new ArrayList<>();
    private List<String> follow = new ArrayList<>();
    public static final String USERS_CHILD = "Vaultusers";
    public static final String FOLLOWING = "following";
    public static final String FOLLOWED = "followed";
    public RecyclerView myRecyclerView;
    private RelativeLayout emptyLayout;
    public final int CONTACT = 1;



    private SparseBooleanArray mSelectedItemsIds;

    Context context;
    private ProgressBar mProgressBar;
    public int mType;
//    private String formtype;
//    String type;

    public ContactsAdapter(DatabaseReference ref, final String uid, ProgressBar mProgressBar, RecyclerView myRecyclerView, RelativeLayout emptyLayout) {
        this.mProgressBar = mProgressBar;
        this.myRecyclerView = myRecyclerView;
        this.emptyLayout = emptyLayout;
        mSelectedItemsIds = new SparseBooleanArray();





        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contacts.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Contacts friendlyMessageObject = postSnapshot.getValue(Contacts.class);
                contacts.add(friendlyMessageObject);




                }

                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater . from(parent. getContext());
        switch (viewType){

            case CONTACT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_contacts, parent, false);
                viewHolder= new ContactViewHolder(view);
                break;
            default:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_contacts, parent, false);
                viewHolder= new ContactViewHolder(view2);
                break;

        }

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       // this.mProgressBar.setVisibility(ProgressBar.GONE);
        holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.TRANSPARENT);
        if (holder.getItemViewType() == CONTACT) {

            final ContactViewHolder viewHolder = (ContactViewHolder) holder;


            Contacts contacts22 = contacts.get(position);

            //   if (contacts22.getState().equalsIgnoreCase("on")) {



            viewHolder.accountName.setText(contacts22.getMeterName());
           viewHolder.accountNumber.setText(contacts22.getMeterNumber());


            viewHolder.accountName.setVisibility(TextView.VISIBLE);
            viewHolder.accountNumber.setVisibility(TextView.VISIBLE);


            //  }


        }
    }



    public Contacts getContact(int position) {
        return this.contacts.get(position);
    }

    @Override
    public int getItemCount() {

        return this.contacts.size();
    }
    @Override
    public int getItemViewType(int position) {

        if(this.contacts.isEmpty()){

            myRecyclerView.setVisibility(RecyclerView.GONE);
            emptyLayout.setVisibility(RelativeLayout.VISIBLE);
            mProgressBar.setVisibility(ProgressBar.GONE);




        }else{
            mType = CONTACT;
        }

        return mType;
        //   return(compositeList.get(position).getType().equalsIgnoreCase(Helper.ADVERT) ? ADVERT:MESSAGE);
    }

    public static  class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        public TextView accountName;
        public TextView accountNumber;

        public ContactViewHolder(View view) {
            super(view);



            accountName = (TextView) itemView.findViewById(R.id.account_name);
            accountNumber  = (TextView) itemView.findViewById(R.id.account_number);//contract_profile_status



            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }


    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }


    //Remove selected selections
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }


    //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


}



