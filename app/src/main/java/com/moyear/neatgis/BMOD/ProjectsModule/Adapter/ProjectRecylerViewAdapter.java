package com.moyear.neatgis.BMOD.ProjectsModule.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moyear.neatgis.BMOD.ProjectsModule.Model.ProjectInfo;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.FileUtils;

import java.util.List;

public class ProjectRecylerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String PROJECT_IMAGE_NAME_JPG = "image.jpg";
    private static String PROJECT_IMAGE_NAME_PNG = "image.png";

    Context context;
    private List<ProjectInfo> projectInfos;

    private OnItemClickListener onRecyclerViewItemClickListener;//点击事件接口

    public static class OnItemClickListener {
        public void onClick(int position) {

        }

        public void onLongClick(int position) {

        }
    }

    public ProjectRecylerViewAdapter(Context context,List<ProjectInfo> list) {

        this.context = context;
        this.projectInfos = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_gridview_item, parent, false);

        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        GridHolder gridHolder = (GridHolder) holder;

        //设置缩略图信息
        String picPath = projectInfos.get(position).getDirPath() + "/" +PROJECT_IMAGE_NAME_JPG;
        boolean isExist = FileUtils.isExist(picPath);
        if (isExist) {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            gridHolder.imageView.setImageBitmap(bitmap);
        }
        String pngPath = projectInfos.get(position).getDirPath() + "/" +PROJECT_IMAGE_NAME_PNG;
        boolean isExistPNG = FileUtils.isExist(pngPath);
        if (isExistPNG) {
            Bitmap bitmap = BitmapFactory.decodeFile(pngPath);
            gridHolder.imageView.setImageBitmap(bitmap);
        }

        final ProjectInfo projectInfo = projectInfos.get(position);
        gridHolder.txtTitle.setText(projectInfo.getDirName());

        gridHolder.txtRemark.setText("存储路径："+projectInfo.getDirPath());

        gridHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onRecyclerViewItemClickListener.onClick(position);
            }
        });

        gridHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                onRecyclerViewItemClickListener.onLongClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (projectInfos == null)
            return -1;
        return projectInfos.size();
    }

    public void setAdapterList(List<ProjectInfo> projectInfos) {
        this.projectInfos = projectInfos;
    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        notifyDataSetChanged();//刷新数据
    }

    class GridHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtRemark;
        ImageView imageView;
        View itemView;

        GridHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            txtTitle = itemView.findViewById(R.id.activity_main_gridview_item_txtName);
            imageView = itemView.findViewById(R.id.activity_main_gridview_item_img);
            txtRemark = (TextView)itemView.findViewById(R.id.activity_main_gridview_item_txtRemark);

            //img.setAdjustViewBounds(true);
            //img.setMaxWidth(BITMAP_MAX_SIZE);
            //img.setMaxHeight(BITMAP_MAX_SIZE);
        }

    }


    /**
     *设置点击事件
     *
     * @param onItemClickListener
     */
    public void setOnRecyclerViewItemClickListener(OnItemClickListener onItemClickListener) {
        this.onRecyclerViewItemClickListener = onItemClickListener;
    }
}
