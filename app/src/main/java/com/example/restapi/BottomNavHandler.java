package com.example.restapi;

import android.app.Activity;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHandler {
    private final Activity activity;
    private final BottomNavigationView bottomNavigationView;

    public BottomNavHandler(Activity activity) {
        this.activity = activity;
        this.bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        setupNavigation();
    }

    private void setupNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.navigation_persons) {
                selectedFragment = new PersonsFragment();
            } else if (itemId == R.id.navigation_wanted) {
                selectedFragment = new WantedFragment();
            } else if (itemId == R.id.navigation_crypto) {
                selectedFragment = new CryptoFragment();
            }

            if (selectedFragment != null) {
                ((AppCompatActivity) activity).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
                return true;
            }
            return false;
        });
    }
} 