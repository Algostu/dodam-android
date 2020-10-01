package com.example.myapplication.Login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.Login.Data.School;
import com.example.myapplication.R;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private List<School> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<School> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.row_list,null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);
            viewHolder.loc = (TextView) convertView.findViewById(R.id.loc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(position).schoolName);
        viewHolder.loc.setText(list.get(position).getRegion());

        return convertView;
    }

    class ViewHolder{
        public TextView label;
        public TextView loc;
    }

}
