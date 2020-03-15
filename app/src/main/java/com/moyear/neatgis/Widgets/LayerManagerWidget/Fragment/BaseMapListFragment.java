package com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter.LayerListAdapter;

public class BaseMapListFragment extends Fragment {

    private MapView mapView;

    private RecyclerView baseMapLayerListView;

    private LayerListAdapter baseMapLayerListAdapter;

    public BaseMapListFragment(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layers_basemap, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {
        baseMapLayerListView = rootView.findViewById(R.id.rv_widget_view_layer_manager_layers_basemapLayers);

        baseMapLayerListAdapter = new LayerListAdapter(getContext(), mapView, mapView.getMap().getBasemap().getBaseLayers());
        baseMapLayerListView.setAdapter(baseMapLayerListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        baseMapLayerListView.setLayoutManager(linearLayoutManager);

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
                if (baseMapLayerListAdapter != null) {
                    baseMapLayerListAdapter.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }
                return true;

            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

        };

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(baseMapLayerListView);

        //更多按钮
        ImageButton basemapnBtnMore = rootView.findViewById(R.id.widget_view_layer_managet_layers_basemap_btnMore);
        basemapnBtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreMenu(v);
            }
        });

    }

    //展示更多菜单
    private void showMoreMenu(View v) {
        PopupMenu pmp = new PopupMenu(getContext(), v);
        pmp.getMenuInflater().inflate(R.menu.menu_layer_handle_tools, pmp.getMenu());
        pmp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_layer_handle_tools_openAllLayer:
                        for (int i = 0;i < mapView.getMap().getBasemap().getBaseLayers().size(); i++) {
                            Layer layer = mapView.getMap().getBasemap().getBaseLayers().get(i);
                            layer.setVisible(true);
                        }
                        baseMapLayerListAdapter.refreshData();
                        break;
                    case R.id.menu_layer_handle_tools_closedAllLayer:
                        for (int i = 0;i < mapView.getMap().getBasemap().getBaseLayers().size(); i++) {
                            Layer layer = mapView.getMap().getBasemap().getBaseLayers().get(i);
                            layer.setVisible(false);
                        }
                        baseMapLayerListAdapter.refreshData();
                        break;
                    case R.id.menu_layer_handle_tools_reverseVisibleLayer://反转可见地底图图层
                        for (int i = 0;i < mapView.getMap().getBasemap().getBaseLayers().size(); i++) {
                            Layer layer = mapView.getMap().getBasemap().getBaseLayers().get(i);
                            if (layer.isVisible()) {
                                layer.setVisible(false);
                            } else {
                                layer.setVisible(true);
                            }
                        }
                        baseMapLayerListAdapter.refreshData();
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
