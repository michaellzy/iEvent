package com.example.ievent.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    protected BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Log.d("TestLog", "onCreate executed");
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toast.makeText(this, "Activity Created", Toast.LENGTH_SHORT).show();
//        viewPager = findViewById(R.id.viewPager);
        //adapter
//        setupViewPager(viewPager);
//        setupBottomNavigationView();
//        Log.d("NavigationItem", "setView");
    }
//    private void setupBottomNavigationView() {
//        Log.d("NavigationSetup", "Setting up navigation listener");
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//
//            Toast.makeText(this, "Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
//
//
//            Log.d("NavigationItem", "Item clicked: " + item.getItemId());
//            Intent intent = null;
//            int itemId = item.getItemId();
//            if (itemId == R.id.navigation_home) {
////                    startActivity(new Intent(BaseActivity.this, MainActivity.class));
//                Log.d("NavigationItem", "Clicked home");
//                intent = new Intent(BaseActivity.this, MainActivity.class);
////                    return true;
//            } else if (itemId == R.id.navigation_search) {
////                    startActivity(new Intent(BaseActivity.this, SearchActivity.class));
//                Log.d("NavigationItem", "Clicked search");
//                intent = new Intent(BaseActivity.this, SearchActivity.class);
////                    return true;
//            }
//
//            if (intent != null) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//                return true;
//            }
//            return false;
//        });
//        Log.d("NavigationSetup", "Navigation listener set");
//    }
//}
//
//    private void setupViewPager(ViewPager viewPager) {
//        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new HomeFragment());   //0
//        fragmentList.add(new SearchFragment()); //1
//        fragmentList.add(new TicketFragment()); //2
//        fragmentList.add(new NoticeFragment()); //3
//
//        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
//        viewPager.setAdapter(viewPagerAdapter);
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (bottomNavigationView != null) {
//                    MenuItem item = bottomNavigationView.getMenu().getItem(position);
//                    item.setChecked(true);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }
    private void setupBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    viewPager.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.navigation_search) {
                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    viewPager.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.navigation_ticket) {
//                    startActivity(new Intent(getApplicationContext(), SearchActivity.class));
//                    viewPager.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.navigation_notifications) {
//                    viewPager.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
    }
}