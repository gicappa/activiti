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
package org.activiti.core.el.juel.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import jakarta.el.ELException;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.activiti.core.el.juel.test.TestCase;
import org.junit.jupiter.api.Test;

/**
 * JUnit test case for {@link TypeConverterImpl}.
 *
 * @author Christoph Beck
 */
public class TypeConverterImplTest extends TestCase {

    /**
     * Test property editor for date objects.
     * Accepts integer strings as text input and uses them as time value in milliseconds.
     */
    public static class DateEditor implements PropertyEditor {

        private Date value;

        public void addPropertyChangeListener(
            PropertyChangeListener listener
        ) {}

        public String getAsText() {
            return value == null ? null : "" + value.getTime();
        }

        public Component getCustomEditor() {
            return null;
        }

        public String getJavaInitializationString() {
            return null;
        }

        public String[] getTags() {
            return null;
        }

        public Object getValue() {
            return value;
        }

        public boolean isPaintable() {
            return false;
        }

        public void paintValue(Graphics gfx, Rectangle box) {}

        public void removePropertyChangeListener(
            PropertyChangeListener listener
        ) {}

        public void setAsText(String text) throws IllegalArgumentException {
            value = new Date(Long.parseLong(text));
        }

        public void setValue(Object value) {
            this.value = (Date) value;
        }

        public boolean supportsCustomEditor() {
            return false;
        }
    }

    static {
        PropertyEditorManager.registerEditor(Date.class, DateEditor.class);
    }

    /**
     * Test enum type
     */
    enum Foo {
        BAR,
        BAZ {
            @Override
            public String toString() {
                return "XXX";
            }
        },
    }

    TypeConverterImpl converter = new TypeConverterImpl();

    @Test
    public void testToBoolean() {
        assertFalse(converter.coerceToBoolean(null));
        assertFalse(converter.coerceToBoolean(""));
        assertTrue(converter.coerceToBoolean(Boolean.TRUE));
        assertFalse(converter.coerceToBoolean(Boolean.FALSE));
        assertTrue(converter.coerceToBoolean("true"));
        assertFalse(converter.coerceToBoolean("false"));
        assertFalse(converter.coerceToBoolean("yes")); // Boolean.valueOf(String) never throws an exception...
    }

    @Test
    public void testToCharacter() {
        assertEquals(
            Character.valueOf((char) 0),
            converter.coerceToCharacter(null)
        );
        assertEquals(
            Character.valueOf((char) 0),
            converter.coerceToCharacter("")
        );
        Character c = Character.valueOf((char) 99);
        assertSame(c, converter.coerceToCharacter(c));
        try {
            converter.coerceToCharacter(Boolean.TRUE);
            fail();
        } catch (ELException e) {}
        try {
            converter.coerceToCharacter(Boolean.FALSE);
            fail();
        } catch (ELException e) {}
        assertEquals(c, converter.coerceToCharacter((byte) 99));
        assertEquals(c, converter.coerceToCharacter((short) 99));
        assertEquals(c, converter.coerceToCharacter(99));
        assertEquals(c, converter.coerceToCharacter(99L));
        assertEquals(c, converter.coerceToCharacter((float) 99.5));
        assertEquals(c, converter.coerceToCharacter(99.5));
        assertEquals(c, converter.coerceToCharacter(new BigDecimal("99.5")));
        assertEquals(c, converter.coerceToCharacter(new BigInteger("99")));
        assertEquals(c, converter.coerceToCharacter("c#"));
        try {
            converter.coerceToCharacter(this);
            fail();
        } catch (ELException e) {}
    }

    @Test
    public <T extends Number> void testToLong() {
        Number zero = 0L;
        Number ninetynine = 99L;
        assertEquals(zero, converter.coerceToLong(null));
        assertEquals(zero, converter.coerceToLong(""));
        assertEquals(
            ninetynine,
            converter.coerceToLong('c')
        );
        assertEquals(ninetynine, converter.coerceToLong((byte) 99));
        assertEquals(ninetynine, converter.coerceToLong((short) 99));
        assertEquals(ninetynine, converter.coerceToLong(99));
        assertEquals(ninetynine, converter.coerceToLong(99L));
        assertEquals(ninetynine, converter.coerceToLong(99F));
        assertEquals(ninetynine, converter.coerceToLong(99.0));
        assertEquals(ninetynine, converter.coerceToLong(new BigDecimal(99)));
        assertEquals(ninetynine, converter.coerceToLong(new BigInteger("99")));
        assertEquals(ninetynine, converter.coerceToLong(ninetynine.toString()));
        try {
            converter.coerceToLong("foo");
            fail();
        } catch (ELException ignored) {}
    }

    @Test
    public <T extends Number> void testToInteger() {
        Number zero = 0;
        Number ninetynine = 99;
        assertEquals(zero, converter.coerceToInteger(null));
        assertEquals(zero, converter.coerceToInteger(""));
        assertEquals(
            ninetynine,
            converter.coerceToInteger('c')
        );
        assertEquals(
            ninetynine,
            converter.coerceToInteger((byte) 99)
        );
        assertEquals(
            ninetynine,
            converter.coerceToInteger((short) 99)
        );
        assertEquals(ninetynine, converter.coerceToInteger(99));
        assertEquals(ninetynine, converter.coerceToInteger(99L));
        assertEquals(ninetynine, converter.coerceToInteger(99F));
        assertEquals(ninetynine, converter.coerceToInteger(99.0));
        assertEquals(ninetynine, converter.coerceToInteger(new BigDecimal(99)));
        assertEquals(
            ninetynine,
            converter.coerceToInteger(new BigInteger("99"))
        );
        assertEquals(
            ninetynine,
            converter.coerceToInteger(ninetynine.toString())
        );
        try {
            converter.coerceToInteger("foo");
            fail();
        } catch (ELException ignored) {}
    }

    @Test
    public <T extends Number> void testToShort() {
        Number zero = (short) 0;
        Number ninetynine = (short) 99;
        assertEquals(zero, converter.coerceToShort(null));
        assertEquals(zero, converter.coerceToShort(""));
        assertEquals(
            ninetynine,
            converter.coerceToShort('c')
        );
        assertEquals(ninetynine, converter.coerceToShort((byte) 99));
        assertEquals(
            ninetynine,
            converter.coerceToShort((short) 99)
        );
        assertEquals(ninetynine, converter.coerceToShort(99));
        assertEquals(ninetynine, converter.coerceToShort(99L));
        assertEquals(ninetynine, converter.coerceToShort(99F));
        assertEquals(ninetynine, converter.coerceToShort(99.0));
        assertEquals(ninetynine, converter.coerceToShort(new BigDecimal(99)));
        assertEquals(ninetynine, converter.coerceToShort(new BigInteger("99")));
        assertEquals(
            ninetynine,
            converter.coerceToShort(ninetynine.toString())
        );
        try {
            converter.coerceToShort("foo");
            fail();
        } catch (ELException ignored) {}
    }

    @Test
    public <T extends Number> void testToByte() {
        Number zero = (byte) 0;
        Number ninetynine = (byte) 99;
        assertEquals(zero, converter.coerceToByte(null));
        assertEquals(zero, converter.coerceToByte(""));
        assertEquals(
            ninetynine,
            converter.coerceToByte('c')
        );
        assertEquals(ninetynine, converter.coerceToByte((byte) 99));
        assertEquals(ninetynine, converter.coerceToByte((short) 99));
        assertEquals(ninetynine, converter.coerceToByte(99));
        assertEquals(ninetynine, converter.coerceToByte(99L));
        assertEquals(ninetynine, converter.coerceToByte(99F));
        assertEquals(ninetynine, converter.coerceToByte(99.0));
        assertEquals(ninetynine, converter.coerceToByte(BigDecimal.valueOf(99)));
        assertEquals(ninetynine, converter.coerceToByte(new BigInteger("99")));
        assertEquals(ninetynine, converter.coerceToByte(ninetynine.toString()));
        try {
            converter.coerceToByte("foo");
            fail();
        } catch (ELException e) {}
    }

    @Test
    public <T extends Number> void testToDouble() {
        Number zero = (double) 0;
        Number ninetynine = 99.0;
        assertEquals(zero, converter.coerceToDouble(null));
        assertEquals(zero, converter.coerceToDouble(""));
        assertEquals(
            ninetynine,
            converter.coerceToDouble('c')
        );
        assertEquals(ninetynine, converter.coerceToDouble((byte) 99));
        assertEquals(
            ninetynine,
            converter.coerceToDouble(Short.valueOf((short) 99))
        );
        assertEquals(ninetynine, converter.coerceToDouble(99));
        assertEquals(ninetynine, converter.coerceToDouble(99L));
        assertEquals(ninetynine, converter.coerceToDouble(99F));
        assertEquals(ninetynine, converter.coerceToDouble(99.0));
        assertEquals(ninetynine, converter.coerceToDouble(new BigDecimal(99)));
        assertEquals(
            ninetynine,
            converter.coerceToDouble(new BigInteger("99"))
        );
        assertEquals(
            ninetynine,
            converter.coerceToDouble(ninetynine.toString())
        );
        try {
            converter.coerceToDouble("foo");
            fail();
        } catch (ELException e) {}
    }

    @Test
    public <T extends Number> void testToFloat() {
        Number zero = (float) 0;
        Number ninetynine = 99F;
        assertEquals(zero, converter.coerceToFloat(null));
        assertEquals(zero, converter.coerceToFloat(""));
        assertEquals(
            ninetynine,
            converter.coerceToFloat('c')
        );
        assertEquals(ninetynine, converter.coerceToFloat((byte) 99));
        assertEquals(
            ninetynine,
            converter.coerceToFloat((short) 99)
        );
        assertEquals(ninetynine, converter.coerceToFloat(99));
        assertEquals(ninetynine, converter.coerceToFloat(99L));
        assertEquals(ninetynine, converter.coerceToFloat(99F));
        assertEquals(ninetynine, converter.coerceToFloat(99.0));
        assertEquals(ninetynine, converter.coerceToFloat(new BigDecimal(99)));
        assertEquals(ninetynine, converter.coerceToFloat(new BigInteger("99")));
        assertEquals(
            ninetynine,
            converter.coerceToFloat(ninetynine.toString())
        );
        try {
            converter.coerceToFloat("foo");
            fail();
        } catch (ELException ignored) {}
    }

    @Test
    public <T extends Number> void testToBigDecimal() {
        Number zero = BigDecimal.valueOf(0);
        Number ninetynine = BigDecimal.valueOf(99);
        assertEquals(zero, converter.coerceToBigDecimal(null));
        assertEquals(zero, converter.coerceToBigDecimal(""));
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal('c')
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal((byte) 99)
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal((short) 99)
        );
        assertEquals(ninetynine, converter.coerceToBigDecimal(99));
        assertEquals(ninetynine, converter.coerceToBigDecimal(99L));
        assertEquals(ninetynine, converter.coerceToBigDecimal(99F));
        assertEquals(ninetynine, converter.coerceToBigDecimal(99.0));
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal(new BigDecimal(99))
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal(new BigInteger("99"))
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigDecimal(ninetynine.toString())
        );
        try {
            converter.coerceToBigDecimal("foo");
            fail();
        } catch (ELException e) {}
    }

    @Test
    public <T extends Number> void testToBigInteger() {
        Number zero = BigInteger.valueOf(0);
        Number ninetynine = BigInteger.valueOf(99);
        assertEquals(zero, converter.coerceToBigInteger(null));
        assertEquals(zero, converter.coerceToBigInteger(""));
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger('c')
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger((byte) 99)
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger((short) 99)
        );
        assertEquals(ninetynine, converter.coerceToBigInteger(99));
        assertEquals(ninetynine, converter.coerceToBigInteger(99L));
        assertEquals(ninetynine, converter.coerceToBigInteger(99F));
        assertEquals(ninetynine, converter.coerceToBigInteger(99.0));
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger(new BigDecimal(99))
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger(new BigInteger("99"))
        );
        assertEquals(
            ninetynine,
            converter.coerceToBigInteger(ninetynine.toString())
        );
        try {
            converter.coerceToBigInteger("foo");
            fail();
        } catch (ELException e) {}
    }

    @Test
    public void testToString() {
        assertSame("foo", converter.coerceToString("foo"));
        assertEquals("", converter.coerceToString(null));
        assertEquals(Foo.BAR.name(), converter.coerceToString(Foo.BAR));
        Object value = new BigDecimal("99.345");
        assertEquals(value.toString(), converter.coerceToString(value));
    }

    @Test
    public void testToEnum() {
        assertNull(converter.coerceToEnum(null, Foo.class));
        assertSame(Foo.BAR, converter.coerceToEnum(Foo.BAR, Foo.class));
        assertNull(converter.coerceToEnum("", Foo.class));
        assertSame(Foo.BAR, converter.coerceToEnum("BAR", Foo.class));
        assertSame(Foo.BAZ, converter.coerceToEnum("BAZ", Foo.class));
    }

    @Test
    public void testToType() {
        assertEquals("foo", converter.coerceToType("foo", String.class));
        assertEquals(0L, converter.coerceToType("0", Long.class));
        assertEquals(
          'c',
            converter.coerceToType("c", Character.class)
        );
        assertEquals(
            Boolean.TRUE,
            converter.coerceToType("true", Boolean.class)
        );
        assertEquals(Foo.BAR, converter.coerceToType("BAR", Foo.class));
        // other types
        assertNull(converter.coerceToType(null, Object.class));
        Object value = new Date(0);
        assertSame(value, converter.coerceToType(value, Object.class));
        assertEquals(new Date(0), converter.coerceToType("0", Date.class));
        assertNull(converter.coerceToType("", Date.class));
        try {
            converter.coerceToType("foo", Date.class);
            fail();
        } catch (Exception e) {}
        assertNull(converter.coerceToType("", getClass()));
        try {
            converter.coerceToType("bar", getClass());
            fail();
        } catch (Exception ignored) {}
        assertEquals(false, converter.coerceToType("false", boolean.class));
        assertEquals((byte) 0, converter.coerceToType("0", byte.class));
        assertEquals((short) 0, converter.coerceToType("0", short.class));
        assertEquals(0, converter.coerceToType("0", int.class));
        assertEquals((long) 0, converter.coerceToType("0", long.class));
        assertEquals((float) 0, converter.coerceToType("0", float.class));
        assertEquals((double) 0, converter.coerceToType("0", double.class));
        assertEquals('0', converter.coerceToType("0", char.class));
    }
}
