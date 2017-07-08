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
package org.ros2.android.hardware;

import android.hardware.SensorEvent;

import org.ros2.android.core.node.AndroidNode;

import java.util.ArrayList;
import java.util.Collection;

import sensor_msgs.msg.Imu;

public class AccelerometerSensorAdapter extends AbstractSensorAdapter<Imu> {

    private volatile Imu imu;
    private long accelTime;

    public AccelerometerSensorAdapter(AndroidNode node, Imu message, String topicName) {
        super(node, message, topicName);
    }

    @Override
    public void publishSensorState() {
        synchronized (this.mutex) {
            this.msg = this.imu;
//            logger.debug("Publish light value : " + this.lux);
//            System.out.println("Publish light value : " + this.lux);
        }
        this.pub.publish(this.msg);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this.mutex) {
            this.imu.getLinearAcceleration().setX(sensorEvent.values[0]);
            this.imu.getLinearAcceleration().setY(sensorEvent.values[1]);
            this.imu.getLinearAcceleration().setZ(sensorEvent.values[2]);

            Collection<Double> tmpCov = new ArrayList<>();
            tmpCov.add(0.01d);
            tmpCov.add(0d);
            tmpCov.add(0d);
            tmpCov.add(0d);
            tmpCov.add(0.01d);
            tmpCov.add(0d);
            tmpCov.add(0d);
            tmpCov.add(0d);
            tmpCov.add(0.01d);// TODO Make Parameter
            this.imu.setLinearAccelerationCovariance(tmpCov);
            this.accelTime = sensorEvent.timestamp;
//            logger.debug("Sensor light value : " + this.lux);
//            System.out.println("Sensor light value : " + this.lux);
        }
    }
}
