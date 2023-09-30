package com.water.eldowas.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.water.eldowas.R;
import com.water.eldowas.model.SearchResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<SearchResults> contactList;
    private List<SearchResults> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView accountNumber, name,  plotNumber, meterNumber, lastReading;
        public Button btnAdd, btnView;


        public MyViewHolder(View view) {
            super(view);
            accountNumber = view.findViewById(R.id.item_search_account_number);
            name = view.findViewById(R.id.item_search_name);
            plotNumber = view.findViewById(R.id.item_search_plot_number);
            meterNumber = view.findViewById(R.id.item_search_meter_number);
           // lastReading = view.findViewById(R.id.item_search_last_reading);
            btnAdd= view.findViewById(R.id.btn_search_add);
            btnView= view.findViewById(R.id.btn_search_bill);
            btnAdd.setOnClickListener(this);
            btnView.setOnClickListener(this);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback


                   // listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == btnAdd.getId()) {
                listener.onAddBtnClicked(contactListFiltered.get(getAdapterPosition()));
            }else if(v.getId() == btnView.getId()){

                listener.onViewBtnClicked(contactListFiltered.get(getAdapterPosition()));
            }
        }
    }


    public SearchAdapter(Context context, List<SearchResults> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_result, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SearchResults contact = contactListFiltered.get(position);
        holder.accountNumber.setText(contact.getAccount());
        holder.name.setText(contact.getName());
        holder.plotNumber.setText(contact.getPlotNumber());
        holder.meterNumber.setText(contact.getMeterNumber());
       // holder.lastReading.setText(contact.getLastReading());



      /*  Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<SearchResults> filteredList = new ArrayList<>();
                    for (SearchResults row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPlotNumber().contains(charSequence)  || row.getMeterNumber().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<SearchResults>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(SearchResults contact);
        void onAddBtnClicked(SearchResults contact);
        void onViewBtnClicked(SearchResults contact);

    }
}
