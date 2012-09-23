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

import com.liferay.portal.kernel.scripting.Scripting;
import com.liferay.portal.kernel.scripting.ScriptingExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegisterClojureExecutorActionTest {

    @Mock
    private ScriptingExecutor executor;

    @Mock
    private Scripting scripting;

    @Captor
    private ArgumentCaptor<ScriptingExecutor> scriptingExecutorArgumentCaptor;

    @Test
    public void testRun() throws Exception {
        RegisterClojureExecutorAction action = spy(new RegisterClojureExecutorAction());
        when(action.getScripting()).thenReturn(scripting);

        action.run(new String[0]);

        verify(scripting).addScriptionExecutor(eq("clojure"), scriptingExecutorArgumentCaptor.capture());
        assertThat(scriptingExecutorArgumentCaptor.getValue()).isInstanceOf(ClojureScriptingExecutor.class);
    }

    @Test
    public void testCreateClojureScriptingExecutor() throws Exception {
        RegisterClojureExecutorAction action = new RegisterClojureExecutorAction();

        ScriptingExecutor executor = action.createClojureScriptingExecutor();

        assertThat(executor).isInstanceOf(ClojureScriptingExecutor.class);
    }

    @Test
    public void testRegisterScriptingExecutor() throws Exception {
        when(executor.getLanguage()).thenReturn("language");
        RegisterClojureExecutorAction action = spy(new RegisterClojureExecutorAction());
        when(action.getScripting()).thenReturn(scripting);

        action.registerScriptingExecutor(executor);

        verify(scripting).addScriptionExecutor("language", executor);
    }

}
