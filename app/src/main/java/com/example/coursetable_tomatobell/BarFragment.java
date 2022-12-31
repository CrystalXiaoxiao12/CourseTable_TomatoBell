package com.example.coursetable_tomatobell;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.coursetable_tomatobell.database.DataBaseHelper;

import im.dacer.androidcharts.BarView;
import java.util.ArrayList;

/**
 * Created by Dacer on 11/15/13.
 */
public class BarFragment extends Fragment {
    private DataBaseHelper mHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHelper= DataBaseHelper.getInstance(getActivity());
        mHelper.openReadLink();
        mHelper.openWriteLink();

    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bar, container, false);
        final BarView barView = (BarView) rootView.findViewById(R.id.bar_view);
        Button button = (Button) rootView.findViewById(R.id.bar_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                randomSet(barView);
            }
        });
        loadBarData(barView);
        return rootView;
    }

    private void loadBarData(BarView barView) {
        ArrayList<String> date=mHelper.getDateForTen();
        ArrayList<Integer> minutes=mHelper.getMinutesForTen();
        barView.setBottomTextList(date);
        barView.setDataList(minutes,10);


    }

    private void randomSet(BarView barView) {
        int random = (int) (Math.random() * 20) + 6;
        ArrayList<String> test = new ArrayList<String>();
        for (int i = 0; i < random; i++) {
            test.add("test");
            test.add("pqg");
            //            test.add(String.valueOf(i+1));
        }
        barView.setBottomTextList(test);

        ArrayList<Integer> barDataList = new ArrayList<Integer>();
        for (int i = 0; i < random * 2; i++) {
            barDataList.add((int) (Math.random() * 100));
        }
        barView.setDataList(barDataList, 1000);
    }
}