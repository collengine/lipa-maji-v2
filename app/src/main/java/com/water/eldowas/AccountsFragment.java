package com.water.eldowas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


import com.google.firebase.database.DatabaseReference;
import com.water.eldowas.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 *
 * to handle interaction events.
 * Use the {@link AccountsFragment#createInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String USERS_CHILD = "Vaultusers";
    private String user_name;
    private String user_profile_pic;
    private  static String uiduserx;
    View v;


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
    public static final String FOLLOWED = "followed";
    private DatabaseReference mFirebaseDatabaseReference;
    private DatabaseReference messagereference;
    private DatabaseReference followmessagereference;
    private ProgressBar mProgressBar;
    private SharedPreferences mSharedPreferences;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    //    private FirebaseRecyclerAdapter<Contacts, collengine.com.ojblack.adapter.ContactsAdapter.ContactViewHolder>
//            mFirebaseAdapter;
    private int positionStarts;
    private String recipientName ;
    private static String recipientUid  ;
    private String bio;
    private String state;
    private String recipientPhotoUrl;
    private String chatIndex;
    private String recipientchatIndex;
    private String wallpaper_url;
    private String useruid;
    private String uid;
    private static final String TAG = "ContactsFragment";
    private   String recipientCatInfoIndex;
    private String chatInfoIndex;
    private String proWall;
    private String followkey;
    public String recipientUidW;
    private RelativeLayout emptyLayout;



    public AccountsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     */



    // TODO: Rename and change types and number of parameters
    public static AccountsFragment createInstance(int itemsCount, String uidz) {
        AccountsFragment fragment = new AccountsFragment();
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

        AccountsFragment  contactsTabFragment  = new AccountsFragment();
        //  Toast.makeText(getContext(),"Number from bundle top :"+ uiduserx , Toast.LENGTH_SHORT).show();
        viewz = inflater.inflate(R.layout.button, container, false);



        return viewz;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //    Toast.makeText(getContext(),"Number from bundle top just :"+ uiduserx , Toast.LENGTH_SHORT).show();
        this.v = view;
        RecyclerView recyclerView = (RecyclerView)viewz.findViewById(R.id.recyclerViewAccounts);



//       follow = (Button)viewz.findViewById(R.id.follow_click_button);
//        unfollow = (Button)viewz.findViewById(R.id.following_click_button);

        setupRecyclerView(recyclerView);
        FloatingActionButton searchFab = (FloatingActionButton)viewz.findViewById(R.id.floatingActionButton_add_account);

        FloatingActionButton addAccountFab = (FloatingActionButton)viewz.findViewById(R.id.floatingActionButton_add_account);
        addAccountFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                startActivity(myIntent);

            }
        });

        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                startActivity(myIntent);

            }
        });
    }




    private void setupRecyclerView(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(mLinearLayoutManager);

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
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private AccountsFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final AccountsFragment.ClickListener clicklistener){

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


