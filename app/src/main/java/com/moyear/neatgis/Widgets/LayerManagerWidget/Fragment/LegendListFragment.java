package com.moyear.neatgis.Widgets.LayerManagerWidget.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Widgets.LayerManagerWidget.Adapter.LegendListAdapter;

public class LegendListFragment extends Fragment {

    MapView mapView;

    RecyclerView listViewLegend;

    public LegendListFragment(MapView mapView) {
        this.mapView = mapView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_layers_legend, container, false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

        listViewLegend = rootView.findViewById(R.id.rv_widget_view_layer_manager_legend_layers);
        LegendListAdapter legendListviewAdapter = new LegendListAdapter(getContext(), mapView.getMap().getOperationalLayers());

        listViewLegend.setAdapter(legendListviewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        listViewLegend.setLayoutManager(linearLayoutManager);

    }

}
