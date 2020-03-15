package com.moyear.neatgis.Activity.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.moyear.neatgis.BMOD.AppModule.Setting.ProjectSetting;
import com.moyear.neatgis.R;

public class SettngsFragment extends Fragment implements View.OnClickListener {

    private SettingsViewModel shareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout lvItemProject = root.findViewById(R.id.lv_item_setting_project);

        lvItemProject.setOnClickListener(this);



        shareViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lv_item_setting_project:
                Intent intent = new Intent(getContext(), ProjectSetting.class);
                startActivity(intent);
                break;

        }
    }
}