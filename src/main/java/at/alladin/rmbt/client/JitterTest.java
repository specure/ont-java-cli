/*******************************************************************************
 * Copyright 2015 SPECURE GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package at.alladin.rmbt.client;

import at.alladin.rmbt.client.v2.task.service.TestSettings;

public class JitterTest extends VoipTest {

    public JitterTest(RMBTClient client, TestSettings nnTestSettings, boolean onlyVoipTest) {
        super(client, nnTestSettings, onlyVoipTest);
    }

    public JitterTest(RMBTClient client, TestSettings nnTestSettings) {
        super(client, nnTestSettings);
    }

    @Override
    protected String getTestId() {
        return RMBTClient.TASK_JITTER;
    }
}