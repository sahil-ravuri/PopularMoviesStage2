package com.example.popularmoviesstage2;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesstage2.Adapter.TrailerAdapter;
import com.example.popularmoviesstage2.DataBase.FavoriteMovie;
import com.example.popularmoviesstage2.DataBase.MovieDatabase;
import com.example.popularmoviesstage2.Model.MainView;
import com.example.popularmoviesstage2.Model.Movies;
import com.example.popularmoviesstage2.Model.Reviews;
import com.example.popularmoviesstage2.Model.Trailers;
import com.example.popularmoviesstage2.Utilities.Json;
import com.example.popularmoviesstage2.Utilities.Network;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class  DetailedActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {
    private Movies movieItem;
    private ArrayList<Reviews> reviewList;
    private ArrayList<Trailers> trailerList;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MovieDatabase mDb;
    private ImageView mFavButton, backdropPath;
    private Boolean isFav;
    MainView mainView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        mainView = ViewModelProviders.of(this).get(MainView.class);
        mFavButton = findViewById(R.id.iv_favButton);
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError("Intent is null");
        }

        movieItem = (Movies) intent.getSerializableExtra("movieItem");
        if (movieItem == null) {
            closeOnError(getString(R.string.Error_NoMovieData));
            return;
        }
        FavoriteMovie favoriteMovie = mainView.check(Integer.parseInt(movieItem.getId()));
        if (favoriteMovie != null)
            setFavorite(true);
        else
            setFavorite(false);
        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mTrailerAdapter = new TrailerAdapter(this, trailerList, this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mTrailerRecyclerView.setLayoutManager(mLayoutManager);
        backdropPath = findViewById(R.id.iv_backdrop);
        mFavButton = findViewById(R.id.iv_favButton);
        mDb = MovieDatabase.getInstance(getApplicationContext());


        getMoreDetails(movieItem.getId());
        setTitle(movieItem.getTitle());
    }

    private void setFavorite(Boolean fav) {
        if (fav) {
            isFav = true;
            mFavButton.setImageResource(R.drawable.fav_on);
        } else {
            isFav = false;
            mFavButton.setImageResource(R.drawable.fav_off);
        }
    }

    private static class SearchURLs {
        URL reviewSearchUrl;
        URL trailerSearchUrl;

        SearchURLs(URL reviewSearchUrl, URL trailerSearchUrl) {
            this.reviewSearchUrl = reviewSearchUrl;
            this.trailerSearchUrl = trailerSearchUrl;
        }
    }

    private static class ResultsStrings {
        String reviewString;
        String trailerString;

        ResultsStrings(String reviewString, String trailerString) {
            this.reviewString = reviewString;
            this.trailerString = trailerString;
        }
    }

    private void getMoreDetails(String id) {
        String reviewQuery = id + File.separator + "reviews";
        String trailerQuery = id + File.separator + "videos";
        SearchURLs searchURLs = new SearchURLs(
                Network.buildUrl(reviewQuery, getText(R.string.api_key).toString()),
                Network.buildUrl(trailerQuery, getText(R.string.api_key).toString())
        );
        new ReviewsQueryTask().execute(searchURLs);
    }


    public class ReviewsQueryTask extends AsyncTask<SearchURLs, Void, ResultsStrings> {
        @Override
        protected ResultsStrings doInBackground(SearchURLs... params) {
            URL reviewsearchUrl = params[0].reviewSearchUrl;
            URL trailersearchUrl = params[0].trailerSearchUrl;

            String reviewResults = null;
            try {
                reviewResults = Network.getResponseFromHttpUrl(reviewsearchUrl);
                reviewList = Json.parseReviewsJson(reviewResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String trailerResults = null;
            try {
                trailerResults = Network.getResponseFromHttpUrl(trailersearchUrl);
                trailerList = Json.parseTrailersJson(trailerResults);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ResultsStrings results = new ResultsStrings(reviewResults, trailerResults);

            return results;
        }

        @Override
        protected void onPostExecute(ResultsStrings results) {
            String searchResults = results.reviewString;
            if (searchResults != null && !searchResults.equals("")) {
                reviewList = Json.parseReviewsJson(searchResults);
                populateDetails();
            }
        }
    }

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.putExtra("finish_on_ended", true);
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void OnListItemClick(Trailers trailerItem) {
        watchYoutubeVideo(trailerItem.getKey());
    }

    private void populateDetails() {

        ((TextView) findViewById(R.id.tv_title)).setText(movieItem.getTitle());
        ((TextView) findViewById(R.id.tv_header_rating)).append(" (" + movieItem.getVote() + "/10)");
        ((RatingBar) findViewById(R.id.rbv_user_rating)).setRating(Float.parseFloat(movieItem.getVote()));
        ((TextView) findViewById(R.id.tv_release_date)).setText(movieItem.getReleaseDate());
        ((TextView) findViewById(R.id.tv_synopsis)).setText(movieItem.getSynopsis());
        mFavButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final FavoriteMovie mov = new FavoriteMovie(
                        Integer.parseInt(movieItem.getId()),
                        movieItem.getTitle(),
                        movieItem.getReleaseDate(),
                        movieItem.getVote(),
                        movieItem.getPopularity(),
                        movieItem.getSynopsis(),
                        movieItem.getImage(),
                        movieItem.getBackdrop()
                );
                if(isFav)
                {
                    mainView.deleteMovie(mov);
                    setFavorite(false);
                }else{
                    mainView.insertMovies(mov);
                    setFavorite(true);
                }
            }
        });


        mTrailerAdapter.setTrailerData(trailerList);

        ((TextView) findViewById(R.id.tv_reviews)).setText("");
        for (int i = 0; i < reviewList.size(); i++) {
            ((TextView) findViewById(R.id.tv_reviews)).append("\n");
            ((TextView) findViewById(R.id.tv_reviews)).append(reviewList.get(i).getContent());
            ((TextView) findViewById(R.id.tv_reviews)).append("\n\n");
            ((TextView) findViewById(R.id.tv_reviews)).append(" - Reviewed by ");
            ((TextView) findViewById(R.id.tv_reviews)).append(reviewList.get(i).getAuthor());
            ((TextView) findViewById(R.id.tv_reviews)).append("\n\n--------------\n");
        }

        String backdropPathURL = Network.buildPosterUrl(movieItem.getBackdrop());

        Picasso.with(this).load(backdropPathURL).placeholder(R.drawable.movieicon).into(backdropPath);

    }

    private void closeOnError(String msg) {
        finish();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
