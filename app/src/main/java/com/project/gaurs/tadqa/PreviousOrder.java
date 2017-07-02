package com.project.gaurs.tadqa;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.gaurs.tadqa.Adapter.Adapter_Previous_Order;
import com.project.gaurs.tadqa.Pojo.PreviousData;
import com.project.gaurs.tadqa.Unnecessary.DatabaseEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousOrder extends android.support.v4.app.Fragment {

    RecyclerView recyclerViewpo;
    Adapter_Previous_Order adapter_previous_order;
    List<PreviousData> foodElements;
    DatabaseEntry databaseEntry;
    RecyclerView.LayoutManager layoutManager;

    public PreviousOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_previous_order, container, false);
        databaseEntry = new DatabaseEntry(getActivity());
        foodElements = getData(databaseEntry);
        adapter_previous_order = new Adapter_Previous_Order(foodElements,getActivity());
        recyclerViewpo = (RecyclerView)view.findViewById(R.id.recyclerpreviousorder);
        recyclerViewpo.setAdapter(adapter_previous_order);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewpo.setLayoutManager(layoutManager);
        recyclerViewpo.hasFixedSize();
        recyclerViewpo.setNestedScrollingEnabled(false);
        return view;
    }
    public static List<PreviousData> getData(DatabaseEntry databaseEntry){
        List<PreviousData> foodElements = new ArrayList<PreviousData>();
        foodElements = databaseEntry.getDataFromPrevious();
        return foodElements;
    }
}
