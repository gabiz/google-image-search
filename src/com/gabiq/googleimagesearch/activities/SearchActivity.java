package com.gabiq.googleimagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.gabiq.googleimagesearch.R;
import com.gabiq.googleimagesearch.adapter.EndlessScrollListener;
import com.gabiq.googleimagesearch.adapter.ImageResultsAdapter;
import com.gabiq.googleimagesearch.models.ImageResult;
import com.gabiq.googleimagesearch.models.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class SearchActivity extends Activity {
    private static final int REQUEST_CODE = 7;
    
    private EditText etQuery;
    private GridView gvResults;
    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter aImageResults;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        settings = new Settings();
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        gvResults.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
                ImageResult result = imageResults.get(position);
                i.putExtra("result", result);
                startActivity(i);
            }
            
        });
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
            
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore(totalItemsCount);
            }

        });
    }
    
    private void loadMore(int startOffset) {
      String query = etQuery.getText().toString();
      AsyncHttpClient client = new AsyncHttpClient();
      String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0"+
              "&q=" + query +
              "&rsz=8" + 
              "&start=" + startOffset +
              "&imgsz=" + settings.getSize() +
              "&imgcolor=" + settings.getColor() +
              "&imgtype=" + settings.getType() +
              "&as_sitesearch=" + settings.getSite();
      client.get(searchUrl, new JsonHttpResponseHandler() {

          @Override
          public void onSuccess(int statusCode, Header[] headers,
                  JSONObject response) {
              Log.d("DEBUG", response.toString());
              try {
                  JSONArray imageResultsJson = response.getJSONObject("responseData").getJSONArray("results");
                  aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              Log.d("INFO", "foo"); 
          }
          
      });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    public void onImageSearch(View v) {
        aImageResults.clear();
        etQuery.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etQuery.getWindowToken(), 0);
        loadMore(0);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public void onLaunchSettings(MenuItem mi){
        Intent i = new Intent(SearchActivity.this, SettingsActivity.class);
        i.putExtra("settings", settings);
        startActivityForResult(i, REQUEST_CODE);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
             settings = (Settings) data.getSerializableExtra("settings");
          }
    } 

}
