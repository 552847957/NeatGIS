package com.moyear.neatgis.File.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.FileUtils;
import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.ViewUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import gisluq.lib.Util.ToastUtils;

import static com.moyear.neatgis.File.FileSelectDialog.SELECTED_MODE_SINGLE_FILE;
import static com.moyear.neatgis.File.FileSelectDialog.SELECTED_MODE_SINGLE_FOLDER;

public class FileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private int selectedMode = -1;//文件选择模式

    private List<File> mFileList;

    private String currentPath;

    private FileSelectDialog fileSelectDialog;

    public  FileListAdapter(Context context, FileSelectDialog fileSelectDialog, String currentPath) {
        this.mContext = context;
        this.currentPath = currentPath;
        this.fileSelectDialog = fileSelectDialog;
        this.selectedMode = fileSelectDialog.getSelectedMode();
        this.mFileList = getFileListByPath(currentPath);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_info, parent, false);

        return new FileItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        File file = mFileList.get(position);
        FileItemHolder fileItemHolder = (FileItemHolder) holder;

        fileItemHolder.txtFileName.setText(file.getName());
        fileItemHolder.txtFileEditTime.setText(com.moyear.neatgis.Utils.FileUtils.getLastModifiedTime(file));

        switch (selectedMode) {
            case SELECTED_MODE_SINGLE_FILE:
                singleFileSelect(holder, position);
                break;
            case SELECTED_MODE_SINGLE_FOLDER:
                singleFolerSelect(holder, position);
                break;
        }

    }

    //文件夹单选模式下的布局
    private void singleFolerSelect(RecyclerView.ViewHolder holder, int position) {
        File file = mFileList.get(position);
        FileItemHolder fileItemHolder = (FileItemHolder) holder;

        if (file.isDirectory()) {
            fileItemHolder.imgFileIcon.setImageBitmap(ViewUtils.getBitmap(mContext, R.drawable.ic_folder_black));
            fileItemHolder.txtFileSize.setVisibility(View.GONE);

            fileItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPath = file.getPath();
                    refreshListViewByPath(currentPath);
                }
            });

            fileItemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtils.showShort(mContext,"文件夹长按");
                    return false;
                }
            });

        } else {
            fileItemHolder.itemView.setVisibility(View.GONE);
        }

    }

    //文件单选模式下的布局
    private void singleFileSelect(RecyclerView.ViewHolder holder, int position) {
        File file = mFileList.get(position);
        FileItemHolder fileItemHolder = (FileItemHolder) holder;

        if (file.isDirectory()) {
            fileItemHolder.imgFileIcon.setImageBitmap(ViewUtils.getBitmap(mContext, R.drawable.ic_folder_black));
            fileItemHolder.txtFileSize.setVisibility(View.GONE);

            fileItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPath = file.getPath();
                    refreshListViewByPath(currentPath);
                }
            });

            fileItemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtils.showShort(mContext,"文件夹长按");
                    return false;
                }
            });
        } else if (file.isFile()){
            fileItemHolder.imgFileIcon.setImageBitmap(ViewUtils.getBitmap(mContext, R.drawable.ic_file_unknown));
            fileItemHolder.txtFileSize.setVisibility(View.VISIBLE);
            fileItemHolder.txtFileSize.setText(FileUtils.getFileSize(file));

            fileItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileSelectDialog.getOnSingleFileSelectListener().onSelectedFile(file.getPath());
                    fileSelectDialog.dismiss();
                }
            });

            fileItemHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ToastUtils.showShort(mContext,"文件长按");
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mFileList == null)
            return -1;
        return mFileList.size();
    }

    /**
     *根据path获取path下的File集合
     * （可只选择文件夹）
     *
     * @param currentPath
     * @return
     */
    private List<File> getFileListByPath(String currentPath) {
        List<File> fileList = new ArrayList<>();

        if (new File(currentPath).exists()) {
            switch (selectedMode) {
                case SELECTED_MODE_SINGLE_FOLDER://文件夹选择模式，只显示文件夹
                    File [] tempfiles = new File(currentPath).listFiles();
                    fileList = com.moyear.neatgis.Utils.FileUtils.listFolders(tempfiles);
                    break;
                default:
                    fileList = com.moyear.neatgis.Utils.FileUtils.fileList2ArrayList(new File(currentPath).listFiles());
            }
        } else {
            Log.e("PathError","Current Path:" + currentPath + " do not exist");
        }

        return fileList;
    }

    /**
     *根据path获取path下的文件列表并刷新RecylerView列表
     *
     * @param path
     */
    public void refreshListViewByPath(String path) {
        if (new File(path).exists()) {
            currentPath = path;
            this.mFileList = getFileListByPath(path);
            notifyDataSetChanged();
        }
    }


//    public void setCurrentPath(String currentPath) {
//        this.currentPath = currentPath;
//    }

    public String getCurrentPath() {
        return currentPath;
    }

    class FileItemHolder extends RecyclerView.ViewHolder {

        View itemView;

        ImageView imgFileIcon;
        TextView txtFileName;
        TextView txtFileEditTime;
        TextView txtFileSize;
        ImageButton btnMore;

        FileItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            imgFileIcon = itemView.findViewById(R.id.img_file_icon);
            txtFileName = itemView.findViewById(R.id.txt_file_name);
            txtFileEditTime = itemView.findViewById(R.id.txt_file_edit_time);
            txtFileSize = itemView.findViewById(R.id.txt_file_size);
            btnMore = itemView.findViewById(R.id.btn_more);
        }

    }
}
