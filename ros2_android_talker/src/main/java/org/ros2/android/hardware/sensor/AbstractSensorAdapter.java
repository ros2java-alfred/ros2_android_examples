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

import org.ros2.android.core.node.AndroidNode;
import org.ros2.rcljava.internal.message.Message;
import org.ros2.rcljava.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSensorAdapter<T extends Message> implements SensorAdapter<T> {

    private static final String TAG = "LightSensorAdapter";
    protected static final Logger logger = LoggerFactory.getLogger(AbstractSensorAdapter.class);

    protected String topicName = "_undefine!";
    protected T msg;
    protected final AndroidNode node;
    protected Publisher<T> pub;

    protected Object mutex = new Object();

    public AbstractSensorAdapter(AndroidNode node, T message, String topicName) {
        this.node = node;
        this.topicName = topicName;

        this.msg = message;
        if (this.topicName != null) {
            this.pub = this.node.<T>createPublisher(
                    (Class<T>) this.msg.getClass(),
                    this.topicName);
        }
    }

    public AndroidNode getNode() {
        return this.node;
    }

    @Override
    public synchronized T getMessage() {
        return this.msg;
    }
}
