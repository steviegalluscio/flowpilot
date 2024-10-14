package ai.flow.android.sensor;

import android.content.Context;

import ai.flow.sensor.SensorInterface;

import ai.flow.python.ServiceCalibrationd;
import ai.flow.python.ServiceControlsd;
import ai.flow.python.ServicePlannerd;
import ai.flow.python.ServiceRadard;

import ai.flow.flowy.ServiceModelparsed;

public class OnroadManager extends SensorInterface {
    private Context ctx;

    public OnroadManager(Context ctx) {
        this.ctx = ctx;
    }

    public void start() {
        System.out.println("Onroad was called, starting services!");
        ServiceModelparsed.start(this.ctx);

		// ServiceControlsd.start(this.ctx, "");
		ServiceRadard.start(this.ctx, "");
		ServiceCalibrationd.start(this.ctx, "");
		ServicePlannerd.start(this.ctx, "");
    }
}