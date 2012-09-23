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

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.SimpleAction;
import com.liferay.portal.kernel.scripting.Scripting;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import com.liferay.portal.kernel.scripting.ScriptingUtil;

public class RegisterClojureExecutorAction extends SimpleAction {

    @Override
    public void run(final String[] ids) throws ActionException {
        final ScriptingExecutor executor = createClojureScriptingExecutor();

        registerScriptingExecutor(executor);
    }

    protected ScriptingExecutor createClojureScriptingExecutor() {
        final Thread thread = Thread.currentThread();
        final ClassLoader classLoader = thread.getContextClassLoader();

        return new ClojureScriptingExecutor(classLoader);
    }

    protected void registerScriptingExecutor(final ScriptingExecutor executor) {
        final String language = executor.getLanguage();
        final Scripting scripting = getScripting();

        scripting.addScriptionExecutor(language, executor);
    }

    protected Scripting getScripting() {
        return ScriptingUtil.getScripting();
    }

}
