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
package org.activiti.core.el.juel.tree;

import jakarta.el.ELException;
import org.activiti.core.el.juel.misc.LocalMessages;

/**
 * Exception type thrown in build phase (scan/parse).
 *
 * @author Christoph Beck
 */
public class TreeBuilderException extends ELException {

    private static final long serialVersionUID = 1L;

    private final String expression;
    private final int position;
    private final String encountered;
    private final String expected;

    public TreeBuilderException(
        String expression,
        int position,
        String encountered,
        String expected,
        String message
    ) {
        super(LocalMessages.get("error.build", expression, message));
        this.expression = expression;
        this.position = position;
        this.encountered = encountered;
        this.expected = expected;
    }

    /**
     * @return the expression string
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @return the error position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return the substring (or description) that has been encountered
     */
    public String getEncountered() {
        return encountered;
    }

    /**
     * @return the substring (or description) that was expected
     */
    public String getExpected() {
        return expected;
    }
}
