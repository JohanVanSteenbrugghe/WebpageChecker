package com.johanvansteenbrugghe.webpagechecker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {

    public static boolean checkInternetAccess(Context context){
        final Activity activity = (Activity) context;
        boolean connectedToInternet = false;
        boolean permissionsToInternet = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connectedToInternet = true;
        }

        if (!connectedToInternet){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Internet Is Not Connected", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            permissionsToInternet = true;
        }

        if (!permissionsToInternet){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Internet Is Not Permitted", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (connectedToInternet && permissionsToInternet){
            return true;
        }
        else{
            return false;
        }
    }

    public static String getUrlRequestResult(String urlString) {
        try{
            String requestResult = "";
            URL url = new URL(urlString);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                if (inputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null){
                        requestResult = requestResult + line;
                    }
                }
                inputStream.close();
            }

            httpURLConnection.disconnect();

            return requestResult;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
