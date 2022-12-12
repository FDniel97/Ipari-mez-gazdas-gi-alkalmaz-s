package com.example.agriculturalmanagement.newFieldData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agriculturalmanagement.NewFieldDataFragment;
import com.example.agriculturalmanagement.R;
import com.example.agriculturalmanagement.model.entities.ComplexArea;
import com.example.agriculturalmanagement.model.entities.GenArea;
import com.example.agriculturalmanagement.model.entities.LinArea;
import com.example.agriculturalmanagement.model.entities.POI;
import com.example.agriculturalmanagement.model.entities.UnitArea;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class FieldShapeEditor extends Fragment implements OnMapReadyCallback{

    private boolean unitAreaEditorMode;
    private Switch setAreaMode;
    private Button addPoint;
    //private Button removePoint;// todo implement point removal method
    //private Button newUnitArea;// todo allow multiple LinArea objects
    //private Button addUnitArea;// todo allow multiple LinArea object
    private Button newComplexArea;
    private TextView areaEditorInfo;
    private GoogleMap editorMap;
    private LatLng pos;
    private Marker marker;
    private Polyline areaObject;// to visualize area in editor using sequence of lines

    private LinArea linArea;
    private ComplexArea fieldShape;
    private ArrayList<LatLng> unitAreaCoords;


    Toast errorMessage;

    public FieldShapeEditor(){

        // Required empty public constructor
        unitAreaEditorMode = true;// true use simplified unit area (rectangular unit areas)
        unitAreaCoords = new ArrayList<LatLng>();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull  GoogleMap googleMap) {

        editorMap = googleMap;
        pos = new LatLng(47.472469, 19.063381);
        marker = editorMap.addMarker(new MarkerOptions().position(pos));
        editorMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        editorMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 16.0f));

        editorMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {

                // updating last coordinate by camera position
                marker.remove();
                pos = editorMap.getCameraPosition().target;
                marker = editorMap.addMarker(new MarkerOptions().position(pos));
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_field_shape_editor, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.shapeEditorMap);

        if (mapFragment != null) mapFragment.getMapAsync(this);

        fieldShape = new ComplexArea();

        setAreaMode = rootView.findViewById(R.id.setAreaMode);
        addPoint = rootView.findViewById(R.id.addPoint);
        //removePoint = rootView.findViewById(R.id.removePoint);
        //addUnitArea = rootView.findViewById(R.id.addUnitArea);
        //newUnitArea = rootView.findViewById(R.id.newUnitArea);
        newComplexArea = rootView.findViewById(R.id.newComplexArea);
        areaEditorInfo = rootView.findViewById(R.id.areaEditorInfo);


        // todo implement differentiated parameterization in case of unit area (width, height)
        //  use default parameters at first version of implementation
        setAreaMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                unitAreaEditorMode = isChecked;

                // initializing with empty, geometry is not defined, or erased the previous one
                linArea = new LinArea();
            }
        });

        // todo problem:
        //  At UnitArea type, only unit geometry is inserted into complex area via predefined geometry shape
        //   In this case, it is not required to determine the sequence of points of the geometry in explicit way
        //  But at LinArea type, the shape of the geometry is defined by an explicit sequence of points
        //   Hence an extra button is needed in order to perform multiple LinArea object in one ComplexArea
        //
        //  In this version of implementation, only one LinArea object and multiple UnitArea objects can be
        //  assigned to a ComplexArea
        addPoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                try {

                    if(unitAreaEditorMode){

                        // simplified editor mode using rectangular unit areas as small building unit of complex area
                        UnitArea newUnitArea = new UnitArea(100.0, 100.0, pos.longitude, pos.latitude);
                        fieldShape.addGenArea(newUnitArea);

                        // drawing unit area onto map
                        areaObject = editorMap.addPolyline(new PolylineOptions().clickable(false).add(

                                new LatLng(newUnitArea.getByInd(0).lat, newUnitArea.getByInd(0).lon),
                                new LatLng(newUnitArea.getByInd(1).lat, newUnitArea.getByInd(1).lon),
                                new LatLng(newUnitArea.getByInd(2).lat, newUnitArea.getByInd(2).lon),
                                new LatLng(newUnitArea.getByInd(3).lat, newUnitArea.getByInd(3).lon),
                                new LatLng(newUnitArea.getByInd(0).lat, newUnitArea.getByInd(0).lon)
                        ));
                    }
                    else{

                        // inserting a vertex to continue to define the shape of geometry

                        // drawing geometry border section onto map
                        if(unitAreaCoords.size() > 0){

                            areaObject = editorMap.addPolyline(new PolylineOptions().clickable(false).add(

                                    new LatLng(unitAreaCoords.get(unitAreaCoords.size() - 1).latitude,
                                            unitAreaCoords.get(unitAreaCoords.size() - 1).longitude),
                                    new LatLng(pos.latitude, pos.longitude)
                            ));
                        }

                        linArea.addCoord(pos.longitude, pos.latitude);
                    }

                    unitAreaCoords.add(pos);

                    // copy coordinate to make the recently persisted marker fixed
                    pos = editorMap.getCameraPosition().target;
                    marker = editorMap.addMarker(new MarkerOptions().position(pos));
                }
                catch (Exception e) {

                    // in case of error at point insertion into geometry
                    //areaEditorInfo.setText(e.getMessage());
                    errorMessage = Toast.makeText(getContext(),
                            e.getMessage() + ", try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        // todo implement point removal method
        /*
        removePoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                unitAreaCoords.remove(unitAreaCoords.size() - 1);
            }
        });
        */

        // creating new ComplexArea geometry object for representing a field geographical shape
        newComplexArea.setOnClickListener(new View.OnClickListener() {

            // first we assume that the user does not cause intentional complex area overlaps

            @Override
            public void onClick(View view) {

                // ComplexArea is useful in case of further expansion of areas or modifications
                //  using these feature, it does not require to take a areal boundary traversal
                //  again to update the geometric shape of the field, only add it or subtract it
                try {

                    if(unitAreaEditorMode) {

                        fieldShape.generateComplexArea();
                    }
                    else {

                        linArea.checkLoop();
                        linArea.computeUnitAreaSize();
                        fieldShape.addGenArea(linArea);
                        fieldShape.generateComplexArea();
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("shape", fieldShape.toString());
                    getParentFragmentManager().setFragmentResult("shapeEditorResult", bundle);
                    Navigation.findNavController(view).popBackStack();
                }
                catch (Exception e){

                    // in case of opened loop or any other errors
                    //areaEditorInfo.setText(e.getMessage());
                    errorMessage = Toast.makeText(getContext(),
                            e.getMessage() + ", try again.", Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }
}
