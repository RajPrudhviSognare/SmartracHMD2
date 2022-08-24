package com.experis.smartrac_HMD2;

/**
 Class Name: ApproveAttendanceTabbedActivity
 Created by Rana Krishna Paul
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.experis.smartrac_HMD2.R;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class ApproveAttendanceTabbedActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ImageView attendanceapprovaltopbarbackImageViewID;
    private ImageView attendanceapprovaltopbarusericonImageViewID;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_attendance_tabbed);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.attendance_approval_topbar_title);

        initAllViews();

        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        //setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        //tabLayout.setupWithViewPager(viewPager);

        //////////////////////////////////////////////////////
        //Added Later
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PagerAdapter(manager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount()-1);
        ///////////////////////////////////////////////////////


        attendanceapprovaltopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        attendanceapprovaltopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        attendanceapprovaltopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(ApproveAttendanceTabbedActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });


    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        attendanceapprovaltopbarbackImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarbackImageViewID);
        attendanceapprovaltopbarusericonImageViewID = (ImageView) findViewById(R.id.attendanceapprovaltopbarusericonImageViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(ApproveAttendanceTabbedActivity.this);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AttendanceApprovalIntimeFragment(), "In TIme");
        adapter.addFragment(new AttendanceApprovalOuttimeFragment(), "Out TIme");
      //  adapter.addFragment(new AttendanceApprovalWeeklyOffFragment(), "Weekly Off");
        adapter.addFragment(new AttendanceApprovalLeaveFragment(), "Leave");
        adapter.addFragment(new AttendanceApprovalMeetingFragment(), "On Duty");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter { //Used Earlier FragmentPagerAdapter
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /*private final SparseArray<Fragment> mFragmentList = new SparseArray<>();
        private final SparseArray<String> mFragmentTitleList = new SparseArray<>();*/

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        //Added Later
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        //Added Later
        @Override
        public Parcelable saveState() {
            return null;
        }
        //Added Later
        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }
    }

    /*public void destroyItem(ViewGroup viewPager, int position, Object object) {
        viewPager.removeView((View) object);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
            ApproveAttendanceTabbedActivity.this.finish();
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }



///////////////////////////////////////////////
    //Added Later
    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {

            Fragment frag = null;
            switch (position){
                case 0:
                    frag = new AttendanceApprovalIntimeFragment();
                    break;
                case 1:
                    frag = new AttendanceApprovalOuttimeFragment();
                    break;
                case 2:
                    frag = new AttendanceApprovalWeeklyOffFragment();
                    break;
                case 3:
                    frag = new AttendanceApprovalLeaveFragment();
                    break;
                case 4:
                    frag = new AttendanceApprovalMeetingFragment();
                    break;
            }
            return frag;
        }
        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title=" ";
            switch (position){
                case 0:
                    title="In time";
                    break;
                case 1:
                    title="Out time";
                    break;
                case 2:
                    title="Weekly off";
                    break;
                case 3:
                    title="Leave";
                    break;
                case 4:
                    title="Meeting";
                    break;
            }

            return title;
        }
    }


}//Main Class


