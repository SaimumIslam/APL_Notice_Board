package com.example.foysal.noticeboard;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import static com.example.foysal.noticeboard.R.id.Resister;

public class SignUpActivity extends AppCompatActivity {

    //    @Override
    Button resister;
    EditText et_firstName, et_lastName, et_email, et_password, et_confirmPassword, et_dicipline, et_batch, et_university;
    String firstName, lastName, email, password, confirmPassword, dicipline, university, batch;

    AlertDialog.Builder builder;
    String reg_url = "http://192.168.0.108/final/SignUp.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        resister = (Button) findViewById(R.id.Resister);
        et_firstName = (EditText) findViewById(R.id.FirstName);
        et_lastName = (EditText) findViewById(R.id.LastName);
        et_email = (EditText) findViewById(R.id.SetEmail);
        et_password = (EditText) findViewById(R.id.SetPassword);
        et_confirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        et_dicipline = (EditText) findViewById(R.id.Dicipline);
        et_batch = (EditText) findViewById(R.id.Batch);
        et_university = (EditText) findViewById(R.id.University);

        builder = new AlertDialog.Builder(SignUpActivity.this);

        resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = et_firstName.getText().toString();
                lastName = et_lastName.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                confirmPassword = et_confirmPassword.getText().toString();
                dicipline = et_dicipline.getText().toString();
                batch = et_batch.getText().toString();
                university = et_university.getText().toString();

                if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("") || dicipline.equals("") || university.equals("") || batch.equals("")) {
                    builder.setTitle("Something went wrong....");
                    builder.setMessage("Please fill all the fields...");
                    displayAlert("Reg_failed");

                } else if (!password.equals(confirmPassword)) {
                    builder.setTitle("Password doesn't match....");
                    builder.setMessage("Please check confirm password.");
                    displayAlert("Password not match");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("up_TAG", response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                builder.setTitle("server response");
                                builder.setMessage(message);
                                displayAlert(code);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            builder.setTitle("Server not response");
                            Log.d("Up_TAG", "Inside error listener");
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("FirstName", firstName);
                            params.put("LastName", lastName);
                            params.put("Email", email);
                            params.put("Password", password);
                            params.put("Dicipline", dicipline);
                            params.put("Batch", batch);
                            params.put("University", university);

                            return params;
                        }
                    };
                    Mysingleton.getInstance(SignUpActivity.this).addToRequestque(stringRequest);
                }
            }
        });
    }

    public void displayAlert(final String code) {
        builder.setMessage(code);

        if (code.equals("Success"))
        {
            builder.setPositiveButton("Create another account?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    et_firstName.setText("");
                    et_lastName.setText("");
                    et_email.setText("");
                    et_password.setText("");
                    et_confirmPassword.setText("");
                    et_dicipline.setText("");
                    et_batch.setText("");
                    et_university.setText("");
                }
            });

            builder.setNegativeButton("Want to SignIn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent i=new Intent(SignUpActivity.this, MainActivity.class);
                    SignUpActivity.this.finish();
                    startActivity(i);
                }
            });
            AlertDialog alertDialo = builder.create();
            alertDialo.show();

        }
        else if (code.equals("Reg_failed")) {
            et_firstName.setText("");
            et_lastName.setText("");
            et_email.setText("");
            et_password.setText("");
            et_confirmPassword.setText("");
            et_dicipline.setText("");
            et_batch.setText("");
            et_university.setText("");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else if(code.equals("Password not match"))
        {
            et_password.setText("");
            et_confirmPassword.setText("");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
