package com.example.techbullsdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;

    private List<SearchList> searchListList;
    private Context mContext;

    public MovieListAdapter( Context mContext) {
        this.mContext = mContext;
        searchListList = new LinkedList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new MovieViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_loading, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
//        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        switch (getItemViewType(position)) {
            case ITEM:
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.tv_name.setText(searchListList.get(position).getTitle()+
                        "( "+searchListList.get(position).getYear()+" )");

                Picasso.get()
                        .load(searchListList.get(position).getPoster())
                        .fit()
                        .into(movieViewHolder.iv_movie);

                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }


    }

    @Override
    public int getItemCount() {
        return searchListList == null ? 0 : searchListList.size();
    }

    public void setMovieList(List<SearchList> searchListList) {
        this.searchListList = searchListList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == searchListList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new SearchList());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = searchListList.size() - 1;
        SearchList result = getItem(position);

        if (result != null) {
            searchListList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(SearchList movie) {
        searchListList.add(movie);
        notifyItemInserted(searchListList.size() - 1);
    }

    public void addAll(List<SearchList> moveResults) {
        for (SearchList result : moveResults) {
            add(result);
        }
    }

    public SearchList getItem(int position) {
        return searchListList.get(position);
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_name;
        AppCompatImageView iv_movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_movie = itemView.findViewById(R.id.iv_movie);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    public void updateList(List<SearchList> list) {
        searchListList = new ArrayList<>();
        searchListList.addAll(list);
        notifyDataSetChanged();
    }
}