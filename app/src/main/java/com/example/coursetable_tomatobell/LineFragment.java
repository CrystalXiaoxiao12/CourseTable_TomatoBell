package com.example.coursetable_tomatobell;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursetable_tomatobell.database.DataBaseHelper;

import im.dacer.androidcharts.BarView;
import im.dacer.androidcharts.LineView;

import java.util.ArrayList;

/**
 * Created by Dacer on 11/15/13.
 */
public class LineFragment extends Fragment {
    int MAXN = 10;
    private DataBaseHelper mHelper;
    private TextView myTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = DataBaseHelper.getInstance(getActivity());
        mHelper.openReadLink();
        mHelper.openWriteLink();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_line, container, false);
        final LineView lineView = (LineView) rootView.findViewById(R.id.line_view);
        final BarView barView = (BarView) rootView.findViewById(R.id.bar_view);
//          返回
        rootView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });
//        标题
        myTitle=(TextView)rootView.findViewById(R.id.titleText);
        myTitle.setText("专注统计");

        initLineView(lineView);
        loadBarData(barView);
        loadCount(lineView);
        return rootView;
    }

    private void loadBarData(BarView barView) {
        ArrayList<String> date = mHelper.getDateForTen();
        ArrayList<Integer> minutes = mHelper.getMinutesForTen();
        barView.setBottomTextList(date);
        barView.setDataList(minutes, 10);
    }

    private void loadCount(LineView lineView) {
        ArrayList<Integer> dataList = mHelper.getCountForTen();

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList);

        lineView.setDataList(dataLists);

    }

    private void initLineView(LineView lineView) {
        ArrayList<String> date = mHelper.getDateForTen();
        lineView.setBottomTextList(date);
        lineView.setColorArray(new int[]{
                Color.parseColor("#F44336"), Color.parseColor("#9C27B0"),
                Color.parseColor("#2196F3"), Color.parseColor("#009688")
        });
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
    }


}