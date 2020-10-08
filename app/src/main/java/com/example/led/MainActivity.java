package com.example.led;

import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.io.OutputStream;
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class MainActivity extends AppCompatActivity {
    Button b1;
    ListView l1;


    //bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    private OutputStream outStream = null;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.b1);
        l1 = (ListView)findViewById(R.id.l1);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Device Not Available",Toast.LENGTH_LONG).show();


        }
        else if(!myBluetooth.isEnabled()){
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }


        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                pairedDevicesList();
            }
        });

    }



    private void pairedDevicesList() {

        

        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if(pairedDevices.size()>0){
            for(BluetoothDevice bt: pairedDevices){
                list.add(bt.getName()+"\n"+bt.getAddress());
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"No Paired Bluetooth Devices Found.",Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        

        l1.setAdapter(adapter);
        l1.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length()-17);

            Intent i = new Intent(MainActivity.this , ledControl.class);

            i.putExtra(EXTRA_ADDRESS,address);
            startActivity(i);
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.i1:
                Intent in1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.43.241/"));
                startActivity(in1);
                break;

            case R.id.i2:
                Intent in2 = new Intent(Intent.ACTION_VIEW,Uri.parse("tel:+918209203870"));
                startActivity(in2);
                break;

            case R.id.i3:
                Intent intent = new Intent (Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"cwidworld@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Report");
                intent.setPackage("com.google.android.gm");
                if (intent.resolveActivity(getPackageManager())!=null)
                    startActivity(intent);
                else
                    Toast.makeText(this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    public void doOne(MenuItem item) {


    }
}
