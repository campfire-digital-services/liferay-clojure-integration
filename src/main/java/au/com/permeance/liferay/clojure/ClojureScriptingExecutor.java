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
import com.liferay.portal.kernel.scripting.BaseScriptingExecutor;
import com.liferay.portal.kernel.scripting.ExecutionException;
import com.liferay.portal.kernel.scripting.ScriptingException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class ClojureScriptingExecutor extends BaseScriptingExecutor {

    private static final String LANGUAGE = "clojure";

    private transient final ClassLoader hookClassLoader;

    private static final Log LOG = LogFactoryUtil.getLog(ClojureScriptingExecutor.class);

    public ClojureScriptingExecutor() {
        this(Thread.currentThread()
                   .getContextClassLoader());
    }

    public ClojureScriptingExecutor(final ClassLoader hookClassLoader) {
        super();
        this.hookClassLoader = hookClassLoader;
    }

    @Override
    public Map<String, Object> eval(final Set<String> allowedClasses,
                                    final Map<String, Object> inputObjects,
                                    final Set<String> outputNames,
                                    final String script) throws ScriptingException {

        if (allowedClasses != null && !allowedClasses.isEmpty()) {
            throw new ExecutionException("Constrained execution not supported for Clojure");
        }

        final Callable<Map<String, Object>> callable = createCallableForScript(inputObjects, outputNames, script);

        return callAndWrapException(callable);
    }

    @Override
    public String getLanguage() {
        return LANGUAGE;
    }

    protected Callable<Map<String, Object>> createCallableForScript(final Map<String, Object> inputObjects,
                                                                    final Set<String> outputNames,
                                                                    final String script) {

        final Callable<Map<String, Object>> callable = new ClojureScriptingCallable(inputObjects, outputNames, script);

        return new CallUnderClassLoader<Map<String, Object>>(callable, hookClassLoader);
    }

    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    protected <V> V callAndWrapException(final Callable<V> callable) throws ScriptingException {
        try {
            return callable.call();
        }
        catch (Exception e) {
            LOG.error("Error executing clojure script", e);
            throw new ScriptingException("Error executing clojure script", e);
        }
    }

}
