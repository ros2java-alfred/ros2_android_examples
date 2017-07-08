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

public class BarometerSensorAdapter extends AbstractSensorAdapter<Float32> {

    private volatile float pressure = 0f;

    public BarometerSensorAdapter(AndroidNode node, Float32 message, String topicName) {
        super(node, message, topicName);
    }

    @Override
    public void publishSensorState() {
        synchronized (this.mutex) {
            this.msg.setData(this.pressure);
            logger.debug("Publish Barometer value : " + this.pressure);
            System.out.println("Publish Barometer value : " + this.pressure);
        }
        this.pub.publish(this.msg);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            synchronized (this.mutex) {
                this.pressure = sensorEvent.values[0];
                logger.debug("Sensor Barometer value : " + this.pressure);
                System.out.println("Sensor Barometer value : " + this.pressure);
            }
        }
    }
}
