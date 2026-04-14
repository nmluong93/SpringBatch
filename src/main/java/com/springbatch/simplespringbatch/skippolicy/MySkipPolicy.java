package com.springbatch.simplespringbatch.skippolicy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MySkipPolicy implements SkipPolicy {
    /**
     * Returns true or false, indicating whether or not processing should continue with
     * the given throwable. Clients may use {@code skipCount < 0} to probe for exception
     * types that are skippable, so implementations should be able to handle gracefully
     * the case where {@code skipCount < 0}. Implementations should avoid throwing any
     * undeclared exceptions.
     *
     * @param t         exception encountered while processing
     * @param skipCount currently running count of skips
     * @return true if processing should continue, false otherwise.
     * @throws SkipLimitExceededException if a limit is breached
     * @throws IllegalArgumentException   if the exception is null
     */
    @Override
    public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
        log.info("Skip Count: {}", skipCount);
        if (t instanceof ValidationException exp && skipCount > 3) {
            return false;
        }
        if (t instanceof FlatFileParseException exp && skipCount > 3) {
            return false;
        }
        return true;
    }
}
