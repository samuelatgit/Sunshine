package com.example.slam.sunshine;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return (id == R.id.action_refresh) ? true: super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        new FetchWeatherTask().execute();
        List<String> weekForecast = Arrays.asList(
                "Today - Sunny - 81/63",
                "Tomorrow - Sunny - 82/63",
                "Wed - Sunny - 83/63",
                "Thu - Sunny - 84/63");

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast );
        ListView l = (ListView) rootView.findViewById(R.id.listview_forecast);
        l.setAdapter(listAdapter);

        return rootView;
    }



    public class FetchWeatherTask extends AsyncTask<URL, Void ,String>
    {
        protected String doInBackground(URL... urls) {
            String json = HttpIo.connect();
            return json;
        }

    }
}
