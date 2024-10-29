package ai.flow.android.sensor;

import android.content.Context;
import android.content.Intent;

import ai.flow.sensor.SensorInterface;

import ai.flow.flowy.ServiceModelparsed;

public class ModelparsedManager extends SensorInterface {
    private Context ctx;

    public ModelparsedManager(Context ctx) {
        this.ctx = ctx;
    }

    public void start() {
        System.out.println("Modelparsed was called, starting services!");
        ServiceModelparsed.start(ctx);
        // Context ctx = this.ctx
//        new java.util.Timer().schedule(
//            new java.util.TimerTask() {
//                @Override
//                public void run() {
//                    System.out.println("Aaaand go!");
//                    ServiceModelparsed.start(ctx);
//                }
//            },
//            5000
//        );
        
    }
    public void stop() {
        this.ctx.stopService(new Intent(this.ctx, ServiceModelparsed.class));
    }
}