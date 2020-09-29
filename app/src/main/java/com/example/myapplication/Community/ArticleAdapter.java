package com.example.myapplication.Community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Community.dataframe.ArticleListFrame;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.Holder> {
    private ArrayList<ArticleListFrame> list = new ArrayList<ArticleListFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public ArticleAdapter(Context context, ArrayList<ArticleListFrame> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public ArticleAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.content.setText(list.get(position).content);
        holder.writer.setText(list.get(position).nickName);
        holder.time.setText(list.get(position).writtenTime);
        holder.reply.setText(String.valueOf(list.get(position).reply));
        holder.heart.setText(String.valueOf(list.get(position).heart));
        holder.article_ID.setText(String.valueOf(list.get(position).articleId));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        protected TextView writer;
        protected TextView time;
        protected TextView reply;
        protected TextView heart;
        protected TextView article_ID;

        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.writer = (TextView) view.findViewById(R.id.writer);
            this.time = (TextView) view.findViewById(R.id.time);
            this.reply = (TextView) view.findViewById(R.id.reply);
            this.heart = (TextView) view.findViewById(R.id.heart);
            this.article_ID = (TextView) view.findViewById(R.id.article_ID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                    Log.d("Recyclerview", "position = " + getAdapterPosition());
                }
            });
        }
    }

}