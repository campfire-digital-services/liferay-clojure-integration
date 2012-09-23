/*
 * This file is part of liferay-clojure-integration.
 *
 * liferay-clojure-integration is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * liferay-clojure-integration is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * liferay-clojure-integration. If not, see <http://www.gnu.org/licenses/>.
 */
package au.com.permeance.liferay.clojure;

import java.util.concurrent.Callable;

public class CallUnderClassLoader<V> implements Callable<V> {

    private transient final Callable<V> callable;

    private transient final ClassLoader callClassLoader;

    public CallUnderClassLoader(final Callable<V> callable,
                                final ClassLoader callClassLoader) {
        this.callable = callable;
        this.callClassLoader = callClassLoader;
    }

    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public V call() throws Exception {
        @SuppressWarnings("PMD.DoNotUseThreads")
        final Thread thread = Thread.currentThread();
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        final ClassLoader threadClassLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(callClassLoader);
        try {
            return callable.call();
        }
        finally {
            thread.setContextClassLoader(threadClassLoader);
        }
    }

}
