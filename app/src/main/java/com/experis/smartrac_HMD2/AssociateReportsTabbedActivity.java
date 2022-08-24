package com.experis.smartrac_HMD2;

/**
 Class Name: AssociateReportsTabbedActivity
 Created by Rana Krishna Paul
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class AssociateReportsTabbedActivity extends AppCompatActivity {

    private PowerManager.WakeLock mWakeLock;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private ProgressDialog progressDialog;

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImageView associatereportstopbarbackImageViewID;
    private ImageView associatereportstopbarusericonImageViewID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associates_report_tabbed);

        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.associates_report_topbar_title);

        /*PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "AttendanceTracking-DoNotDimScreen");
        mWakeLock.acquire();*/

        initAllViews();

        viewPager = (ViewPager) findViewById(R.id.viewpager2);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        tabLayout.setupWithViewPager(viewPager);

        associatereportstopbarbackImageViewID.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        associatereportstopbarusericonImageViewID.setVisibility(View.GONE);
        //User Icon Click Event
        associatereportstopbarusericonImageViewID.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(AssociateReportsTabbedActivity.this, ChangePasswordActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });


    }//onCreate()

    private void initAllViews() {
        //shared preference
        prefs = getSharedPreferences(CommonUtils.PREFERENCE_NAME, MODE_PRIVATE);
        prefsEditor = prefs.edit();

        associatereportstopbarbackImageViewID = (ImageView) findViewById(R.id.associatereportstopbarbackImageViewID);
        associatereportstopbarusericonImageViewID = (ImageView) findViewById(R.id.associatereportstopbarusericonImageViewID);

        //Progress Dialog
        progressDialog = new ProgressDialog(AssociateReportsTabbedActivity.this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AssociateReportsAttendanceFragment(), "Attendance");
       // adapter.addFragment(new AssociateReportsTargetAchievementFragment(), "Target / Achievement");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

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
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    public void onBackPressed()
    {
        AssociateReportsTabbedActivity.this.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
    @Override
    public void onResume(){
        super.onResume();
    }



}
