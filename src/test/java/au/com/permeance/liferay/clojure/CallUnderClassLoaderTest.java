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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.Callable;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CallUnderClassLoaderTest {

    @Mock
    private ClassLoader mockClassLoader;

    @Test
    public void testCall() throws Exception {
        MockCallable mockCallable = new MockCallable();
        CallUnderClassLoader<Object> callUnderClassLoader = new CallUnderClassLoader<Object>(mockCallable, mockClassLoader);
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();

        callUnderClassLoader.call();

        assertThat(Thread.currentThread().getContextClassLoader()).isSameAs(originalClassLoader);
        assertThat(mockCallable.callingClassLoader).isSameAs(mockClassLoader);
    }

    private class MockCallable implements Callable<Object> {

        private ClassLoader callingClassLoader;

        @Override
        public Object call() throws Exception {
            callingClassLoader = Thread.currentThread().getContextClassLoader();
            return null;
        }

    }

}
