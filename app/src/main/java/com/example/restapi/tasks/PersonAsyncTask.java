package com.example.restapi.tasks;

import android.os.AsyncTask;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restapi.adapters.PersonAdapter;
import com.example.restapi.models.Person;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public class PersonAsyncTask extends AsyncTask<Void, Void, Person> {
    private static final String TAG = "PersonAsyncTask";
    private final PersonAdapter adapter;
    private final List<Person> personList;

    public PersonAsyncTask(PersonAdapter adapter, List<Person> personList) {
        this.adapter = adapter;
        this.personList = personList;
    }

    @Override
    protected Person doInBackground(Void... voids) {
        try {
            URL url = new URL("https://randomuser.me/api");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder result = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONObject jsonObject = new JSONObject(result.toString());
            JSONObject user = jsonObject.getJSONArray("results").getJSONObject(0);
            
            Person person = new Person();
            
            // Get name components
            JSONObject name = user.getJSONObject("name");
            person.setTitle(name.getString("title"));
            person.setName(name.getString("first") + " " + name.getString("last"));
            
            // Get location
            JSONObject location = user.getJSONObject("location");
            JSONObject street = location.getJSONObject("street");
            String fullAddress = street.getString("number") + " " + street.getString("name") + ", " +
                               location.getString("city") + ", " + location.getString("state") + ", " +
                               location.getString("country");
            person.setLocation(fullAddress);
            
            // Get other fields
            person.setGender(user.getString("gender"));
            person.setEmail(user.getString("email"));
            person.setPhone(user.getString("phone"));
            
            // Get image
            JSONObject picture = user.getJSONObject("picture");
            person.setImage(picture.getString("large"));

            return person;
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching person data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Person person) {
        if (person != null) {
            personList.add(person);
            adapter.notifyItemInserted(personList.size() - 1);
        }
    }
} 