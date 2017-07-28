package com.deyizai.taptapdetaildemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created  on 2017-7-26.
 *
 * @author cdy
 * @version 1.0.0
 */

public class Fragment_Detail extends Fragment {
    View view;
    String content;
    @Bind(R.id.poster_pager)
    ViewPager poster_pager;
    String posters[] = {"https://img.taptapdada.com/market/images/6bb0e66078e63fdf9e7624167945728d.png","https://img.taptapdada.com/market/images/41d3de0e112a6dc982546c7377ac782e.png","https://img.taptapdada.com/market/images/4f8ac7a489f00d9d3ee4e7152924dc24.png","https://img.taptapdada.com/market/images/76e9756b913e58241042caa9b9239317.png","https://img.taptapdada.com/market/images/198129113ad4cfcb4a0b6c429f6e0640.png","https://img.taptapdada.com/market/lcs/170978c6eb89f5a1ed5f4734b2cb9952_360.png","https://img.taptapdada.com/market/images/6bb0e66078e63fdf9e7624167945728d.png"};
    ArrayList<View> mViewList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fre,container,false);
        ((TextView)view.findViewById(R.id.intorduce)).setText(Html.fromHtml(getResources().getString(R.string.introduce_demo)));
        ButterKnife.bind(this,view);

        poster_pager.setVisibility(View.VISIBLE);

        mViewList=new ArrayList<>();

        for(String poster : posters) {
            ImageView view = new ImageView(getActivity());
            Glide.with(this).load(poster).placeholder(R.drawable.default_bg).fallback(R.drawable.default_bg).centerCrop().into(view);
            mViewList.add(view);
        }

        poster_pager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return posters.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
                container.addView(mViewList.get(position));
                return mViewList.get(position);
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
                container.removeView(mViewList.get(position));
            }
        });
        return view;
    }
}
