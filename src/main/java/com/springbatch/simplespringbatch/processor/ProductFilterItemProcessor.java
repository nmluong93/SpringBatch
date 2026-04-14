package com.springbatch.simplespringbatch.processor;

import com.springbatch.simplespringbatch.domain.Product;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class ProductFilterItemProcessor implements ItemProcessor<Product, Product> {
    /**
     * Process the provided item, returning a potentially modified or new item for
     * continued processing. If the returned result is {@code null}, it is assumed that
     * processing of the item should not continue.
     * <p>
     * A {@code null} item will never reach this method because the only possible sources
     * are:
     * <ul>
     * <li>an {@link ItemReader} (which indicates no more items)</li>
     * <li>a previous {@link ItemProcessor} in a composite processor (which indicates a
     * filtered item)</li>
     * </ul>
     *
     * @param item to be processed, never {@code null}.
     * @return potentially modified or new item for continued processing, {@code null} if
     * processing of the provided item should not continue.
     * @throws Exception thrown if exception occurs during processing.
     */
    @Override
    public @Nullable Product process(Product item) throws Exception {
        if (item.getProductId() % 2 == 0) {
            return item;
        }
        return null;
    }
}
