package com.example.focuslite;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.focuslite.Constants.DEVICE_NAME;

public class Connectivity extends Fragment {
    ImageView image;
    Button getstart;
    LinearLayout ConnectingMsg;
    TextView myText, Connect;
    RotateAnimation anim;
    Animation anim1;

    private static final String TAG = "Connectivity";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

//    Name of the connected device
    private String mConnectedDeviceName = null;

//    Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;

//    String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;

//    Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

//    Member object for the chat services
    private BluetoothChatService mChatService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't
            // started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connectivity, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        image = (ImageView) view.findViewById(R.id.animationView);
        ConnectingMsg = (LinearLayout) view.findViewById(R.id.connectingLinear);
        Connect = (TextView) view.findViewById(R.id.connect);
        myText = (TextView) view.findViewById(R.id.dots);
        getstart = (Button) view.findViewById(R.id.getstart);
    }

//    Set up the UI and background operations for chat.
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the DeviceListActivity1 to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

//    The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            try {
                                Connect.setText("Connected, Please bowl the ball");
                                myText.setVisibility(View.VISIBLE);
                                getstart.setVisibility(View.GONE);
                                anim.cancel();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            myText.setVisibility(View.VISIBLE);
                            Connect.setText("Connecting");
                            Rotate();
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            try {
                                anim1.cancel();
                                ConnectingMsg.setVisibility(View.GONE);
                                anim.cancel();
                            } catch (Exception e) {
                                e.getMessage();
                            }
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Fragment fragment = new TabFragment(readMessage);
                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), fragment)
                            .addToBackStack(null)
                            .commit();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getActivity(), "Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    ConnectingMsg.setVisibility(View.VISIBLE);
                    Connect.setText("Connected, Please bowl the ball");
                    myText.setVisibility(View.VISIBLE);
                    getstart.setVisibility(View.GONE);
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    public void Rotate() {
        anim = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setFillAfter(true);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(5000);
        image.startAnimation(anim);

        ConnectingMsg.setVisibility(View.VISIBLE);

        anim1 = new AlphaAnimation(0.0f, 1.0f);
        anim1.setDuration(500); //You can manage the time of the blink with this parameter
        anim1.setStartOffset(20);
        anim1.setRepeatMode(Animation.REVERSE);
        anim1.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim1);
    }


//    Establish connection with other divice
//    @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
//    @param secure Socket Security type - Secure (true) , Insecure (false)
    private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
