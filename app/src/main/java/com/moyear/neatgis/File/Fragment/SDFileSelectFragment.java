package com.moyear.neatgis.File.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moyear.neatgis.File.Adapter.FileListAdapter;
import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.FileUtils;

import java.io.File;

import gisluq.lib.Util.ToastUtils;

import static com.moyear.neatgis.File.FileSelectDialog.SELECTED_MODE_SINGLE_FOLDER;

public class SDFileSelectFragment extends Fragment {

    private FileSelectDialog fileSelectDialog;

    private int selectMode;

    private String currentPath;

    private RecyclerView rlSdFileList;

    private LinearLayout lvBackToParent;

    private FileListAdapter fileListAdapter;

    private FloatingActionButton fabOk;


    public SDFileSelectFragment(FileSelectDialog fileSelectDialog) {
        this.selectMode = fileSelectDialog.getSelectedMode();
        this.fileSelectDialog = fileSelectDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_filechoose_from_sd, container, false);

        bindView(rootView);
        initView();

        return rootView;
    }

    private void bindView(View rootView) {
        rlSdFileList = rootView.findViewById(R.id.rl_file_choose_from_sd);
        lvBackToParent = rootView.findViewById(R.id.lv_file_back_to_parent);

        fabOk = rootView.findViewById(R.id.fab_ok);

    }

    private void initView() {
        currentPath = FileUtils.getSdCardPath();
        File rootDirFile = new File(currentPath);//内部储存根目录

        if (!rootDirFile.exists()) {
            Log.e("NullFile","内部储存根目录获取失败");
            return;
        }

        fileListAdapter = new FileListAdapter(getContext(), fileSelectDialog, rootDirFile.getPath());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rlSdFileList.setLayoutManager(manager);
        rlSdFileList.setAdapter(fileListAdapter);

        lvBackToParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fileListAdapter.getCurrentPath() != null) {

                    if (fileListAdapter.getCurrentPath().equals(FileUtils.getSdCardPath())) {
                        ToastUtils.showShort(getContext(),"当前目录下无法返回上一级");
                        return;
                    }

                    File parentFile = new File(fileListAdapter.getCurrentPath()).getParentFile();

                    if (parentFile != null && parentFile.exists()) {
//                        ToastUtils.showShort(getContext(),parentFile.getPath());
                        fileListAdapter.refreshListViewByPath(parentFile.getPath());
                    } else {
                        ToastUtils.showShort(getContext(),"无父目录，或者父目录不可用");
                    }

                } else {
                    ToastUtils.showShort(getContext(),"文件列表为null");
                }

            }
        });

        if (selectMode == SELECTED_MODE_SINGLE_FOLDER)
            fabOk.setVisibility(View.VISIBLE);

        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO:运行效果正常，但代码逻辑待修改
                currentPath = fileListAdapter.getCurrentPath();
                fileSelectDialog.getOnSingleFileSelectListener().onSelectedFile(currentPath);
                fileSelectDialog.dismiss();
            }
        });

    }
}
