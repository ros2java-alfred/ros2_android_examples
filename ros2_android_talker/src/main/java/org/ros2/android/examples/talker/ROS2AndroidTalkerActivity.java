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
import org.ros2.android.core.BaseRosService;
import org.ros2.android.core.node.AndroidNativeNode;
import org.ros2.android.core.node.AndroidNode;
import org.ros2.android.examples.hardware.sensor.BarometerSensorNode;
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

    private BaseRosService executor;

    private Button buttonStart;
    private Button buttonStop;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        this.buttonStart = (Button)findViewById(R.id.buttonStart);
        this.buttonStart.setOnClickListener(this); // Register the onClick listener with the implementation above

        this.buttonStop = (Button)findViewById(R.id.buttonStop);
        this.buttonStop.setOnClickListener(this); // Register the onClick listener with the implementation above
        this.buttonStop.setEnabled(false);

//        ROS2AndroidTalkerApplication app = (ROS2AndroidTalkerApplication)getApplication();
//        executor = app.getRosService();
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

        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);

        if (node == null) {
            node = new TalkerNode(this, "tessst");

//                nodeAccelrometer    = new AccelerometerSensorNode(this, "accel_node", 100, TimeUnit.NANOSECONDS);
//                nodeTemp            = new AmbientTemperatureSensorNode(this, "temp_node", 2, TimeUnit.SECONDS); // Disable android phone.
            nodeBarometer       = new BarometerSensorNode(this,"pressure_node", 1, TimeUnit.SECONDS);
//                nodeCompass         = new CompassSensorNode(this, "compass_node", 500, TimeUnit.NANOSECONDS);
//                nodeGyroscope       = new GyroscopeSensorNode(this, "gyro_node", 100, TimeUnit.NANOSECONDS);
//                nodeImu             = new ImuSensorNode(this, "imu_node", 100, TimeUnit.NANOSECONDS);
            nodeLight           = new LightSensorNode(this, "light_node", 500, TimeUnit.MILLISECONDS);
            nodeProximity       = new ProximitySensorNode(this, "proximity_node", 100, TimeUnit.MILLISECONDS);

            ROS2AndroidTalkerApplication app = (ROS2AndroidTalkerApplication) getApplication();
            executor = app.getRosService();
            executor.addNode(node);

            if (nodeAccelerometer != null)  { executor.addNode(nodeAccelerometer); }
            if (nodeTemp != null)           { executor.addNode(nodeTemp); }
            if (nodeBarometer != null)      { executor.addNode(nodeBarometer); }
            if (nodeCompass != null)        { executor.addNode(nodeCompass); }
            if (nodeGyroscope != null)      { executor.addNode(nodeGyroscope); }
            if (nodeImu != null)            { executor.addNode(nodeImu); }
            if (nodeLight != null)          { executor.addNode(nodeLight); }
            if (nodeProximity != null)      { executor.addNode(nodeProximity); }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private void stopListener() {
        Log.d(logtag,"onClick() called - stop button");
        Toast.makeText(this, "The Stop button was clicked.", Toast.LENGTH_LONG).show();

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);

        if (node != null)               { executor.removeNode(node); }

        if (nodeAccelerometer != null)  { executor.removeNode(nodeAccelerometer); }
        if (nodeTemp != null)           { executor.removeNode(nodeTemp); }
        if (nodeBarometer != null)      { executor.removeNode(nodeBarometer); }
        if (nodeCompass != null)        { executor.removeNode(nodeCompass); }
        if (nodeGyroscope != null)      { executor.removeNode(nodeGyroscope); }
        if (nodeImu != null)            { executor.removeNode(nodeImu); }
        if (nodeLight != null)          { executor.removeNode(nodeLight); }
        if (nodeProximity != null)      { executor.removeNode(nodeProximity); }

        Log.d(logtag,"onClick() ended - stop button");
    };


    @Override
    protected void onStart() {//activity is started and visible to the user
        Log.d(logtag,"onStart() called");
        super.onStart();
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
        super.onStop();

    }
    @Override
    protected void onDestroy() {//android has killed this activity
        Log.d(logtag,"onDestroy() called");
        super.onDestroy();
    }
}
