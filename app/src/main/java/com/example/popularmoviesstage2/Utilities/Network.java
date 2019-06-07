package com.example.popularmoviesstage2.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class Network {
    private final static String BASE_URL = "https://image.tmdb.org/t/p/";

    private final static String WIDTH = "original";
    private final static String BASE_URI = "https://api.themoviedb.org/3/movie";
    private final static String API_KEY = "api_key";

    public static URL buildUrl(String movieSearchQuery, String apiKey) {
        Uri builtUri = Uri.parse(BASE_URI).buildUpon().appendEncodedPath(movieSearchQuery).appendQueryParameter(API_KEY,apiKey).build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String buildPosterUrl(String poster) {

        String path = BASE_URL + WIDTH + "/" + poster;
        return path;

    }
}
