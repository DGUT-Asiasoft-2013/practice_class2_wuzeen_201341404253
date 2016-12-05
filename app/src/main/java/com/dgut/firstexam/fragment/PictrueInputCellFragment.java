package com.dgut.firstexam.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgut.firstexam.R;

/**
 * Created by Administrator on 2016/12/5.
 */

public class PictrueInputCellFragment extends BaseInputCelllFragment {

    final int REQUESTCODE_CAMERA=1;
    final int REQUESTCODE_ALBUM=2;


    TextView lable;
    TextView hint;
    ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inputcell_pictrue, container, false);

        lable = (TextView) view.findViewById(R.id.labletext);
        hint = (TextView) view.findViewById(R.id.hinttext);
        imageView= (ImageView) view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onImageViewClicked();
            }
        });

        return view;
    }

     void onImageViewClicked() {
         String[] items={
                 "拍照",
                 "相册"
         };
        new AlertDialog.Builder(getActivity()).setTitle(lable.getText())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i)
                        {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                pickFromAlbum();
                                break;
                            default:
                        }
                    }
                }).setNegativeButton("取消",null).show();

    }

    private void takePhoto() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent,REQUESTCODE_CAMERA);
    }

    private void pickFromAlbum() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUESTCODE_ALBUM);
    }
    @Override
    public void setLableText(String labletext) {
        lable.setText(labletext);
    }

    @Override
    public void setHintText(String hinttext) {
        hint.setText(hinttext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode== Activity.RESULT_CANCELED)
       {
           return;
       }
        if(requestCode==REQUESTCODE_CAMERA)
        {
           Bitmap bitmap= (Bitmap) data.getExtras().get("data");
           imageView.setImageBitmap(bitmap);
        }else if (requestCode==REQUESTCODE_ALBUM){
            try
            {
                // 读取uri所在的图片
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }


    }
}