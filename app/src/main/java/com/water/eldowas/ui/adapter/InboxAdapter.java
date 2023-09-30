package com.water.eldowas.ui.adapter;

/**
 * Created by sulu on 24/06/2018.
 */


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


import com.water.eldowas.model.Reciept;
import com.water.eldowas.R;

/**
 * Created by Mako on 2/9/2017.
 */
public class InboxAdapter extends RecyclerView.Adapter <InboxAdapter.InboxViewHolder> {
    private static final String TAG = "Inbox_Adapter_Activity";
    public List<Reciept> reciept ;
    private RelativeLayout myRelativeLayout;
    private RecyclerView myRecylerView;
    public final int CONTACT = 1;
    public final int EMPTY = 2;
    private ProgressBar progressBar;
    public int mType;
    Context context;
//    private String formtype;
//    String type;

    public InboxAdapter(DatabaseReference ref, final String UID, final RecyclerView myRecylerView, final RelativeLayout myRelativeLayout ){
        this.myRecylerView = myRecylerView;
        this.myRelativeLayout = myRelativeLayout;
        this.reciept = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reciept.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    Reciept friendlyMessageObject = postSnapshot.getValue(Reciept.class);
                    //   if (null == friendlyMessageObject.getUid() || friendlyMessageObject.getUid().equalsIgnoreCase(UID)) {
                    reciept.add(friendlyMessageObject);

                }
                notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if(contacts.isEmpty()){
//            myRecylerView.setVisibility(RecyclerView.GONE);
//            myRelativeLayout.setVisibility(RelativeLayout.VISIBLE);
//
//        }
    }
    @Override
    public InboxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InboxViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater . from(parent. getContext());
        switch (viewType){

            case CONTACT:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reciept, parent, false);
                viewHolder= new InboxAdapter.InboxViewHolder(view);
                break;

            default:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reciept, parent, false);
                viewHolder= new InboxAdapter.InboxViewHolder(view2);
                break;

        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InboxViewHolder viewHolder, int position) {

        Reciept contacts12 = reciept.get(position);


        viewHolder.month.setText(contacts12.getMonth());
        viewHolder.month.setVisibility(TextView.VISIBLE);

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                (contacts12.getDateCreatedLong()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        viewHolder.timestamp.setText(timeAgo);
        viewHolder.timestamp.setVisibility(TextView.VISIBLE);

        viewHolder.mpesaNumber.setText(contacts12.getMpesaRecieptNumber());
        viewHolder.mpesaNumber.setVisibility(TextView.VISIBLE);
        viewHolder.amount.setText(contacts12.getAmount());
        viewHolder.amount.setVisibility(TextView.VISIBLE);





        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClicked) {
                //    Toast.makeText(context, "nani"  , Toast.LENGTH_SHORT).show();
                //      Toast.makeText(context,"Single Click on Image :"+ position, Toast.LENGTH_SHORT).show();

            }
        });

    }


    public interface ItemClickListener{
        void onClick(View view, int position, boolean isLongClicked);
    }
    @Override
    public int getItemViewType(int position) {

        mType = CONTACT;
        // }

        return mType;

    }

    @Override
    public int getItemCount() {

        return reciept.size();
    }

    public Reciept getContact(int position){

        return this.reciept.get(position);
    }


    public static  class InboxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView month;
        public TextView mpesaNumber;
        public TextView amount;
        public TextView timestamp;

        public ItemClickListener clickListener;


        public InboxViewHolder(View view) {
            super(view);
//            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);// this contains the name of sender
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            month = (TextView) itemView.findViewById(R.id.reciept_month);
            mpesaNumber = (TextView) itemView.findViewById(R.id.reciept_mpesa);
            amount = (TextView) itemView.findViewById(R.id.reciept_amount);
            timestamp = (TextView) itemView.findViewById(R.id.reciept_timestamp);

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