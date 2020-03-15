package com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.data.Field;
import com.moyear.neatgis.R;

import java.util.List;

public class LayerFieldsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;

    private List<Field> mListFields;

    public LayerFieldsListAdapter(Context context,List<Field> listFileds) {

        this.mContext = context;
        this.mListFields = listFileds;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer_attr_field, parent, false);

        return new FieldItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Field field = mListFields.get(position);

        FieldItemHolder filedItemHolder = (FieldItemHolder) holder;

        if (!field.isNullable()) {
            filedItemHolder.txtFiledMust.setVisibility(View.VISIBLE);
        }

        filedItemHolder.txtFiledName.setText(field.getName());

        filedItemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showFieldInfo(field);
                return false;
            }
        });

        filedItemHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pm = new PopupMenu(mContext, v);
                pm.getMenuInflater().inflate(R.menu.menu_dialog_attr_filed_item, pm.getMenu());
                if (!field.isEditable())
                    pm.getMenu().findItem(R.id.menu_dialog_layer_attr_filed_item_remove).setEnabled(false);
                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_dialog_layer_attr_filed_item_view://查看字段详细信息
                                showFieldInfo(field);
                                break;
                            case R.id.menu_dialog_layer_attr_filed_item_export:
                                //TODO：将字段导出为json文件
                                Toast.makeText(mContext, field.toJson(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_dialog_layer_attr_filed_item_remove:
                                Toast.makeText(mContext, "移除，代码待写!!!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                pm.show();
            }

        });


    }

    @Override
    public int getItemCount() {
        if (mListFields == null)
            return -1;
        return mListFields.size();
    }


    class FieldItemHolder extends RecyclerView.ViewHolder {

        View itemView;

        TextView txtFiledMust;
        TextView txtFiledName;
        ImageButton btnMore;

        FieldItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            txtFiledMust = (TextView)itemView.findViewById(R.id.txt_layer_attr_field_item_necessary);
            txtFiledName = itemView.findViewById(R.id.txt_layer_attr_field_item_name);
            btnMore = itemView.findViewById(R.id.btn_layer_attr_field_item_more);
        }

    }

    private void showFieldInfo(Field field) {
        AppCompatActivity activity = (AppCompatActivity)mContext;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_layer_attr_field_info,null);

        EditText edtFieldName = view.findViewById(R.id.edt_layer_attr_field_name);
        EditText edtFieldAliasName = view.findViewById(R.id.edt_layer_attr_field_alias_name);
        EditText edtFieldValueType = view.findViewById(R.id.edt_layer_attr_field_value_type);
        AppCompatCheckBox ckFieldMust = view.findViewById(R.id.cb_layer_attr_field_is_necessary);
        ImageButton btnClose = view.findViewById(R.id.btn_layer_attr_field_close);

        edtFieldName.setText(field.getName());
        edtFieldAliasName.setText(field.getAlias());
        edtFieldAliasName.setText(field.getAlias());
        edtFieldValueType.setText(field.getFieldType().name());
        if (!field.isNullable())
            ckFieldMust.setChecked(true);

        if (!field.isEditable()) {
            edtFieldName.setEnabled(false);
            edtFieldAliasName.setEnabled(false);
            edtFieldValueType.setEnabled(false);
            ckFieldMust.setEnabled(false);
        }

        builder.setView(view);
        if (field.isEditable()) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        builder.setNegativeButton("取消",null);

        //解决AlertDialog无dismiss问题
        final AlertDialog dialog = builder.show();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
