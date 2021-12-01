package com.example.bluetooth;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT=0;
    private static final int REQUEST_DICOVER_BT=1;

    TextView MBTstatus,MPairedDevices;
    Button MOn,MOff,MGPDevices,MDiscoverable;
    BluetoothAdapter MBTAdapter;
    ImageView im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect_components();
        BT_available();
        SetImage();
        MOn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(!MBTAdapter.isEnabled())
                    showtoast("Turning on bluetooth");
                Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,REQUEST_ENABLE_BT);
            }
        });
        MDiscoverable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MBTAdapter.isDiscovering())
                {
                    showtoast("making your device discoverable");
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent,REQUEST_DICOVER_BT);
                }
            }
        });
        MOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MBTAdapter.isEnabled()){
                    MBTAdapter.disable();
                    showtoast("turning bluetooth off");
                    im.setImageResource(R.drawable.ic_action_off);
                }
                else
                    showtoast("bluetooth is already off");
            }
        });
        MGPDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MBTAdapter.isEnabled())
                {
                    MPairedDevices.setText("paird devices");
                    Set<BluetoothDevice> devices=MBTAdapter.getBondedDevices();
                    for(BluetoothDevice device:devices)
                    {
                        MPairedDevices.append("\nDevice : "+device.getName()+","+device);
                    }
                }
                else
                    showtoast("BT is off, Turn on BT to get paired devices");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK) {
                    im.setImageResource(R.drawable.ic_action_on);
                    showtoast("BT is on");
                }
                else
                    showtoast("couldn't turn on BT");
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connect_components() {
        MBTstatus=findViewById(R.id.etStatusBTx);
        MOn=findViewById(R.id.etOnx);
        MOff=findViewById(R.id.etOffx);
        MDiscoverable=findViewById(R.id.etDiscoverablex);
        MGPDevices=findViewById(R.id.etGetPDx);
        MPairedDevices=findViewById(R.id.etTextx);
        MBTAdapter=BluetoothAdapter.getDefaultAdapter();
    }
    private void BT_available(){
        if(MBTAdapter==null)
            MBTstatus.setText("BT is not available");
        else
            MBTstatus.setText("BT is available");

    }
    private void SetImage()
    {
        if (MBTAdapter.isEnabled()){
            im.setImageResource(R.drawable.ic_action_on);
        }
        else {
            im.setImageResource(R.drawable.ic_action_off);
        }
    }

    //toast msg function//
    private void showtoast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    
}