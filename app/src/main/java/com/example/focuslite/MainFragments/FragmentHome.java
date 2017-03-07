package com.example.focuslite.MainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.focuslite.Connectivity;
import com.example.focuslite.R;

public class FragmentHome extends Fragment {
    Button Connect;

    public FragmentHome() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("F\t\tTRAINING");
        setHasOptionsMenu(true);
        DeclareVariable(view);

        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = new Connectivity();
                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                            .addToBackStack(null)
                            .commit();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    private void DeclareVariable(View v) {
        Connect = (Button) v.findViewById(R.id.connect);
    }
}
