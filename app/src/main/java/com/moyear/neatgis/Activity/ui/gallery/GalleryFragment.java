package com.moyear.neatgis.Activity.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
;
import com.moyear.neatgis.R;
import com.moyear.neatgis.Views.DraggableCoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transformation.TransformationChildCard;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);


        fab = view.findViewById(R.id.fab);
        View closeButton = view.findViewById(R.id.close_button);
        TransformationChildCard sheet = view.findViewById(R.id.sheet);
        View scrim = view.findViewById(R.id.scrim);

        fab.setOnClickListener(v -> fab.setExpanded(!fab.isExpanded()));
        closeButton.setOnClickListener(v -> fab.setExpanded(false));
        scrim.setOnClickListener(v -> fab.setExpanded(false));

        DraggableCoordinatorLayout container2 = (DraggableCoordinatorLayout) view;
        container2.setViewDragListener(new DraggableCoordinatorLayout.ViewDragListener() {
            @Override
            public void onViewCaptured(@NonNull View view, int i) {
                sheet.setDragged(true);
            }

            @Override
            public void onViewReleased(@NonNull View view, float v, float v1) {
                sheet.setDragged(false);
            }
        });

        container2.addDraggableChild(fab);
        container2.addDraggableChild(sheet);


        return view;
    }


}