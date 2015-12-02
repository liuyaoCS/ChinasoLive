package com.chinaso.cl.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.chinaso.cl.R;
import com.chinaso.cl.common.Constants;

import com.recorder.upload.FlushFlowActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_record, container, false);

        Intent intent = new Intent(getActivity(), FlushFlowActivity.class);
        intent.putExtra("activityId", Constants.activityID);
        intent.putExtra("userId", Constants.userId);
        intent.putExtra("secretKey", Constants.secretKey);
        startActivity(intent);

        return view;
    }

}
