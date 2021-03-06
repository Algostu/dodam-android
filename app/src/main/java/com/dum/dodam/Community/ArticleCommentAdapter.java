package com.dum.dodam.Community;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.ArticleCommentFrame;
import com.dum.dodam.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ArticleCommentFrame> list = new ArrayList<ArticleCommentFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    private OnListItemSelectedInterface2 mListener2;

    public ArticleCommentAdapter(Context context, ArrayList<ArticleCommentFrame> list, OnListItemSelectedInterface listener, OnListItemSelectedInterface2 listener2) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.mListener2 = listener2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_comment_card, parent, false);
                return new CommentViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_recomment_card, parent, false);
                return new RecommentViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_comment_card, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (list.get(position).parentReplyID == 0) {
            CommentViewHolder holder1 = (CommentViewHolder) holder;
            holder1.nickName.setText(list.get(position).nickName);
            holder1.writtenTime.setText(list.get(position).writtenTime);
            holder1.content.setText(list.get(position).content);
            holder1.itemView.setTag(position);
            holder1.replyID = list.get(position).replyID;
            holder1.parentReplyID = list.get(position).parentReplyID;
            holder1.edit = list.get(position).edit;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date wriDate;
                wriDate = format.parse(list.get(position).writtenTime);
                TIME_MAXIMUM timeDiff = new TIME_MAXIMUM();
                String diffStr = timeDiff.calculateTime(wriDate);
                holder1.writtenTime.setText(diffStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            RecommentViewHolder holder2 = (RecommentViewHolder) holder;
            holder2.nickName.setText(list.get(position).nickName);
            holder2.writtenTime.setText(list.get(position).writtenTime);
            holder2.content.setText(list.get(position).content);
            holder2.itemView.setTag(position);
            holder2.replyID = list.get(position).replyID;
            holder2.parentReplyID = list.get(position).parentReplyID;

            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date wriDate;
                wriDate = format.parse(list.get(position).writtenTime);
                TIME_MAXIMUM timeDiff = new TIME_MAXIMUM();
                String diffStr = timeDiff.calculateTime(wriDate);
                holder2.writtenTime.setText(diffStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("debug", "position" + position);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView nickName;
        protected TextView writtenTime;
        protected TextView content;
        protected int replyID;
        protected int parentReplyID;
        protected boolean edit;
        protected ImageView im_comment;
        protected ImageView im_menu;
        protected ConstraintLayout layout;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            writtenTime = itemView.findViewById(R.id.writtenTime);
            content = itemView.findViewById(R.id.content);
            im_comment = itemView.findViewById(R.id.im_comment);
            im_menu = itemView.findViewById(R.id.im_menu);
            layout = itemView.findViewById(R.id.comment_bg);

            im_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });

            im_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener2.onItemSelected2(view, getAdapterPosition());
                }
            });
        }
    }

    public class RecommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView nickName;
        protected TextView writtenTime;
        protected TextView content;
        protected int replyID;
        protected int parentReplyID;

        public RecommentViewHolder(@NonNull View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nickName);
            writtenTime = itemView.findViewById(R.id.writtenTime);
            content = itemView.findViewById(R.id.content);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mListener.onItemSelected(v, getAdapterPosition());
//                }
//            });
        }
    }

    public int getItemViewType(int position) {
        if (list.get(position).parentReplyID == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface2 {
        void onItemSelected2(View v, int position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}