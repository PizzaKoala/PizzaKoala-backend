package com.PizzaKoala.Pizza.domain.Util;

import java.util.Optional;

public class ClassUtil {
    public static <T> Optional<T> getSafeCastingInstance(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? Optional.of(clazz.cast(o)) : Optional.empty();
    }
}
