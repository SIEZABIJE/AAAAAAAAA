package com.example.restapi.tasks;

import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import com.example.restapi.adapters.WantedAdapter;
import com.example.restapi.models.WantedPerson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javax.net.ssl.HttpsURLConnection;

public class WantedAsyncTask extends AsyncTask<Void, Void, WantedPerson> {
    private WantedAdapter adapter;
    private List<WantedPerson> wantedList;
    private static final String TAG = "WantedAsyncTask";
    private static final String API_URL = "https://api.fbi.gov/@wanted?pageSize=20&page=1&sort_on=modified&sort_order=desc";

    public WantedAsyncTask(WantedAdapter adapter, List<WantedPerson> wantedList) {
        this.adapter = adapter;
        this.wantedList = wantedList;
    }

    private String transformImageUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.isEmpty()) {
            return null;
        }

        // Extract the path part of the URL
        String path = originalUrl.substring("https://www.fbi.gov".length());
        
        // Remove any double slashes
        while (path.contains("//")) {
            path = path.replace("//", "/");
        }
        
        // If the URL contains "@@images/image", transform it to use "@@download/image/preview"
        if (path.contains("@@images/image")) {
            path = path.replace("@@images/image", "@@download/image/preview");
        }
        
        // Reconstruct the full URL
        String transformedUrl = "https://www.fbi.gov" + path;
        Log.d(TAG, "URL transformation: " + originalUrl + " -> " + transformedUrl);
        
        return transformedUrl;
    }

    @Override
    protected WantedPerson doInBackground(Void... voids) {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            connection.setRequestProperty("Origin", "https://www.fbi.gov");
            connection.setRequestProperty("Referer", "https://www.fbi.gov/wanted");
            
            Log.d(TAG, "Sending request to: " + API_URL);
            
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                Log.e(TAG, "Server returned code: " + connection.getResponseCode());
                Log.e(TAG, "Response message: " + connection.getResponseMessage());
                return null;
            }

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder result = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String jsonResponse = result.toString();
            Log.d(TAG, "Raw JSON response length: " + jsonResponse.length());
            
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray items = jsonObject.getJSONArray("items");
            Log.d(TAG, "Number of items in response: " + items.length());
            
            if (items.length() == 0) {
                Log.e(TAG, "No wanted persons found in response");
                return null;
            }
            
            // Get a random item from the list
            Random random = new Random();
            int randomIndex = random.nextInt(items.length());
            JSONObject randomItem = items.getJSONObject(randomIndex);
            Log.d(TAG, "Selected item index: " + randomIndex);
            Log.d(TAG, "Item keys available: " + randomItem.toString());
            
            WantedPerson person = new WantedPerson();
            person.setTitle(randomItem.getString("title"));
            
            // Handle HTML content in description and details
            String description = randomItem.optString("description", "No description available");
            String details = randomItem.optString("details", "No details available");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString();
                details = Html.fromHtml(details, Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                description = Html.fromHtml(description).toString();
                details = Html.fromHtml(details).toString();
            }
            
            person.setDescription(description);
            person.setRewardText(randomItem.optString("reward_text", "No reward information"));
            person.setDetails(details);
            person.setNationality(randomItem.optString("nationality", "Unknown"));
            person.setStatus(randomItem.optString("status", "Unknown"));

            // Get the image URL with detailed logging
            Log.d(TAG, "Checking for image fields...");
            Log.d(TAG, "Has 'images' field: " + randomItem.has("images"));
            Log.d(TAG, "Has 'image' field: " + randomItem.has("image"));
            
            String imageUrl = null;
            
            if (randomItem.has("images")) {
                JSONArray images = randomItem.getJSONArray("images");
                Log.d(TAG, "Number of images: " + images.length());
                if (images.length() > 0) {
                    JSONObject image = images.getJSONObject(0);
                    Log.d(TAG, "Image object: " + image.toString());
                    // Try different possible image URL fields
                    imageUrl = image.optString("original", 
                             image.optString("large",
                             image.optString("thumb",
                             image.optString("url", ""))));
                    Log.d(TAG, "Found image URL from images array: " + imageUrl);
                }
            }
            
            if (imageUrl == null || imageUrl.isEmpty()) {
                if (randomItem.has("image")) {
                    imageUrl = randomItem.getString("image");
                    Log.d(TAG, "Found direct image URL: " + imageUrl);
                }
            }
            
            if (imageUrl == null || imageUrl.isEmpty()) {
                Log.e(TAG, "No valid image URL found in the response");
            } else {
                // Transform the image URL to use the download endpoint
                String transformedUrl = transformImageUrl(imageUrl);
                Log.d(TAG, "Transformed image URL: " + transformedUrl);
                
                // Test if the transformed URL is accessible
                try {
                    URL imageTestUrl = new URL(transformedUrl);
                    HttpsURLConnection imageTest = (HttpsURLConnection) imageTestUrl.openConnection();
                    imageTest.setRequestMethod("HEAD");
                    imageTest.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                    int responseCode = imageTest.getResponseCode();
                    Log.d(TAG, "Image URL test response code: " + responseCode);
                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        person.setImageUrl(transformedUrl);
                    } else {
                        Log.e(TAG, "Image URL not accessible: " + transformedUrl + " (Response: " + responseCode + ")");
                        // Try without transformation as fallback
                        person.setImageUrl(imageUrl);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error testing image URL: " + e.getMessage());
                    // Use original URL as fallback
                    person.setImageUrl(imageUrl);
                }
            }

            // Log the person object for debugging
            Log.d(TAG, "Created person object: " + 
                  "\nTitle: " + person.getTitle() +
                  "\nImage URL: " + person.getImageUrl() +
                  "\nDescription length: " + person.getDescription().length());

            return person;
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching wanted person data", e);
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(WantedPerson person) {
        if (person != null) {
            wantedList.add(person);
            adapter.notifyItemInserted(wantedList.size() - 1);
            Log.d(TAG, "Successfully added new wanted person: " + person.getTitle());
        } else {
            Log.e(TAG, "Failed to add wanted person - person object was null");
        }
    }
} 