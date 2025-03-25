package com.example.restapi.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.example.restapi.adapters.CryptoAdapter;
import com.example.restapi.models.Crypto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.List;
import java.util.Random;

public class CryptoAsyncTask extends AsyncTask<Void, Void, Crypto> {
    private static final String TAG = "CryptoAsyncTask";
    private final CryptoAdapter adapter;
    private final List<Crypto> cryptoList;

    public CryptoAsyncTask(CryptoAdapter adapter, List<Crypto> cryptoList) {
        this.adapter = adapter;
        this.cryptoList = cryptoList;
    }

    @Override
    protected Crypto doInBackground(Void... voids) {
        try {
            // First, get the list of cryptocurrencies
            URL currenciesUrl = new URL("https://api.coinbase.com/v2/currencies/crypto");
            HttpsURLConnection currenciesConnection = (HttpsURLConnection) currenciesUrl.openConnection();
            InputStream currenciesStream = currenciesConnection.getInputStream();
            BufferedReader currenciesReader = new BufferedReader(new InputStreamReader(currenciesStream));
            StringBuilder currenciesResult = new StringBuilder();
            String line;
            
            while ((line = currenciesReader.readLine()) != null) {
                currenciesResult.append(line);
            }

            JSONObject currenciesJson = new JSONObject(currenciesResult.toString());
            JSONArray data = currenciesJson.getJSONArray("data");
            
            // Get a random crypto from the list
            Random random = new Random();
            JSONObject randomCrypto = data.getJSONObject(random.nextInt(data.length()));
            String code = randomCrypto.getString("code");
            
            // Now, get the price for this cryptocurrency
            URL priceUrl = new URL("https://api.coinbase.com/v2/prices/" + code + "-USD/spot");
            HttpsURLConnection priceConnection = (HttpsURLConnection) priceUrl.openConnection();
            InputStream priceStream = priceConnection.getInputStream();
            BufferedReader priceReader = new BufferedReader(new InputStreamReader(priceStream));
            StringBuilder priceResult = new StringBuilder();
            
            while ((line = priceReader.readLine()) != null) {
                priceResult.append(line);
            }

            JSONObject priceJson = new JSONObject(priceResult.toString());
            String price = priceJson.getJSONObject("data").getString("amount");
            
            // Create Crypto object with all information
            Crypto crypto = new Crypto();
            crypto.setCode(code);
            crypto.setName(randomCrypto.getString("name"));
            crypto.setColor(randomCrypto.optString("color", "#000000"));
            crypto.setPrice("$" + price);

            return crypto;
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error fetching crypto data", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Crypto crypto) {
        if (crypto != null) {
            cryptoList.add(crypto);
            adapter.notifyItemInserted(cryptoList.size() - 1);
        }
    }
} 