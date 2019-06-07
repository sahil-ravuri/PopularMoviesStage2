package com.example.popularmoviesstage2.Utilities;

import com.example.popularmoviesstage2.Model.Movies;
import com.example.popularmoviesstage2.Model.Reviews;
import com.example.popularmoviesstage2.Model.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class Json {
    public static ArrayList<Movies> parseMoviesJson(String json) {
        try {

            Movies movie;
            JSONObject json_object = new JSONObject(json); //(json.replace("\\",""));

            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            ArrayList<Movies> movieitems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                movie = new Movies(movieJson.optString("id","Not Available"),movieJson.optString("original_title","Not Available"),movieJson.optString("release_date","Not Available"),movieJson.optString("vote_average","Not Available"),movieJson.optString("popularity","Not Available"),movieJson.optString("overview","Not Available"),movieJson.optString("poster_path","Not Available"),movieJson.optString("backdrop_path","Not Available"));

                movieitems.add(movie);
            }
            return movieitems;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<Reviews> parseReviewsJson(String json) {
        try {

            Reviews review;
            JSONObject json_object = new JSONObject(json); //(json.replace("\\",""));

            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            ArrayList<Reviews> reviewitems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                review = new Reviews(
                        movieJson.optString("author","Not Available"),
                        movieJson.optString("content","Not Available"),
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("url","Not Available")
                );

                reviewitems.add(review);
            }
            return reviewitems;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static ArrayList<Trailers> parseTrailersJson(String json) {
        try {
            Trailers trailer;
            JSONObject json_object = new JSONObject(json); //(json.replace("\\",""));
            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            ArrayList<Trailers> traileritems = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                trailer = new Trailers(
                        movieJson.optString("name","Not Available"),
                        movieJson.optString("site","Not Available"),
                        movieJson.optString("key","Not Available")
                );

                traileritems.add(trailer);
            }
            return traileritems;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
