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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import permeance.liferay.clojure.ClojureScriptable;
import permeance.liferay.clojure.ClojureScriptableImpl;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

/**
 * This class wraps the clojure generated ClojureScriptable protocol and ClojureScriptableImpl implementation classes
 * for execution of scripts from within Liferay.
 */
public class ClojureScriptingCallable implements Callable<Map<String, Object>> {

    /**
     * Stores the logger for reporting errors.
     */
    private static final Log LOG = LogFactoryUtil.getLog(ClojureScriptingCallable.class);

    /**
     * Stores a reference to an object implementing the ClojureScriptable protocol for executing scripts.
     */
    private static final ClojureScriptable CLOJURE_SCRIPTABLE = new ClojureScriptableImpl();

    /**
     * Stores the map of names to values of input objects, as supplied through
     * {@link com.liferay.portal.kernel.scripting.BaseScriptingExecutor#eval(Set, Map, Set, String)}.
     */
    private final transient Map<String, Object> inputObjects;

    /**
     * Stores the map of names to values of output names, as supplied through
     * {@link com.liferay.portal.kernel.scripting.BaseScriptingExecutor#eval(Set, Map, Set, String)}.
     */
    private final transient Set<String> outputNames;

    /**
     * Stores the script to execute, as supplied through
     * {@link com.liferay.portal.kernel.scripting.BaseScriptingExecutor#eval(Set, Map, Set, String)}.
     */
    private final transient String script;

    /**
     * Creates a new instance of ClojureScriptingCallable, storing the supplied input objects, output names and script
     * for use in the {@link #call()} method.
     *
     * @param inputObjects a map of input names to values to expose to the script.
     * @param outputNames  a set of strings representing keys of the result map to return from {@link #call()}.
     * @param script       the script to execute.
     */
    public ClojureScriptingCallable(final Map<String, Object> inputObjects,
                                    final Set<String> outputNames,
                                    final String script) {
        this.inputObjects = inputObjects;
        this.outputNames = outputNames;
        this.script = script;
    }

    /**
     * Executes the {@link #script} associated with this instance, supplying the input objects and output names to
     * the {@link #CLOJURE_SCRIPTABLE} to pass onto the script.
     *
     * @return a map representing the result of calling the script and extracting the {@link #outputNames} from its
     *         result. Note: there are no guarantees made that this map will contain any of the requested keys - that's
     *         up to the individual script to provide. However this method does ensure that any map keys which aren't
     *         in {@link #outputNames} are removed.
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Map<String, Object> call() {
        final Object result = CLOJURE_SCRIPTABLE.run_script(inputObjects, outputNames, script);

        if (outputNames == null
            || outputNames.isEmpty()
            || result == null) {
            return emptyMap();
        }

        if (!(result instanceof Map)) {
            LOG.warn(format("Unknown return type from clojure script: %s (%s)", result, result.getClass()));
            return emptyMap();
        }

        return retainKeys((Map<String, Object>) result, outputNames);
    }

    /**
     * This method retains the supplied keys in the supplied map - keys in the map which are missing from the set are
     * absent from the returned map.
     *
     * @param map  the map to retain keys within.
     * @param keys the keys to retain.
     * @param <K>  the type of keys maintained by this map
     * @param <V>  the type of mapped values
     *
     * @return a new map containing the keys supplied.
     */
    protected final <K, V> Map<K, V> retainKeys(final Map<K, V> map,
                                                final Collection<K> keys) {
        final ConcurrentMap<K, V> result = new ConcurrentHashMap<K, V>(keys.size());

        for (final K key : keys) {
            final V value = map.get(key);
            if (value != null) {
                result.put(key, value);
            }
        }

        return result;
    }

}
