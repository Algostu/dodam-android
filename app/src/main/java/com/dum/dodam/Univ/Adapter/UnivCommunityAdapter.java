package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.TIME_MAXIMUM;
import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.UnivArticleFrame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnivCommunityAdapter extends RecyclerView.Adapter<UnivCommunityAdapter.Holder> {
    private ArrayList<UnivArticleFrame> list = new ArrayList<UnivArticleFrame>();
    private Context context;
    private UnivCommunityAdapter.OnListItemSelectedInterface mListener;

    public UnivCommunityAdapter(Context context, ArrayList<UnivArticleFrame> list, UnivCommunityAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public UnivCommunityAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
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
        public TextView articleID;

        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.writer = (TextView) view.findViewById(R.id.writer);
            this.time = (TextView) view.findViewById(R.id.time);
            this.reply = (TextView) view.findViewById(R.id.reply);
            this.heart = (TextView) view.findViewById(R.id.heart);
            this.articleID = (TextView) view.findViewById(R.id.article_ID);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UnivCommunityAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.content.setText(list.get(position).content);
        holder.writer.setText(list.get(position).nickName);
        holder.time.setText(list.get(position).writtenTime);
        holder.reply.setText(String.valueOf(list.get(position).reply));
        holder.heart.setText(String.valueOf(list.get(position).heart));
        holder.articleID.setText(String.valueOf(list.get(position).articleID));
        holder.itemView.setTag(position);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date wriDate;
            wriDate = format.parse(list.get(position).writtenTime);
            TIME_MAXIMUM timeDiff = new TIME_MAXIMUM();
            String diffStr = timeDiff.calculateTime(wriDate);
            holder.time.setText(diffStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
}