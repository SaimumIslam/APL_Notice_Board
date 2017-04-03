package com.example.foysal.noticeboard;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import android.widget.TextView;


public class Home extends AppCompatActivity {

    String dicipline,batch,university;
    int userId;
    String code;

    String get_url="http://192.168.0.108/final/seen.php";

    ListAdapter listAdapter;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Intent in = getIntent();
        Bundle b = in.getExtras();

        userId=b.getInt("UserId");
        dicipline=b.getString("Dicipline");
        batch=b.getString("Batch");
        university=b.getString("University");

        listView=(ListView)findViewById(R.id.Notification);

        StringRequest stringReques = new StringRequest(Request.Method.POST,get_url,new Response.Listener<String>()
        {
            @Override
            public void onResponse(final String response)
            {
                try
                {
                    ArrayList<String> list=new ArrayList<String>();
                    final JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        code = jsonObject.getString("Title");
                        Log.d("title",code);
                        list.add(code);
                    }
                    listAdapter=new ArrayAdapter<String>(Home.this,android.R.layout.simple_list_item_1,list)
                    {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            View view = super.getView(position, convertView, parent);
                            TextView text = (TextView) view.findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String n_title=String.valueOf(parent.getItemAtPosition(position));
                            Intent sub=new Intent(Home.this,subscribe.class);
                            view.setBackgroundResource(android.R.color.darker_gray);
                            try
                            {
                                JSONObject jsonObjec = jsonArray.getJSONObject(position);
                                String des=jsonObjec.getString("Description");
                                String fn=jsonObjec.getString("FirstName");
                                String date=jsonObjec.getString("Date");
                                Toast.makeText(Home.this,n_title,Toast.LENGTH_LONG).show();
                                Bundle bundl=new Bundle();
                                bundl.putString("Title",n_title);
                                bundl.putString("Description",des);
                                bundl.putString("FirstName",fn);
                                bundl.putString("Date",date);
                                sub.putExtras(bundl);
                                Home.this.startActivity(sub);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this,"Error",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Dicipline",dicipline);
                params.put("Batch", batch);
                params.put("University",university);
                params.put("UserId",String.valueOf(userId));
                return params;
            }
        };
        Mysingleton.getInstance(Home.this).addToRequestque(stringReques);
    }
}



