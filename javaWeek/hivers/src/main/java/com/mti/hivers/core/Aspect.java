package com.mti.hivers.core;

import com.epita.utils.annotation.NotNull;

public interface Aspect<BEAN_TYPE> {

    BEAN_TYPE proxify(@NotNull final Provider<BEAN_TYPE> provider,
                      @NotNull final BEAN_TYPE bean);
}
