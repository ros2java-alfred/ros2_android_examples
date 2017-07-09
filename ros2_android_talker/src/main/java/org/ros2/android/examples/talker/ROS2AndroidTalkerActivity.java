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
import org.ros2.android.examples.hardware.sensor.LightSensorNode;
import org.ros2.android.examples.hardware.sensor.ProximitySensorNode;
import org.ros2.rcljava.node.topic.Publisher;
import org.ros2.rcljava.time.WallTimer;
import org.ros2.rcljava.time.WallTimerCallback;

import java.util.concurrent.TimeUnit;

public class ROS2AndroidTalkerActivity extends BaseRosActivity {

    private class TalkerNode extends AndroidNativeNode implements WallTimerCallback {
        int i = 0;
        private Publisher<std_msgs.msg.String> pub;
        private WallTimer timer;

        public TalkerNode(Context ctx, String name) {
            super(name, ctx);

            this.pub = this.<std_msgs.msg.String>createPublisher(
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

    private static String logtag = "ROS2TalkerActivity";//for use as the tag when logging

    private AndroidNode node;
    private AndroidNode nodeLight;
    private AndroidNode nodeTemp;
    private AndroidNode nodeProximity;

    private BaseRosService executor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(startListener); // Register the onClick listener with the implementation above

        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(stopListener); // Register the onClick listener with the implementation above
        buttonStop.setEnabled(false);

//        ROS2AndroidTalkerApplication app = (ROS2AndroidTalkerApplication)getApplication();
//        executor = app.getRosService();
    }

    //Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - start button");
            Toast.makeText(ROS2AndroidTalkerActivity.this, "The Start button was clicked.", Toast.LENGTH_LONG).show();
            Log.d(logtag,"onClick() ended - start button");
            Button buttonStart = (Button)findViewById(R.id.buttonStart);
            Button buttonStop = (Button)findViewById(R.id.buttonStop);
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);

            if (node == null) {
                node = new TalkerNode(ROS2AndroidTalkerActivity.this, "tessst");
                nodeLight = new LightSensorNode(ROS2AndroidTalkerActivity.this, "light_node", 500, TimeUnit.MILLISECONDS);
//                nodeTemp = new AmbientTemperatureSensorNode(ROS2AndroidTalkerActivity.this, "temp_node", 2, TimeUnit.SECONDS);
                nodeProximity = new ProximitySensorNode(ROS2AndroidTalkerActivity.this, "proximity_node", 100, TimeUnit.MILLISECONDS);

                ROS2AndroidTalkerApplication app = (ROS2AndroidTalkerApplication) getApplication();
                executor = app.getRosService();
                executor.addNode(node);
                executor.addNode(nodeLight);
//                executor.addNode(nodeTemp);
                executor.addNode(nodeProximity);
            }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - stop button");
            Toast.makeText(ROS2AndroidTalkerActivity.this, "The Stop button was clicked.", Toast.LENGTH_LONG).show();

            executor.removeNode(node);
            executor.removeNode(nodeLight);
//            executor.removeNode(nodeTemp);
            executor.removeNode(nodeProximity);

            Button buttonStart = (Button)findViewById(R.id.buttonStart);
            Button buttonStop = (Button)findViewById(R.id.buttonStop);
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
            Log.d(logtag,"onClick() ended - stop button");
        }
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
