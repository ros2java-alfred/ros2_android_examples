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
package org.ros2.android.examples.listener;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros2.android.core.BaseRosActivity;
import org.ros2.android.core.RosConfig;
import org.ros2.android.core.RosManager;
import org.ros2.android.core.node.AndroidNativeNode;
import org.ros2.android.core.node.AndroidNode;
import org.ros2.rcljava.node.topic.SubscriptionCallback;
import org.ros2.rcljava.node.topic.Subscription;

public class ROS2AndroidListenerActivity extends BaseRosActivity implements OnClickListener {

    private class ListenerNode extends AndroidNativeNode {
        private Subscription<std_msgs.msg.String> sub;

        ListenerNode(Context ctx, String name) {
            super(name, ctx);

            this.sub = this.<std_msgs.msg.String>createSubscription(
                    std_msgs.msg.String.class, "chatter",
                    new SubscriptionCallback<std_msgs.msg.String>() {
                        @Override
                        public void dispatch(std_msgs.msg.String msg) {
                            publishProgress("I heard: " + msg.getData());
                        }
                    }
            );

        }

        protected void publishProgress(String progress) {
            listenerView.append(progress + "\r\n");
        }

        @Override
        public void dispose() {
            this.sub.dispose();
            super.dispose();
        }
    }

    private static String logtag = "ROS2AndroidListenerActivity";//for use as the tag when logging

    private AndroidNode node;

    private RosConfig config;
    private RosManager manager;

    private Button buttonStart;
    private Button buttonStop;

    private TextView listenerView;

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

        listenerView = this.findViewById(R.id.listenerView);
        listenerView.setMovementMethod(new ScrollingMovementMethod());
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
        Toast.makeText(ROS2AndroidListenerActivity.this, "The Start button was clicked.", Toast.LENGTH_LONG).show();
        Log.d(logtag,"onClick() ended - start button");

        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);

        if (this.node == null) {
            this.node = new ListenerNode(this, "listener");

            if (this.manager != null) {
                this.manager.addNode(this.node);
            }
        }
    };

    // Create an anonymous implementation of OnClickListener
    private void stopListener() {
        Log.d(logtag,"onClick() called - stop button");
        Toast.makeText(ROS2AndroidListenerActivity.this, "The Stop button was clicked.", Toast.LENGTH_LONG).show();

        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);

        if (this.manager != null) {
            if (node != null) {
                this.manager.removeNode(node);
            }
        }
        Log.d(logtag,"onClick() ended - stop button");
    };


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
