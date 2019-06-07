package com.example.popularmoviesstage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmoviesstage2.Adapter.MovieAdapter;
import com.example.popularmoviesstage2.DataBase.FavoriteMovie;
import com.example.popularmoviesstage2.Model.MainView;
import com.example.popularmoviesstage2.Model.Movies;
import com.example.popularmoviesstage2.Utilities.Json;
import com.example.popularmoviesstage2.Utilities.Network;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";
    private static String currentSort;

    private List<Movies> movieList;

    private RecyclerView mMovieRecyclerView;

    private MainView viewModel;

    private List<FavoriteMovie> favMovs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieRecyclerView = findViewById(R.id.rv_main);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieRecyclerView.setHasFixedSize(true);

        movieList = new ArrayList<>();
        favMovs = new ArrayList<>();

        setTitle(getString(R.string.app_name) );

        viewModel = ViewModelProviders.of(this).get(MainView.class);

        loadMovies();
    }

    private void setIconInMenu(Menu menu, int id_menu, int id_lable, int id_icon) {
        MenuItem item = menu.findItem(id_menu);
        SpannableStringBuilder builder = new SpannableStringBuilder("  "+getResources().getString(id_lable));
        builder.setSpan(new ImageSpan(this,id_icon),0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        item.setTitle(builder);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        setIconInMenu(menu,R.id.id_popular,R.string.popular,R.drawable.poplaricon);
        setIconInMenu(menu,R.id.id_top_rated,R.string.top_rated,R.drawable.toprated);
        setIconInMenu(menu,R.id.id_favorites,R.string.favorites,R.drawable.fav);
        return super.onCreateOptionsMenu(menu);
    }


    private void loadMovies() {
        makeMovieSearchQuery();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       switch(id){
           case R.id.id_popular:
               ClearMovieItemList();
               currentSort = SORT_POPULAR;
               setTitle(getString(R.string.app_name));
               loadMovies();
               return true;
           case R.id.id_top_rated:
               ClearMovieItemList();
               currentSort = SORT_TOP_RATED;
               setTitle("Top Rated");
               loadMovies();
               return true;
           case R.id.id_favorites:
               ClearMovieItemList();
               currentSort = SORT_FAVORITE;
               setTitle("Favorites");
               loadMovies();
               return true;
       }
        return super.onOptionsItemSelected(item);
    }

    private void ClearMovieItemList() {
        if (movieList != null) {
            movieList.clear();
        } else {
            movieList = new ArrayList<>();
        }
    }

    private void makeMovieSearchQuery() {
        if (currentSort.equals(SORT_FAVORITE)) {
            ClearMovieItemList();
            viewModel.getAllMovies().observe(this, new Observer<List<FavoriteMovie>>() {
                @Override
                public void onChanged(@Nullable List<FavoriteMovie> favs) {
                    if(favs.size()>0) {
                        favMovs.clear();
                        favMovs = favs;
                    }
                }
            });
            for (int i = 0; i< favMovs.size(); i++) {
                Movies mov = new Movies(
                        String.valueOf(favMovs.get(i).getId()),
                        favMovs.get(i).getTitle(),
                        favMovs.get(i).getReleaseDate(),
                        favMovs.get(i).getVote(),
                        favMovs.get(i).getPopularity(),
                        favMovs.get(i).getSynopsis(),
                        favMovs.get(i).getImage(),
                        favMovs.get(i).getBackdrop()
                );
                movieList.add( mov );
            }
        } else {
            String movieQuery = currentSort;
            URL movieSearchUrl = Network.buildUrl(movieQuery, getText(R.string.api_key).toString());
            new MoviesQueryTask().execute(movieSearchUrl);
        }
    }


    public class MoviesQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String searchResults = null;
            try {
                searchResults = Network.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            if (searchResults != null && !searchResults.equals("")) {
                movieList.clear();
                movieList = Json.parseMoviesJson(searchResults);
                setAdapter(movieList);
            }
        }
    }

    private void setAdapter(List<Movies> movieList) {
        MovieAdapter mMovieAdapter = new MovieAdapter(this, movieList);
        mMovieRecyclerView.setAdapter(mMovieAdapter);
    }

}
