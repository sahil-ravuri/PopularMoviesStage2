package com.example.popularmoviesstage2.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmoviesstage2.Model.Trailers;
import com.example.popularmoviesstage2.R;

import java.util.ArrayList;
import java.util.List;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private final Context mContext;
    private List<Trailers> mTrailerList;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(Trailers trailerItem);
    }

    public TrailerAdapter(Context mContext, ArrayList<Trailers> items, ListItemClickListener listener) {
        this.mContext = mContext;
        mTrailerList = items;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailers;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mTrailerList == null ? 0 : mTrailerList.size();
    }

    public void setTrailerData(List<Trailers> trailerItemList) {
        mTrailerList = trailerItemList;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listTrailerItemView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            listTrailerItemView = itemView.findViewById(R.id.tv_trailer_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mTrailerList.get(clickedPosition));
        }

        public void bind(int position) {
            listTrailerItemView.setText(mTrailerList.get(position).getName());
        }
    }
}
