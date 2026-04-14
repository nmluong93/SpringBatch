package com.springbatch.simplespringbatch.processor;

import com.springbatch.simplespringbatch.domain.Product;
import com.springbatch.simplespringbatch.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

import java.util.Random;

@Slf4j
public class ProductFilterItemProcessor implements ItemProcessor<Product, Product> {
    //FIXME just for testing for transient exception
    private static int retryCount = 0;

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
        log.info("Filter for Product {}", item.toString());
//        if (item.getProductId() % 2 == 0) {
//            return item;
//        }
//        return null;
        if (item.getProductPrice() == 500 && retryCount++ < 7) {
            log.error("Exception thrown");
            throw new MyException("Product price is less than 500");
        }
        return item;
    }
}
