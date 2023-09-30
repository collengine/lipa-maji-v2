package com.water.eldowas.util;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.water.eldowas.R;
import com.water.eldowas.model.NotificationModel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SONU on 27/03/16.
 */
public class ListView_Adapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>  {
    public ArrayList<Item_Model> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private String[] colorlist = {"#F44336","#9C27B0","#3F51B5","#673AB7","#2196F3","#00BCD4","#009688"};
    public int mType;
    private String type;
    private final int ACCOUNT = 1;
    private final int AFTERACCOUNT = 2;

    public ListView_Adapter(Context context,
                                ArrayList<Item_Model> arrayList, String type) {
        this.context = context;
        this.type = type;
        this.arrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater . from(parent. getContext());

        switch (viewType){

            case ACCOUNT:
                View viewTWO = inflater.inflate(R . layout.item_notification , parent , false );
                viewHolder= new AccountViewHolder(viewTWO);
                break;
            case  AFTERACCOUNT:
                View viewONE = inflater.inflate(R . layout.item_notification , parent, false );
                viewHolder = new DemoViewHolder(viewONE);

                break;


            default:
                View viewThree = inflater.inflate(R . layout.item_my_bill , parent , false );
                viewHolder= new DemoViewHolder(viewThree);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Setting text over text view

        if (mType == ACCOUNT){
            final AccountViewHolder eHolder = (AccountViewHolder) holder;


            NotificationModel notificationModel = new NotificationModel(arrayList.get(position).getNotificationTitle(), arrayList.get(position).getNotificationMessage(),arrayList.get(position).getNotificationTimestamp(),arrayList.get(position).getDateCreated() );
Log.e("Adapter" , notificationModel.toString());

////            HashMap<String,Object> hashMap = notificationModel.getDateCreatedLong();
          // Log.e("ListViewAdapter", "The long value "  + String.valueOf(notificationModel.getDateCreatedLong()));
            long lo  = (long) 1541595975463L;

            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    (notificationModel.getDateCreatedLong()),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

            int color = getColor();
            eHolder.notificationTitle.setText(notificationModel.getNotificationTitle());
            eHolder.notficationMessage.setText(notificationModel.getNotificationMessage());
            eHolder.notificationTimestamp.setText(timeAgo);


        }else if(mType == AFTERACCOUNT){
            final DemoViewHolder eHolder = (DemoViewHolder) holder;

           // NotificationModel notificationModel = new NotificationModel(arrayList.get(position).getNotificationTitle(), arrayList.get(position).getNotificationMessage(),arrayList.get(position).getNotificationMessage(),arrayList.get(position).getDateCreated() );
         //   Log.e("ListViewAdapter", "The long value "  + String.valueOf(notificationModel.getDateCreatedLong()));
            NotificationModel notificationModel = new NotificationModel(arrayList.get(position).getNotificationTitle(), arrayList.get(position).getNotificationMessage(),arrayList.get(position).getNotificationTimestamp(),arrayList.get(position).getDateCreated() );
            Log.e("Adapter" , notificationModel.toString());

            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    (notificationModel.getDateCreatedLong()),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            eHolder.notificationTitle.setText(notificationModel.getNotificationTitle());
            eHolder.notficationMessage.setText(notificationModel.getNotificationMessage());
            eHolder.notificationTimestamp.setText(timeAgo);
        }




        /** Change background color of the selected items in list view  **/
        holder.itemView
                .setBackgroundColor(mSelectedItemsIds.get(position) ? 0x9934B5E4
                        : Color.WHITE);


    }


    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public Item_Model getAccount(int position) {
        return this.arrayList.get(position);
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


    public int getColor(){
        Random random = new Random();
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }


    public class DemoViewHolder extends RecyclerView.ViewHolder {


        public TextView notificationTitle, notficationMessage, notificationTimestamp;




        public DemoViewHolder(View view) {
            super(view);



            this.notificationTitle = (TextView) view.findViewById(R.id.notification_title);
            this.notficationMessage = (TextView) view.findViewById(R.id.notification_message);
            this.notificationTimestamp = (TextView) view.findViewById(R.id.notification_time_stamp);

        }
    }


    public class AccountViewHolder extends RecyclerView.ViewHolder {


        public TextView notificationTitle, notficationMessage, notificationTimestamp;



        public AccountViewHolder(View view) {
            super(view);


            this.notificationTitle = (TextView) view.findViewById(R.id.notification_title);
            this.notficationMessage = (TextView) view.findViewById(R.id.notification_message);
            this.notificationTimestamp = (TextView) view.findViewById(R.id.notification_time_stamp);


        }
    }

    @Override
    public int getItemViewType(int position) {


        if(type.equalsIgnoreCase("Account")){
            mType = ACCOUNT;
        }else if(type.equalsIgnoreCase("AfterAccount")){
            mType = AFTERACCOUNT;
        }
        return mType;
        //   return(compositeList.get(position).getType().equalsIgnoreCase(Helper.ADVERT) ? ADVERT:MESSAGE);
    }
}