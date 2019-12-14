package com.epita.utils.validation;

import com.epita.utils.exception.InvalidArgumentException;
import com.epita.utils.exception.InvalidValueException;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum Fault {
    NULL("is null", Objects::isNull),
    NO_NULL_IN_COLLECTION("has null values", Fault::collectionHasNulls);

    public final String message;
    final Predicate<Object> validationPredicate;

    Fault(final String message,
          final Predicate<Object> validationPredicate) {
        this.message = message;
        this.validationPredicate = validationPredicate;
    }

    public InvalidArgumentException forField(final String field) {
        return new InvalidArgumentException(field, this);
    }
    public InvalidValueException forValue() {
        return new InvalidValueException(this);
    }

    public <OBJECT_TYPE> OBJECT_TYPE validate(final OBJECT_TYPE value, final String field) {
        if (validationPredicate.test(value)) {
            throw forField(field);
        }

        return value;
    }

    public <OBJECT_TYPE> OBJECT_TYPE validate(final OBJECT_TYPE value) {
        if (validationPredicate.test(value)) {
            throw forValue();
        }

        return value;
    }

    public static <OBJECT_TYPE> OBJECT_TYPE validate(final OBJECT_TYPE value, final String field, final Fault... faults) {
        Stream.of(faults).forEach(fault -> fault.validate(value, field));
        return value;
    }

    public static <OBJECT_TYPE> OBJECT_TYPE validate(final OBJECT_TYPE value, final Fault... faults) {
        Stream.of(faults).forEach(fault -> fault.validate(value));
        return value;
    }

    private static boolean collectionHasNulls(final Object maybeCollection) {
        if (!(maybeCollection instanceof Collection)) {
            return true;
        }

        final Collection<?> collection = (Collection) maybeCollection;
        return collection.stream().anyMatch(Objects::isNull);
    }
}
