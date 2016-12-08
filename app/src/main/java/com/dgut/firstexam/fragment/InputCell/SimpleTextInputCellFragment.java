package com.dgut.firstexam.fragment.InputCell;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dgut.firstexam.R;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SimpleTextInputCellFragment extends BaseInputCelllFragment {

    TextInputEditText edit;
    TextInputLayout textInputLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inputcell_simpletext, container, false);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.TextWrapper);
        edit = (TextInputEditText) view.findViewById(R.id.edit);
        return view;
    }

    public void setEidtError(String error) {
        edit.setError(error);

    }

    public void setLayoutError(String error) {

        textInputLayout.setError(error);

    }

    @Override
    public void setLableText(String labletext) {
        return;
    }

    public void setHintText(String hintText) {
        textInputLayout.setHint(hintText);
        edit.setTextColor(Color.parseColor("#FFFFFF"));
    }


    public void setIsPassword(boolean isPassword) {
        if (isPassword) {
            edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {

            edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        }


    }


    @Override
    public void onResume() {
        super.onResume();

    }


}
