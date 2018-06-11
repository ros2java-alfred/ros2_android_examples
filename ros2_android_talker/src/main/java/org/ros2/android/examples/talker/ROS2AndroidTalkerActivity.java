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
package org.ros2.android.examples.talker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.ros2.android.core.BaseRosActivity;
import org.ros2.android.core.RosConfig;
import org.ros2.android.core.RosManager;
import org.ros2.android.core.node.AndroidNativeNode;
import org.ros2.android.core.node.AndroidNode;
import org.ros2.android.examples.hardware.sensor.BarometerSensorNode;
import org.ros2.android.examples.hardware.sensor.CameraSensorNode;
import org.ros2.android.examples.hardware.sensor.LightSensorNode;
import org.ros2.android.examples.hardware.sensor.ProximitySensorNode;

import org.ros2.rcljava.node.topic.Publisher;
import org.ros2.rcljava.time.WallTimer;
import org.ros2.rcljava.time.WallTimerCallback;

import java.util.concurrent.TimeUnit;

public class ROS2AndroidTalkerActivity extends BaseRosActivity implements OnClickListener {

    private class TalkerNode extends AndroidNativeNode implements WallTimerCallback {
        int i = 0;
        private Publisher<std_msgs.msg.String> pub;
        private WallTimer timer;

        TalkerNode(Context ctx, String name) {
            super(name, ctx);

            this.pub = this.createPublisher(
                    std_msgs.msg.String.class,
                    "chatter");

            this.timer = this.createWallTimer(500, TimeUnit.MILLISECONDS, this);
        }

        @Override
        public void tick() {
            std_msgs.msg.String msg = new std_msgs.msg.String();
            msg.setData("Hello World: " + ++i);

            System.out.println("Publishing: \"" + msg.getData() + "\"");
            this.pub.publish(msg);
        }

        @Override
        public void dispose() {
            this.timer.dispose();
            this.pub.dispose();
            super.dispose();
        }
    }

    private static String logtag = "ROS2TalkerActivity";    //for use as the tag when logging

    private AndroidNode nodeAccelerometer = null;  // Disable bug covariance.
    private AndroidNode nodeTemp = null;           // Disable android phone.
    private AndroidNode nodeBarometer = null;
    private AndroidNode nodeCompass = null;        // Disable bug covariance.
    private AndroidNode nodeGyroscope = null;      // Disable bug covariance.
    private AndroidNode nodeImu = null;            // Disable bug covariance.
    private AndroidNode nodeLight = null;
    private AndroidNode nodeProximity = null;

    private AndroidNode node;

    private RosConfig config;
    private RosManager manager;

    private Button buttonStart;
    private Button buttonStop;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        this.buttonStart = this.findViewById(R.id.buttonStart);
        this.buttonStart.setOnClickListener(this); // Register the onClick listener with the implementation above

        this.buttonStop = this.findViewById(R.id.buttonStop);
        this.buttonStop.setOnClickListener(this); // Register the onClick listener with the implementation above
        this.buttonStop.setEnabled(false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStart:
                this.startListener();
                break;
            case R.id.buttonStop:
                this.stopListener();
                break;
        }
    }

    //Create an anonymous implementation of OnClickListener
    private void startListener() {
        Log.d(logtag,"onClick() called - start button");
        Toast.makeText(this, "The Start button was clicked.", Toast.LENGTH_LONG).show();
        Log.d(logtag,"onClick() ended - start button");

        this.buttonStart.setEnabled(false);
        this.buttonStop.setEnabled(true);

        if (this.node == null) {
            this.node = new TalkerNode(this, "talker");

            CameraSensorNode cameraSensorNode = null;
            if (CameraSensorNode.checkCameraHardware(this)) {
                cameraSensorNode = new CameraSensorNode("camera", this);
            }

//                this.nodeAccelrometer    = new AccelerometerSensorNode(this, "accel_node", 100, TimeUnit.NANOSECONDS);
//                this.nodeTemp            = new AmbientTemperatureSensorNode(this, "temp_node", 2, TimeUnit.SECONDS); // Disable android phone.
            this.nodeBarometer       = new BarometerSensorNode(this,"pressure_node", 1, TimeUnit.SECONDS);
//                this.nodeCompass         = new CompassSensorNode(this, "compass_node", 500, TimeUnit.NANOSECONDS);
//                this.nodeGyroscope       = new GyroscopeSensorNode(this, "gyro_node", 100, TimeUnit.NANOSECONDS);
//                this.nodeImu             = new ImuSensorNode(this, "imu_node", 100, TimeUnit.NANOSECONDS);
            this.nodeLight           = new LightSensorNode(this, "light_node", 500, TimeUnit.MILLISECONDS);
            this.nodeProximity       = new ProximitySensorNode(this, "proximity_node", 100, TimeUnit.MILLISECONDS);

            if (this.manager != null) {
                this.manager.addNode(this.node);
                if (cameraSensorNode != null) { this.manager.addNode(cameraSensorNode); }

                if (this.nodeAccelerometer != null) { this.manager.addNode(this.nodeAccelerometer); }
                if (this.nodeTemp != null) { this.manager.addNode(this.nodeTemp); }
                if (this.nodeBarometer != null) { this.manager.addNode(this.nodeBarometer); }
                if (this.nodeCompass != null) { this.manager.addNode(this.nodeCompass); }
                if (this.nodeGyroscope != null) { this.manager.addNode(this.nodeGyroscope); }
                if (this.nodeImu != null) { this.manager.addNode(this.nodeImu); }
                if (this.nodeLight != null) { this.manager.addNode(this.nodeLight); }
                if (this.nodeProximity != null) { this.manager.addNode(this.nodeProximity); }
            }
        }
    }

    // Create an anonymous implementation of OnClickListener
    private void stopListener() {
        Log.d(logtag,"onClick() called - stop button");
        Toast.makeText(this, "The Stop button was clicked.", Toast.LENGTH_LONG).show();

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);

        if (this.manager != null) {
            if (node != null) { this.manager.removeNode(node); }

            if (nodeAccelerometer != null) { this.manager.removeNode(nodeAccelerometer); }
            if (nodeTemp != null) { this.manager.removeNode(nodeTemp); }
            if (nodeBarometer != null) { this.manager.removeNode(nodeBarometer); }
            if (nodeCompass != null) { this.manager.removeNode(nodeCompass); }
            if (nodeGyroscope != null) { this.manager.removeNode(nodeGyroscope); }
            if (nodeImu != null) { this.manager.removeNode(nodeImu); }
            if (nodeLight != null) { this.manager.removeNode(nodeLight); }
            if (nodeProximity != null) { this.manager.removeNode(nodeProximity); }
        }
        Log.d(logtag,"onClick() ended - stop button");
    }


    @Override
    protected void onStart() {//activity is started and visible to the user
        Log.d(logtag,"onStart() called");
        super.onStart();

        this.manager = new RosManager(this, new Runnable() {
            @Override
            public void run() {
                config = manager.getConfig(RosConfig.CONFIG_TYPE_DEFAULT);
                manager.connect(config);
                startListener();
            }
        });
    }
    @Override
    protected void onResume() {//activity was resumed and is visible again
        Log.d(logtag,"onResume() called");
        super.onResume();


    }
    @Override
    protected void onPause() { //device goes to sleep or another activity appears
        Log.d(logtag,"onPause() called");//another activity is currently running (or user has pressed Home)
        super.onPause();

    }
    @Override
    protected void onStop() { //the activity is not visible anymore
        Log.d(logtag,"onStop() called");

        if (this.manager != null) {
            this.manager.disconnect();
        }

        super.onStop();
    }
    @Override
    protected void onDestroy() {//android has killed this activity
        Log.d(logtag,"onDestroy() called");
        super.onDestroy();
    }
}
