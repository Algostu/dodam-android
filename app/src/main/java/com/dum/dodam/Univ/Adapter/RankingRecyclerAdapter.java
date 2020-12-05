package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.MajorFrame;
import com.dum.dodam.Univ.dataframe.UnivFrame;

import java.util.ArrayList;

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerAdapter.Holder> {
    private ArrayList<UnivFrame> univList = new ArrayList<>();
    private ArrayList<MajorFrame> majorList = new ArrayList<>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    private int type;

    public RankingRecyclerAdapter(Context context, ArrayList<UnivFrame> univList, ArrayList<MajorFrame> majorList, int type, OnListItemSelectedInterface listener) {
        this.context = context;
        this.univList = univList;
        this.majorList = majorList;
        this.mListener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_ranking_page_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return (null != univList ? univList.size() : 0);
        } else {
            return (null != majorList ? majorList.size() : 0);
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView tv_rank;
        protected TextView tv_title;
        protected ImageView iv_logo;
        protected TextView tv_feature;

        public Holder(View view) {
            super(view);
            this.tv_rank = view.findViewById(R.id.tv_rank);
            this.tv_title = view.findViewById(R.id.tv_title);
            this.iv_logo = view.findViewById(R.id.iv_logo);
            this.tv_feature = view.findViewById(R.id.tv_feature);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1) {
                        mListener.onItemSelected1(v, univList.get(getAbsoluteAdapterPosition()));
                    } else {
                        mListener.onItemSelected2(v, majorList.get(getAbsoluteAdapterPosition()));
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RankingRecyclerAdapter.Holder holder, final int position) {

        holder.tv_rank.setText(String.valueOf(position + 1));

        if (type == 1) {
            holder.tv_feature.setVisibility(View.GONE);
            holder.tv_title.setText(univList.get(position).univName);

        } else {
            holder.iv_logo.setVisibility(View.GONE);
            holder.tv_title.setText(majorList.get(position).mClass);

            MajorFrame frame = majorList.get(position);
            holder.tv_feature.setText(String.format("%s / %s / %s", frame.employment_rate, frame.gender, frame.avg_salary));
        }

        holder.itemView.setTag(position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected1(View v, UnivFrame univFrame);

        void onItemSelected2(View v, MajorFrame majorFrame);
    }
}