package com.water.eldowas.ui.adapter;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.water.eldowas.model.BillSummary;

import java.util.ArrayList;
import java.util.List;


import com.water.eldowas.R;

/**
 * Created by Mako on 2/9/2017.
 *
 */

public class BillSummaryAdapter extends RecyclerView.Adapter <BillSummaryAdapter.AdvertsViewHolder> {
    private static final String TAG = "Adverts_Adapter";
    public static List<BillSummary> advertses = new ArrayList<BillSummary>();
    Context context;
//    private String formtype;
//    String type;

    public BillSummaryAdapter(List<BillSummary> billSummaryList){
        this.advertses = billSummaryList;

    }
    @Override
    public AdvertsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new AdvertsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdvertsViewHolder viewHolder, int position) {

        viewHolder.billNo.setText(advertses.get(position).getBillNo());
        viewHolder.billNo.setVisibility(TextView.VISIBLE);

        viewHolder.currentBillAmount.setText(advertses.get(position).getCurrentBillAmount());
        viewHolder.currentBillAmount.setVisibility(TextView.VISIBLE);

        viewHolder.previousReading.setText(advertses.get(position).getPreviousReading());
        viewHolder.previousReading.setVisibility(TextView.VISIBLE);

        viewHolder.currentReading.setText(advertses.get(position).getCurrentReading());
        viewHolder.currentReading.setVisibility(TextView.VISIBLE);

        viewHolder.consumption.setText(advertses.get(position).getConsumption());
        viewHolder.consumption.setVisibility(TextView.VISIBLE);

        viewHolder.billType.setText(advertses.get(position).getBillType());
        viewHolder.billType.setVisibility(TextView.VISIBLE);

        viewHolder.dateOfReading.setText(advertses.get(position).getDateOfReading());
        viewHolder.dateOfReading.setVisibility(TextView.VISIBLE);

        viewHolder.dueDate.setText(advertses.get(position).getDueDate());
        viewHolder.dueDate.setVisibility(TextView.VISIBLE);

        viewHolder.billingPeriod.setText(advertses.get(position).getBillingPeriod());
        viewHolder.billingPeriod.setVisibility(TextView.VISIBLE);

        viewHolder.totalBalance.setText(advertses.get(position).getAccBalance());
        viewHolder.totalBalance.setVisibility(TextView.VISIBLE);








        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {


            }
        });

    }


    public interface ItemClickListener{
        void onClick(View view, int position, boolean isLongClicked);
    }

    @Override
    public int getItemCount() {
        return advertses.size();
    }

    public BillSummary getAdvert(int position){

        return this.advertses.get(position);
    }
    public static  class AdvertsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView billNo;
        public TextView currentBillAmount;
        public TextView totalBalance;

        public TextView previousReading;
        public TextView currentReading;
        public TextView consumption;
        public TextView billType;
        public TextView dateOfReading;
        public TextView dueDate;
        public TextView billingPeriod;



        public ItemClickListener clickListener;


        public AdvertsViewHolder(View view) {
            super(view);
//            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);// this contains the name of sender
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            billNo = (TextView)view.findViewById(R.id.bill_number);
            currentBillAmount = (TextView)view.findViewById(R.id.bill_amount);
            previousReading = (TextView)view.findViewById(R.id.bill_previous);
            currentReading = (TextView)view.findViewById(R.id.bill_current_reading);
            consumption = (TextView)view.findViewById(R.id.bill_consumption);
            billType = (TextView)view.findViewById(R.id.bil_type);
            dateOfReading = (TextView)view.findViewById(R.id.bill_date_read);
            dueDate = (TextView)view.findViewById(R.id.bill_due_date);
            billingPeriod = (TextView)view.findViewById(R.id.bill_period);
            totalBalance  = (TextView)view.findViewById(R.id.bill_total_balance);




            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

        }
        public void setClickListener(ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }
    }
}