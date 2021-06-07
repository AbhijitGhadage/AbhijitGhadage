package com.example.techbullsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<SearchList> searchListList;
    private Context mContext;

    public MovieListAdapter(List<SearchList> searchListList, Context mContext) {
        this.searchListList = searchListList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        holder.tv_name.setText(searchListList.get(position).getTitle()+
                "( "+searchListList.get(position).getYear()+" )");

        Picasso.get()
                .load(searchListList.get(position).getPoster())
                .fit()
                .into(holder.iv_movie);
    }

    @Override
    public int getItemCount() {
        return searchListList.size();
    }

    public void updateList(List<SearchList> list) {
        searchListList = new ArrayList<>();
        searchListList.addAll(list);
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_name;
        AppCompatImageView iv_movie;

        public MovieViewHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv_name);
            iv_movie = v.findViewById(R.id.iv_movie);
        }
    }
}