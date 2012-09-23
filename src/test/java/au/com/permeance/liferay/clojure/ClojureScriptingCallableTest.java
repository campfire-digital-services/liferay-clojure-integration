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

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.liferay.portal.kernel.util.StringPool.BLANK;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public class ClojureScriptingCallableTest {

    @Test
    public void testCall() throws Exception {
        ClojureScriptingCallable callable = new ClojureScriptingCallable(Collections.<String, Object>singletonMap("one", 1),
                                                                         Collections.<String>singleton("result"),
                                                                         "(let [one (get input-objects \"one\")] {\"result\" (+ one one)})");

        Map<String, Object> result = callable.call();

        assertThat(result)
                .hasSize(1)
                .includes(entry("result", 2L));
    }

    @Test
    public void testCallWithNoOutputNames() throws Exception {
        ClojureScriptingCallable callable = new ClojureScriptingCallable(Collections.<String, Object>singletonMap("one", 1),
                                                                         Collections.<String>emptySet(),
                                                                         "(let [one (get input-objects \"one\")] {\"result\" (+ one one)})");

        Map<String, Object> result = callable.call();

        assertThat(result).isEmpty();
    }

    @Test
    public void testCallWithNullOutputNames() throws Exception {
        ClojureScriptingCallable callable = new ClojureScriptingCallable(Collections.<String, Object>singletonMap("one", 1),
                                                                         null,
                                                                         "(let [one (get input-objects \"one\")] {\"result\" (+ one one)})");

        Map<String, Object> result = callable.call();

        assertThat(result).isEmpty();
    }

    @Test
    public void testCallWithNonMapReturnType() throws Exception {
        ClojureScriptingCallable callable = new ClojureScriptingCallable(Collections.<String, Object>singletonMap("one", 1),
                                                                         Collections.<String>singleton("result"),
                                                                         "(let [one (get input-objects \"one\")] (+ one one))");

        Map<String, Object> result = callable.call();

        assertThat(result).isEmpty();
    }

    @Test
    public void testRetainKeys() throws Exception {
        Map<String, Integer> map = new LinkedHashMap<String, Integer>(2);
        map.put("one", 1);
        map.put("two", 2);

        Set<String> keys = new HashSet<String>(2);
        keys.add("one");
        keys.add("three");

        ClojureScriptingCallable callable = new ClojureScriptingCallable(Collections.<String, Object>emptyMap(),
                                                                         Collections.<String>emptySet(),
                                                                         BLANK);
        Map<String, Integer> result = callable.retainKeys(map, keys);

        assertThat(result)
                .hasSize(1)
                .includes(entry("one", 1));
    }

}
