package com.example.foysal.noticeboard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class subscribe extends AppCompatActivity {

    TextView t_display;
    String title,description,firstName,date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        t_display = (TextView) findViewById(R.id.Description);

        final Intent in = getIntent();
        Bundle b = in.getExtras();
        title = b.getString("Title");
        description = b.getString("Description");
        firstName=b.getString("FirstName");
        date=b.getString("Date");

        t_display.setText("Title:  "+title+"\n\nDescription:  "+description+"\n\n Posted by :  "+firstName+"\n\nPublished at : "+date);


    }
}
