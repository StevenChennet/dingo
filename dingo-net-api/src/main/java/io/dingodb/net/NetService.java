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

package io.dingodb.net;

import io.dingodb.common.Location;
import io.dingodb.net.api.ApiRegistry;

public interface NetService extends AutoCloseable {

    static NetService getDefault() {
        return NetServiceProvider.getDefault().get();
    }

    /**
     * Return a new channel connected to the remote-end.
     *
     * @param location remote node
     * @return the channel connected to the remote node
     */
    Channel newChannel(Location location);

    /**
     * Returns new channel connected to the remote-end.
     *
     * @param location location
     * @return the channel connected to the remote node
     */
    Channel newChannel(Location location, boolean keepAlive);

    /**
     * Set {@link MessageListenerProvider} on the net service,  When the remote-end send a message to current
     * service, will create new {@link MessageListener} instance to listen new channel.
     */
    void setMessageListenerProvider(String tag, MessageListenerProvider listenerProvider);

    /**
     * Unset {@link MessageListenerProvider} on the net service.
     */
    void unsetMessageListenerProvider(String tag);

    void registerTagMessageListener(String tag, MessageListener listener);

    void unregisterTagMessageListener(String tag, MessageListener listener);

    /**
     * Listen the port, When receive message, notify the registered listener.
     *
     * @param port listen port
     */
    void listenPort(int port) throws Exception;

    /**
     * Cancel listen the port.
     * @param port port listen port
     */
    void cancelPort(int port) throws Exception;

    /**
     * Returns api registry instance, {@link ApiRegistry}.
     * @return api registry instance
     */
    ApiRegistry apiRegistry();
}
