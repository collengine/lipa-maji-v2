package com.water.eldowas.util;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.water.eldowas.R;

/**
 * Created by SONU on 27/03/16.
 */
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