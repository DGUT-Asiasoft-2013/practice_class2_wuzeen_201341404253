package com.dgut.firstexam.fragment.widgets;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dgut.firstexam.R;

/**
 * Created by Administrator on 2016/12/3.
 */

public class VersionFragment  extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_version, container, false);
        TextView tvVersion= (TextView) view.findViewById(R.id.tv_version);

        tvVersion.setText(getVersionName());

        return view;
          }

    private String getVersionName()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        String version;
        try {
            packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
            version = "当前版本号： "+packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version="获取版本信息失败";
        }

        return version;
    }
}
