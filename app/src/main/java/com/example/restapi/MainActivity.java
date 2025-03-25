package com.example.restapi;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "RestApp";
    private FloatingActionButton startRandom;
    private BottomNavHandler bottomNavHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        startRandom = findViewById(R.id.startRandom);
        startRandom.setOnClickListener(v->{
            MyAsyncTask myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute();
        });

        bottomNavHandler = new BottomNavHandler(this);

        if (savedInstanceState == null) {
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.navigation_persons);
        }
    }
}