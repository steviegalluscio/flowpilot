package ai.flow.flowy;
//Java_org_jagheterfredrik_flowapp_ServicePandad_nativeInit

import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

public class ServiceModelparsed extends Service implements Runnable {

    private static final String TAG = "ServiceModelparsed";
    // Thread for Python code
    private Thread applicationThread = null;

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
        System.loadLibrary("modelparsed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (applicationThread != null) {
            Log.v("python service", "service exists, do not start again");
            return startType();
        }
        applicationThread = new Thread(this);
        applicationThread.start();

        System.out.println("Service pid is " + android.os.Process.myPid());
        return startType();
    }

    public static void start(Context ctx) {
        Intent serviceIntent = new Intent(ctx, ServiceModelparsed.class);
        ctx.startService(serviceIntent);
    }

    @Override
    public void run(){
        nativeInit();
    }

    // Native part
    public static native void nativeInit();
}
