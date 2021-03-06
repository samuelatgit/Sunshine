package com.example.slam.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    ArrayAdapter<String> _modelAdapter;
    View _rootView ;
    ListView _listView ;

    public ForecastFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
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
        if(id == R.id.action_refresh){
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_main, container, false);
        _listView = (ListView) _rootView.findViewById(R.id.listview_forecast);

        bindData(_rootView, new String[]{"Loading..."});


        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = _modelAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                        //.putExtra("forecast", forecast);
                startActivity(intent);

                toastMessage(forecast);
            }
        });

        return _rootView;
    }

    private void bindData(View rootView, String[] data)
    {
        if (_modelAdapter != null) {
            _modelAdapter.clear();
            _modelAdapter.addAll(data);
        }
        else {
            _modelAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList<String>(Arrays.asList(data)) );
            _listView.setAdapter(_modelAdapter);
        }
    }

    private void toastMessage(String msg){
        Context context = this.getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast t = Toast.makeText(context, msg, duration);
        //t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        t.show();
    }


    private void updateWeather() {
        try {
//                String[] forecasts = weatherTask.execute("94043").get();
//                _weekForecast = Arrays.asList( forecasts);
//                bindData(_rootView);
//                Log.v("DATA", "here");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_defaultValue));
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute(location);
        }catch(Exception ex){
            Log.e(this.getClass().getName(), ex.getMessage());
        }
    }

    public class FetchWeatherTask extends AsyncTask<String, Void ,String[]>
    {
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            bindData(_rootView, strings);
        }

        protected String[] doInBackground(String... urls) {
            String json = HttpIo.connect(urls[0]);
            try {
                OpenWeatherHelper ow = new OpenWeatherHelper(getActivity());
                return ow.getWeatherDataFromJson(json, 7);
            }catch(Exception ex){
                Log.e(this.getClass().getName(), ex.getMessage());
                return null;
            }
        }

    }
}
