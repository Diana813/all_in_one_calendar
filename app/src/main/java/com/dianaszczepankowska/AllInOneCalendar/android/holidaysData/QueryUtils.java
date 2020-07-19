package com.dianaszczepankowska.AllInOneCalendar.android.holidaysData;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    public static final int READ_TIMEOUT = 10000; //milliseconds
    public static final int CONNECT_TIMEOUT = 15000; //milliseconds


    private QueryUtils() {
    }

    public static List<Holiday> extractFeatureFromJson(String holidaysJSON) {
        if (TextUtils.isEmpty(holidaysJSON)) {
            return null;
        }

        List<Holiday> holidays = new ArrayList<>();
        String name = null;
        String date = null;
        //String holidays = null;

        try {

           /* JSONObject jsonObj = new JSONObject(holidaysJSON);
            JSONObject response = jsonObj.optJSONObject("response");
            assert response != null;
            JSONArray jsonArray = response.getJSONArray("holidays");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objects = jsonArray.getJSONObject(i);
                Iterator key = objects.keys();
                while (key.hasNext()) {
                    String holidayName = key.next().toString();
                    if (holidayName.equals("name")) {
                        holidays = objects.getString(holidayName);
                    }
                }
            }*/
            JSONObject jsonObj = new JSONObject(holidaysJSON);
            JSONArray jsonArray = jsonObj.getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objects = jsonArray.getJSONObject(i);

                Iterator key = objects.keys();
                while (key.hasNext()) {
                    String holidayName = key.next().toString();
                    if (holidayName.equals("summary")) {
                        name = objects.getString(holidayName);

                    }

                    if (holidayName.equals("start")) {
                        JSONObject jsonObject = objects.getJSONObject("start");

                        date = jsonObject.getString("date");

                    }
                }
                if (name != null && date != null) {
                    holidays.add(new Holiday(name, date));
                }
            }


        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing holidays JSON results", e);
        }
        return holidays;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    public static List<Holiday> fetchHolidaysData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }

}


