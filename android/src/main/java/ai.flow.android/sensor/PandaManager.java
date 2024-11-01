package ai.flow.android.sensor;

import android.content.Context;

import ai.flow.sensor.SensorInterface;

import ai.flow.flowy.ServicePandad;

public class PandaManager implements SensorInterface {
    private Context ctx;

    public PandaManager(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void dispose() {}

    @Override
    public void stop() {}

    public void start() {
        ServicePandad.start(this.ctx);
    }
}