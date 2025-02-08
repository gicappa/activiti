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
package org.activiti.common.util.conf;

import org.activiti.common.util.DateFormatterProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ActivitiCoreCommonUtilAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public DateFormatterProvider dateFormatterProvider(
    @Value("${spring.activiti.date-format-pattern:yyyy-MM-dd[['T']HH:mm:ss[.SSS][XXX]]}")
    String dateFormatPattern) {
    return new DateFormatterProvider(dateFormatPattern);
  }

}
