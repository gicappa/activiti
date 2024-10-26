/*
 * Copyright ${project.inceptionYear}-2020 ${project.organization.name}.
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
package org.activiti.engine.impl.interceptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.lang.reflect.Field;
import org.activiti.engine.ApplicationStatusHolder;
import org.activiti.engine.impl.agenda.DefaultActivitiEngineAgenda;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.LoggerFactory;

public class CommandContextTest {

  @Before
  public void resetStatusToRunning() throws Exception {
    setStaticValue(ApplicationStatusHolder.class.getDeclaredField("running"), true);
  }

  @Test
  public void should_logExceptionAtErrorLevel_when_closing() {
    var appender = startLogger();

    startAndCloseCommandContext(new RuntimeException());

    assertLogLevel(appender, Level.ERROR);
  }

  @Ignore // GK
  @Test
  public void should_LogExceptionAtWarningLevel_when_closing() {
    var appender = startLogger();

    ApplicationStatusHolder.shutdown();

    startAndCloseCommandContext(new RuntimeException());

    assertLogLevel(appender, Level.WARN);
  }

  @Test
  public void should_notLogException_when_closing() {
    var appender = startLogger();
    startAndCloseCommandContext(null);
    assertThat(appender.list).isEmpty();
  }

  private void startAndCloseCommandContext(Exception exception) {
    var command = (Command<Object>) a -> "Hello world!";

    var processEngineConfiguration = new ProcessEngineConfigurationImpl() {
      @Override
      public CommandInterceptor createTransactionInterceptor() {
        return new CommandContextInterceptor();
      }
    };

    processEngineConfiguration.setEngineAgendaFactory(DefaultActivitiEngineAgenda::new);

    var commandContext = new CommandContext(command, processEngineConfiguration);

    commandContext.exception = exception;
    try {
      commandContext.close();
    } catch (Exception e) {
      //suppress exception
    }
  }

  private void assertLogLevel(ListAppender<ILoggingEvent> appender, Level level) {
    assertThat(appender.list)
      .extracting(
        ILoggingEvent::getFormattedMessage, ILoggingEvent::getLevel)
      .contains(tuple("Error while closing command context", level));
  }

  private ListAppender<ILoggingEvent> startLogger() {
    var fooLogger = (Logger) LoggerFactory.getLogger(CommandContext.class);
    var appender = new ListAppender<ILoggingEvent>();
    fooLogger.addAppender(appender);
    appender.start();
    return appender;
  }

  private void setStaticValue(Field field, Object value) throws Exception {
    field.setAccessible(true);
    field.set(null, value);
  }
}
