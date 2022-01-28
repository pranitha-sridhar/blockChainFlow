package com.example.blockchaindemo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;

import com.example.blockchaindemo.MainActivity;
import com.example.blockchaindemo.R;
import com.example.blockchaindemo.databinding.FragmentPowBinding;
import com.example.blockchaindemo.managers.SharedPreferancesManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PowFragment extends DialogFragment implements View.OnClickListener {
    private Context context;
    private SharedPreferancesManager preferancesManager;

    ImageButton close;
    TextInputEditText editSetPower;
    MaterialButton continueButton;

    public PowFragment() {
        // Required empty public constructor
    }

    public static PowFragment newInstance() {
        return new PowFragment();
    }

    @Override
    public void onAttach(@NonNull Context mcontext) {
        super.onAttach(mcontext);
        context=mcontext.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pow, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferancesManager=new SharedPreferancesManager(context);
        close=view.findViewById(R.id.close);
        continueButton=view.findViewById(R.id.continueButton);
        editSetPower=view.findViewById(R.id.editSetPower);

        close.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        editSetPower.setText(String.valueOf(preferancesManager.getPowValue()));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog= super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(dialog.getWindow()!=null)dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:dismiss();
                            break;
            case R.id.continueButton:
                if(editSetPower.getText()!=null){
                    String pow=editSetPower.getText().toString();
                    preferancesManager.setPowValue(Integer.parseInt(pow));
                    /*if(getActivity()!=null) {
                        startActivity(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()));
                        getActivity().finish();
                    }
                    else*/
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    dismiss();
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context=null;
    }
}