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

import org.ros2.android.hardware.sensor.AbstractSensorNode;
import org.ros2.android.hardware.sensor.ImuSensorAdapter;

import java.util.concurrent.TimeUnit;

import sensor_msgs.msg.Imu;

public class ImuSensorNode extends AbstractSensorNode<Imu> {

//    private static final String TAG = "ImuSensorNode";
//    private static final Logger logger = LoggerFactory.getLogger(ImuSensorNode.class);

    public ImuSensorNode(Context context, String name, int typeSensor, long time, TimeUnit timeUnit) {
        super(context, name, typeSensor, time, timeUnit);

        // Set Adapter of Android Sensor to ROS2 node.
        this.sensorAdapter = new ImuSensorAdapter(this, new Imu(), "imu");
    }
}
