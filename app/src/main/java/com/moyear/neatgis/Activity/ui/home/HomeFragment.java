package com.moyear.neatgis.Activity.ui.home;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moyear.neatgis.BMOD.MapModule.View.MapActivity;
import com.moyear.neatgis.BMOD.ProjectsModule.Adapter.ProjectRecylerViewAdapter;
import com.moyear.neatgis.BMOD.ProjectsModule.Model.ProjectInfo;
import com.moyear.neatgis.Base.BaseFragmentDialog;
import com.moyear.neatgis.Config.ProjectConfigEntity;
import com.moyear.neatgis.Config.SystemDirPath;
import com.moyear.neatgis.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.moyear.neatgis.Utils.ProjectUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

import gisluq.lib.Util.SysUtils;
import gisluq.lib.Util.ToastUtils;


public class HomeFragment extends Fragment {

    private Context context;
    private List<ProjectInfo> projectInfos;
    private ProjectRecylerViewAdapter appRecylerlViewAdapter;

    private HomeViewModel homeViewModel;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);//在Fragment中创建option菜单

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initRecylerView(rootView);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "添加代码！！！", Snackbar.LENGTH_LONG)
                .setAction("确定", null).show());


        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        return rootView;
    }

    //初始化项目列表
    private void initRecylerView(View rootView) {
        recyclerView = rootView.findViewById(R.id.activity_mian_recylerview);

        projectInfos = ProjectUtils.getProjectInfos(getContext());
        if (projectInfos.size() <= 0) {
            Toast.makeText(getActivity(), "未找到任何项目", Toast.LENGTH_SHORT).show();
        }

        appRecylerlViewAdapter = new ProjectRecylerViewAdapter(context,projectInfos);
        appRecylerlViewAdapter.setOnRecyclerViewItemClickListener(new ProjectRecylerViewAdapter.OnItemClickListener(){

            @Override
            public void onClick(int position) {
                ProjectInfo projectInfo = projectInfos.get(position);

                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("DirName",projectInfo.getDirName());
                intent.putExtra("DirPath",projectInfo.getDirPath());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                super.onLongClick(position);

                String [] items = {"查看","移动","复制","重命名","属性"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

                builder.show();
            }
        });
        recyclerView.setAdapter(appRecylerlViewAdapter);

        boolean isPad = SysUtils.isPad(Objects.requireNonNull(getActivity()));

        int spanCount = 2;

        if (isPad) spanCount = 3;

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);

        recyclerView.setLayoutManager(layoutManager);
    }



    /**
     * 刷新项目
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class RefreshTask extends AsyncTask<Integer, Integer, Boolean> {

        private Context context;
        private ProgressDialog progressDialog;//等待对话框

        public RefreshTask(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), null, "工程列表刷新...");
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            SystemClock.sleep(1000);
            try {
                projectInfos = ProjectUtils.getProjectInfos(context);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result) {
                appRecylerlViewAdapter.setAdapterList(projectInfos);//重新设置任务列表
                appRecylerlViewAdapter.refreshData();//刷新数据
                Toast.makeText(getActivity(), "工程列表刷新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "工程列表刷新失败", Toast.LENGTH_SHORT).show();
            }

        }
    }


    //创建option菜单
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_activity_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_projects_refresh:
                RefreshTask refreshTask = new RefreshTask(context);
                refreshTask.execute();
                break;
            case R.id.menu_project_new:
                createProject();
                break;
            case R.id.menu_open_from_local:
                showToast("从本地打开地图，代码待写！！！");
                break;
            case R.id.menu_open_from_web:
                showToast("打开网络地图，代码待写！！！");
                break;
            case R.id.menu_open_sample:
                showToast("打开示例工程，代码待写！！！");
                break;
            case R.id.menu_projects_sort:
                showToast("排序，代码待写！！！");
                break;
            case R.id.menu_notes_layout_list:
                showToast("列表布局，代码待写！！！");
                break;
            case R.id.menu_notes_layout_grid:
                showToast("网格布局，代码待写！！！");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //新建项目
    private void createProject() {
        BaseFragmentDialog projectCreateDialog = new BaseFragmentDialog("新建项目");

        View contentView = getLayoutInflater().inflate(R.layout.content_dialog_project_create, null, false);

        EditText edtProjectName = contentView.findViewById(R.id.edt_project_name);
        EditText edtProjectOwner = contentView.findViewById(R.id.edt_project_owner);
        EditText edtProjectDescription = contentView.findViewById(R.id.edt_project_description);
        EditText edtProjectSpatialReference = contentView.findViewById(R.id.edt_project_spatial_reference);


        Button btnOk = contentView.findViewById(R.id.btn_ok);
        Button btnCancel = contentView.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectCreateDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtProjectName.getText().toString().trim().isEmpty()) {
                    ToastUtils.showShort(getContext(),"请输入项目名称");
                    return;
                }

                String projectPath = SystemDirPath.getProjectPath(context) + "/" + edtProjectName.getText().toString();

                ProjectConfigEntity projectConfigEntity = new ProjectConfigEntity();
                projectConfigEntity.setProjectName(edtProjectName.getText().toString());
                projectConfigEntity.setProjectOwner(edtProjectOwner.getText().toString());
                projectConfigEntity.setDescription(edtProjectDescription.getText().toString());
                projectConfigEntity.setSpatialName(edtProjectSpatialReference.getText().toString());

                //检测是否存在相同的项目名称
                if (new File(projectPath).exists()) {
                    ToastUtils.showShort(getContext(),"该项目已存在，无法重复创建");
                    return;
                }

                if (ProjectUtils.createProject(projectConfigEntity, projectPath)) {
                    ToastUtils.showShort(getContext(),"创建项目成功");

                    RefreshTask refreshTask = new RefreshTask(context);
                    refreshTask.execute();
                } else
                    ToastUtils.showShort(getContext(),"创建项目失败");
                projectCreateDialog.dismiss();
            }
        });


        projectCreateDialog.setView(contentView);
        projectCreateDialog.show(getFragmentManager(),"新建项目");

    }

    private void showToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


}