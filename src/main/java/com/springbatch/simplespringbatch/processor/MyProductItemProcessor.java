package com.springbatch.simplespringbatch.processor;

import com.springbatch.simplespringbatch.domain.OSProduct;
import com.springbatch.simplespringbatch.domain.Product;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class MyProductItemProcessor implements ItemProcessor<Product, OSProduct> {
    private static final Logger log = LoggerFactory.getLogger(MyProductItemProcessor.class);

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
    public @Nullable OSProduct process(Product item) throws Exception {
        int price = item.getProductPrice();
        item.setProductPrice(price * 110/100);
        log.info("Processing product {} with original price {} and increasing to {}", item.getProductName(), price, item.getProductPrice());

        int taxPercent = item.getProductCategory().equals("Sports Accessories") ? 5 : 18;
        String sku = item.getProductName().substring(0, 3) + item.getProductId();
        int shippingRate = item.getProductPrice() < 1000 ? 75: 0;
        return new OSProduct(item, taxPercent, sku, shippingRate);
    }
}
