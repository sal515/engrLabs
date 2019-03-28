package ca.engrLabs_390.engrlabs;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mancj.materialsearchbar.MaterialSearchBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.engrLabs_390.engrlabs.database_files.LabDataModel;
import ca.engrLabs_390.engrlabs.recyclerView.dataAdapter_recyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

public class ExpandableRecyclerWithBottomNav extends AppCompatActivity {

    private static final String TAG = "RecyclerViewActivity";

    private FirebaseDatabase database;
    private DatabaseReference databaseRootRef;
    private DatabaseReference databaseDynamicDataRef;
    private ValueEventListener labDetailsListenerVar;

    // Testing TextBox for Bottom Navigation Bar
    private TextView mTextSelectionTextBox;

    // Bottom Navigation Bar variable
    BottomNavigationView navigation;

    // FIXME: Related to favourite buttom commenting out for now; until I go through the logic
    // favourite checking variable
    //    boolean favouritesOnly = false;

    // Recycler Adapter variable declaration
    dataAdapter_recyclerView recyclerViewAdapter;

    // =========  Search bar stuff   ==========
    //Suggestion list for search bar
    //List<String> suggestList = new ArrayList<>();

    //Search Card - Search Bar for the softwares
    MaterialSearchBar materialSearchBar;
    static String text = "";
    CardView searchCard;
    ImageView sortButton;

    // =========  Search bar stuff   ==========

    // ========= Firebase variables =================

    FirebaseFirestore db;
    // variables
    HashMap<String, Object> document;
    HashMap labsDynamicDataMap;
    List<String> labKeys;
    // FIXME: Temp listArray to add IEEE to the recyclerView
    List<LabDataModel> tempLabObjects;
    List<LabDataModel> labObjects;
    HashMap<String, Object> labs;

    // ========= Firebase variables =================


    // DummyClassData
//    List<LabDataModel> dummyClassList = new ArrayList<>();
//    int floorMode;
//    List<LabDataModel> filteredDummyList = new ArrayList<>();
//    String filterSelection;

    // Why and How recyclerView: https://guides.codepath.com/android/using-the-recyclerview
    // Using a RecyclerView has the following key steps:
    // 1. Add RecyclerView support library to the gradle build file
    // 2. Define a model class to use as the data source
    // 3. Add a RecyclerView to your activity to display the items
    // 4. Create a custom row layout XML file to visualize the item
    // 5. Create a RecyclerView.Adapter and ViewHolder to render the item
    // 6. Bind the adapter to the data source to populate the RecyclerView

    // RecyclerView Reference variable
    RecyclerView recyclerViewVar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_recycler_with_bottom_nav);

        // Calling the initial setup functions -> "ORDER of CALL MATTERS"
        initializeAllReferences();

        // FIXME: where should the search bar logic be called ??
        searchBarLogic();

        setListeneres();


        // Creating reference of databaseRootRef - to write to db
//        FirebaseDatabase databaseRootRef = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = databaseRootRef.getReference("/");


        databaseRootRef = FirebaseDatabase.getInstance().getReference();


//        myRef.setValue("Hello, World!");


//        setListeneres();


        // calling the recycler binding function -- !!Should be called only once!!
        bindingAdapterToRecycleViewer();


    }

    @Override
    protected void onStart() {
        super.onStart();
//        floorMode = 0;
//        filterSelection = "";
//        navigation.setSelectedItemId(R.id.navigation_home);
        //FIXME: ???????? Avoid called this whole recycler initializer function  ????????
//        bindingAdapterToRecycleViewer();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener labDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                // extract the snapshot as an object
                Object labObj = dataSnapshot.getValue();


                // Check if the object is of type HashMap, if it is cast it to HashMap
                if (labObj instanceof HashMap) {
                    labsDynamicDataMap = new HashMap((HashMap)labObj);
                    labKeys = new ArrayList<String>(labsDynamicDataMap.keySet());
                }

                String databaseNumberofStudents;
                for (int j = 0; j < labKeys.size(); j++) {

//                    tempLabObjects = new ArrayList<LabDataModel>();
                    LabDataModel tempLabObj = new LabDataModel();
                    tempLabObj.setRoomStr(labKeys.get(j));
                    databaseNumberofStudents = (String) ((HashMap) labsDynamicDataMap.get("B204")).get("NumberOfStudentsPresent");
                    tempLabObj.setNumberOfStudentsPresent(databaseNumberofStudents);
                    tempLabObjects.add(tempLabObj);

//                    tempLabObjects.get(new LabDataModel().setRoom(labKeys.get(j)));
                }

                recyclerViewAdapter.swapItems(tempLabObjects);


//                HashMap labHashMap = (HashMap) labObj;
//                labHashMap.get("H41");

                // [START_EXCLUDE]
                String i = "Work>";

//                mAuthorView.setText(post.author);
//                mTitleView.setText(post.title);
//                mBodyView.setText(post.body);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
//                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
//                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        databaseDynamicDataRef.addValueEventListener(labDetailsListener);
        labDetailsListenerVar = labDetailsListener;

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (labDetailsListenerVar != null) {
            databaseRootRef.removeEventListener(labDetailsListenerVar);
        }
    }

    private void initializeAllReferences() {

        // initialize databaseRootRef variable
//        db = FirebaseFirestore.getInstance();

        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        databaseDynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");


        // initializing the arrayList of LabDataModel objects extracted from the databaseRootRef
        labObjects = new ArrayList<LabDataModel>();
        tempLabObjects = new ArrayList<LabDataModel>();
        labsDynamicDataMap = new HashMap();


        mTextSelectionTextBox = (TextView) findViewById(R.id.message);

        // Initializing the bottom nav bar reference
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);

        // Initialize RecyclerView variable
        recyclerViewVar = findViewById(R.id.expandingRecyclerView);

        //Dummy Class List
//        floorMode = 0;
//        filterSelection = "";
//        dummyClassList.add(new LabDataModel(8,21,25,10,30,40));
//        dummyClassList.add(new LabDataModel(8,23,22,12,30,60));
//        dummyClassList.add(new LabDataModel(8,47,24,9,30,20));
//        dummyClassList.add(new LabDataModel(9,13,21,5,30,10));
//        dummyClassList.add(new LabDataModel(9,17,28,16,30,30));
//        dummyClassList.add(new LabDataModel(9,21,24,24,30,35));
//        dummyClassList.add(new LabDataModel(10,52,22,18,30,25));
//        dummyClassList.add(new LabDataModel(10,16,26,14,30,45));

        //Initialize Search Card and Suggestion List

        sortButton = findViewById(R.id.sortImage);
        searchCard = findViewById(R.id.searchCard);
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);

    }

    private void searchBarLogic() {
        materialSearchBar.setHint("Enter your software or 'all'");
        searchCard.setVisibility(View.GONE);
//        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //When user type their text, the suggestion list will update
//                materialSearchBar.updateLastSuggestions(suggestList);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setListeneres() {
        // setting the databaseRootRef document Event listener
//        final DocumentReference docRef = db.collection("PUBLIC_DATA").document("Labs");
//        docRef.addSnapshotListener(LabsDocumentEventListener);

        // Setting the bottom nav bar onItemSelection Listener
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        sortButton.setOnClickListener(sortButtonListener);
        materialSearchBar.setOnSearchActionListener(materialOnSearchListener);

    }

//            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {

    // Definition of the document listener
    private EventListener<DocumentSnapshot> LabsDocumentEventListener = new EventListener<DocumentSnapshot>() {


        @Override
        public void onEvent(@Nullable DocumentSnapshot snapshot,
                            @Nullable FirebaseFirestoreException e) {

            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (snapshot != null && snapshot.exists()) {
                // Log.d(TAG, "Current data: " + snapshot.getData());

                // List<String> labKeys = new ArrayList<String>(snapshot.getData().keySet());

                // getting the whole document as a hash map
                document = new HashMap<String, Object>(snapshot.getData());
                // getting all the keys (Building + RoomNumber) for all the labs into an arrayList
                labKeys = new ArrayList<String>(document.keySet());

                // empty LabDataModel object to be used in for loop
                LabDataModel labObj;
                String labkey;
                Object tempObj;
                int IntTempData;
                String StringTempData;
                String lookForKeyLvL0;
                String lookForKeyLvL1;
                // looping through every data in the labKeys arraylist
                for (int i = 0; i < labKeys.size(); i++) {
                    labkey = labKeys.get(i);
                    labObj = new LabDataModel();

                    // Test print out all the keys in the array list of keys - Passed
                    // Log.d(TAG, i + ": " + labKeys.get(i));

                    // getting the key = DynamicData of type hashmap in the lab hashmap
                    if (document.get(labkey) instanceof HashMap) {

                        // getting Building code from the databaseRootRef
                        // TODO: get Building code

                        // getting Room Number from the databaseRootRef
                        lookForKeyLvL0 = "DynamicData";
                        lookForKeyLvL1 = "Room";
                        if (((HashMap) document.get(labkey)).get(lookForKeyLvL0) instanceof HashMap) {
                            // HashMap tempObj = ((HashMap) ((HashMap) document.get(labkey)).get("DynamicData")).get("Room").getClass().getSimpleName();
                            tempObj = ((HashMap) ((HashMap) document.get(labkey)).get(lookForKeyLvL0)).get(lookForKeyLvL1);

                            // checking output is expected: passed
                            // Log.d(TAG, "Room-> " + ((HashMap) ((HashMap) document.get(labkey)).get("DynamicData")).get("Room"));
                            // Checking what is the type of the fields in dynamic data: It is String
                            // Log.d(TAG, "Room-> " + ((HashMap) ((HashMap) document.get(labkey)).get("DynamicData")).get("Room").getClass().getSimpleName());

                            IntTempData = 0;
                            try {
                                IntTempData = Integer.parseInt(String.valueOf(tempObj));
                            } catch (NumberFormatException e1) {
                                e1.printStackTrace();
                            }
                            labObj.setRoom(IntTempData);
                            // Log.d(TAG, lookForKeyLvL1 + "->> " + IntTempData);
                        }

                        // getting Room Number as string  from the databaseRootRef
                        lookForKeyLvL0 = "DynamicData";
                        lookForKeyLvL1 = "Room";
                        if (((HashMap) document.get(labkey)).get(lookForKeyLvL0) instanceof HashMap) {
                            tempObj = ((HashMap) ((HashMap) document.get(labkey)).get(lookForKeyLvL0)).get(lookForKeyLvL1);
                            StringTempData = "";
                            try {

                                StringTempData = String.valueOf(tempObj);
                            } catch (NumberFormatException e1) {
                                e1.printStackTrace();
                            }
                            labObj.setRoomStr(StringTempData);
                            // Log.d(TAG, lookForKeyLvL1 + "->> " + IntTempData);
                        }

                        // getting Temperature from the databaseRootRef
                        lookForKeyLvL0 = "DynamicData";
                        lookForKeyLvL1 = "Temperature";
                        if (((HashMap) document.get(labkey)).get(lookForKeyLvL0) instanceof HashMap) {
                            tempObj = ((HashMap) ((HashMap) document.get(labkey)).get(lookForKeyLvL0)).get(lookForKeyLvL1);
                            StringTempData = "";
                            try {
//                                IntTempData = Integer.parseInt(String.valueOf(tempObj));
                                StringTempData = String.valueOf(tempObj);
                            } catch (NumberFormatException e1) {
                                e1.printStackTrace();
                            }
                            labObj.setTemperature(StringTempData);
                            //  Log.d(TAG, lookForKeyLvL1 + "->> " + labObj.getTemperature());
                        }

                        // getting NumberOfStudents from the databaseRootRef
                        lookForKeyLvL0 = "DynamicData";
                        lookForKeyLvL1 = "NumberOfStudentsPresent";
                        if (((HashMap) document.get(labkey)).get(lookForKeyLvL0) instanceof HashMap) {
                            // HashMap tempObj = ((HashMap) ((HashMap) document.get(labkey)).get("DynamicData")).get("Room").getClass().getSimpleName();
                            tempObj = ((HashMap) ((HashMap) document.get(labkey)).get(lookForKeyLvL0)).get(lookForKeyLvL1);

                            StringTempData = "";
                            try {
                                StringTempData = String.valueOf(tempObj);
                            } catch (NumberFormatException e1) {
                                e1.printStackTrace();
                            }
                            labObj.setNumberOfStudentsPresent(StringTempData);
                            // Log.d(TAG, lookForKeyLvL1 + "->> " + labObj.getNumberOfStudentsPresent());
                        }

                        // TODO : interface room capacity
                        // getting RoomCapacity from the databaseRootRef
                        labObj.setTotalCapacity(30);

                        // TODO : interface upcoming class
                        // getting UpcomingClass from the databaseRootRef - map
                    }
                    // saving all the lab objects to the tempLabObject Array
                    tempLabObjects.add(labObj);
//                    recyclerViewAdapter.notifyItemChanged(i);
//                    recyclerViewVar.getLayoutManager().onItemsChanged(recyclerViewVar);
                    Log.d(TAG, "Lab rooms: " + "->> " + labObj.getRoom());
                }
                // testing if the objects are there
                // for (int labObjPos = 0; labObjPos < tempLabObjects.size(); labObjPos++) {
                //  Log.d(TAG, "Lab rooms: " + "->> " + tempLabObjects.get(labObjPos).getRoom());
                // }

//                for (int labObjPos = 0; labObjPos < tempLabObjects.size(); labObjPos++) {
//                    Log.d(TAG, "Lab rooms: " + "->> " + tempLabObjects.get(labObjPos).getRoomStr());
//                }


//                bindingAdapterToRecycleViewer();
//                recyclerViewAdapter.addMoreItemsToRecyclerView(tempLabObjects);


//                recyclerViewAdapter.swapItems(tempLabObjects);
//                bindingAdapterToRecycleViewer();
            } else {
                Log.d(TAG, "Current data: null");
            }

//            recyclerViewAdapter.submitList(tempLabObjects);
//            recyclerViewAdapter.notifyDataSetChanged();
//            recyclerViewVar.notifyAll();
        }
    };

    private MaterialSearchBar.OnSearchActionListener materialOnSearchListener
            = new MaterialSearchBar.OnSearchActionListener() {
        @Override
        public void onSearchStateChanged(boolean enabled) {

            //When search bar is closed
            //Restore original adapter
            if (!enabled) {
                //materialSearchBar.setText("");
                sortButton.setVisibility(View.VISIBLE);
                text = materialSearchBar.getText();
                //materialSearchBar.setText("");
                //filterSelection = "";
                //FIXME: Avoid called this whole recycler initializer function
                //bindingAdapterToRecycleViewer();
            } else {
                sortButton.setVisibility(View.GONE);
                materialSearchBar.setText(text);
                //materialSearchBar.setText("");
            }
        }

        @Override
        public void onSearchConfirmed(CharSequence text) {
//            filterSelection = materialSearchBar.getText();
            //FIXME: Avoid called this whole recycler initializer function
                bindingAdapterToRecycleViewer();
        }

        @Override
        public void onButtonClicked(int buttonCode) {
            //***************doesn't work, don't know why
                /*
                if (buttonCode == BUTTON_BACK){
                    materialSearchBar.setText(null);
                    filterSelection = null;
                    //FIXME: Avoid called this whole recycler initializer function
                    bindingAdapterToRecycleViewer();
                }
                */
        }
    };

    private ImageView.OnClickListener sortButtonListener
            = new View.OnClickListener() {
        public void onClick(View v) {
            LabSortFragment dialog = new LabSortFragment();
            dialog.show(getSupportFragmentManager(), "Insert Course");
            // your code here
        }
    };


    // Bottom Navigation bar OnItemSelectionListener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // FIXME: Again not sure about the logic of the favourites option
            // favouritesOnly = false;
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextSelectionTextBox.setText(R.string.title_home);
//                    recyclerViewAdapter.notifyDataSetChanged();
//                    floorMode = 0;
                    //FIXME: Avoid called this whole recycler initializer function
//                    bindingAdapterToRecycleViewer();
                    return true;
                case R.id.navigation_favourites:
//                    mTextSelectionTextBox.setText(R.string.title_favourites);
//                    favouritesOnly = true;
                    //FIXME: Avoid called this whole recycler initializer function
//                    bindingAdapterToRecycleViewer();
                    //Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    //startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
//                    if (searchCard.getVisibility() == View.VISIBLE) {
//                        searchCard.setVisibility(View.GONE);
//                    } else {
//                        searchCard.setVisibility(View.VISIBLE);
//                    }
                    return false;
                case R.id.navigation_floor_8:
                    LabDataModel generatorObj = new LabDataModel();
                    recyclerViewAdapter.swapItems(generatorObj.generateLabs(10));

//                    mTextSelectionTextBox.setText(R.string.Floor_8);
//                    floorMode = 8;
                    //FIXME: Avoid called this whole recycler initializer function
//                    bindingAdapterToRecycleViewer();
                    return true;
                case R.id.navigation_floor_9:
//                    mTextSelectionTextBox.setText(R.string.Floor_9);
//                    floorMode = 9;
                    //FIXME: Avoid called this whole recycler initializer function
//                    bindingAdapterToRecycleViewer();
                    return true;
            }
            return false;
        }
    };


    private void bindingAdapterToRecycleViewer() {
//        List<LabDataModel> data;

//        Queue<Integer> openedQueue = new LinkedList<>();

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


//        filteredDummyList.clear();
//        for(int i = 0;i<dummyClassList.size();i++){
//            if ((dummyClassList.get(i).floor == floorMode) || floorMode == 0)
//            {
//                filteredDummyList.add(dummyClassList.get(i));
//            }
//        }

//        List<LabDataModel> searchFilteredList;
//        searchFilteredList = filterClasses(filteredDummyList);


//      FIXME: Set the data to the data arraylist - to pass to the array adapter
        // set the data here
//        data = tempLabObjects;

//        LabDataModel generatorObj = new LabDataModel();
//        recyclerViewAdapter = new dataAdapter_recyclerView(generatorObj.generateLabs(2));

        recyclerViewAdapter = new dataAdapter_recyclerView(tempLabObjects);


//        recyclerViewAdapter = new dataAdapter_recyclerView(tempLabObjects);

        // set the custom adapter to the recycler view with the data model passed in
        recyclerViewVar.setAdapter(recyclerViewAdapter);


        // set the layout manager position the data according to the xml
        recyclerViewVar.setLayoutManager(new LinearLayoutManager(this));

//        recyclerViewVar.getLayoutManager().notify();

        // passing the adapter with the array list of data
//        recyclerViewAdapter.submitList(tempLabObjects);
//        recyclerViewAdapter.notifyDataSetChanged();


        //FIXME: FIX animator for nice visualization
        // Need to work on the animators -- not working currently
//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        recyclerViewVar.setItemAnimator(itemAnimator);

    }

    // FIXME: Not sure if we will need it
//    private List<LabDataModel> filterClasses(List<LabDataModel> input){
//
//        List<LabDataModel> returnClassList = new ArrayList<>();
//
//        for(int i=0;i<input.size();i++){
//            if (favouritesOnly == true){
//                if (input.get(i).favourite == true){
//                        returnClassList.add(input.get(i));
//                }
//            }
//            else{
//                returnClassList.add(input.get(i));
//            }
//        }
//        return returnClassList;
//    }

}
