package com.dum.dodam.Scheduler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Scheduler.dataframe.Todo;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.Holder> {
    private ArrayList<Todo> list = new ArrayList<>();
    private TodoListAdapter.OnListItemSelectedInterface mListener;
    private Context context;

    public TodoListAdapter(Context context, ArrayList<Todo> list, TodoListAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public TodoListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_todo, parent, false);
        TodoListAdapter.Holder holder = new TodoListAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        final protected TextView todo_content;
        final protected ImageView ic_remove;
        final protected CheckBox todo_done;

        public Holder(View view) {
            super(view);
            this.todo_content = (TextView) view.findViewById(R.id.todo_content);
            this.ic_remove = (ImageView) view.findViewById(R.id.ic_remove);
            this.todo_done = (CheckBox) view.findViewById(R.id.todo_done);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.Holder holder, final int position) {
        holder.todo_content.setText(list.get(position).content);
        holder.todo_done.setChecked(list.get(position).done);
        if(list.get(position).visible){
            holder.ic_remove.setVisibility(View.VISIBLE);
        } else {
            holder.ic_remove.setVisibility(View.GONE);
        }
        final TodoListAdapter.Holder orgHolder = holder;
        holder.todo_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    orgHolder.todo_content.setTextColor(Color.GRAY);
                    orgHolder.todo_content.setTypeface(null, Typeface.ITALIC);
                    orgHolder.todo_content.setPaintFlags(orgHolder.todo_content.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    orgHolder.todo_content.setTextColor(Color.BLACK);
                    orgHolder.todo_content.setTypeface(null, Typeface.NORMAL);
                    orgHolder.todo_content.setPaintFlags(0);
                }
            }
        });

        holder.ic_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        });
    }
}