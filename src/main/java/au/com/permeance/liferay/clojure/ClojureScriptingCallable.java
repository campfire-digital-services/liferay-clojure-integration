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

import permeance.liferay.clojure.ClojureScriptableImpl;
import permeance.liferay.clojure.ClojureScriptable;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;

public class ClojureScriptingCallable implements Callable<Map<String, Object>> {

    private static final Log LOG = LogFactoryUtil.getLog(ClojureScriptingCallable.class);

    private transient final Map<String, Object> inputObjects;

    private transient final Set<String> outputNames;

    private transient final String script;

    public ClojureScriptingCallable(final Map<String, Object> inputObjects,
                                    final Set<String> outputNames,
                                    final String script) {
        this.inputObjects = inputObjects;
        this.outputNames = outputNames;
        this.script = script;
    }

    @Override
    @SuppressWarnings({"unchecked", "PMD.SignatureDeclareThrowsException"})
    public Map<String, Object> call() throws Exception {
        final ClojureScriptable clojureScriptable = new ClojureScriptableImpl();
        final Object result = clojureScriptable.run_script(inputObjects, outputNames, script);

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

    protected <K, V> Map<K, V> retainKeys(final Map<K, V> result,
                                          final Collection<K> keys) {
        final ConcurrentMap<K, V> map = new ConcurrentHashMap<K, V>(keys.size());

        for (final K key : keys) {
            final V value = result.get(key);
            if (value != null) {
                map.put(key, value);
            }
        }

        return map;
    }

}
