package com.water.eldowas.ui;

/**
 * Created by sulu on 24/06/2018.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import
        androidx.appcompat.app.ActionBarDrawerToggle;
import
        androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.water.eldowas.R;
import com.water.eldowas.model.UserInformation;
import com.water.eldowas.ui.adapter.NavigationDrawerAdapter;
import com.water.eldowas.model.NavDrawerItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDrawer extends Fragment {
    public static final String USERS_CHILD = "Vaultusers";
    public static final String UID = "useruid";
    //   private  static String uiduserx;
    private  static String proppic;
    private static String TAG = FragmentDrawer.class .getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private DrawerLayout drawerLayout;
    private View containerView;
    private CircleImageView user_pic;
    private TextView userFirstName;
    private TextView userLastName;
    private TextView phoneNumber;
    private static String[] titles = null ;
    private FragmentDrawerListener drawerListener;

    private String UIDCONSTANT = "uid";
    public static final String idPreference = "idPrefs";
    private SharedPreferences idSharedpreferences;
    private String USERPIC = "userpic";
    private String USERWALL = "userpic";
    private String uiduserx;


    public FragmentDrawer() {
    }
    public void setDrawerListener(FragmentDrawerListener listener) {
        this .drawerListener = listener;
    }
    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
// preparing navigation drawer items
        for ( int i = 0 ; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);
// drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        // uiduserx = this.getArguments().getString(UID);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        mDrawerLayout = (DrawerLayout)layout.findViewById(R.id.fragment_navigation_drawer) ;//
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager( new
                LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener( new
                RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        user_pic = (CircleImageView)layout.findViewById(R.id.profilePicPartThree_act);
        userFirstName = (TextView)layout.findViewById(R.id.pay_profile_firstname);
        userLastName = (TextView)layout.findViewById(R.id.pay_profile_lasttname);
        phoneNumber = (TextView)layout.findViewById(R.id.pay_profile_user_name);




        return layout;
    }
    public void setUp( int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar, String uid) {
        uiduserx = uid;

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super .onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super .onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
            @Override
            public void onDrawerSlide(View drawerView, float
                    slideOffset) {
                super .onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha( 1 - slideOffset / 2 );
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post( new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
//FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(userUID)
        DatabaseReference messagereference = FirebaseDatabase.getInstance().getReference().child(USERS_CHILD).child(uiduserx);


        messagereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInformation userInformation = dataSnapshot.getValue(UserInformation.class);

                if(userInformation != null){
                    String picurl = userInformation.getPhotoUrl();
                    String username = userInformation.getUserName();
                    String firstname = userInformation.getFirstName();
                    String lastname = userInformation.getLastName();
                    userFirstName.setText(firstname);
                    userLastName.setText(lastname );
                    phoneNumber.setText(username);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }
    static class RecyclerTouchListener implements
            RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context,
                                     final RecyclerView recyclerView, final
                                     ClickListener clickListener) {
            this .clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }
                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
        @Override
        public void
        onRequestDisallowInterceptTouchEvent( boolean
                                                      disallowIntercept) {
        }
    }
    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int
                position);
    }
}