package com.example.kevinsight.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kevinsight.API.ApiUtilities;
import com.example.kevinsight.Adapter.Adapter;
import com.example.kevinsight.Model.MainNews;
import com.example.kevinsight.Model.NewsModel;
import com.example.kevinsight.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    String api = "52bc8b20d9ea4442baa28977e2215f14";
    ArrayList<NewsModel> allNews = new ArrayList<>();
    ArrayList<NewsModel> filteredNews = new ArrayList<>();
    Adapter adapter;
    String country = "in";
    private RecyclerView rvNews;
    private SearchView searchView;
    private TextView tvNoResults;
    LottieAnimationView loading;
    LinearLayout llNoInt;
    TextView tvNoInt;
    ImageView ivNoInt;
    Button btnNoInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvNews = view.findViewById(R.id.rvsearch);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        searchView = view.findViewById(R.id.search_news);
        loading = view.findViewById(R.id.loadingsearch);
        llNoInt = view.findViewById(R.id.llNoIntSearch);
        tvNoInt = view.findViewById(R.id.tvNoIntSearch);
        ivNoInt = view.findViewById(R.id.ivNoIntSearch);
        btnNoInt = view.findViewById(R.id.btnNoIntSearch);

        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), filteredNews);
        rvNews.setAdapter(adapter);

        loading.setVisibility(View.VISIBLE);

        btnNoInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                llNoInt.setVisibility(View.GONE);
                loadAllNews();
            }
        });

        loadAllNews();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNews(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    rvNews.setVisibility(View.GONE);
                } else {
                    rvNews.setVisibility(View.VISIBLE);
                    filterNews(newText);
                }
                return true;
            }
        });
    }

    private void loadAllNews() {
        ApiUtilities.getApiInterface().getNews(country, 100, api).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    allNews.addAll(response.body().getArticles());
                    filteredNews.addAll(allNews);
                    adapter.notifyDataSetChanged();
                    searchView.setEnabled(true);
                    tvNoResults.setVisibility(View.GONE);
                    searchView.setVisibility(View.VISIBLE);
                } else {
                    llNoInt.setVisibility(View.VISIBLE);
                    searchView.setEnabled(false);
                    tvNoResults.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                loading.setVisibility(View.GONE);
                llNoInt.setVisibility(View.VISIBLE);
                searchView.setEnabled(false);
                tvNoResults.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
            }
        });
    }


    private void filterNews(String query) {
        if (TextUtils.isEmpty(query)) {
            filteredNews.clear();
            filteredNews.addAll(allNews);
            adapter.notifyDataSetChanged();
            checkIfNewsEmpty();
        } else {
            filteredNews.clear();
            for (NewsModel news : allNews) {
                if (news.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredNews.add(news);
                }
            }
            adapter.notifyDataSetChanged();
            checkIfNewsEmpty();
        }
    }

    private void checkIfNewsEmpty() {
        if (filteredNews.isEmpty()) {
            rvNews.setVisibility(View.GONE);
            tvNoResults.setVisibility(View.VISIBLE);
        } else {
            rvNews.setVisibility(View.VISIBLE);
            tvNoResults.setVisibility(View.GONE);
        }
    }
}
