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

public class RotationVectorSensorAdapter extends AbstractSensorAdapter<Float32> {

    //    private volatile Imu imu; //TODO To Enable
    private Object quatTime;

    public RotationVectorSensorAdapter(AndroidNode node, Float32 message, String topicName) {
        super(node, message, topicName);
    }

    @Override
    public void publishSensorState() {
        synchronized (this.mutex) {
            //TODO To Enable
//            this.msg.setOrientation(this.imu.getOrientation());
//            this.msg.setOrientationCovariance(this.imu.getOrientationCovariance);

//            logger.debug("Publish Gyro value : " + this.lux);
//            System.out.println("Publish Gyro value : " + this.lux);
        }
        this.pub.publish(this.msg);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            synchronized (this.mutex) {
                //TODO To Enable
//                float[] quaternion = new float[4];
//                SensorManager.getQuaternionFromVector(quaternion, sensorEvent.values);
//                this.imu.getOrientation().setW(quaternion[0]);
//                this.imu.getOrientation().setX(quaternion[1]);
//                this.imu.getOrientation().setY(quaternion[2]);
//                this.imu.getOrientation().setZ(quaternion[3]);
//                double[] tmpCov = {0.001,0,0, 0,0.001,0, 0,0,0.001};// TODO Make Parameter
//                this.imu.setOrientationCovariance(tmpCov);
                this.quatTime = sensorEvent.timestamp;

//                logger.debug("Sensor Rotate value : " + this.lux);
//                System.out.println("Sensor Rotate value : " + this.lux);
            }
        }
    }
}
