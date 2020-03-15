package com.moyear.neatgis.Widgets.QueryWidget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.DialogUtils;
import com.moyear.neatgis.Widgets.QueryWidget.Bean.KeyAndValueBean;

import java.util.List;

/**
 * 属性列表适配器
 * Created by gis-luq on 2017/5/5.
 * edited by moyear on 2020.03.06
 */
public class AttributeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<KeyAndValueBean> keyAndValueBeans;

    public AttributeAdapter(Context c, List<KeyAndValueBean> keyAndValueBeans) {
        this.context = c;
        this.keyAndValueBeans = keyAndValueBeans;
    }


    /**
     * 刷新数据
     */
    public void refreshData() {
        try {
            notifyDataSetChanged();//刷新数据
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 刷新数据
     * @param keyAndValueBeans
     */
    public void refreshData(List<KeyAndValueBean> keyAndValueBeans) {
        if (keyAndValueBeans == null)
            return;

        this.keyAndValueBeans.clear();
        this.keyAndValueBeans.addAll(keyAndValueBeans);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.widget_view_query_mapquery_fielditem, null);

        return new AttributeItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        AttributeItemHolder holder = (AttributeItemHolder) viewHolder;

        holder.txtName.setText(keyAndValueBeans.get(position).getKey());
        holder.txtValue.setText(keyAndValueBeans.get(position).getValue());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = keyAndValueBeans.get(position).getKey()+":"+ keyAndValueBeans.get(position).getValue();
                DialogUtils.showDialog(context,"字段属性",msg);
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (keyAndValueBeans == null) {
            return 0;
        } else {
            return keyAndValueBeans.size();
        }
    }


    class AttributeItemHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView txtName;
        TextView txtValue;

        AttributeItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            txtName = itemView.findViewById(R.id.widget_view_query_mapquery_fielditem_txtName);
            txtValue = itemView.findViewById(R.id.widget_view_query_mapquery_fielditem_txtValue);

        }

    }
}
