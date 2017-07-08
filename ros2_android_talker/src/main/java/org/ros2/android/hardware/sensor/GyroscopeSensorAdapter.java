/* Copyright 2017 Mickael Gaillard <mick.gaillard@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ros2.android.hardware.sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import org.ros2.android.core.node.AndroidNode;

import std_msgs.msg.Float32;

// import sensor_msgs.msg.Imu; //TODO To Enable

public class GyroscopeSensorAdapter extends AbstractSensorAdapter<Float32> {

//    private volatile Imu imu; //TODO To Enable

    public GyroscopeSensorAdapter(AndroidNode node, Float32 message, String topicName) {
        super(node, message, topicName);
    }

    @Override
    public void publishSensorState() {
        synchronized (this.mutex) {
            //TODO To Enable
//            this.msg.setAngularVelocity(this.imu.getAngularVelocity());
//            this.msg.setAngularVelocityCovariance(this.imu.getAngularVelocityCovariance);

//            logger.debug("Publish Gyro value : " + this.lux);
//            System.out.println("Publish Gyro value : " + this.lux);
        }
        this.pub.publish(this.msg);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            synchronized (this.mutex) {
                //TODO To Enable
//                this.imu.getAngularVelocity().setX(sensorEvent.values[0]);
//                this.imu.getAngularVelocity().setY(sensorEvent.values[1]);
//                this.imu.getAngularVelocity().setZ(sensorEvent.values[2]);
//                double[] tmpCov = {0.0025,0,0, 0,0.0025,0, 0,0,0.0025};// TODO Make Parameter
//                this.imu.setAngularVelocityCovariance(tmpCov);

//                logger.debug("Sensor Gyro value : " + this.lux);
//                System.out.println("Sensor Gyro value : " + this.lux);
            }
        }
    }
}
