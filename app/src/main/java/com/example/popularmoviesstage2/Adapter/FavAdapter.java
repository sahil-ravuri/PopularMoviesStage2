package com.example.popularmoviesstage2.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmoviesstage2.DetailedActivity;
import com.example.popularmoviesstage2.Model.Movies;
import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.Utilities.Network;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MovieViewHolder>{

    Context context;
    List<Movies> movies;

    public FavAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public FavAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new FavAdapter.MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MovieViewHolder movieViewHolder, int i) {
        Picasso.with(context).load(Network.buildPosterUrl(movies.get(i).getImage())).placeholder(R.drawable.movieicon)
                .into(movieViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.id_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("movieItem",movies.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}