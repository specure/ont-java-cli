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
package at.alladin.rmbt.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberUtil {

    // logger instance
    private static final Logger logger = LoggerFactory.getLogger(NumberUtil.class);

    /**
     * @param number
     * @return
     */
    public final static boolean isInteger(Object number) {
        try {
            Integer.parseInt(String.valueOf(number));
            return true;
        } catch (Throwable t) {
            logger.error(t.getMessage());
            return false;
        }
    }
}
