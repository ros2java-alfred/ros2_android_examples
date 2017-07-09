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
import org.ros2.android.hardware.sensor.CompassSensorAdapter;

import java.util.concurrent.TimeUnit;

import sensor_msgs.msg.MagneticField;

public class CompassSensorNode extends AbstractSensorNode<MagneticField> {

//    private static final String TAG = "CompassSensorNode";
//    private static final Logger logger = LoggerFactory.getLogger(CompassSensorNode.class);

    public CompassSensorNode(Context context, String name, long time, TimeUnit timeUnit) {
        super(context, name, Sensor.TYPE_MAGNETIC_FIELD, time, timeUnit);

        // Set Adapter of Android Sensor to ROS2 node.
        this.sensorAdapter = new CompassSensorAdapter(this, new MagneticField(), "magnet");
    }
}
