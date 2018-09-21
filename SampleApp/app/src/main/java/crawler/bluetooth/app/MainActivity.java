package crawler.bluetooth.app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import 	android.os.Handler;
import com.jraska.console.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import io.palaima.smoothbluetooth.Device;
import io.palaima.smoothbluetooth.SmoothBluetooth;

public class MainActivity extends AppCompatActivity {


    private TextView mTextViewAngleLeft;
    private TextView mTextViewStrengthLeft;
    private SmoothBluetooth mSmoothBluetooth;



    private List<Integer> mBuffer = new ArrayList<>();
    private String data = "-1";


    private SmoothBluetooth.Listener mListener = new SmoothBluetooth.Listener() {
        @Override
        public void onBluetoothNotSupported() {
            //device does not support bluetooth
        }

        @Override
        public void onBluetoothNotEnabled() {
            //bluetooth is disabled, probably call Intent request to enable bluetooth
        }

        @Override
        public void onConnecting(Device device) {
            //called when connecting to particular device
            //Toast.makeText(MainActivity.this, "Connecting to: " + device.getName(), Toast.LENGTH_SHORT).show();
            writeCommand("Connecting to: " + device.getName());
        }

        @Override
        public void onConnected(Device device) {
            //called when connected to particular device
            //Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
            writeCommand("Connected");
        }

        @Override
        public void onDisconnected() {
            //called when disconnected from device
            writeCommand("Disconnected");
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onConnectionFailed(Device device) {
            //called when connection failed to particular device
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onDiscoveryStarted() {
            //called when discovery is started
           // Toast.makeText(MainActivity.this, "Searching...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDiscoveryFinished() {
            //called when discovery is finished
        }

        @Override
        public void onNoDevicesFound() {
            //called when no devices found
            mSmoothBluetooth.tryConnection();
        }

        @Override
        public void onDevicesFound(final List<Device> deviceList,
                                   final SmoothBluetooth.ConnectionCallback connectionCallback) {
            //receives discovered devices list and connection callback
            //you can filter devices list and connect to specific one
            //connectionCallback.connectTo(deviceList.get(position));

            Device spiderDevice = null;

            for (Device device: deviceList) {
                if(device.getName().equals("HC-06")) {
                    spiderDevice = device;
                }
            }

            connectionCallback.connectTo(spiderDevice);



        }

        @Override
        public void onDataReceived(int data) {
            //receives all bytes
            mBuffer.add(data);
            StringBuilder sb = new StringBuilder();

            if(data == 6) {

            }

            if (data == 3 && !mBuffer.isEmpty()) {
                //if (data == 0x0D && !mBuffer.isEmpty() && mBuffer.get(mBuffer.size()-2) == 0xA0) {

                for (int integer : mBuffer) {
                    sb.append((char) integer);
                }
                mBuffer.clear();

                writeCommand(sb.toString());


            }

        }
    };

    private void writeCommand(String command) {
        Console.writeLine(getTime() + " " +  command);
    }

    private String getTime() {

        SimpleDateFormat s = new SimpleDateFormat("hh:mm:ss");
        String format = s.format(new Date());

        return format;
        //String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" + String.format("%02d", calendar.get(Calendar.SECOND));
    }

    public void addListenerOnButton() {

        Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // the other points in the Radar
/*
                co.geeksters.radar.Radar radar = (co.geeksters.radar.Radar) findViewById(R.id.radar);
                ArrayList<co.geeksters.radar.RadarPoint> points = new ArrayList<co.geeksters.radar.RadarPoint>();

                points.add(new co.geeksters.radar.RadarPoint("identifier1", 10.00420f,22.0090f));


                radar.setPoints(points);
                radar.refresh(); */

                mSmoothBluetooth.send("5", true);


            }

        });



        Button buttonDance = (Button) findViewById(R.id.buttonDance);

        buttonDance.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mSmoothBluetooth.send("6", true);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.indian);
                        mp.start();
                    }
                }, 2000);

            }

        });


        Button buttonFreeRoam = (Button) findViewById(R.id.buttonFreeRoam);

        buttonFreeRoam.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mSmoothBluetooth.send("7", true);
            }

        });

        Button buttonHeart = (Button) findViewById(R.id.buttonHeart);

        buttonHeart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mSmoothBluetooth.send("8", true);
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSmoothBluetooth = new SmoothBluetooth(getApplicationContext());
        mSmoothBluetooth.setListener(mListener);

        mSmoothBluetooth.tryConnection();

        // Writing to console

        setContentView(R.layout.activity_main);
        addListenerOnButton();

        mTextViewAngleLeft = (TextView) findViewById(R.id.textView_angle_left);
        mTextViewStrengthLeft = (TextView) findViewById(R.id.textView_strength_left);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!data.equals("-1")) {
                            mSmoothBluetooth.send(data, true);
                        }


                    }
                });
            }
        },0,500);


        JoystickView joystickLeft = (JoystickView) findViewById(R.id.joystickView_left);
        joystickLeft.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mTextViewAngleLeft.setText(angle + "°");

                if(angle == 0) {
                    data="-1";
                } else if(angle < 60 || angle > 300) {
                    data = "4";
                } else if(angle < 120) {
                    data = "1";
                } else if (angle < 240) {
                    data = "3";
                } else {
                    data = "2";
                }


                mTextViewStrengthLeft.setText(data);








            }
        });


      //  co.geeksters.radar.Radar radar = (co.geeksters.radar.Radar) findViewById(R.id.radar);

        //And here set the reference Point (or for exemple your GPS location)
       // radar.setReferencePoint(new co.geeksters.radar.RadarPoint("myLocation", 10.00000f,22.0000f));

        // the other points in the Radar
       // ArrayList<co.geeksters.radar.RadarPoint> points = new ArrayList<co.geeksters.radar.RadarPoint>();
/*
        points.add(new co.geeksters.radar.RadarPoint("identifier1", 10.00200f,22.0000f));
        points.add(new co.geeksters.radar.RadarPoint("identifier2", 10.00220f,22.0000f));
        points.add(new co.geeksters.radar.RadarPoint("identifier3", 10.00420f,22.0010f));
        */

     //   radar.setPoints(points);



        //Remove title bar
     /*   this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();*/
        //decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

/*
        mTextViewAngleRight = (TextView) findViewById(R.id.textView_angle_right);
        mTextViewStrengthRight = (TextView) findViewById(R.id.textView_strength_right);

        JoystickView joystickRight = (JoystickView) findViewById(R.id.joystickView_right);
        joystickRight.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                mTextViewAngleRight.setText(angle + "°");
                mTextViewStrengthRight.setText(strength + "%");
            }
        }); */
    }
}