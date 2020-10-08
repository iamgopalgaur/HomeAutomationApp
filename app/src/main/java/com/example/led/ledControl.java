package com.example.led;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ledControl extends AppCompatActivity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static final java.lang.String ACTION_VOICE_SEARCH_HANDS_FREE =  "android.speech.action.VOICE_SEARCH_HANDS_FREE";



    Button btn1, btn3,btn4,btn5;
    SeekBar bright;
    TextView lumn;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btsocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);


        Intent newint = getIntent();
        address = newint.getStringExtra(MainActivity.EXTRA_ADDRESS);
        Log.wtf("ledControl Address:", address);

        btn1 = (Button) findViewById(R.id.btn1);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        bright = (SeekBar) findViewById(R.id.seekBar);
        lumn = (TextView) findViewById(R.id.lumn);

        bright.setEnabled(false);




        new ConnectBT().execute(address);

        //brightness
        bright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                if (fromUser == true) {
                    lumn.setText(String.valueOf(progress));
                    try {
                        btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(progress)));
                    } catch (IOException e) {

                    }
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = btn1.getText().toString();


                if (text.equals("ON")) {
                    turnOnLed();
                    btn1.setText("OFF");
                    bright.setEnabled(true);
                }
                if (text.equals("OFF")) {
                    turnOffLed();
                    btn1.setText("ON");
                    bright.setProgress(0);
                    lumn.setText("Brightness");
                    bright.setEnabled(false);

                }
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = btn4.getText().toString();

                if (text.equals("ON")) {
                    turnOnLed2();
                    btn4.setText("OFF");
                }

                if (text.equals("OFF")) {
                    turnOffLed2();
                    btn4.setText("ON");
                    bright.setProgress(0);
                    lumn.setText("Brightness");
                    bright.setEnabled(false);

                }


            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = btn5.getText().toString();

                if (text.equals("ON")) {
                    turnOnLed3();
                    btn5.setText("OFF");
                }

                if (text.equals("OFF")) {
                    turnOffLed3();
                    btn5.setText("ON");
                    bright.setProgress(0);
                    lumn.setText("Brightness");
                    bright.setEnabled(false);

                }

            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();

            }
        });


    }




    private void turnOffLed() {
        if (btsocket != null) {
            try {
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(0)));
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    private void turnOnLed() {
        if (btsocket != null) {
            try {
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(1)));
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void turnOnLed2(){
        if(btsocket != null){
            try{
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(2)));
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void turnOnLed3(){
        if(btsocket != null){
            try{
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(3)));
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void turnOffLed2(){
        if(btsocket != null){
            try{
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(4)));
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void turnOffLed3(){
        if(btsocket != null){
            try{
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(5)));
            }
            catch (IOException e){
                msg("Error");
            }
        }
    }

    private void Disconnect() {
        if (btsocket != null) {
            try {
                btsocket.getOutputStream().write(Integer.parseInt(String.valueOf(0)));
                btsocket.close();
            } catch (IOException e) {
                msg("Error");
            }

        }
        finish();

    }

    private void msg(final String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    public void doOne(MenuItem item) {

        startVoiceRecognitionActivity();

    }


    private class ConnectBT extends AsyncTask<String, Void, Void> {
        private boolean ConnectSuccess = true;


        @Override
        protected void onPreExecute() {
            Log.i("ledControl", "Device Connecting");
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please Wait!!");
        }

        @Override
        protected Void doInBackground(String... devices) {
            try {
                if (btsocket == null || !isBtConnected) {
                    Log.wtf("ledControl", "AsyncTask Called");
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    Log.wtf("AsyncTask:", address);
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(devices[0]);
                    btsocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btsocket.connect();
                    Log.i("ledControl:", "Connectd to Device");
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP bluetooth? Try Again.");
                finish();
            } else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i1:
                Intent in1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.43.241/"));
                startActivity(in1);
                break;

            case R.id.i2:
                Intent in2 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:+918209203870"));
                startActivity(in2);
                break;

            case R.id.i3:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"cwidworld@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivity(intent);
                else
                    Toast.makeText(this, "Gmail App is not installed", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {

            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);



            if (matches.contains("turn on green light")) {

                turnOnLed();
                btn1.setText("OFF");

            }

            if (matches.contains("turn on yellow light")) {

                turnOnLed2();
                btn4.setText("OFF");

            }

            if (matches.contains("turn on red light")) {

                turnOnLed3();
                btn5.setText("OFF");

            }
            if (matches.contains("turn off green light")) {

                turnOffLed();
                btn1.setText("ON");

            }
            if (matches.contains("turn off yellow light")) {

                turnOffLed2();
                btn4.setText("ON");

            }
            if (matches.contains("turn off red light")) {

               turnOffLed3();
                btn5.setText("ON");

            }
            if(matches.contains("disconnect")){
                Disconnect();
            }
            if(matches.contains("turn on all the lights")){
                turnOnLed();
                turnOnLed2();
                turnOnLed3();
                btn1.setText("OFF");
                btn4.setText("OFF");
                btn5.setText("OFF");
            }
            if(matches.contains("turn off all the lights")){
                turnOffLed();
                turnOffLed2();
                turnOffLed3();
                btn1.setText("ON");
                btn4.setText("ON");
                btn5.setText("ON");
            }
            if(matches.isEmpty()){
                Toast.makeText(this,"Inappropriate command",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
