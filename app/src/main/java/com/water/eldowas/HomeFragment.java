package com.water.eldowas;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


import com.water.eldowas.model.Contacts;


import com.water.eldowas.model.UserInformation;
import com.water.eldowas.ui.activity.AddAccountActivity;
import com.water.eldowas.ui.activity.SearchActivity;
import com.water.eldowas.ui.adapter.ContactsAdapter;
import com.water.eldowas.util.Item_Model;
import com.water.eldowas.util.RecyclerClick_Listener;
import com.water.eldowas.util.RecyclerTouchListener;
import com.water.eldowas.util.RecyclerView_Adapter;
import com.water.eldowas.util.Toolbar_ActionMode_Callback;


/**
 * A simple {@link Fragment} subclass.
 *
 * to handle interaction events.
 * Use the {@link HomeFragment#createInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String USERS_CHILD = "Vaultusers";
    public static final String USERS_ACCOUNTS = "CustomersAccount";
    private String user_name;
    private String user_profile_pic;
    private  static String uiduserx;
    View v;
    private ContactsAdapter contactsAdapter;
    private static ArrayList<Item_Model> item_models;
    private static RecyclerView_Adapter adapter;
    private List<UserInformation> contacts = new ArrayList<>();

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
    private String firstname;
    private String lastname;
    private static final String TAG = "ContactsFragment";
    private   String recipientCatInfoIndex;
    private String chatInfoIndex;
    private static Contacts contactThis;
    private static Contacts contactrecipient;
    private String proWall;
    private String followkey;
    public String accountNumber;
    private RelativeLayout emptyLayout;
    public ActionMode mActionMode;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     */



    // TODO: Rename and change types and number of parameters
    public static HomeFragment createInstance(int itemsCount, String uidz) {
        HomeFragment fragment = new HomeFragment();
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

        HomeFragment  contactsTabFragment  = new HomeFragment();
        //  Toast.makeText(getContext(),"Number from bundle top :"+ uiduserx , Toast.LENGTH_SHORT).show();
        viewz = inflater.inflate(R.layout.fragment_contacts_tab, container, false);
        emptyLayout = (RelativeLayout)viewz.findViewById(R.id.layout_empty);
        mMessageRecyclerView = (RecyclerView)viewz.findViewById(R.id.recyclerViewContacts);


        return viewz;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //    Toast.makeText(getContext(),"Number from bundle top just :"+ uiduserx , Toast.LENGTH_SHORT).show();
        this.v = view;
        RecyclerView recyclerView = (RecyclerView)viewz.findViewById(R.id.recyclerViewContacts);



//       follow = (Button)viewz.findViewById(R.id.follow_click_button);
//        unfollow = (Button)viewz.findViewById(R.id.following_click_button);

        setupRecyclerView(recyclerView);
        implementRecyclerViewClickListeners();
        contactsAdapter.notifyDataSetChanged();
        mProgressBar = (ProgressBar)viewz.findViewById(R.id.progressBarFra);
        mProgressBar.setVisibility(ProgressBar.GONE);
        FloatingActionButton fabSearch = (FloatingActionButton)viewz.findViewById(R.id.contact_fabButton);
        FloatingActionButton fabAddAccount = (FloatingActionButton)viewz.findViewById(R.id.fab_add_account);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), SearchActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                myIntent.putExtra("USER_NAME", user_name);
                myIntent.putExtra("USER_FIRST_NAME", firstname);
                myIntent.putExtra("USER_LAST_NAME", lastname);

                startActivity(myIntent);

            }
        });

        fabAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(getContext(), AddAccountActivity.class);
                myIntent.putExtra("USER_ID", uiduserx);
                myIntent.putExtra("USER_NAME", user_name);
                myIntent.putExtra("USER_FIRST_NAME", firstname);
                myIntent.putExtra("USER_LAST_NAME", lastname);
                startActivity(myIntent);

            }
        });
//
        //   inits();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {


        FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(uiduserx).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {// mFirebaseDatabaseReference.child(USERS_CHILD).child(uiduserx).child(FOLLOWING)

                user_name = dataSnapshot.child("userName").getValue(String.class);
                uid = dataSnapshot.child("uid").getValue(String.class);
                firstname = dataSnapshot.child("firstName").getValue(String.class);
                lastname = dataSnapshot.child("lastName").getValue(String.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(false);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        contactsAdapter = new ContactsAdapter(mFirebaseDatabaseReference.child(USERS_ACCOUNTS).child(uiduserx), uiduserx, mProgressBar, mMessageRecyclerView, emptyLayout);
        mMessageRecyclerView.setAdapter(contactsAdapter);


        contactsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = contactsAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

    }




        //Implement item click and long click over recycler view
        private void implementRecyclerViewClickListeners() {
        mMessageRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mMessageRecyclerView, new RecyclerClick_Listener() {
                @Override
                public void onClick(View view, int position) {
                    //If ActionMode not null select item
                    if (mActionMode != null)
                        onListItemSelect(position);
                }

                @Override
                public void onLongClick(View view, int position) {
                    //Select item on long click
                    onListItemSelect(position);
                }
            }));
        }

        //List item select method
        private void onListItemSelect(int position) {
            contactsAdapter.toggleSelection(position);//Toggle the selection

            boolean hasCheckedItems = contactsAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


            if (hasCheckedItems && mActionMode == null)
                // there are some selected items, start the actionMode
                mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new Toolbar_ActionMode_Callback(getActivity(),adapter, null, item_models, false, uiduserx));
            else if (!hasCheckedItems && mActionMode != null)
                // there no selected items, finish the actionMode
                mActionMode.finish();

            if (mActionMode != null)
                //set action mode title on item selection
                mActionMode.setTitle(String.valueOf(contactsAdapter
                        .getSelectedCount()) + " selected");


        }
        //Set action mode null after use
        public void setNullToActionMode() {
            if (mActionMode != null){
                mActionMode = null;

            }

        }

        //Delete selected rows
        public void deleteRows() {
            SparseBooleanArray selected = contactsAdapter
                    .getSelectedIds();//Get selected ids

            //Loop all selected ids
            for (int i = (selected.size() - 1); i >= 0; i--) {
                if (selected.valueAt(i)) {
                    //If current id is selected remove the item via key
                    contactsAdapter.contacts.remove(selected.keyAt(i));
                    contactsAdapter.notifyDataSetChanged();//notify adapter

                }
            }
            Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
            mActionMode.finish();//Finish action mode after use

        }





}


