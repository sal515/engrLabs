package ca.engrLabs_390.engrlabs.TA_Section;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import ca.engrLabs_390.engrlabs.ExpandableRecycler;
import ca.engrLabs_390.engrlabs.MainActivity;
import ca.engrLabs_390.engrlabs.R;
import ca.engrLabs_390.engrlabs.Settings;
import ca.engrLabs_390.engrlabs.SharedPreferenceHelper;

public class CourseSectionSelection extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button signOutButton;
    ListView classList;
    public SharedPreferenceHelper sharedPreferenceHelper;
    ArrayList<String> list = new ArrayList<>();

    // =========  Nav Drawer Stuff   ==========
    DrawerLayout drawer;
    NavigationView navigationView;
    MenuItem homePageNavButton;
    MenuItem labListNavButton;
    MenuItem taLoginNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_section_selection);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.navBar)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status)); //status bar or the time bar at the top
        }
        mAuth = FirebaseAuth.getInstance();

        //format ActionBar and Content
        ActionBar ab = getSupportActionBar();   //get the Action Bar object
        ab.setTitle("TA Course Selection");   //set the title
        ab.setDisplayHomeAsUpEnabled(true); //enable UP button, parent is declared in the manifest

        //Sign out Button
        signOutButton = findViewById(R.id.signOutButtonInstructorPage);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();

                signOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        classList = findViewById(R.id.addedCourseListView);
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        drawer = findViewById(R.id.drawerContainer);
        navigationView = findViewById(R.id.nav_view);

        homePageNavButton = findViewById(R.id.homepage);
        labListNavButton = findViewById(R.id.lablist);
        taLoginNavButton = findViewById(R.id.taSection);
        navigationView.getMenu().getItem(1).setVisible(false);
        navigationView.getMenu().getItem(2).setVisible(false);
        navigationView.getMenu().getItem(3).setVisible(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homepage:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        navigationView.getMenu().findItem(R.id.taSection).setChecked(false);
                        break;
                    case R.id.lablist:
                        startActivity(new Intent(getApplicationContext(), ExpandableRecycler.class));
                        navigationView.getMenu().findItem(R.id.taSection).setChecked(false);
                        break;
                    case R.id.taSection:
                        //tartActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    //Creates ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_selection,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume(){
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        Settings profile = sharedPreferenceHelper.getSettings();
        list.clear();
        if (profile != null){
            if (profile.courseList.size() > 0){
                for (int i=0;i<profile.courseList.size();i++){
                    list.add(profile.courseList.get(i).courseName + "     " + profile.courseList.get(i).sectionName);
                }
            }
            else{
                list.add("No Courses Added");
            }
        }
        else{
            list.add("No Courses Added");
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,list);
        arrayAdapter.notifyDataSetChanged();
        classList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        navigationView.getMenu().findItem(R.id.lablist).setChecked(false);
        navigationView.getMenu().findItem(R.id.homepage).setChecked(false);
        navigationView.getMenu().findItem(R.id.taSection).setChecked(true);
    }

    //Handles action button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_course:

                Intent insertCourseIntent = new Intent(getApplicationContext(), insertCourseActivity.class);
                startActivity(insertCourseIntent);

//                ChooseCourseDialogFragment dialog = new ChooseCourseDialogFragment();
//                dialog.show(getSupportFragmentManager(), "Insert Course");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        mAuth.signOut();
//        updateUI(null);
    }

}
