package com.moyear.neatgis.File.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.moyear.neatgis.BMOD.ProjectsModule.Model.DataFileInfo;
import com.moyear.neatgis.Config.SystemDirPath;
import com.moyear.neatgis.File.Adapter.DataLibraryListAdapter;
import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.FileUtils;
import com.moyear.neatgis.Views.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LibraryFileChooseFragment extends Fragment {

    private String TAG = "LibraryFileChooseFragment";

    private FileSelectDialog fileSelectDialog;

    private EmptyRecyclerView rvLibrary;

    private List<DataFileInfo> mDataBaseList;

    private LinearLayout lvBackToParent;

    public LibraryFileChooseFragment(FileSelectDialog fileSelectDialog) {

        this.fileSelectDialog = fileSelectDialog;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filechoose_from_library, container, false);

        bindView(rootView);
        initView(rootView);

        return rootView;
    }

    private void bindView(View rootView) {
        rvLibrary = rootView.findViewById(R.id.rl_file_choose_from_library);
        lvBackToParent = rootView.findViewById(R.id.lv_file_back_to_parent);

    }

    private void initView(View rootView) {
        mDataBaseList = getDataBaseList();

        DataLibraryListAdapter dataLibraryListAdapter = new DataLibraryListAdapter(getContext(), mDataBaseList, fileSelectDialog);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLibrary.setLayoutManager(manager);
        rvLibrary.setAdapter(dataLibraryListAdapter);

        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view_no_list, null, false);
        rvLibrary.setEmptyView(emptyView);

        lvBackToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataLibraryListAdapter.backToParentPath(getContext());
            }
        });

    }


    private List<DataFileInfo> getDataBaseList() {
        List<DataFileInfo> dbInfoList = new ArrayList<>();

        String dataBasePath = SystemDirPath.getDataPaths(getContext());
        File dbFileDir = new File(dataBasePath);

        if (!dbFileDir.exists()) {
            dbFileDir.mkdirs();
            Log.d(TAG,"未找到路径，故创建" + dbFileDir.getAbsolutePath());

            return dbInfoList;
        }

        File [] dbDirs = dbFileDir.listFiles();

        for (File database : dbDirs) {

            if (database.isDirectory()) {
                DataFileInfo dbInfo = new DataFileInfo();
                dbInfo.setDataBaseDir(true);//数据库文件夹类型
                dbInfo.setDbName(database.getName());
                dbInfo.setDbPath(database.getPath());
                dbInfo.setOwner("拥有者代写");
                dbInfo.setDbLastModifiedTime(FileUtils.getLastModifiedTime(database));

                dbInfoList.add(dbInfo);

            }

        }

        return dbInfoList;

    }

}
