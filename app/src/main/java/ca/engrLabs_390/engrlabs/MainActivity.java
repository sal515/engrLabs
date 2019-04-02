// This is the barebone Project
package ca.engrLabs_390.engrlabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import ca.engrLabs_390.engrlabs.TA_Section.LoginActivity;
import ca.engrLabs_390.engrlabs.dataModels.SIngleton2ShareData;
import ca.engrLabs_390.engrlabs.notifications.AlarmReceiver;

public class MainActivity extends AppCompatActivity {

    // Declare reference variables
    Button loginBtn;
    Button homepageBtn;
    ImageView logo;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
//        setupWindowAnimations();

        SIngleton2ShareData.downloadDynamicDataForRecyclerStartUp();
        SIngleton2ShareData.extractParsedSoftwareData();

        initializeReferences();
        initializeListeners();

        notificationManager = NotificationManagerCompat.from(this);

        setScheduledNotification(Calendar.MONDAY,22,45,0);  //Monday at 10:45pm

    }

    // Slide animation between activity
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    void initializeReferences() {
        loginBtn = findViewById(R.id.loginBtn);
        homepageBtn = findViewById(R.id.homepageBtn);
        logo = findViewById(R.id.logo);
        logo.setImageResource(R.drawable.ic_logo_better);
    }

    void initializeListeners() {
        loginBtn.setOnClickListener(loginBtnOnclickListener);
        homepageBtn.setOnClickListener(homepageBtnOnclickListener);
    }


    Button.OnClickListener loginBtnOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_loginActivity();
        }
    };

    Button.OnClickListener homepageBtnOnclickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_homepageActivity();
        }
    };


    // going from mainActivity to ExpandableRecylerView with Bottom Nav
    public void goto_homepageActivity() {
        Intent intent = new Intent(this, ExpandableRecycler.class);

        startActivity(intent);
    }

    // going from mainActivity to login activity for testing
    public void goto_loginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void setScheduledNotification(int dayOfWeek, int hourOfDay, int minute, int second){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_WEEK,dayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,second);

        //cal.add(Calendar.MINUTE, 1);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
}
