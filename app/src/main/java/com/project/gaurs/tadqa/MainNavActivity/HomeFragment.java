package com.project.gaurs.tadqa.MainNavActivity;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.project.gaurs.tadqa.Adapter.Adapter_Menu;
import com.project.gaurs.tadqa.Pojo.FoodElements;
import com.project.gaurs.tadqa.R;
import com.project.gaurs.tadqa.Unnecessary.HttpHandler;
import com.project.gaurs.tadqa.Unnecessary.SlidingImage_Adapter;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends android.support.v4.app.Fragment {

    ProgressDialog loading;
    List<FoodElements> foodElements1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu recyclerViewadapter;
    private static ViewPager mPager;
    private static int currentPage = 0;
    String URL = "https://api.myjson.com/bins/yg4en";
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.paneer, R.drawable.parantha_1, R.drawable.kathiroll, R.drawable.rice, R.drawable.nonveg};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    private DatabaseReference mDatabase;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        new GetElements().execute();
        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }

    private void init(View view) {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 7500, 7500);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private class GetElements extends AsyncTask<String, String, List<FoodElements>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = new ProgressDialog(getActivity());
            loading.setMessage("One cannot think well, love well, sleep well, if one has not dined well!");
            loading.setCancelable(false);
            loading.show();

        }

        @Override
        protected List<FoodElements> doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(URL);
            foodElements1 = new ArrayList<>();
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray array = json.getJSONArray("menu");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jObject= array.getJSONObject(i);
                        FoodElements foodElements = new FoodElements();
                        try {
                            foodElements.setPhoto(jObject.getString("Imagepath"));
                            foodElements.setFoodType(jObject.getString("Category"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        foodElements1.add(foodElements);
                    }

                } catch (JSONException e) {

                }
            }

            return foodElements1;
        }

        @Override
        protected void onPostExecute(List<FoodElements> result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (loading.isShowing())
                loading.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            recyclerViewadapter = new Adapter_Menu(getActivity(), foodElements1);
            recyclerView.setAdapter(recyclerViewadapter);
        }
    }
}
