package com.water.eldowas.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.water.eldowas.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by SONU on 27/03/16.
 */
public class RecyclerView_Adapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>  {
    public ArrayList<Item_Model> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private String[] colorlist = {"#F44336","#9C27B0","#3F51B5","#673AB7","#2196F3","#00BCD4","#009688"};
    public int mType;
    private String type;
    private final int ACCOUNT = 1;
    private final int AFTERACCOUNT = 2;

    public RecyclerView_Adapter(Context context,
                                ArrayList<Item_Model> arrayList, String type) {
        this.context = context;
        this.type = type;
        this.arrayList = arrayList;
        mSelectedItemsIds = new SparseBooleanArray();

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
                View viewTWO = inflater.inflate(R . layout.item_fetch_account , parent , false );
                viewHolder= new AccountViewHolder(viewTWO);
                break;
            case  AFTERACCOUNT:
                View viewONE = inflater.inflate(R . layout.item_my_bill , parent, false );
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

            int color = getColor();
            eHolder.accountNmae.setText(arrayList.get(position).getTitle());
            eHolder.accountNumber.setText(arrayList.get(position).getSubTitle());
            String initial = arrayList.get(position).getTitle().substring(0,1).toUpperCase();

            eHolder.accountInitial.setText(initial);
            if(position <  colorlist.length){
                GradientDrawable draw = new GradientDrawable();
                draw.setShape(GradientDrawable.OVAL);
                draw.setColor(Color.parseColor(colorlist[position]));
                eHolder.accountInitial.setBackground(draw);
            }else{
                GradientDrawable draw = new GradientDrawable();
                draw.setShape(GradientDrawable.OVAL);
                draw.setColor(Color.parseColor(colorlist[0]));
                eHolder.accountInitial.setBackground(draw);
            }

        }else if(mType == AFTERACCOUNT){
            final DemoViewHolder eHolder = (DemoViewHolder) holder;
            int color = getColor();
            eHolder.accountNmae.setText(arrayList.get(position).getTitle());
            eHolder.accountNumber.setText(arrayList.get(position).getSubTitle());
            String initial = arrayList.get(position).getTitle().substring(0,1).toUpperCase();

            eHolder.accountInitial.setText(initial);
            if(position <  colorlist.length){
                GradientDrawable draw = new GradientDrawable();
                draw.setShape(GradientDrawable.OVAL);
                draw.setColor(Color.parseColor(colorlist[position]));
                eHolder.accountInitial.setBackground(draw);
                eHolder.btn.setBackgroundColor(Color.parseColor(colorlist[position]));
            }else{
                GradientDrawable draw = new GradientDrawable();
                draw.setShape(GradientDrawable.OVAL);
                draw.setColor(Color.parseColor(colorlist[0]));
                eHolder.accountInitial.setBackground(draw);
                eHolder.btn.setBackgroundColor(Color.parseColor(colorlist[0]));
            }
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


        public TextView accountNmae, accountNumber, accountInitial;
        public de.hdodenhof.circleimageview.CircleImageView circleImage;
        public Button btn ;


        public DemoViewHolder(View view) {
            super(view);


            this.accountNmae = (TextView) view.findViewById(R.id.item_bill_account_name);
            this.accountNumber = (TextView) view.findViewById(R.id.item_bill_account_number);
            this.accountInitial = (TextView) view.findViewById(R.id.item_bill_account_initial);
//        this.circleImage = (de.hdodenhof.circleimageview.CircleImageView)view.findViewById(R.id.circle_image);
            this.btn = (Button)view.findViewById(R.id.item_bill_account_viewBtn);

        }
    }


    public class AccountViewHolder extends RecyclerView.ViewHolder {


        public TextView accountNmae, accountNumber, accountInitial;



        public AccountViewHolder(View view) {
            super(view);


            this.accountNmae = (TextView) view.findViewById(R.id.item_bill_account_name);
            this.accountNumber = (TextView) view.findViewById(R.id.item_bill_account_number);
            this.accountInitial = (TextView) view.findViewById(R.id.item_bill_account_initial);


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