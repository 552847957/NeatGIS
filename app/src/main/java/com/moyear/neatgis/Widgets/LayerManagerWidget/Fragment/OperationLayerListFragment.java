package com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.File.FileSelectDialog;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Utils.LayerOperateUtils;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter.LayerListAdapter;

import gisluq.lib.Util.ToastUtils;


public class OperationLayerListFragment extends Fragment {

    MapView mapView;

    RecyclerView rvFeatureLayers;

    LayerListAdapter featureLayerListAdapter;


    public OperationLayerListFragment(MapView mapView) {
        this.mapView = mapView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layers_operation, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

        rvFeatureLayers = rootView.findViewById(R.id.rv_widget_view_layer_manager_layers_featureLayers);

        featureLayerListAdapter = new LayerListAdapter(getContext(), mapView, mapView.getMap().getOperationalLayers());
        rvFeatureLayers.setAdapter(featureLayerListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvFeatureLayers.setLayoutManager(linearLayoutManager);

        //拖拽移动图层
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                //首先回调的方法,返回int表示是否监听该方向
                int dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;//拖拽
                int swipeFlag = 0;//侧滑删除
                return makeMovementFlags(dragFlag, swipeFlag);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if (featureLayerListAdapter != null) {
                    featureLayerListAdapter.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }
                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rvFeatureLayers);


        ImageButton operationBtnMore = rootView.findViewById(R.id.widget_view_layer_managet_layers_operationlayer_btnMore);
        operationBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreMenu(v);
            }
        });

    }

    private void showMoreMenu(View v) {
        PopupMenu pmp = new PopupMenu(getContext(), v);
        pmp.getMenuInflater().inflate(R.menu.menu_layer_handle_tools, pmp.getMenu());
        pmp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_layer_handle_tools_layer_add:
                        ToastUtils.showShort(getContext(),"新建图层，代码待写");

                        FileSelectDialog fileChooserDialog = new FileSelectDialog(getContext(), new FileSelectDialog.OnSingleFileSelectListener() {
                            @Override
                            public void onSelectedFile(String selectedFilePath) {
                                ToastUtils.showShort(getContext(), "选中文件，路径为：" + selectedFilePath);
                            }
                        });
                        fileChooserDialog.show(getActivity().getSupportFragmentManager(),"FileSelectDialog");

                        break;
                    case R.id.menu_layer_handle_tools_scale:
                        Envelope maxExtentEnvelope = LayerOperateUtils.getMaxExtentFromLayers(mapView.getMap().getOperationalLayers());

                        Viewpoint viewpoint = new Viewpoint(maxExtentEnvelope);
                        mapView.setViewpoint(viewpoint);
                        break;
                    case R.id.menu_layer_handle_tools_openAllLayer:
                        for (int i = 0;i < mapView.getMap().getOperationalLayers().size(); i++) {
                            Layer layer = mapView.getMap().getOperationalLayers().get(i);
                            layer.setVisible(true);
                        }
                        featureLayerListAdapter.refreshData();
                        break;
                    case R.id.menu_layer_handle_tools_closedAllLayer:
                        for (int i = 0;i < mapView.getMap().getOperationalLayers().size(); i++) {
                            Layer layer = mapView.getMap().getOperationalLayers().get(i);
                            layer.setVisible(false);
                        }
                        featureLayerListAdapter.refreshData();
                        break;
                    case R.id.menu_layer_handle_tools_reverseVisibleLayer://反转可见图层
                        for (int i = 0;i < mapView.getMap().getOperationalLayers().size(); i++) {
                            Layer layer = mapView.getMap().getOperationalLayers().get(i);
                            if (layer.isVisible()) {
                                layer.setVisible(false);
                            } else {
                                layer.setVisible(true);
                            }
                        }
                        featureLayerListAdapter.refreshData();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        pmp.show();
    }


}
