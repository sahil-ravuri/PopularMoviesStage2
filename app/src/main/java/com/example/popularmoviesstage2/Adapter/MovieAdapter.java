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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{

    Context context;
    List<Movies> movies;

    public MovieAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MovieViewHolder(LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int i) {
          Picasso.with(context).load(Network.buildPosterUrl(movies.get(i).getImage()))
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








/*extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private List<Movies> mMovieItemList;
    private final Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Movies movieItem);
    }

    public MovieAdapter(List<Movies> movieItemList, ListItemClickListener listener, Context context) {

        mMovieItemList = movieItemList;

        mOnClickListener = listener;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovieItemList == null ? 0 : mMovieItemList.size();
    }

    public void setMovieData(List<Movies> movieItemList) {
//        Log.d(TAG,"Num movies: " + movieItemList.size());
        mMovieItemList = movieItemList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listMovieItemView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            listMovieItemView = itemView.findViewById(R.id.id_poster);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            Movies movieItem = mMovieItemList.get(listIndex);
            listMovieItemView = itemView.findViewById(R.id.id_poster);
            String posterPathURL = Network.buildPosterUrl(movieItem.getImage());
            try {
                Picasso.with(mContext)
                        .load(posterPathURL)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(listMovieItemView);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mMovieItemList.get(clickedPosition));
        }
    }

}
*/