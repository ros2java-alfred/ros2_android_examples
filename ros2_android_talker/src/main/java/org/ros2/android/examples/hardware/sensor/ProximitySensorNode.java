package org.ros2.android.examples.hardware.sensor;

import android.content.Context;
import android.hardware.Sensor;

import org.ros2.android.hardware.sensor.AbstractSensorNode;
import org.ros2.android.hardware.sensor.ProximitySensorAdapter;

import java.util.concurrent.TimeUnit;

import std_msgs.msg.Float32;

/**
 * Created by micky on 7/8/17.
 */

public class ProximitySensorNode extends AbstractSensorNode<Float32> {

//    private static final String TAG = "ProximitySensorNode";
//    private static final Logger logger = LoggerFactory.getLogger(ProximitySensorNode.class);

    public ProximitySensorNode(Context context, String name, long time, TimeUnit timeUnit) {
        super(context, name, Sensor.TYPE_PROXIMITY, time, timeUnit);

        // Set Adapter of Android Sensor to ROS2 node.
        this.sensorAdapter = new ProximitySensorAdapter(this, new Float32(), "proximity");
    }
}
