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

import jakarta.el.ELException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Arithmetic Operations as specified in chapter 1.7.
 *
 * @author Christoph Beck
 */
public class NumberOperations {

  private static final Long LONG_ZERO = 0L;

  private static boolean isDotEe(String value) {
    int length = value.length();
    for (int i = 0; i < length; i++) {
      switch (value.charAt(i)) {
        case '.':
        case 'E':
        case 'e':
          return true;
      }
    }
    return false;
  }

  private static boolean isDotEe(Object value) {
    return value instanceof String && isDotEe((String) value);
  }

  private static boolean isFloatOrDouble(Object value) {
    return value instanceof Float || value instanceof Double;
  }

  private static boolean isFloatOrDoubleOrDotEe(Object value) {
    return isFloatOrDouble(value) || isDotEe(value);
  }

  private static boolean isBigDecimalOrBigInteger(Object value) {
    return value instanceof BigDecimal || value instanceof BigInteger;
  }

  private static boolean isBigDecimalOrFloatOrDoubleOrDotEe(
    Object value
  ) {
    return value instanceof BigDecimal || isFloatOrDoubleOrDotEe(value);
  }

  public static Number add(
    TypeConverter converter,
    Object o1,
    Object o2
  ) {
    if (o1 == null && o2 == null) {
      return LONG_ZERO;
    }
    if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
      return converter
        .convert(o1, BigDecimal.class)
        .add(converter.convert(o2, BigDecimal.class));
    }
    if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
      if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
        return converter
          .convert(o1, BigDecimal.class)
          .add(converter.convert(o2, BigDecimal.class));
      }
      return (
        converter.convert(o1, Double.class) +
          converter.convert(o2, Double.class)
      );
    }
    if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
      return converter
        .convert(o1, BigInteger.class)
        .add(converter.convert(o2, BigInteger.class));
    }
    return (
      converter.convert(o1, Long.class) +
        converter.convert(o2, Long.class)
    );
  }

  public static Number sub(
    TypeConverter converter,
    Object o1,
    Object o2
  ) {
    if (o1 == null && o2 == null) {
      return LONG_ZERO;
    }
    if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
      return converter
        .convert(o1, BigDecimal.class)
        .subtract(converter.convert(o2, BigDecimal.class));
    }
    if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
      if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
        return converter
          .convert(o1, BigDecimal.class)
          .subtract(converter.convert(o2, BigDecimal.class));
      }
      return (
        converter.convert(o1, Double.class) -
          converter.convert(o2, Double.class)
      );
    }
    if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
      return converter
        .convert(o1, BigInteger.class)
        .subtract(converter.convert(o2, BigInteger.class));
    }
    return (
      converter.convert(o1, Long.class) -
        converter.convert(o2, Long.class)
    );
  }

  public static Number mul(
    TypeConverter converter,
    Object o1,
    Object o2
  ) {
    if (o1 == null && o2 == null) {
      return LONG_ZERO;
    }
    if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
      return converter
        .convert(o1, BigDecimal.class)
        .multiply(converter.convert(o2, BigDecimal.class));
    }
    if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
      if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
        return converter
          .convert(o1, BigDecimal.class)
          .multiply(converter.convert(o2, BigDecimal.class));
      }
      return (
        converter.convert(o1, Double.class) *
          converter.convert(o2, Double.class)
      );
    }
    if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
      return converter
        .convert(o1, BigInteger.class)
        .multiply(converter.convert(o2, BigInteger.class));
    }
    return (
      converter.convert(o1, Long.class) *
        converter.convert(o2, Long.class)
    );
  }

  public static Number div(
    TypeConverter converter,
    Object o1,
    Object o2
  ) {
    if (o1 == null && o2 == null) {
      return LONG_ZERO;
    }
    if (isBigDecimalOrBigInteger(o1) || isBigDecimalOrBigInteger(o2)) {
      return converter
        .convert(o1, BigDecimal.class)
        .divide(
          converter.convert(o2, BigDecimal.class),
          RoundingMode.HALF_UP
        );
    }
    return (
      converter.convert(o1, Double.class) /
        converter.convert(o2, Double.class)
    );
  }

  public static Number mod(
    TypeConverter converter,
    Object o1,
    Object o2
  ) {
    if (o1 == null && o2 == null) {
      return LONG_ZERO;
    }
    if (
      isBigDecimalOrFloatOrDoubleOrDotEe(o1) ||
        isBigDecimalOrFloatOrDoubleOrDotEe(o2)
    ) {
      return (
        converter.convert(o1, Double.class) %
          converter.convert(o2, Double.class)
      );
    }
    if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
      return converter
        .convert(o1, BigInteger.class)
        .remainder(converter.convert(o2, BigInteger.class));
    }
    return (
      converter.convert(o1, Long.class) %
        converter.convert(o2, Long.class)
    );
  }

  public static Number neg(TypeConverter converter, Object value) {
    switch (value) {
      case null -> {
        return LONG_ZERO;
      }
      case BigDecimal bigDecimal -> {
        return bigDecimal.negate();
      }
      case BigInteger bigInteger -> {
        return bigInteger.negate();
      }
      case Double v -> {
        return Double.valueOf(-v.doubleValue());
      }
      case Float v -> {
        return Float.valueOf(-v.floatValue());
      }
      case String s -> {
        if (isDotEe(s)) {
          return -converter.convert(value, Double.class).doubleValue();
        }
        return -converter.convert(value, Long.class).longValue();
      }
      case Long l -> {
        return Long.valueOf(-l.longValue());
      }
      case Integer i -> {
        return Integer.valueOf(-i.intValue());
      }
      case Short i -> {
        return Short.valueOf((short) -i.shortValue());
      }
      case Byte b -> {
        return Byte.valueOf((byte) -b.byteValue());
      }
      default -> {
      }
    }
    throw new ELException(
      LocalMessages.get("error.negate", value.getClass())
    );
  }
}
