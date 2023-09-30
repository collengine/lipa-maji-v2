package com.water.eldowas.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.view.ActionMode;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.water.eldowas.ListView_Fragment;
import com.water.eldowas.R;
import com.water.eldowas.RecyclerView_Fragment;
import com.water.eldowas.ui.activity.EditAccountActivity;
import com.water.eldowas.ui.activity.MainActivity;

import java.util.ArrayList;

import static com.water.eldowas.service.MyFirebaseMessagingService.NOTIFICATION;

/**
 * Created by SONU on 22/03/16.
 */
public class Toolbar_ActionMode_Callback implements ActionMode.Callback {

    private final DatabaseReference databaseReference;
    private Context context;
    private RecyclerView_Adapter recyclerView_adapter;
    private ListView_Adapter listView_adapter;
    private ArrayList<Item_Model> message_models;
    private boolean isListViewFragment;
    private String uid;
    private String TAG = "ToolActionMode";
    private String indexs;


    public Toolbar_ActionMode_Callback(Context context, RecyclerView_Adapter recyclerView_adapter, ListView_Adapter listView_adapter, ArrayList<Item_Model> message_models, boolean isListViewFragment, String uid) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.listView_adapter = listView_adapter;
        this.message_models = message_models;
        this.isListViewFragment = isListViewFragment;
        this.uid = uid;
        this.databaseReference = FirebaseDatabase.getInstance().getReference ();
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_main, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {



        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_edit), MenuItemCompat.SHOW_AS_ACTION_NEVER);
//            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_forward), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else if(!(Build.VERSION.SDK_INT < 11)  && isListViewFragment == false) {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if(!(Build.VERSION.SDK_INT < 11)  && isListViewFragment == true) {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
//            menu.findItem(R.id.action_forward).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        String index = "" ;
        switch (item.getItemId()) {
            case R.id.action_delete:





                //Get selected ids on basis of current fragment action mode
                SparseBooleanArray selected;
                if (isListViewFragment)
                    selected = listView_adapter
                            .getSelectedIds();
                else
                    selected = recyclerView_adapter
                            .getSelectedIds();

                int selectedMessageSize = selected.size();

                //Loop to all selected items
                for (int i = (selectedMessageSize - 1); i >= 0; i--) {
                    if (selected.valueAt(i)) {
                        //get selected data in Model
                        Item_Model model = message_models.get(selected.keyAt(i));
                        String title = model.getTitle();
                        String subTitle = model.getSubTitle();
                        index = model.getDbIndex();
                        indexs = model.getNotificationTimestamp();


                        //Print the data to show if its working properly or not
                        if(index!= null  || indexs!= null){

                            if(isListViewFragment){

                                String indeces = model.getNotificationTimestamp();
                                //databaseReference.child(NOTIFICATION).child(userid)
                                FirebaseDatabase.getInstance().getReference ().child(NOTIFICATION).child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e(TAG, dataSnapshot.toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference ().child(NOTIFICATION).child(uid).child(indeces).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }else if(!isListViewFragment){

                                index = model.getDbIndex();
                                FirebaseDatabase.getInstance().getReference ().child("CustomersAccount").child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Log.e(TAG, dataSnapshot.toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference ().child("CustomersAccount").child(uid).child(index).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                            }


                        }



                    }
                }


                //  Toast.makeText(context, "You selected Copy menu.", Toast.LENGTH_SHORT).show();//Show toast
                mode.finish();//Finish action mode



                break;
            case R.id.action_edit:

                String indexs = "" ;
                //Get selected ids on basis of current fragment action mode
                SparseBooleanArray selecteds;
                if (isListViewFragment)
                    selecteds = listView_adapter
                            .getSelectedIds();
                else
                    selecteds = recyclerView_adapter
                            .getSelectedIds();

                int selectedMessageSizes = selecteds.size();

                //Loop to all selected items
                for (int i = (selectedMessageSizes - 1); i >= 0; i--) {
                    if (selecteds.valueAt(i)) {
                        //get selected data in Model
                        Item_Model model = message_models.get(selecteds.keyAt(i));
                        String title = model.getTitle();
                        String subTitle = model.getSubTitle();
                        index = model.getIndex();
                        //Print the data to show if its working properly or not


                    }
                }

                if(selectedMessageSizes == 1) {
                    Intent myIntent = new Intent(context, EditAccountActivity.class);

                    myIntent.putExtra("KEY", indexs);
                    myIntent.putExtra("USER_ID", uid);
                    myIntent.putExtra("USER_NAME", indexs);
                    myIntent.putExtra("USER_FIRST_NAME", uid);
                    myIntent.putExtra("USER_LAST_NAME", indexs);
                    myIntent.putExtra("USER_NAME", uid);
                    myIntent.putExtra("ACCOUNT_NUMBER", uid);




                    context.startActivity(myIntent);
//                    Log.e("Selected Items", "ive sent intent");
                }else {
                    Toast.makeText(context.getApplicationContext(),"Cannot Edit this Post :", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(context, "You selected Copy menu.", Toast.LENGTH_SHORT).show();//Show toast
                mode.finish();//Finish action mode
                break;
//            case R.id.action_forward:
//                Toast.makeText(context, "You selected Forward menu.", Toast.LENGTH_SHORT).show();//Show toast
//                mode.finish();//Finish action mode
//                break;


        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
        //First check current fragment action mode
        if (isListViewFragment) {
            listView_adapter.removeSelection();  // remove selection
            Fragment listFragment = new MainActivity().getFragment(1);//Get list fragment
            if (listFragment != null)
                ((ListView_Fragment) listFragment).setNullToActionMode();//Set action mode null
        } else {
            recyclerView_adapter.removeSelection();  // remove selection
            Fragment recyclerFragment = new MainActivity().getFragment(0);//Get recycler fragment
            if (recyclerFragment != null)
                ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();//Set action mode null
        }
    }
}
