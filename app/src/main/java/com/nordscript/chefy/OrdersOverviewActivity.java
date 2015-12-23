package com.nordscript.chefy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersOverviewActivity extends AppCompatActivity {

    private static final String ORDERS_API_URL = "http://217.146.78.187/api/orders";
    private static final String DEBUG_TAG = "Chefy";
    private ArrayList<Order> ordersList = new ArrayList<>();
    private ListView ordersListView;
    private OrdersAdapter ordersAdapter;
    private TextView activeOrderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_overview);

        activeOrderView = (TextView) findViewById(R.id.active_order);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new FetchOrdersListTask().execute(ORDERS_API_URL);
        } else {
            activeOrderView.setText("No network connection available.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchOrdersListTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            URL url;
            HttpURLConnection urlConnection = null;
            JSONArray response = new JSONArray();

            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int responseCode = urlConnection.getResponseCode();

                if(responseCode == 200){
                    String responseString = readStream(urlConnection.getInputStream());
                    Log.v(DEBUG_TAG, responseString);
                    response = new JSONArray(responseString);
                }else{
                    Log.v(DEBUG_TAG, "Response code:"+ responseCode);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(JSONArray array) {
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = array.getJSONObject(i);
                    Date startTime = new Date(Long.parseLong(jsonObject.getString("startTime")));
                    Date endTime = new Date(Long.parseLong(jsonObject.getString("endTime")));
                    String client = jsonObject.getString("clientName");
                    String contactNumber = jsonObject.getString("contactNumber");
                    Order row = new Order(startTime, endTime, client, contactNumber);
                    ordersList.add(row);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ordersListView = (ListView) findViewById(R.id.orders_list);
            ordersAdapter = new OrdersAdapter(OrdersOverviewActivity.this,
                                                    R.layout.order_list_view, ordersList);
            ordersListView.setAdapter(ordersAdapter);
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }
}
