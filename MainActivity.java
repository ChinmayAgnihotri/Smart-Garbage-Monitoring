package com.example.firebaseconnect;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.telephony.SmsManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
TextView d;
TextView m;
DatabaseReference ref;
Button b;
int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        d = (TextView)findViewById(R.id.dist);
        b = (Button)findViewById(R.id.btn);
        m = (TextView)findViewById(R.id.mq2);
        OkHttpClient client = new OkHttpClient();



        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked","yes clicked");

                ref = FirebaseDatabase.getInstance().getReference();
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String mqvalue = dataSnapshot.child("MQ2").getValue().toString();
                        m.setText(mqvalue);
                        String distance = dataSnapshot.child("Distance").getValue().toString();
                        d.setText(distance);

                        int sv= Integer.parseInt(distance);
                        String mob="9512731294";
                        String msg="WARNING!!! Dustbin is about to full";


                        if(Integer.parseInt(distance) < 5 && mob != null && msg != null && flag == 1) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(mob,null,msg,null,null);
                            Log.d("sms sent","sent message is donw\n");
                            flag = 0;
                        }

                        if(Integer.parseInt(distance) > 5 && Integer.parseInt(distance)<500){
                            flag = 1;
                        }
                        if(sv < 50)
                        {
                                String url ="http://192.168.1.55/example/send_sms.php?phone="+mob+"&msg="+msg+"level is: "+distance+"and gas is:"+mqvalue;
                                OkHttpClient client=new OkHttpClient();
                                Request request=new Request.Builder().url(url).build();

                                try
                                {
                                    Response response=client.newCall(request).execute();
                                    String data=response.body().string();

                                    if (!response.isSuccessful())
                                    {
                                        Toast.makeText(MainActivity.this, "Not Sent", Toast.LENGTH_SHORT).show();
                                        throw new IOException("Unexpected code " + response);
                                    }
                                    Toast.makeText(MainActivity.this, "Sent Message Successfully", Toast.LENGTH_SHORT).show();

                                    return ;
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        }
                   @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

    }
}
