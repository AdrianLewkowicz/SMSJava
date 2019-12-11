package com.example.obsugamenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SendMessageButton();
        SMSReceiver = new SMSListener();
    }

    private void SendMessageButton(){
        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("123456789", null, "Wiadomosc testowa", null, null);
                    Toast.makeText(
                            MainActivity.this,
                            "Wiadomosc została wysłana",
                            Toast.LENGTH_LONG
                    ).show();
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Nie udało się wysłac SMSa",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
    private SMSListener SMSReceiver;


}

class SMSListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        final Bundle bundle = intent.getExtras();

        try{
            if(bundle != null){
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for(int i = 0; i< pdusObj.length;i++){
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    // wyświetlamy tę wiadomość

                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: " + senderNum + ", message: " + message,duration);
                    toast.show();
                }// end for loop
            }// bundle is null
        }catch (Exception e){
            Log.e("SmsReceiver","Exception smsReceiver" + e);
        }
    }



}
