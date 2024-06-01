package com.example.kevinsight.Fragment;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.kevinsight.API.ApiUtilities;
import com.example.kevinsight.Adapter.Adapter;
import com.example.kevinsight.Model.MainNews;
import com.example.kevinsight.Model.NewsModel;
import com.example.kevinsight.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    String api = "52bc8b20d9ea4442baa28977e2215f14";
    ArrayList<NewsModel> modelArrayList;
    Adapter adapter;
    String country = "in";
    private RecyclerView rvHome;
    LottieAnimationView loading;
    LinearLayout llNoInt;
    TextView tvNoInt;
    ImageView ivNoInt;
    Button btnNoInt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvHome = view.findViewById(R.id.rvhome);
        modelArrayList = new ArrayList<>();
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), modelArrayList);
        rvHome.setAdapter(adapter);
        loading = view.findViewById(R.id.loadinghome);
        llNoInt = view.findViewById(R.id.llNoIntHome);
        tvNoInt = view.findViewById(R.id.tvNoIntHome);
        ivNoInt = view.findViewById(R.id.ivNoIntHome);
        btnNoInt = view.findViewById(R.id.btnNoIntHome);

        loading.setVisibility(View.VISIBLE);

        btnNoInt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                llNoInt.setVisibility(View.GONE);
                findNews();
            }
        });

        findNews();
    }

    private void findNews() {

        ApiUtilities.getApiInterface().getNews(country,100,api).enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    modelArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                } else {
                    llNoInt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                loading.setVisibility(View.GONE);
                llNoInt.setVisibility(View.VISIBLE);
            }
        });
    }
}