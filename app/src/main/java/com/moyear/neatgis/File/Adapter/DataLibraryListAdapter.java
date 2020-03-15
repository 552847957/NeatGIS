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

import com.moyear.neatgis.BMOD.ProjectsModule.Model.DataFileInfo;
import com.moyear.neatgis.Config.SystemDirPath;
import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.FileUtils;
import com.moyear.neatgis.Utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gisluq.lib.Util.ToastUtils;

public class DataLibraryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = "DataLibraryListAdapter";

    private final static int VIEW_TYPE_EMPTY = 0;//空布局视图视图

    private final static int VIEW_TYPE_DATABASE = 2;//数据库视图

    private final static int VIEW_TYPE_DATAFILE = 3;//数据文件视图

    private FileSelectDialog fileSelectDialog;

    private Context context;

    private static String currentPath;//当前所在的目录

    private static String rootPath;

    private List<DataFileInfo> mDataFile;

    public DataLibraryListAdapter(Context context, List<DataFileInfo> dataLibraries, FileSelectDialog fileSelectDialog) {
        this.context = context;
        this.mDataFile = dataLibraries;

        this.fileSelectDialog = fileSelectDialog;

        this.rootPath = SystemDirPath.getDataPaths(context);//数据储存库根目录
        this.currentPath = rootPath;//默认将当前路径设为数据储存库根目录

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file_info, parent, false);

        return new LibraryItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        LibraryItemHolder libraryItemHolder = (LibraryItemHolder) viewHolder;

        DataFileInfo dataLibraryInfo = mDataFile.get(position);

        libraryItemHolder.txtDBName.setText(dataLibraryInfo.getDbName());
        libraryItemHolder.txtDBEditTime.setText(dataLibraryInfo.getDbLastModifiedTime());

        libraryItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dbPath = dataLibraryInfo.getDbPath();

                if (new File(dbPath).isDirectory()) {
                    refreshData(getDataFiles(dbPath));

                    currentPath = dbPath;//将点击的文件夹路径设为当前路径

                } else {
                    //TODO:选择的是文件，返回文件路径
                    //ToastUtils.showShort(context, "选择的是：" + dbPath);

                    fileSelectDialog.getOnSingleFileSelectListener().onSelectedFile(dbPath);
                    fileSelectDialog.dismiss();
                }

            }
        });

        if (dataLibraryInfo.isDataBaseDir()) {
            libraryItemHolder.imgDBIcon.setImageBitmap(ViewUtils.getBitmap(context, R.drawable.ic_database));

        } else {
            File file = new File(dataLibraryInfo.getDbPath());

            if (file.exists()) {

                if (file.isDirectory())
                    libraryItemHolder.imgDBIcon.setImageBitmap(ViewUtils.getBitmap(context, R.drawable.ic_folder_black));
                else
                    libraryItemHolder.imgDBIcon.setImageBitmap(ViewUtils.getBitmap(context, R.drawable.ic_file_unknown));

            }
        }

    }


    @Override
    public int getItemCount() {
        return mDataFile.size();
    }

    class LibraryItemHolder extends RecyclerView.ViewHolder {

        View itemView;

        ImageView imgDBIcon;
        TextView txtDBName;
        TextView txtDBEditTime;
        TextView txtDBSize;
        ImageButton btnMore;

        LibraryItemHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;
            imgDBIcon = itemView.findViewById(R.id.img_file_icon);
            txtDBName = itemView.findViewById(R.id.txt_file_name);
            txtDBEditTime = itemView.findViewById(R.id.txt_file_edit_time);
            txtDBSize = itemView.findViewById(R.id.txt_file_size);
            btnMore = itemView.findViewById(R.id.btn_more);
        }

    }


    /**
     * 根据所给列表刷新数据
     * @param dataFileInfoList
     */
    public void refreshData(List<DataFileInfo> dataFileInfoList) {

        mDataFile.clear();
        mDataFile.addAll(dataFileInfoList);

        notifyDataSetChanged();
    }

    /**
     *根据所给的路径来自动获取文件以刷新
     *
     * @param targetPath
     */
    public void refreshDataByPath(String targetPath) {

        refreshData(getDataFiles(targetPath));

        currentPath = targetPath;//将点击的文件夹路径设为当前路径

    }


    /**
     *
     *根据路径获取下面的数据文件信息列表
     * @param path
     * @return
     */
    private List<DataFileInfo> getDataFiles(String path) {

        List<DataFileInfo> dataFileList = new ArrayList<>();

        if (path == null)
            return dataFileList;

        File dbDir = new File(path);

        if (dbDir.exists()) {
            File [] files = dbDir.listFiles();

            if (files == null) {
                return dataFileList;
            }

            for (File file : files) {
                DataFileInfo dataFileInfo = new DataFileInfo();

                if (path.equals(rootPath)) {
                    dataFileInfo.setDataBaseDir(true);
                } else {
                    dataFileInfo.setDataBaseDir(false);
                }

                dataFileInfo.setDbName(file.getName());
                dataFileInfo.setDbLastModifiedTime(FileUtils.getLastModifiedTime(file));
                dataFileInfo.setDbPath(file.getPath());

                dataFileList.add(dataFileInfo);
            }
        } else {
            Log.d(TAG, dbDir.getAbsolutePath() + "不存在");
        }

        return dataFileList;

    }


    /**
     * 根据当前所在目录返回上一级
     * @param context
     */
    public void backToParentPath(Context context) {

        if (currentPath == null) {
            Log.e(TAG, "Current Path is null");
            return;
        }

        if (currentPath.equals(rootPath)) {
            ToastUtils.showShort(context, "当前目录已到根目录，已无法返回上一级");
        } else {
            File currentDir = new File(currentPath);

            if (currentDir.exists()) {
                String parentPath = currentDir.getParent();

                if (parentPath != null)
                    refreshDataByPath(parentPath);

            }
        }

    }


}
