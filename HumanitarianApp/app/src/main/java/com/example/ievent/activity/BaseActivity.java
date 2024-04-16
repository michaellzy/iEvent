package com.example.ievent.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ievent.R;
import com.example.ievent.adapter.ViewPagerAdapter;
import com.example.ievent.fragment.HomeFragment;
import com.example.ievent.fragment.NoticeFragment;
import com.example.ievent.fragment.SearchFragment;
import com.example.ievent.fragment.TicketFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {
    protected FirebaseAuth mAuth;

    protected Firebase database;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPager);


        setupViewPager(viewPager);
        setupBottomNavigationView();
    }

    private void setupViewPager(ViewPager viewPager) {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());   //0
        fragmentList.add(new SearchFragment()); //1
        fragmentList.add(new TicketFragment()); //2
        fragmentList.add(new NoticeFragment()); //3

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (bottomNavigationView != null) {
                    MenuItem item = bottomNavigationView.getMenu().getItem(position);
                    item.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
                    viewPager.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
                    viewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
    }
}