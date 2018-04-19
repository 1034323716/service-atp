package org.mahatma.atp.common.util;

import java.util.function.Function;

/**
 * Created by JiYunfei on 17-9-25.
 */
enum TestFieldType {

    STRING(String.class, s -> s),
    BOOLEAN(boolean.class, Boolean.class, s -> Boolean.parseBoolean(s)),
    BYTE(byte.class, Byte.class, s -> Byte.parseByte(s)),
    SHORT(short.class, Short.class, s -> Short.parseShort(s)),
    INTEGER(int.class, Integer.class, s -> Integer.parseInt(s)),
    LONG(long.class, Long.class, s -> Long.parseLong(s)),
    FLOAT(float.class, Float.class, s -> Float.parseFloat(s)),
    DOUBLE(double.class, Double.class, s -> Double.parseDouble(s)),
    CHAR(char.class, Character.class) {
        @Override
        public Object convertFrom(Class<?> toClazz, String s) {
            if (s.length() == 1) {
                return s.charAt(0);
            } else {
                throw new IllegalArgumentException("expects char:" + s);
            }
        }
    },
    ENUM(Enum.class) {
        @Override
        public Object convertFrom(Class<?> toClazz, String s) {
            return Enum.valueOf((Class<Enum>) toClazz, s);
        }
    },
// TODO support Date & DateTime
//	DATE(Date.class) {
//		@Override
//		public Object convertFrom(Class<?> toClazz, SetterNode node) {
//			return DateFormat.F
//		}
//	},
//	DATETIME(DateTime.class) {
//		@Override
//		public Object convertFrom(Class<?> toClazz, SetterNode node) {
//			return null;
//		}
//	},
    ;

    private Class<?>[] supportTypes;
    private Function<String, Object> parser;

    TestFieldType(Class<?> clazz) {
        supportTypes = new Class<?>[]{clazz};
    }

    TestFieldType(Class<?> c1, Class<?> c2) {
        supportTypes = new Class<?>[]{c1, c2};
    }

    TestFieldType(Class<?> clazz, Function<String, Object> parser) {
        this(clazz);
        this.parser = parser;
    }

    TestFieldType(Class<?> c1, Class<?> c2, Function<String, Object> parser) {
        this(c1, c2);
        this.parser = parser;
    }

    private boolean isSupport(Class<?> clazz) {
        if (supportTypes == null || supportTypes.length == 0) {
            return false;
        }
        for (Class<?> type : supportTypes) {
            if (type == clazz || type.equals(clazz) || type.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static TestFieldType valueOf(Class<?> clazz) {
        for (TestFieldType type : TestFieldType.values()) {
            if (type.isSupport(clazz)) {
                return type;
            }
        }
        return null;
    }

    public Object convertFrom(Class<?> toClazz, String text) {
        if (parser != null) {
            return parser.apply(text);
        } else {
            throw new UnsupportedOperationException("Must override convertFrom() method");
        }
    }
}
