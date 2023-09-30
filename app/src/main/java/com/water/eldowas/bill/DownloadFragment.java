package com.water.eldowas.bill;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.water.eldowas.R;

public class DownloadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String USER_ID = "USER_ID";
    private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    private static final String PERIOD= "PERIOD";

    // TODO: Rename and change types of parameters
    private String uid;
    private String accountNumber;
    private String period;
    private ProgressBar downloadPb;


    public DownloadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uid Parameter 1.
     * @param accountNumber Parameter 2.
     * @return A new instance of fragment DownloadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownloadFragment newInstance(String uid, String accountNumber, String period) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, uid);
        args.putString(ACCOUNT_NUMBER, accountNumber);
        args.putString(PERIOD, period);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(USER_ID);
            accountNumber = getArguments().getString(ACCOUNT_NUMBER);
            period = getArguments().getString(PERIOD);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);



    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getContext(),PdfReaderActivity.class);
        intent.putExtra(USER_ID, uid);
        intent.putExtra(ACCOUNT_NUMBER, accountNumber);
        startActivity(intent);
    }







}
