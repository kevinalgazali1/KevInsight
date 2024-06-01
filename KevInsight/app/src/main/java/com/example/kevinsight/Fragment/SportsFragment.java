package com.example.kevinsight.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SportsFragment extends Fragment {
    String api = "52bc8b20d9ea4442baa28977e2215f14";
    ArrayList<NewsModel> modelArrayList;
    Adapter adapter;
    String country = "in";
    String category = "sports";
    private RecyclerView rvSports;
    LottieAnimationView loading;
    LinearLayout llNoInt;
    TextView tvNoInt;
    ImageView ivNoInt;
    Button btnNoInt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSports = view.findViewById(R.id.rvscports);
        modelArrayList = new ArrayList<>();
        rvSports.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(), modelArrayList);
        rvSports.setAdapter(adapter);
        loading = view.findViewById(R.id.loadingsports);
        llNoInt = view.findViewById(R.id.llNoIntSports);
        tvNoInt = view.findViewById(R.id.tvNoIntSports);
        ivNoInt = view.findViewById(R.id.ivNoIntSports);
        btnNoInt = view.findViewById(R.id.btnNoIntSports);

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

        ApiUtilities.getApiInterface().getCategoryNews(country, category, 100, api).enqueue(new Callback<MainNews>() {
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