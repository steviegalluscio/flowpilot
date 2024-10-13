package ai.flow.flowy;
//Java_org_jagheterfredrik_flowapp_ServicePandad_nativeInit

import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

import static android.app.PendingIntent.FLAG_MUTABLE;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

public class ServicePandad extends Service implements Runnable {

    private static final String TAG = "ServicePandad";
    // Thread for Python code
    private Thread applicationThread = null;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private BroadcastReceiver usbReceiver;

    public int startType() {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("pandad");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (applicationThread != null) {
            Log.v("python service", "service exists, do not start again");
            return startType();
        }
        // this.fd = intent.getIntExtra("fd", 0);
        // System.out.println("I was rang with " + this.fd);

        applicationThread = new Thread(this);
        applicationThread.start();

        System.out.println("Service pid is " + android.os.Process.myPid());
        return startType();
    }

    public static void start(Context ctx) {
        Intent serviceIntent = new Intent(ctx, ServicePandad.class);
        ctx.startService(serviceIntent);
    }

    @Override
    public void run(){
        usbReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                System.out.println("Receiving!");
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (this) {
                        UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if(device != null) {
                                // call method to set up device communication
                                UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                                UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(device);
                                // usbDeviceConnection.claimInterface(device.getInterface(1), true);
                                Log.d(TAG, "permission granted for serial "+usbDeviceConnection.getSerial());
                                nativeInit(usbDeviceConnection.getFileDescriptor());
                            }
                        }
                        else {
                            Log.d(TAG, "permission denied for device " + device);
                        }
                    }
                }
            }
        };

		System.out.println("launch 1");

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Intent usbIntent = new Intent(ACTION_USB_PERMISSION);
        usbIntent.setPackage(this.getPackageName());
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, usbIntent, FLAG_MUTABLE);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
		System.out.println("Number of devices found: "+deviceList.size());
        for (UsbDevice usbDevice : deviceList.values())
        {
            manager.requestPermission(usbDevice, permissionIntent);
        }
    }

    // Native part
    public static native void nativeInit(int fd);
}
