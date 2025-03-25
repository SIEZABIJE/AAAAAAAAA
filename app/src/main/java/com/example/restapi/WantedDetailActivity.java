package com.example.restapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WantedDetailActivity extends AppCompatActivity {
    private static final String TAG = "WantedDetailActivity";
    private Picasso picasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_wanted_detail);

            setupPicasso();

            String imageUrl = getIntent().getStringExtra("imageUrl");
            String title = getIntent().getStringExtra("title");
            String description = getIntent().getStringExtra("description");
            String rewardText = getIntent().getStringExtra("rewardText");
            String details = getIntent().getStringExtra("details");
            String nationality = getIntent().getStringExtra("nationality");
            String status = getIntent().getStringExtra("status");

            ImageView imageView = findViewById(R.id.detailImage);
            TextView titleText = findViewById(R.id.detailTitle);
            TextView descriptionText = findViewById(R.id.detailDescription);
            TextView rewardText_view = findViewById(R.id.detailReward);
            TextView detailsText = findViewById(R.id.detailDetails);
            TextView nationalityText = findViewById(R.id.detailNationality);
            TextView statusText = findViewById(R.id.detailStatus);

            if (imageView == null) {
                Log.e(TAG, "ImageView not found in layout");
                return;
            }

            if (titleText != null) titleText.setText(title != null ? title : "");
            if (descriptionText != null) descriptionText.setText(description != null ? description : "");
            if (rewardText_view != null) rewardText_view.setText(rewardText != null ? rewardText : "");
            if (detailsText != null) detailsText.setText(details != null ? details : "");
            if (nationalityText != null) nationalityText.setText("Nationality: " + (nationality != null ? nationality : "Unknown"));
            if (statusText != null) statusText.setText("Status: " + (status != null ? status : "Unknown"));

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Log.d(TAG, "Loading image from URL: " + imageUrl);
                try {
                    picasso.load(imageUrl)
                        .placeholder(R.drawable.person)
                        .error(R.drawable.person)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "Image loaded successfully from: " + imageUrl);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "Error loading image from: " + imageUrl, e);
                                imageView.setImageResource(R.drawable.person);
                            }
                        });
                } catch (Exception e) {
                    Log.e(TAG, "Error setting up image loading", e);
                    imageView.setImageResource(R.drawable.person);
                }
            } else {
                imageView.setImageResource(R.drawable.person);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate", e);
        }
    }

    private void setupPicasso() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, br")
                        .header("Connection", "keep-alive")
                        .header("Referer", "https://www.fbi.gov/wanted")
                        .build();
                    return chain.proceed(request);
                })
                .build();

            picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .loggingEnabled(true)
                .build();
        } catch (Exception e) {
            Log.e(TAG, "Error setting up Picasso", e);
            picasso = Picasso.get();
        }
    }
} 