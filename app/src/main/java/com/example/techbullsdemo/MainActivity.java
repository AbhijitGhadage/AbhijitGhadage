package com.example.techbullsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.edit_search)
    AppCompatEditText edit_search;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    List<SearchList> searchArrayList = new ArrayList<>();
    MovieListAdapter movieListAdapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        movieListAdapter = new MovieListAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(movieListAdapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();


    }



        private void loadNextPage() {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MovieResponse> call = apiService.getMoviesList("batman", "d378bdc1");
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    movieListAdapter.removeLoadingFooter();
                    isLoading = false;

                    searchArrayList = response.body().getSearchArrayList();
                    movieListAdapter.addAll(searchArrayList);

                    if (currentPage != TOTAL_PAGES) movieListAdapter.addLoadingFooter();
                    else isLastPage = true;
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }


        private void loadFirstPage() {

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MovieResponse> call = apiService.getMoviesList("batman", "d378bdc1");
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    searchArrayList = response.body().getSearchArrayList();
//                    progressBar.setVisibility(View.GONE);
                    movieListAdapter.addAll(searchArrayList);

                    if (currentPage <= TOTAL_PAGES) movieListAdapter.addLoadingFooter();
                    else isLastPage = true;
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }

            });
        }


      /*  ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMoviesList("batman", "d378bdc1");
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                searchArrayList.clear();
//                Log.d("Response", response.body());
                String res = null;
                if (response.body() != null) {
                    res = response.body().getResponse();

                    if (res.equalsIgnoreCase("True")) {
                        searchArrayList = response.body().getSearchArrayList();

                        movieListAdapter = new MovieListAdapter(searchArrayList, getApplicationContext());
                        recyclerView.setAdapter(movieListAdapter);
                        movieListAdapter.notifyDataSetChanged();

//                        recyclerView.setAdapter(new MovieListAdapter(searchArrayList,  getApplicationContext()));

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }*/

    @OnTextChanged(R.id.edit_search)
    public void search() {
        if (!searchArrayList.isEmpty()) {
            List<SearchList> temp = new ArrayList<SearchList>();
            for (SearchList product : searchArrayList) {
                String name = product.getTitle().toLowerCase();
                String address = product.getImdbID().toLowerCase();
                String mobile = product.getType().toLowerCase();
                if (name.contains(edit_search.getText().toString().trim())
                        || address.contains(edit_search.getText().toString().trim())
                        || mobile.contains(edit_search.getText().toString().trim())) {
                    temp.add(product);
                }
            }
            movieListAdapter.updateList(temp);
        }
    }



}