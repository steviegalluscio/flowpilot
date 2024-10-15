package ai.flow.android.sensor;

import android.content.Context;
import android.content.Intent;

import ai.flow.sensor.SensorInterface;

import ai.flow.python.ServiceCalibrationd;
import ai.flow.python.ServiceControlsd;
import ai.flow.python.ServicePlannerd;
import ai.flow.python.ServiceRadard;
import ai.flow.python.ServiceFlowreset;

import ai.flow.flowy.ServiceModelparsed;

public class OnroadManager extends SensorInterface {
    private Context ctx;

    public OnroadManager(Context ctx) {
        this.ctx = ctx;
    }

    public void start() {
        System.out.println("Onroad was called, starting services!");
        ServiceModelparsed.start(this.ctx);
    }

    public void stop() {
        ServiceFlowreset.start(this.ctx, "");
        this.ctx.stopService(new Intent(this.ctx, ServiceControlsd.class));
        this.ctx.stopService(new Intent(this.ctx, ServiceRadard.class));
		this.ctx.stopService(new Intent(this.ctx, ServiceCalibrationd.class));
		this.ctx.stopService(new Intent(this.ctx, ServicePlannerd.class));

		ServiceControlsd.start(this.ctx, "");
		ServiceRadard.start(this.ctx, "");
		ServiceCalibrationd.start(this.ctx, "");
		ServicePlannerd.start(this.ctx, "");
    }
}