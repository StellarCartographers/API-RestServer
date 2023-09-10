package space.tscg.util;

import static panda.std.Blank.BLANK;
import static panda.std.Blank.voidness;
import static space.tscg.util.ResultImpl.State.ERROR;
import static space.tscg.util.ResultImpl.State.OK;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import panda.std.AttemptFailedException;
import panda.std.Blank;
import panda.std.Option;
import panda.std.function.ThrowingConsumer;
import panda.std.function.ThrowingFunction;
import panda.std.function.ThrowingRunnable;
import panda.std.function.ThrowingSupplier;

public class ResultImpl<VALUE, ERROR> {

    public enum State {
        OK,
        ERROR
    }

    private final State state;
    private final VALUE value;
    private final ERROR error;

    private ResultImpl(State state, @Nullable VALUE value, @Nullable ERROR error) {
        if ((value != null) && (error != null)) {
            throw new IllegalStateException("Value and error are not null - Cannot determine state of Result");
        }

        this.state = state;
        this.value = value;
        this.error = error;
    }

    public static <VALUE, ERROR> @NotNull ResultImpl<VALUE, ERROR> ok(VALUE value) {
        return new ResultImpl<>(OK, value, null);
    }

    public static <ERROR> @NotNull ResultImpl<Blank, ERROR> ok() {
        return new ResultImpl<>(OK, BLANK, null);
    }

    public static <VALUE, ERROR> @NotNull ResultImpl<VALUE, ERROR> error(ERROR err) {
        return new ResultImpl<>(ERROR, null, err);
    }

    public static <VALUE> @NotNull ResultImpl<VALUE, Blank> error() {
        return new ResultImpl<>(ERROR, null, BLANK);
    }

    public static <VALUE, ERROR> @NotNull ResultImpl<VALUE, ERROR> when(boolean condition, @NotNull Supplier<VALUE> value, @NotNull Supplier<ERROR> err) {
        return condition ? ok(value.get()) : error(err.get());
    }

    public static <VALUE, ERROR> @NotNull ResultImpl<VALUE, ERROR> when(boolean condition, VALUE value, ERROR err) {
        return condition ? ok(value) : error(err);
    }

    public static <ERROR extends Exception> @NotNull ResultImpl<Void, @NotNull Exception> runThrowing(@NotNull ThrowingRunnable<@NotNull Exception> runnable) throws AttemptFailedException {
        return runThrowing(Exception.class, runnable);
    }

    public static <ERROR extends Throwable> @NotNull ResultImpl<Void, ERROR> runThrowing(
        @NotNull Class<? extends ERROR> exceptionType,
        @NotNull ThrowingRunnable<@NotNull ERROR> runnable
    ) throws AttemptFailedException {
        return supplyThrowing(exceptionType, () -> {
            runnable.run();
            return voidness();
        });
    }

    public static <VALUE> @NotNull ResultImpl<VALUE, Exception> supplyThrowing(@NotNull ThrowingSupplier<VALUE, @NotNull Exception> supplier) {
        return supplyThrowing(Exception.class, supplier);
    }

    @SuppressWarnings("unchecked")
    public static <VALUE, ERROR extends Throwable> @NotNull ResultImpl<VALUE, ERROR> supplyThrowing(
            @NotNull Class<? extends ERROR> exceptionType,
            @NotNull ThrowingSupplier<VALUE, @NotNull ERROR> supplier
    ) throws AttemptFailedException {
        try {
            return ResultImpl.ok(supplier.get());
        } catch (Throwable throwable) {
            if (exceptionType.isAssignableFrom(throwable.getClass())) {
                return ResultImpl.error((ERROR) throwable);
            }

            throw new AttemptFailedException(throwable);
        }
    }

    public <SECOND_VALUE, R> @NotNull ResultImpl<R, ERROR> merge(
        @NotNull ResultImpl<SECOND_VALUE, ? extends ERROR> second,
        @NotNull BiFunction<VALUE, SECOND_VALUE, R> mergeFunction
    ) {
        return flatMap(firstValue -> second.map(secondValue -> mergeFunction.apply(firstValue, secondValue)));
    }

    public <MAPPED_VALUE> @NotNull ResultImpl<MAPPED_VALUE, ERROR> map(@NotNull Function<VALUE, MAPPED_VALUE> function) {
        return isOk() ? ok(function.apply(get())) : projectToError();
    }

    public @NotNull ResultImpl<Blank, ERROR> mapToBlank() {
        return isOk() ? ok() : projectToError();
    }

    public @NotNull ResultImpl<VALUE, Blank> mapErrToBlank() {
        return isErr() ? error() : projectToValue();
    }

    public <MAPPED_ERROR> @NotNull ResultImpl<VALUE, MAPPED_ERROR> mapErr(@NotNull Function<ERROR, MAPPED_ERROR> function) {
        return isOk() ? projectToValue() : error(function.apply(getError()));
    }

    @SuppressWarnings("unchecked")
    public <MAPPED_VALUE> @NotNull ResultImpl<MAPPED_VALUE, ERROR> flatMap(@NotNull Function<VALUE, @NotNull ResultImpl<MAPPED_VALUE, ? extends ERROR>> function) {
        return isOk()
                ? (ResultImpl<MAPPED_VALUE, ERROR>) function.apply(get())
                : projectToError();
    }

    @SuppressWarnings("unchecked")
    public <MAPPED_ERROR> @NotNull ResultImpl<VALUE, MAPPED_ERROR> flatMapErr(@NotNull Function<@NotNull ERROR, @NotNull ResultImpl<? extends VALUE, MAPPED_ERROR>> function) {
        return isErr()
                ? (ResultImpl<VALUE, MAPPED_ERROR>) function.apply(getError())
                : projectToValue();
    }

    public @NotNull ResultImpl<VALUE, ERROR> filter(@NotNull Predicate<VALUE> predicate, @NotNull Function<VALUE, ERROR> errorSupplier) {
        return isOk() && !predicate.test(get()) ? error(errorSupplier.apply(get())) : this;
    }

    public @NotNull ResultImpl<VALUE, ERROR> filterNot(@NotNull Predicate<VALUE> predicate, @NotNull Function<VALUE, ERROR> errorSupplier) {
        return filter(value -> !predicate.test(value), errorSupplier);
    }

    public @NotNull ResultImpl<VALUE, ERROR> filter(@NotNull Function<VALUE, @Nullable ERROR> filtersBody) {
        return flatMap(value -> {
            ERROR error = filtersBody.apply(value);
            return error == null ? ok(value) : error(error);
        });
    }

    public static class FilteredResult<VALUE, FILTER_ERROR, EXPECTED_ERROR> {

        private final @Nullable ResultImpl<VALUE, EXPECTED_ERROR> previousError;
        private final @Nullable ResultImpl<VALUE, FILTER_ERROR> currentResult;

        private FilteredResult(@Nullable ResultImpl<VALUE, EXPECTED_ERROR> previousError, @Nullable ResultImpl<VALUE, FILTER_ERROR> currentResult) {
            if ((previousError == null) && (currentResult == null)) {
                throw new IllegalArgumentException("Both previousError and currentResult are null");
            }
            this.previousError = previousError;
            this.currentResult = currentResult;
        }

        public @NotNull ResultImpl<VALUE, EXPECTED_ERROR> mapFilterError(@NotNull Function<FILTER_ERROR, EXPECTED_ERROR> mapper) {
            return previousError != null
                    ? previousError
                    : Objects.requireNonNull(currentResult).mapErr(mapper);
        }

    }

    @SuppressWarnings("unchecked")
    public @NotNull <E extends Exception> FilteredResult<VALUE, E, ERROR> filterWithThrowing(@NotNull ThrowingConsumer<VALUE, @NotNull E> filtersBody) {
        return (@NotNull FilteredResult<VALUE, E, ERROR>) fold(
            value -> {
                try {
                    filtersBody.accept(value);
                    return new FilteredResult<>(null, ok(value));
                } catch (Exception e) {
                    //noinspection unchecked
                    return new FilteredResult<>(null, error((E) e));
                }
            },
            error -> new FilteredResult<>(error(error), null)
        );
    }

    public <COMMON> COMMON fold(@NotNull Function<VALUE, COMMON> valueMerge, @NotNull Function<ERROR, COMMON> errorMerge) {
        return isOk() ? valueMerge.apply(get()) : errorMerge.apply(getError());
    }

    public boolean matches(Predicate<VALUE> condition) {
        return isOk() && condition.test(value);
    }

    public <MAPPED_VALUE> @NotNull ResultImpl<MAPPED_VALUE, ERROR> is(@NotNull Class<MAPPED_VALUE> type, @NotNull Function<VALUE, ERROR> errorSupplier) {
        return this
                .filter(type::isInstance, errorSupplier)
                .map(type::cast);
    }

    public @NotNull ResultImpl<ERROR, VALUE> swap() {
        return isOk() ? error(get()) : ok(getError());
    }

    public ResultImpl<VALUE, ERROR> consume(@NotNull Consumer<VALUE> valueConsumer, @NotNull Consumer<ERROR> errorConsumer) {
        return this.peek(valueConsumer).onError(errorConsumer);
    }

    @SuppressWarnings("unchecked")
    public <REQUIRED_VALUE, REQUIRED_ERROR> @NotNull ResultImpl<REQUIRED_VALUE, REQUIRED_ERROR> project() {
        return (ResultImpl<REQUIRED_VALUE, REQUIRED_ERROR>) this;
    }

    @SuppressWarnings("unchecked")
    public <REQUIRED_ERROR> @NotNull ResultImpl<VALUE, REQUIRED_ERROR> projectToValue() {
        return (ResultImpl<VALUE, REQUIRED_ERROR>) this;
    }

    @SuppressWarnings("unchecked")
    public <REQUIRED_VALUE> @NotNull ResultImpl<REQUIRED_VALUE, ERROR> projectToError() {
        return (ResultImpl<REQUIRED_VALUE, ERROR>) this;
    }

    public @NotNull ResultImpl<VALUE, ERROR> orElse(@NotNull Function<ERROR, @NotNull ResultImpl<VALUE, ERROR>> orElse) {
        return isOk() ? this : orElse.apply(getError());
    }

    public @NotNull VALUE orElseGet(@NotNull Function<ERROR, VALUE> orElse) {
        return isOk() ? get() : orElse.apply(getError());
    }

    public <E extends Exception> @NotNull VALUE orThrow(@NotNull ThrowingFunction<ERROR, E, E> consumer) throws E {
        if (isOk()) {
            return get();
        }

        throw consumer.apply(getError());
    }

    /**
     * @see panda.std.Result#orThrow(panda.std.function.ThrowingFunction)
     */
    @Deprecated
    public <E extends Exception> @NotNull VALUE orElseThrow(@NotNull ThrowingFunction<ERROR, E, E> consumer) throws E {
        return orThrow(consumer);
    }

    public @NotNull ResultImpl<VALUE, ERROR> peek(@NotNull Consumer<VALUE> consumer) {
        if (isOk()) {
            consumer.accept(get());
        }

        return this;
    }

    public @NotNull ResultImpl<VALUE, ERROR> onError(@NotNull Consumer<ERROR> consumer) {
        if (isErr()) {
            consumer.accept(getError());
        }

        return this;
    }

    public boolean isOk() {
        return state == OK;
    }

    public boolean isErr() {
        return state == ERROR;
    }

    public VALUE get() {
        if (isErr()) {
            throw new IllegalStateException("Result contains error - Cannot get the success value");
        }

        return value;
    }

    public ERROR getError() {
        if (isOk()) {
            throw new IllegalStateException("Result completed successfully - Cannot get the error value");
        }

        return error;
    }

    public Object getAny() {
        return isOk() ? value : error;
    }

    @SuppressWarnings("unchecked")
    public <AS> AS getAnyAs() {
        return (AS) getAny();
    }

    public @NotNull Option<@NotNull VALUE> toOption() {
        return Option.of(value);
    }

    public @NotNull Option<@NotNull ERROR> errorToOption() {
        return Option.of(error);
    }

    public @Nullable VALUE orNull() {
        return value;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if ((to == null) || (getClass() != to.getClass())) {
            return false;
        }

        ResultImpl<?, ?> other = (ResultImpl<?, ?>) to;
        return Objects.equals(value, other.value) && Objects.equals(error, other.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return "Result{" + (isOk() ? "VALUE=" + value : "ERR=" + error) + "}";
    }
}
