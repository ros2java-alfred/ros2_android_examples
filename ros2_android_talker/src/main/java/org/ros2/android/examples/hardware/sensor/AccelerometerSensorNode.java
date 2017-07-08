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
package org.ros2.android.examples.hardware.sensor;

import android.content.Context;
import android.hardware.Sensor;

import org.ros2.android.hardware.sensor.AbstractSensorNode;
import org.ros2.android.hardware.sensor.AccelerometerSensorAdapter;

import java.util.concurrent.TimeUnit;

import std_msgs.msg.Float32;

//import sensor_msgs.msg.Imu; //TODO To Enable

public class AccelerometerSensorNode extends AbstractSensorNode<Float32> {

//    private static final String TAG = "AccelerometerSensorNode";
//    private static final Logger logger = LoggerFactory.getLogger(AccelerometerSensorNode.class);

    public AccelerometerSensorNode(Context context, String name, long time, TimeUnit timeUnit) {
        super(context, name, Sensor.TYPE_ACCELEROMETER, time, timeUnit);

        // Set Adapter of Android Sensor to ROS2 node.
        this.sensorAdapter = new AccelerometerSensorAdapter(this, new Float32(), "accel");
    }
}
