package com.coolpeace.global.common;


public interface ValuedEnum {
    String getValue();

    static <E extends Enum<E> & ValuedEnum> E of(Class<E> enumClass, String value) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException();
    }
}
