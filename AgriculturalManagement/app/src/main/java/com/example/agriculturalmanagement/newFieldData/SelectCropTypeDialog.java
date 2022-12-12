package com.example.agriculturalmanagement.newFieldData;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.agriculturalmanagement.NewFieldDataFragment;
import com.example.agriculturalmanagement.model.AppViewModel;
import com.example.agriculturalmanagement.model.entities.Crop;

import java.util.ArrayList;
import java.util.List;

public class SelectCropTypeDialog extends DialogFragment {

    AppViewModel viewModel;
    LiveData<List<Crop>> allCrops;

    public SelectCropTypeDialog() {

        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        allCrops = viewModel.getAllCrops();
        int numOfCrops = allCrops.getValue().size();
        ArrayList<CharSequence> cropsArray = new ArrayList<CharSequence>();

        for(int i = 0; i < numOfCrops; ++i)
            cropsArray.add(allCrops.getValue().get(i).getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        cropsArray.toArray();
        builder.setTitle("Select crop").setItems((CharSequence[])cropsArray.toArray(),
            new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("cropId", which + "");
                    bundle.putString("cropName", allCrops.getValue().get(which).getName());
                    getParentFragmentManager().setFragmentResult("cropSelectorResult", bundle);
                }
                catch (Exception e){

                    builder.setTitle("Select another crop");
                }
            }
        });

        return builder.create();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        //View rootView = inflater.inflate(R.layout.fragment_select_crop_type_dialog, container, false);

        RadioGroup cropTypes = rootView.findViewById(R.id.cropList);
        int numOfCrops = allCrops.getValue().size();

        RadioButton cropType;

        ColorStateList colorStateList = new ColorStateList(

                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_enabled}
                },
                new int[]{Color.GRAY, Color.BLACK}
        );

        // adding crop types to container
        Crop crop;
        for (int i = 0; i < numOfCrops; ++i) {

            crop = allCrops.getValue().get(i);
            cropType = new RadioButton(getActivity());

            cropType.setId(crop.getId());
            cropType.setLayoutParams(new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            cropType.setButtonTintList(colorStateList);
            cropType.setChecked(false);
            cropType.setText(crop.getName());
            cropTypes.addView(cropType);
        }

        ((RadioButton) cropTypes.getChildAt(0)).setChecked(true);

        cropTypes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                try {
                    newField.setCropType(i);
                    validField = true;
                } catch (Exception e) {

                    radioGroup.check(0);
                    validField = false;
                }
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }*/
}