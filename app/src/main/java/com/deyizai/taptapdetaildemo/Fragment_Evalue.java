package com.deyizai.taptapdetaildemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created  on 2017-7-26.
 *
 * @author cdy
 * @version 1.0.0
 */

public class Fragment_Evalue extends Fragment {

    View view;
    String content;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fre,container,false);
        ((TextView)view.findViewById(R.id.intorduce)).setText(Html.fromHtml(getResources().getString(R.string.introduce_demo)));
        return view;
    }
}
