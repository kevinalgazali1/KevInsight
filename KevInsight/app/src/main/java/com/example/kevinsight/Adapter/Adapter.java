package com.example.kevinsight.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kevinsight.Model.NewsModel;
import com.example.kevinsight.R;
import com.example.kevinsight.WebViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<NewsModel> modelArrayList;

    public Adapter(Context context, ArrayList<NewsModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layoutitem, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", modelArrayList.get(position).getUrl());
                context.startActivity(intent);
            }
        });

        holder.mheading.setText(modelArrayList.get(position).getTitle());
        holder.mtime.setText("Published At : - "+modelArrayList.get(position).getPublishedAt());
        holder.mauthor.setText(modelArrayList.get(position).getAuthor());
        holder.mcontent.setText(modelArrayList.get(position).getDescription());
        String imageUrl = modelArrayList.get(position).getUrlToImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.ivcontent);
        } else {
            Picasso.get().load(R.drawable.no_img).into(holder.ivcontent);
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mheading, mcontent, mauthor, mtime;
        CardView cardView;
        ImageView ivcontent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mheading = itemView.findViewById(R.id.mainHeading);
            mcontent = itemView.findViewById(R.id.content);
            mauthor = itemView.findViewById(R.id.author);
            mtime = itemView.findViewById(R.id.time);
            ivcontent = itemView.findViewById(R.id.ivContent);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
