package validation;

import java.util.function.Supplier;

public final class Assert {

    private Assert() {}

     public static <EXCEPTION_TYPE extends Throwable>
     void isTrue(final boolean predicate,
                 final Supplier<EXCEPTION_TYPE> exceptionSupplier) throws EXCEPTION_TYPE {

        if (!predicate) {
            throw exceptionSupplier.get();
        }
    }

     public static <EXCEPTION_TYPE extends Throwable>
     void isFalse(final boolean predicate,
                 final Supplier<EXCEPTION_TYPE> exceptionSupplier) throws EXCEPTION_TYPE {

        if (predicate) {
            throw exceptionSupplier.get();
        }
    }
}
