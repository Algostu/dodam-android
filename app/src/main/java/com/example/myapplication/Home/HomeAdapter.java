package com.example.myapplication.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Home.dataframe.MyCommunityFrame;
import com.example.myapplication.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.Holder> {
    private ArrayList<MyCommunityFrame> list = new ArrayList<MyCommunityFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public HomeAdapter(Context context, ArrayList<MyCommunityFrame> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public HomeAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_my_community, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.Holder holder, final int position) {
        String community_name = null;
        if (1 == list.get(position).communityID) community_name = "교내 질문";
        else if (2 == list.get(position).communityID) community_name = "교내 자유";
        else if (3 == list.get(position).communityID) community_name = "동네 자유";
        else if (4 == list.get(position).communityID) community_name = "동네 모집";
        else if (5 == list.get(position).communityID) community_name = "동네 질문";
        else if (6 == list.get(position).communityID) community_name = "입시 자유";
        else if (7 == list.get(position).communityID) community_name = "학원 인강";
        else if (8 == list.get(position).communityID) community_name = "아주대학교";

        holder.communityID.setText(community_name);
        holder.title.setText(list.get(position).title);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView communityID;
        protected TextView title;

        public Holder(View view) {
            super(view);
            this.communityID = (TextView) view.findViewById(R.id.communityID);
            this.title = (TextView) view.findViewById(R.id.title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

}