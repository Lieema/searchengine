package com.mti.hivers.impl.provider;

import com.mti.hivers.annotation.Mutate;
import com.mti.hivers.annotation.NotNull;
import com.mti.hivers.annotation.Nullable;
import com.mti.hivers.core.Aspect;
import com.mti.hivers.validation.Fault;

import java.util.Arrays;
import java.util.function.Supplier;

@Mutate
public class Singleton<BEAN_TYPE> extends AbstractProvider<BEAN_TYPE> {

    @NotNull
    private final Supplier<? extends BEAN_TYPE> supplier;

    private BEAN_TYPE value;
    private boolean initialized = false;

    @SuppressWarnings("unchecked")
    public Singleton(@NotNull final Class<BEAN_TYPE> providedClass,
                     @NotNull final Supplier<? extends BEAN_TYPE> supplier,
                     @NotNull final Aspect<BEAN_TYPE>... aspects) {
        super(providedClass, Arrays.asList(aspects));
        this.supplier = Fault.NULL.validate(supplier, "supplier");
    }

    public Singleton(@NotNull final Class<BEAN_TYPE> providedClass,
                     @NotNull final BEAN_TYPE singleton) {

        this(providedClass, () -> singleton);
    }

    @Override
    @Nullable
    public BEAN_TYPE provide() {
        if (!initialized) {
            initialized = true;
            value = proxify(supplier.get());
        }
        return value;
    }
}
