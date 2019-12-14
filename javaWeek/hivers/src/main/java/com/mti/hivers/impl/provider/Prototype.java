package com.mti.hivers.impl.provider;

import com.epita.utils.annotation.NotNull;
import com.epita.utils.annotation.Nullable;
import com.epita.utils.annotation.Pure;
import com.epita.utils.validation.Fault;

import java.util.Collections;
import java.util.function.Supplier;

@Pure
public class Prototype<BEAN_TYPE> extends AbstractProvider<BEAN_TYPE> {

    @NotNull
    private final Supplier<? extends BEAN_TYPE> supplier;

    public Prototype(@NotNull final Class<BEAN_TYPE> providedClass,
                     @NotNull final Supplier<? extends BEAN_TYPE> supplier) {

        super(providedClass, Collections.emptyList());
        this.supplier = Fault.NULL.validate(supplier, "supplier");
    }

    @Nullable
    @Override
    public BEAN_TYPE provide() {
        return proxify(supplier.get());
    }
}
