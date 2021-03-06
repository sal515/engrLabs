package ca.engrLabs_390.engrlabs.dataModels;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SIngleton2ShareData extends Application {

    private static final String TAG = "SingleTon";


    private static List<LabDataModel> labDynamicDataObjects;
    private static List<String> softwareList;
    private static HashMap softwareMap;
    private static HashMap labsMap;

    private static HashMap currentSemesterCoursesMap;


    // references to the database
    private static FirebaseDatabase database;
    private static DatabaseReference databaseRootRef;
    private static DatabaseReference dynamicDataRef;
    private static DatabaseReference softwaresRef;
    private static DatabaseReference LabsRef;
    private static DatabaseReference currentSemesterCoursesRef;
    private static DatabaseReference currentSemesterLabsRef;
    // valueEvent listener
    private static ValueEventListener labDetailsListenerVar;


    public static void downloadDynamicDataForRecyclerStartUp() {
        // databaseRootRef = FirebaseDatabase.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        databaseRootRef = database.getReference();
        dynamicDataRef = databaseRootRef.child("/PUBLIC_DATA/DynamicData");
        softwaresRef = databaseRootRef.child("/PUBLIC_DATA/Softwares");
        LabsRef = databaseRootRef.child("/PUBLIC_DATA/Labs");


        currentSemesterCoursesRef = databaseRootRef.child("/PUBLIC_DATA/CurrentSemesterCourses");


        labDynamicDataObjects = new ArrayList<LabDataModel>();

//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        ValueEventListener labDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // extract the snapshot as an object
                Object labObj = dataSnapshot.getValue();

                HashMap dynamicDataMap;
                List<String> dynamicDatakeysList;
                dynamicDataMap = new HashMap();
                dynamicDatakeysList = new ArrayList<String>();


                // Check if the object is of type HashMap, if it is cast it to HashMap
                if (labObj instanceof HashMap) {
                    dynamicDataMap = new HashMap((HashMap) labObj);
                    dynamicDatakeysList = new ArrayList<String>(dynamicDataMap.keySet());

                }

                // Variable declarations for the RecyclerVeiw Rows
                int floor;
                int Room;
                String RoomCode;
                String Temperature;
                String NumberOfStudentsPresent;
                String TotalCapacity;
                String AvailableSpots;
                String LabAvailable;
                String BuildingCode;
                String LocationCode;
                HashMap<String, String> upcomingClass = new HashMap<String, String>();

                for (int j = 0; j < dynamicDatakeysList.size(); j++) {
                    // tempDynamicDataList = new ArrayList<LabDataModel>();
                    LabDataModel tempDynamicDataObj = new LabDataModel();

                    // AvailableSpots
                    AvailableSpots = ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("AvailableSpots").toString();
                    BuildingCode = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("BuildingCode");
                    LabAvailable = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("LabAvailable");
                    LocationCode = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("LocationCode");
                    try {
                        Room = Integer.parseInt((String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("Room"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(TAG, "Error Loading Room from Database");

                        Room = 000;

                    }

                    try {
                        RoomCode = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("RoomCode");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        Log.w(TAG, "Error Loading Room Code from Database");
                        RoomCode = "000";
                    }
                    // setting the temperature
                    Temperature = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("Temperature");
                    TotalCapacity = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("TotalCapacity");
                    // setting the number of students
                    NumberOfStudentsPresent = (String) ((HashMap) dynamicDataMap.get(dynamicDatakeysList.get(j))).get("NumberOfStudentsPresent");


                    tempDynamicDataObj.setAvailableSpots(AvailableSpots);
                    tempDynamicDataObj.setBuildingCode(BuildingCode);
                    tempDynamicDataObj.setLabAvailability(LabAvailable);
                    tempDynamicDataObj.setLocationCode(LocationCode);
                    tempDynamicDataObj.setRoom(Room);
                    tempDynamicDataObj.setRoomCode(RoomCode);
                    tempDynamicDataObj.setTemperature(Temperature);
                    tempDynamicDataObj.setTotalCapacity(TotalCapacity);

                    // setting the Room number
//                    tempDynamicDataObj.setRoomStr(dynamicDatakeysList.get(j));
                    // NumberOfStudentsPresent = (String) ((HashMap) dynamicDataMap.get("B204")).get("NumberOfStudentsPresent");
                    tempDynamicDataObj.setNumberOfStudentsPresent(NumberOfStudentsPresent);

                    // Adding the tempDynamicData object created to the List
                    labDynamicDataObjects.add(tempDynamicDataObj);
                }

                if (!dynamicDatakeysList.isEmpty()) {
                    dynamicDatakeysList.clear();
                }

                Log.w(TAG, "ThreadWorking");

                String i = "Work>";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                // Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                // Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        dynamicDataRef.addListenerForSingleValueEvent(labDetailsListener);
//                labDetailsListenerVar = labDetailsListener;

//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI stuff can be done during the computation thread is running
//
//                    }
//                });
//            }
//        }).start();

//        if (labDetailsListenerVar != null) {
//            databaseRootRef.removeEventListener(labDetailsListenerVar);
//        }
    }


    public static void grabLabsWithSoftwareData() {
        ValueEventListener labWithSoftwares = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labsKeyList;
                Object LabsObj = dataSnapshot.getValue();
                if (LabsObj instanceof HashMap) {
                    labsMap = new HashMap((HashMap) LabsObj);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };

        LabsRef.addListenerForSingleValueEvent(labWithSoftwares);

    }


    public static void extractParsedSoftwareData() {

        softwareList = new ArrayList<String>();

//        final Handler handler = new Handler();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {

        // Downloading the parsed software data from the dabase
        ValueEventListener labDetailsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "Software Parsing Data download function called ");


//                        HashMap softwaresMap;
                List<String> softwareKeysList;
//                        softwareKeysList = new ArrayList<String>();


                // extract the snapshot as an object
                Object labObj = dataSnapshot.getValue();
                // Check if the object is of type HashMap, if it is cast it to HashMap
                if (labObj instanceof HashMap) {
                    softwareMap = new HashMap((HashMap) labObj);
                    softwareList = new ArrayList<String>(softwareMap.keySet());
                }

                String i = "Work";
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        softwaresRef.addListenerForSingleValueEvent(labDetailsListener);

//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI stuff can be done at the end of the actual computation in the thread
//                    }
//                });
//            }
//        }).start();

    }


    public static void grabCurrentSemesterCourses() {
        ValueEventListener currentSemesterCoursesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labsKeyList;
                Object courseObj = dataSnapshot.getValue();
                if (courseObj instanceof HashMap) {
                    currentSemesterCoursesMap = new HashMap((HashMap) courseObj);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };
        currentSemesterCoursesRef.addListenerForSingleValueEvent(currentSemesterCoursesListener);
    }


    public static List<String> getCoursesList() {
        List<String> coursesList = new ArrayList<String>();
        try {

            coursesList = new ArrayList<String>(currentSemesterCoursesMap.keySet());
        } catch (Exception e) {
            Log.w(TAG, "Error getting the list of labs for the searched Software");
        }

        return coursesList;
    }

    public static List<String> getSections(String courseNameCode) {
        List<String> sectionsList = new ArrayList<String>();
        try {
            HashMap coursesMap = new HashMap((HashMap) ((HashMap) currentSemesterCoursesMap.get(courseNameCode)).get("Sections"));
            sectionsList = new ArrayList<String>(coursesMap.keySet());
        } catch (Exception e) {
            Log.w(TAG, "Error getting the list of labs for the searched Software");
        }
        return sectionsList;
    }

    public static List<String> getTimeOfSection(String courseNameCode, String sectionCode) {
        List<String> sectionStartTime = new ArrayList<String>();
        try {
            HashMap sectionDetailsMap = new HashMap((HashMap)((HashMap) ((HashMap) currentSemesterCoursesMap.get(courseNameCode)).get("Sections")).get(sectionCode));
            sectionStartTime = new ArrayList<String>();
            sectionStartTime.add((String) sectionDetailsMap.get("StartHour"));
            sectionStartTime.add((String) sectionDetailsMap.get("StartMin"));
            sectionStartTime.add((String) sectionDetailsMap.get("StartSecond"));

        } catch (Exception e) {
            Log.w(TAG, "Error getting the list of labs for the searched Software");
        }
        return sectionStartTime;
    }


    public static List<String> getLabList(String softwareName) {

        List<String> labList;
        labList = new ArrayList<String>();

        try {
            // softwareName = "AGI32_18_3_PTBPE_193";
            HashMap labsMap = new HashMap((HashMap) ((HashMap) softwareMap.get(softwareName)).get("Labs"));
            labList = new ArrayList<String>(labsMap.keySet());
        } catch (Exception e) {
            Log.w(TAG, "Error getting the list of labs for the searched Software");
        }
        return labList;
    }

    // labRoomCode ="H807"
    public static List<String> getSoftwareList(String labRoomCode) {

        List<String> softwareList;
        softwareList = new ArrayList<String>();

        try {
            // softwareName = "AGI32_18_3_PTBPE_193";
            HashMap labSoftwareMap = new HashMap((HashMap) ((HashMap) labsMap.get(labRoomCode)).get("Softwares"));
            softwareList = new ArrayList<String>(labSoftwareMap.keySet());
        } catch (Exception e) {
            Log.w(TAG, "Error getting the list of labs for the searched Software");
        }
        return softwareList;
    }

    public static List<LabDataModel> getLabDynamicDataObjects() {
        return labDynamicDataObjects;
    }

    public static void setLabDynamicDataObjects(List<LabDataModel> labDynamicDataObjects) {
        SIngleton2ShareData.labDynamicDataObjects = labDynamicDataObjects;
    }

    public static List<String> getSoftwareList() {
        return softwareList;
    }

    public static void setSoftwareList(List<String> softwareList) {
        SIngleton2ShareData.softwareList = softwareList;
    }



}
