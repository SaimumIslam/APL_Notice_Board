package com.example.foysal.noticeboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    //All Widgets id start capital...then all smaller
    //all variable name starts with smaller..
    //widgets variable starts with type
    //private static Button signUp,signIn;
    EditText et_email,et_password;
    String logIn_email,logIn_password;
    Button signIn;
    String login_url="http://192.168.0.108/final/LogIn.php";
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_email = (EditText) findViewById(R. id.Email);
        et_password = (EditText) findViewById(R. id.Password);
        signIn = (Button)findViewById(R. id.SignIn);
        builder=new AlertDialog.Builder(MainActivity.this);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logIn_email=et_email.getText().toString();
                logIn_password=et_password.getText().toString();

                if(logIn_email.equals("")||logIn_password.equals(""))
                {
                    builder.setTitle("Please fillup the form again");
                    displayAlert("Fill Email and Password");
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            Intent intent=new Intent(MainActivity.this,Spinner.class);
                            Log.d("LOGIN_TAG", response);
                            try {
                                Log.d("log_In","in try");
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                if(code.equals("login_failed"))
                                {
                                    builder.setTitle("logIn error");
                                    displayAlert(code);
                                }
                                else
                                {
                                    Log.d("log_In","in intent");
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("UserId",jsonObject.getInt("UserId"));
                                    bundle.putString("Dicipline",jsonObject.getString("Dicipline"));
                                    bundle.putString("Batch",jsonObject.getString("Batch"));
                                    bundle.putString("University",jsonObject.getString("University"));
                                    intent.putExtras(bundle);
                                    MainActivity.this.startActivity(intent);
                                }
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
                            Log.d("log_In","in Error");
                            Toast.makeText(MainActivity.this,"server rnot response",Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<String,String>();
                            params.put("Email",logIn_email);
                            params.put("Password",logIn_password);
                            return params;
                        }
                    };
                    Mysingleton.getInstance(MainActivity.this).addToRequestque(stringRequest);
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
                    et_email.setText("");
                    et_password.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    public void UserSignUp(View view){
        //to go to resister page
        Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
        MainActivity.this.finish();
        startActivity(intent);
    }

    public void onBackPressed() {
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(MainActivity.this);
        exitbuilder.setTitle("Attention");
        exitbuilder.setMessage("Do You Want To Exit?");
        exitbuilder.setCancelable(true);

        exitbuilder.setPositiveButton("YES, Exit",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog mydialog=exitbuilder.create();
        mydialog.show();
    }

}
