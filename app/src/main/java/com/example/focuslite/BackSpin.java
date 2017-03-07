package com.example.focuslite;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.focuslite.Database.DataHelp;
import com.example.focuslite.Database.MyOpenHelper;

import java.util.ArrayList;

public class BackSpin extends Fragment {
    ListView List;
    TextView Msg;
    MyOpenHelper db;
    DataHelp dh;
    ArrayList<String> Speed = new ArrayList<>();
    ArrayList<Integer> CatId=new ArrayList<>();
    ArrayAdapter<String> adapter;
    int UserId;

    public BackSpin() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_backspin, container, false);
        Variables(v);
        SetList();

        return v;
    }

    private void SetList() {
        Speed = db.getCSVData(UserId);
        if (Speed.size() > 0) {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, Speed);
            List.setAdapter(adapter);
        } else {
            List.setVisibility(View.GONE);
            Msg.setVisibility(View.VISIBLE);
        }
    }

    private void Variables(View v) {
        setHasOptionsMenu(true);
        List = (ListView) v.findViewById(R.id.list);
        Msg = (TextView) v.findViewById(R.id.msg);
        db = new MyOpenHelper(getActivity());
        dh = new DataHelp(getActivity());
        UserId = db.getUserId();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem Clear = menu.findItem(R.id.action_clear);
        Clear.setVisible(true);
        Clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (dh.DeleteInputs(UserId)) {
                    Toast.makeText(getActivity(), "Data Cleared", Toast.LENGTH_SHORT).show();
                    SetList();
                }
                else
                    Toast.makeText(getActivity(), "Data Not Cleared", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        super.onPrepareOptionsMenu(menu);
    }

}