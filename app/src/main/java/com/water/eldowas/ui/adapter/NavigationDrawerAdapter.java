package com.water.eldowas.ui.adapter;

/**
 * Created by sulu on 24/06/2018.
 */


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

import com.water.eldowas.R;
import com.water.eldowas.model.NavDrawerItem;

/**
 * Created by Ravi Tamada on 12-03-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<NavDrawerItem> data = Collections.emptyList();

    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this .context = context;
        inflater = LayoutInflater.from(context);
        this .data = data;
    }
    public void delete( int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    //  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater . from(parent. getContext());


        switch (viewType) {
            case 1:
                View viewONE = inflater.inflate(R . layout. nav_drawer_profile , parent, false );
                viewHolder = new MyViewHolder(viewONE);

                break;

            case 2:
                View viewTWO = inflater.inflate(R . layout. nav_drawer_myads , parent , false );
                viewHolder= new MyViewHolderMyAds(viewTWO);
                break;
            case 3:
                View viewTHREE = inflater.inflate(R . layout. nav_drawer_invite , parent , false );
                viewHolder= new MyViewHolderInvite(viewTHREE);
                break;
            case 4:
                View viewFOUR = inflater.inflate(R . layout. nav_drawer_sign_out , parent , false );
                viewHolder= new MyViewHolderSignOut(viewFOUR);
                break;
            case 5:
                View viewFIVE = inflater.inflate(R . layout. nav_drawer_feedback , parent , false );
                viewHolder= new MyViewHolderMyFeedBack(viewFIVE);
                break;
            default:
                View viewT =inflater.inflate(R . layout. nav_drawer_profile , parent, false );
                viewHolder = new MyViewHolder(viewT);
                break;


        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1){

            final MyViewHolder mHolder = (MyViewHolder) holder;
            NavDrawerItem friendlyMessage1 = data.get(position);
            mHolder.title.setText(friendlyMessage1.getTitle());
            mHolder.title.setVisibility(TextView.VISIBLE);
            //mHolder.leftIcon.setVisibility(ImageView.VISIBLE);

        }else
        if (holder.getItemViewType() == 2){

            final MyViewHolderMyAds nHolder = (MyViewHolderMyAds) holder;
            NavDrawerItem friendlyMessage1 = data.get(position);
            nHolder.title.setText(friendlyMessage1.getTitle());
            nHolder.title.setVisibility(TextView.VISIBLE);
//            nHolder.leftIcon.setVisibility(ImageView.VISIBLE);

        }else
        if (holder.getItemViewType() == 3){

            final MyViewHolderInvite oHolder = (MyViewHolderInvite) holder;
            NavDrawerItem friendlyMessage1 = data.get(position);
            oHolder.title.setText(friendlyMessage1.getTitle());
            oHolder.title.setVisibility(TextView.VISIBLE);
            //oHolder.leftIcon.setVisibility(ImageView.VISIBLE);

        }else
        if (holder.getItemViewType() == 4){

            final MyViewHolderSignOut oHolder = (MyViewHolderSignOut) holder;
            NavDrawerItem friendlyMessage1 = data.get(position);
            oHolder.title.setText(friendlyMessage1.getTitle());
            oHolder.title.setVisibility(TextView.VISIBLE);
         //   oHolder.leftIcon.setVisibility(ImageView.VISIBLE);

        } else
        if (holder.getItemViewType() == 5){

            final MyViewHolderMyFeedBack oHolder = (MyViewHolderMyFeedBack) holder;
            NavDrawerItem friendlyMessage1 = data.get(position);
            oHolder.title.setText(friendlyMessage1.getTitle());
            oHolder.title.setVisibility(TextView.VISIBLE);
           // oHolder.leftIcon.setVisibility(ImageView.VISIBLE);

        }



    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       // ImageView leftIcon;
        TextView title;

        public MyViewHolder(View itemView) {
            super (itemView);
            title = (TextView) itemView.findViewById(R.id.title);
           // leftIcon = (ImageView) itemView.findViewById(R.id.profile_icon);
        }
    }
    public static class MyViewHolderMyAds extends RecyclerView.ViewHolder {
        ImageView leftIcon;
        TextView title;

        public MyViewHolderMyAds(View itemView) {
            super (itemView);
            title = (TextView) itemView.findViewById(R.id.title);
         //   leftIcon = (ImageView) itemView.findViewById(R.id.profile_icon);
        }
    }

    public static class MyViewHolderMyFeedBack extends RecyclerView.ViewHolder {
        ImageView leftIcon;
        TextView title;

        public MyViewHolderMyFeedBack(View itemView) {
            super (itemView);
            title = (TextView) itemView.findViewById(R.id.title);
           // leftIcon = (ImageView) itemView.findViewById(R.id.feed_back_icon);
        }
    }

    public static class MyViewHolderInvite extends RecyclerView.ViewHolder {
        ImageView leftIcon;
        TextView title;

        public MyViewHolderInvite(View itemView) {
            super (itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            //leftIcon = (ImageView) itemView.findViewById(R.id.invite_icon);
        }
    }
    public static class MyViewHolderSignOut extends RecyclerView.ViewHolder {
        ImageView leftIcon;
        TextView title;

        public MyViewHolderSignOut(View itemView) {
            super (itemView);
            title = (TextView) itemView.findViewById(R.id.title);
          //  leftIcon = (ImageView) itemView.findViewById(R.id.sign_out_icon);
        }
    }

    @Override
    public int getItemViewType (int position) {
        if (position == 0) {
            return 1;
        } else if (position == 1) {
            return 2;
        } else if (position == 2) {
            return 3;
        } else if (position == 3) {
            return 4;
        }else if (position == 4) {
            return 5;
        } else {
            return 4;
        }
    }

}