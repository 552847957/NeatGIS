package com.moyear.neatgis.Common.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyear.neatgis.R;

import java.util.List;

public class CommonSpinnerAdapter extends BaseAdapter {

    Context context;

    List<String> mSpinnerList;

    public CommonSpinnerAdapter(Context context, List<String> spinnerList) {
        this.context = context;
        this.mSpinnerList = spinnerList;

    }

    @Override
    public int getCount() {
        return mSpinnerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView =  LayoutInflater.from(context).inflate(R.layout.item_spinner_simple, null);

        AdapterHolder adapterHolder = new AdapterHolder();
        adapterHolder.txtName = itemView.findViewById(R.id.txt_item_spinner_simple_item_name);
        adapterHolder.txtName.setText(mSpinnerList.get(position));

        return itemView;
    }


    public class AdapterHolder {//列表绑定项
//        public ImageView imageView;
        public TextView txtName;//列表名


    }
}
