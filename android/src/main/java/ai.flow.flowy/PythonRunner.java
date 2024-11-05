package ai.flow.flowy;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.util.Log;

import org.kivy.android.PythonUtil;

import java.io.File;
import java.util.HashMap;

public class PythonRunner extends Service {
    private static final String TAG = "PythonRunner";
    private static final String ACTION_USB_PERMISSION = "ai.flow.flowy.USB_PERMISSION";

    private BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            System.out.println("RECEIVING INTENT: " + action);

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (device == null) {
                        return;
                    }
                    if (device.getVendorId() != 0xbbaa) {
                        Log.i(TAG, "Found USB device but not CommaAI vendor id");
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
                    ((UsbManager) context.getSystemService(Context.USB_SERVICE)).requestPermission(device, pendingIntent);
                }
            } else if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                            // bootstub = (device.getProductID() & 0xF0) == 0xe0
                            if (device.getVendorId() != 0xbbaa) {
                                Log.i(TAG, "Got non-CommaAI vendor id after request!");
                            }
                            UsbDeviceConnection usbDeviceConnection = usbManager.openDevice(device);
                            Log.i(TAG, "Permission granted for serial " + usbDeviceConnection.getSerial());
                            int fd = usbDeviceConnection.getFileDescriptor();
                            if (device.getProductId() == 0xddee) {
                                //We are in bootstub
                                Log.i(TAG, "Flashing Panda");
                                PythonRunner.flashNative(fd);
                            } else if (device.getProductId() == 0xddcc) {
                                //Not in bootstub, reset
                                Log.i(TAG, "Resetting Panda device to bootstub");
                                int upToDate = 1;//PythonRunner.run(fd);

//                                Log.i(TAG, "Returned "+upToDate);
                                if (upToDate != 0) {
                                    Log.i(TAG, "Panda is up to date!");
                                } else {
                                    Log.i(TAG, "Panda needs an update!");
//                                    PythonRunner.resetNative(fd, true);
                                }
                            } else {
                                Log.i(TAG, "Got unknown CommaAI product id");
//                                int upToDate = PythonRunner.run(fd);
//                                int upToDate2 = PythonRunner.run(fd);
                                Log.i(TAG, "Run was run");
//                                Log.i(TAG, "Returned "+upToDate+" and "+upToDate2);
                            }

                        }
                    } else {
                        Log.i(TAG, "Permission denied for device " + device);
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "CREATING PythonRunner");
        System.loadLibrary("pandaflash");
        String app_root = getFilesDir().getAbsolutePath() + "/app";
        File app_root_file = new File(app_root);
        PythonUtil.loadLibraries(app_root_file, new File(getApplicationInfo().nativeLibraryDir));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "STARTING PythonRunner");
        // Request permission for newly plugged devices
        IntentFilter attachFilter = new IntentFilter();
        attachFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        attachFilter.addAction(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, attachFilter, Context.RECEIVER_EXPORTED);

        // Request permission for already plugged devices
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Intent usbIntent = new Intent(ACTION_USB_PERMISSION);
        usbIntent.setPackage(this.getPackageName());
        PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, usbIntent, PendingIntent.FLAG_MUTABLE);

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, filter, Context.RECEIVER_EXPORTED);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        System.out.println("Number of devices found: " + deviceList.size());
        for (UsbDevice device : deviceList.values()) {
            if (device.getVendorId() != 0xbbaa) {
                Log.i(TAG, "Found USB device but not CommaAI vendor id");
            }
            manager.requestPermission(device, permissionIntent);
        }
        return START_NOT_STICKY;
    }

    public static native String resetNative(int fd, boolean enterBootstub);

    public static native String flashNative(int fd);

    public static native int run(int fd, String obj_path);

//    public static boolean doRun(){
//        return PythonRunner.run(0);
//    }

    public static void start(Context ctx) {
        Intent serviceIntent = new Intent(ctx, PythonRunner.class);
        ctx.startService(serviceIntent);
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }
}
