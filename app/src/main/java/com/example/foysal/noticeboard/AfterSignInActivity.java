package com.example.foysal.noticeboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AfterSignInActivity extends AppCompatActivity {
    String dicipline,batch,university,noticeId;
    int userId;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_sign_in_activity);

            final Intent i=getIntent();
            Bundle b=i.getExtras();
            userId=b.getInt("UserId");
            dicipline=b.getString("Dicipline");
            batch=b.getString("Batch");
            university=b.getString("University");

        }

    public void crtNBOnClick(View view){

        Intent intent=new Intent(this, CreateNotice.class);
        Bundle bundle=new Bundle();
        bundle.putInt("UserId",userId);
        bundle.putString("Dicipline",dicipline);
        bundle.putString("Batch",batch);
        bundle.putString("University",university);
        intent.putExtras(bundle);
        AfterSignInActivity.this.startActivity(intent);
    }

    public void NewsFBOnClick(View view)
    {
        Intent intent=new Intent(this, NewsfeedActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("UserId",userId);
        bundle.putString("Dicipline",dicipline);
        bundle.putString("Batch",batch);
        bundle.putString("University",university);
        intent.putExtras(bundle);
        AfterSignInActivity.this.startActivity(intent);
    }

    public void OldBOnclick(View view)
    {
        Intent intent=new Intent(this,Home.class);
        Bundle bundle=new Bundle();
        bundle.putInt("UserId",userId);
        bundle.putString("Dicipline",dicipline);
        bundle.putString("Batch",batch);
        bundle.putString("University",university);
        intent.putExtras(bundle);
        AfterSignInActivity.this.startActivity(intent);
    }


}
