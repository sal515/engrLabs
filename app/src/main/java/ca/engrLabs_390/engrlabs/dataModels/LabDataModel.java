package ca.engrLabs_390.engrlabs.dataModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;

//public class LabDataModel implements Comparable<LabDataModel> {
public class LabDataModel implements Cloneable {
    private int floor;
    private int Room;
    private String RoomCode;
    private String Temperature;
    private String NumberOfStudentsPresent;
    private String TotalCapacity;
    //    private int upcomingClass;
    private boolean favourite;
    private boolean clicked;
    private String timeStamp;
    private String AvailableSpots;
    private String LabAvailability;
    private String BuildingCode;
    private String LocationCode;
    private HashMap<String, String> upcomingClass = new HashMap<String, String>();
    private long upcomingclassTime;


    @Override
    protected Object clone() throws CloneNotSupportedException {
        LabDataModel clone = null;
        try {
            clone = (LabDataModel) super.clone();

            // copy new data object to cloned method
            clone.setUpcomingClass((HashMap) this.getUpcomingClass());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return clone;
    }

    // reference : https://www.geeksforgeeks.org/overriding-equals-method-in-java/
    @Override
    public boolean equals(@Nullable Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof LabDataModel)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        LabDataModel labDataModel = (LabDataModel) o;

//        // Compare the data members and return accordingly
//        return Double.compare(re, c.re) == 0
//                && Double.compare(im, c.im) == 0;

        // Whatever checks added here will be updated in the recyclerView when database data changes
        return this.getNumberOfStudentsPresent().equals(labDataModel.getNumberOfStudentsPresent()) &&
                this.getTemperature().equals(labDataModel.getTemperature()) &&
                this.getLabAvailability().equals(labDataModel.getLabAvailability()) &&
                Objects.equals(this.getUpcomingClass().get("Category"), labDataModel.getUpcomingClass().get("Category")) &&
                Objects.equals(this.getUpcomingClass().get("StartHour"), labDataModel.getUpcomingClass().get("StartHour")) &&
                Objects.equals(this.getUpcomingClass().get("StartMin"), labDataModel.getUpcomingClass().get("StartMin")) &&
                Objects.equals(this.getUpcomingClass().get("StartSec"), labDataModel.getUpcomingClass().get("StartSec")) &&
                Objects.equals(this.getUpcomingClass().get("Subject"), labDataModel.getUpcomingClass().get("Subject")) &&
                Objects.equals(this.getUpcomingClass().get("Title"), labDataModel.getUpcomingClass().get("Title"));
    }

    public LabDataModel() {
        this.floor = -1;
        this.Room = -1;
        this.Temperature = "";
        this.NumberOfStudentsPresent = "";
        this.TotalCapacity = "";
        this.favourite = false;
        this.clicked = false;
        this.timeStamp = "";
        this.LabAvailability = "";
        this.upcomingclassTime = Long.valueOf(-1);
//        this.upcomingClass = null;
        this.upcomingClass.put("Category", "");
        this.upcomingClass.put("StartHour", "");
        this.upcomingClass.put("StartMin", "");
        this.upcomingClass.put("StartSec", "");
        this.upcomingClass.put("Subject", "");
        this.upcomingClass.put("Title", "");
    }

    public List<LabDataModel> generateLabs(int size) {
        List<LabDataModel> labDataModelList = new ArrayList<>();
        LabDataModel tempObj;

        for (int i = 0; i < size; i++) {
            tempObj = new LabDataModel();
            tempObj.setRoom(i);
            labDataModelList.add(tempObj);
        }

        int j = 0;
        return labDataModelList;
    }

    // getters and setters

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRoom() {
        return Room;
    }

    public void setRoom(int room) {
        this.Room = room;
    }


    public String getRoomCode() {
        return RoomCode;
    }

    public void setRoomCode(String roomCode) {
        RoomCode = roomCode;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        this.Temperature = temperature;
    }


    public String getNumberOfStudentsPresent() {
        return NumberOfStudentsPresent;
    }

    public void setNumberOfStudentsPresent(String numberOfStudentsPresent) {
        this.NumberOfStudentsPresent = numberOfStudentsPresent;
    }

    public String getTotalCapacity() {
        return TotalCapacity;
    }

    public void setTotalCapacity(String totalCapacity) {
        TotalCapacity = totalCapacity;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAvailableSpots() {
        return AvailableSpots;
    }

    public void setAvailableSpots(String availableSpots) {
        AvailableSpots = availableSpots;
    }

    public String getLabAvailability() {
        return LabAvailability;
    }

    public void setLabAvailability(String labAvailable) {
        this.LabAvailability = labAvailable;
    }

    public String getBuildingCode() {
        return BuildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        BuildingCode = buildingCode;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public HashMap<String, String> getUpcomingClass() {
        return upcomingClass;
    }

    public void setUpcomingClass(HashMap<String, String> upcomingClass) {
        this.upcomingClass = upcomingClass;
    }

    public long getUpcomingclassTime() {
        return upcomingclassTime;
    }

    public void setUpcomingclassTime(long upcomingclassTime) {
        this.upcomingclassTime = upcomingclassTime;
    }
}
