/*
 * Copyright 2021 DataCanvas
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

package io.dingodb.expr.runtime;

import java.io.Serializable;
import javax.annotation.Nullable;

public interface EvalContext extends Serializable {
    /**
     * Get the value of a variable by its id.
     *
     * @param id the id of the variable
     * @return the value of the variable
     */
    Object get(Object id);

    /**
     * Set the value of a variable by its id.
     *
     * @param id    the id of the variable
     * @param value the new value of the variable
     */
    void set(Object id, Object value);

    @Nullable
    default EvalEnv getEnv() {
        return null;
    }
}
