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

import com.liferay.portal.kernel.scripting.ExecutionException;
import com.liferay.portal.kernel.scripting.ScriptingException;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.liferay.portal.kernel.util.StringPool.BLANK;
import static java.util.Collections.singleton;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;

public class ClojureScriptingExecutorTest {

    @Test
    public void testEval() throws Exception {
        ClojureScriptingExecutor executor = new ClojureScriptingExecutor();

        Map<String, Object> result = executor.eval(Collections.<String>emptySet(),
                                                   Collections.<String, Object>singletonMap("one", 1),
                                                   Collections.<String>singleton("result"),
                                                   "(let [one (get input-objects \"one\")] {\"result\" (+ one one)})");

        assertThat(result)
                .hasSize(1)
                .includes(entry("result", 2L));
    }

    @Test(expected = ExecutionException.class)
    public void testEvalWithAllowedClasses() throws Exception {
        ClojureScriptingExecutor executor = new ClojureScriptingExecutor();

        executor.eval(singleton("class"),
                      Collections.<String, Object>emptyMap(),
                      Collections.<String>emptySet(),
                      BLANK);
    }

    @Test
    public void testGetLanguage() throws Exception {
        ClojureScriptingExecutor executor = new ClojureScriptingExecutor();

        String language = executor.getLanguage();

        assertThat(language).isEqualTo("clojure");
    }


    @Test
    public void testCreateCallableForScript() throws Exception {
        ClojureScriptingExecutor executor = new ClojureScriptingExecutor();

        Callable<Map<String, Object>> callable = executor.createCallableForScript(Collections.<String, Object>singletonMap("one", 1),
                                                                                  Collections.<String>singleton("result"),
                                                                                  "(let [one (get input-objects \"one\")] {\"result\" (+ one one)})");
        Map<String, Object> result = callable.call();

        assertThat(callable).isInstanceOf(CallUnderClassLoader.class);
        assertThat(result)
                .hasSize(1)
                .includes(entry("result", 2L));
    }

    @Test(expected = ScriptingException.class)
    public void testCallAndWrapException() throws Exception {
        ClojureScriptingExecutor executor = new ClojureScriptingExecutor();

        executor.callAndWrapException(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                throw new Exception();
            }
        });
    }

}
