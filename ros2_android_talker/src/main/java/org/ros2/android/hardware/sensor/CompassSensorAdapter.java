package org.ros2.android.hardware.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import org.ros2.android.core.node.AndroidNode;

import std_msgs.msg.Float32;

//import sensor_msgs.msg.Imu; //TODO To Enable

public class CompassSensorAdapter extends AbstractSensorAdapter<Float32> {

    private long magneTime;

    public CompassSensorAdapter(AndroidNode node, Float32 message, String topicName) {
        super(node, message, topicName);
    }

    @Override
    public void publishSensorState() {
        synchronized (this.mutex) {
            //TODO

//            logger.debug("Publish Imu value : " + this.imu);
//            System.out.println("Publish Imu value : " + this.imu);
        }
        this.pub.publish(this.msg);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            synchronized (this.mutex) {
                //TODO

                this.magneTime = sensorEvent.timestamp;

//                logger.debug("Sensor Imu value : " + this.imu);
//                System.out.println("Sensor Imu value : " + this.imu);
            }
        }
    }
}
