package com.example.restapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restapi.R;
import com.example.restapi.WantedDetailActivity;
import com.example.restapi.models.WantedPerson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WantedAdapter extends RecyclerView.Adapter<WantedAdapter.ViewHolder> {
    private List<WantedPerson> wantedList;
    private Context context;
    private static final String TAG = "WantedAdapter";
    private Picasso picasso;

    public WantedAdapter(List<WantedPerson> wantedList) {
        this.wantedList = wantedList;
    }

    private void setupPicasso(Context context) {
        if (picasso != null) return;
        
        Log.d(TAG, "Setting up custom Picasso instance");
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

        picasso = new Picasso.Builder(context)
            .downloader(new OkHttp3Downloader(client))
            .loggingEnabled(true)
            .build();
        
        Log.d(TAG, "Custom Picasso instance created successfully");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        setupPicasso(context);
        View view = LayoutInflater.from(context).inflate(R.layout.wanted_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WantedPerson person = wantedList.get(position);

        String imageUrl = person.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Log.d(TAG, "Position " + position + " - Loading image from URL: " + imageUrl);
            
            if (picasso == null) {
                Log.e(TAG, "Picasso instance is null, falling back to default");
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.person)
                    .error(R.drawable.person)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Position " + position + " - Image loaded successfully from: " + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Position " + position + " - Error loading image from: " + imageUrl, e);
                        }
                    });
            } else {
                picasso.load(imageUrl)
                    .placeholder(R.drawable.person)
                    .error(R.drawable.person)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Position " + position + " - Image loaded successfully from: " + imageUrl);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "Position " + position + " - Error loading image from: " + imageUrl, e);
                        }
                    });
            }
        } else {

            holder.imageView.setImageResource(R.drawable.person);
        }

        holder.titleView.setText(person.getTitle());
        String description = person.getDescription();
        if (description != null && description.length() > 100) {
            description = description.substring(0, 97) + "...";
        }
        holder.descriptionView.setText(description);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WantedDetailActivity.class);
            intent.putExtra("imageUrl", person.getImageUrl());
            intent.putExtra("title", person.getTitle());
            intent.putExtra("description", person.getDescription());
            intent.putExtra("rewardText", person.getRewardText());
            intent.putExtra("details", person.getDetails());
            intent.putExtra("nationality", person.getNationality());
            intent.putExtra("status", person.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return wantedList.size();
    }

    public void updateList(List<WantedPerson> newList) {
        this.wantedList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;
        TextView descriptionView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.wantedImage);
            titleView = view.findViewById(R.id.wantedTitle);
            descriptionView = view.findViewById(R.id.wantedDescription);
        }
    }
} 