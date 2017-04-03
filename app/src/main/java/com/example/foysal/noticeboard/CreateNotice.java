package com.example.foysal.noticeboard;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateNotice extends AppCompatActivity {

    private static EditText et_title, et_description;
    private static Button post;
    private static AutoCompleteTextView at_type;

    String title, description, type;
    String dicipline,batch,university,noticeId;
    int userId;



    String[] Notice_Type = {"Routine", "Game", "Project", "Examination", "Emergency","Study","Tour","Sports","Lab","Class Test","Need"};
    AlertDialog.Builder builder;
    String post_url = "http://192.168.0.108/final/Publish.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notice_activity);

        et_title = (EditText) findViewById(R.id.Title);
        et_description = (EditText) findViewById(R.id.Description);
        at_type = (AutoCompleteTextView) findViewById(R.id.NoticeType);
        post = (Button) findViewById(R.id.Post);
        builder = new AlertDialog.Builder(CreateNotice.this);
        //for the auto text suggestion
        at_type = (AutoCompleteTextView) findViewById(R.id.NoticeType);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_item, Notice_Type);
        at_type.setThreshold(1);
        at_type.setAdapter(adapter);

        final Intent i=getIntent();
        Bundle b=i.getExtras();
        userId=b.getInt("UserId");
        dicipline=b.getString("Dicipline");
        batch=b.getString("Batch");
        university=b.getString("University");


        post.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {
                                        title = et_title.getText().toString();
                                        description = et_description.getText().toString();
                                        type = at_type.getText().toString();

                                        if (title.equals("") || description.equals("") || type.equals(""))
                                        {
                                            builder.setTitle("Please fill up all again");
                                            displayAlert("Fill title ,description, notice type");
                                        } else {
                                            //notice post request
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, post_url, new Response.Listener<String>()
                                            {
                                                @Override
                                                public void onResponse(String response)
                                                {
                                                    try
                                                    {
                                                        //getting data from json array
                                                        JSONArray jsonArray = new JSONArray(response);
                                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                                        String code = jsonObject.getString("code");
                                                        if (code.equals("Success"))
                                                        {
                                                            addNotification(description);
                                                            builder.setTitle(code);
                                                            displayAlert(jsonObject.getString("message"));
                                                        }
                                                        else
                                                        {
                                                            Log.d("post", "else");
                                                            builder.setTitle("Post failed");
                                                            displayAlert(jsonObject.getString("message"));
                                                        }
                                                    } catch (JSONException e)
                                                    {
                                                        e.printStackTrace();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.d("post", "in Error");
                                                    Toast.makeText(CreateNotice.this, "Error", Toast.LENGTH_LONG).show();
                                                    error.printStackTrace();
                                                }
                                            }) {
                                                @Override
                                                //pushing data in php
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> params = new HashMap<String, String>();
                                                    params.put("Title", title);
                                                    params.put("Description", description);
                                                    params.put("NoticeType", type);
                                                    params.put("UserId", String.valueOf(userId));
                                                    return params;
                                                }
                                            };
                                            Mysingleton.getInstance(CreateNotice.this).addToRequestque(stringRequest);
                                        }
                                    }
                                }
        );

    }


    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                et_title.setText("");
                et_description.setText("");
                at_type.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addNotification(String des) {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.al)
                        .setContentTitle("A new notice Published!!!")
                        .setContentText(des);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
