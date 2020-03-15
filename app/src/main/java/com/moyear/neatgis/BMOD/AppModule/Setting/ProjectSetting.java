package com.moyear.neatgis.BMOD.AppModule.Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;

import gisluq.lib.Util.ToastUtils;

public class ProjectSetting extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_project);

        LinearLayout lvProjectDir = findViewById(R.id.lv_item_project_dir);

        lvProjectDir.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_item_project_dir:
                FileSelectDialog fileChooserDialog = new FileSelectDialog(this, FileSelectDialog.SELECTED_MODE_SINGLE_FOLDER, new FileSelectDialog.OnSingleFileSelectListener() {
                    @Override
                    public void onSelectedFile(String selectedFilePath) {
                        ToastUtils.showShort(getApplicationContext(), "选择的路径是：" + selectedFilePath);
                    }
                });
                fileChooserDialog.show(getSupportFragmentManager(),"FileSelectDialog");
                break;

        }
    }
}
