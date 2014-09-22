package com.gabiq.googleimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gabiq.googleimagesearch.R;
import com.gabiq.googleimagesearch.models.Settings;

public class SettingsActivity extends Activity implements OnItemSelectedListener {
    private Spinner spImageSize;
    private Spinner spColorFilter;
    private Spinner spImageType;
    private TextView etSiteFilter;
    
    private Settings settings;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        
        settings = (Settings) getIntent().getSerializableExtra("settings");
        
        spImageSize = (Spinner) findViewById(R.id.spImageSize);
        spColorFilter = (Spinner) findViewById(R.id.spColorFilter);
        spImageType = (Spinner) findViewById(R.id.spImageType);
        etSiteFilter = (TextView) findViewById(R.id.etSiteFilter);
        
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.image_sizes, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageSize.setAdapter(sizeAdapter);
        spImageSize.setSelection(sizeAdapter.getPosition(settings.getSize()));
        spImageSize.setOnItemSelectedListener(this);
        
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.color_filter, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spColorFilter.setAdapter(colorAdapter);
        spColorFilter.setSelection(colorAdapter.getPosition(settings.getColor()));
        spColorFilter.setOnItemSelectedListener(this);
        
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.image_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spImageType.setAdapter(typeAdapter);
        spImageType.setSelection(typeAdapter.getPosition(settings.getType()));
        spImageType.setOnItemSelectedListener(this);
        
        etSiteFilter.setText(settings.getSite());
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
            long id) {
        switch (parent.getId()) {
            
            case R.id.spImageSize:
                settings.setSize((String) parent.getItemAtPosition(pos));
                break;
            
            case R.id.spColorFilter:
                settings.setColor((String) parent.getItemAtPosition(pos));
                break;
                
            case R.id.spImageType:
                settings.setType((String) parent.getItemAtPosition(pos));
                break;
                
            default:
                break;
        }
        
    }

    
    public void onSave(View view) {
        settings.setSite(etSiteFilter.getText().toString());
        
        Intent intent = new Intent();
        intent.putExtra("settings", settings);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        
    }
}
