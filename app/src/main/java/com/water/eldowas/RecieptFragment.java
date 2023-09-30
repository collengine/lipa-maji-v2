package com.water.eldowas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


import com.water.eldowas.model.Reciept;
import com.water.eldowas.ui.activity.MainActivity;
import com.water.eldowas.ui.adapter.InboxAdapter;



public class RecieptFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String USERS_CHILD = "Vaultusers";
    private String user_name;
    private String user_profile_pic;
    private  static String uiduserx;
    View v;
    InboxAdapter inboxAdapter;
    private List<Reciept> inbox = new ArrayList<>();

    private View viewz;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";
    // private OnFragmentInteractionListener mListener;
    public static final String UID = "uidkey";
    public static final String CHATS = "chats";
    public static final String CHATS_INFO = "chatsInfo";
    public static final String FOLLOWING = "following";
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference messagereference;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    private int positionStarts;
    private String recipientName ;
    private String recipientUid  ;
    private String lastText ;
    private String lastTextTime  ;
    private String recipientPhotoUrl;//wallpaper
    private String wallpaper;
    private String chatIndex;
    private String recipientchatIndex;
    private String recipientUids;
    private static  String recipientUidW;
    private static final String TAG = "IbboxTab";
    private  String useuid;
    public    Intent myIntent;
    private RelativeLayout myRelativeLayout;

    public RecieptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     */



    // TODO: Rename and change types and number of parameters
    public static RecieptFragment createInstance(int itemsCount, String uidz) {
        RecieptFragment fragment = new RecieptFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        bundle.putString(UID , uidz);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            uiduserx = this.getArguments().getString(UID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        uiduserx = this.getArguments().getString(UID);



        //  Toast.makeText(getContext(),"Number from bundle top :"+ uiduserx , Toast.LENGTH_SHORT).show();
        viewz = inflater.inflate(R.layout.fragment_reciept, container, false);


        return viewz;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //    Toast.makeText(getContext(),"Number from bundle top just :"+ uiduserx , Toast.LENGTH_SHORT).show();
        this.v = view;
        RecyclerView recyclerView = (RecyclerView)viewz.findViewById(R.id.recyclerViewInbox);


        setupRecyclerView(recyclerView);
//
        //   inits();

        loadData();


    }

    private void loadData() {

        // mFirebaseDatabaseReference.child(USERS_CHILD).child(uiduserx).child(CHATS_INFO).child(key).setValue(contactrecipient);


        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(CHATS_INFO);
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // THIS SHOULD WORK I WILL BE RIGHT HERE

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inbox.clear();
                for(DataSnapshot single : dataSnapshot.getChildren()){


                    inbox.add(single.getValue(Reciept.class));

                }
                //         Toast.makeText(getContext(),"Number from bundle :"+ uiduserx + user_profile_pic, Toast.LENGTH_SHORT).show();
                inboxAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
    private void setupRecyclerView(RecyclerView recyclerView) {

        //   Bundle bundle = getArguments();
        //  uiduser1 = mainActivity.getUidNumber();

        //  uiduser = loginActivity.getUid();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        //    databaseReference.child(USERS_CHILD).child(userUID).setValue(userInformation);

        //      Toast.makeText(getContext(),"Number from bundle the main check bundle function------2 :"+ uiduserx, Toast.LENGTH_SHORT).show();


        mFirebaseDatabaseReference =  FirebaseDatabase.getInstance().getReference();
    //    mFirebaseDatabaseReference =  FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(uiduserx).child(CHATS_INFO);
        //      mProgressBar = (ProgressBar)v. findViewById(R.id.progressBarInbox);
        mMessageRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerViewInbox);
        myRelativeLayout = (RelativeLayout)v.findViewById(R.id.layout_empty);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inboxAdapter = new InboxAdapter(mFirebaseDatabaseReference, uiduserx,mMessageRecyclerView , myRelativeLayout);
        mMessageRecyclerView.setAdapter(inboxAdapter);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);

        mMessageRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecieptFragment.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                view.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        inboxAdapter.notifyDataSetChanged();

                        Reciept contact = inboxAdapter.getContact(position);
                        recipientUidW = contact.getMpesaRecieptNumber();
                        Intent myIntent = new Intent(getContext(),MainActivity.class);

                        myIntent.putExtra("USER_ID", uiduserx);

                        myIntent.putExtra("KEY", recipientUidW);





//                        messagereference = FirebaseDatabase.getInstance().getReference().child(USERS_CHILD);


//                        startActivity(myIntent);

                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add("Item " + i);
            }
        }
        return itemList;
    }



    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private RecieptFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final RecieptFragment.ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}


