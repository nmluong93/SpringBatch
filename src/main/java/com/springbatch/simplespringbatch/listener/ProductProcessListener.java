package com.springbatch.simplespringbatch.listener;

import com.springbatch.simplespringbatch.domain.OSProduct;
import com.springbatch.simplespringbatch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemProcessListener;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductProcessListener implements ItemProcessListener<Product, OSProduct> {
    @Override
    public void beforeProcess(Product item) {
        log.info("MyItemProcessListener:beforeProcess - {}", item.getProductName());
    }

    @Override
    public void afterProcess(Product item, OSProduct result) {
        log.info("MyItemProcessListener:afterProcess - {}", item.getProductName());
    }

    /**
     * Called if an exception was thrown from {@link ItemProcessor#process(Object)}.
     *
     * @param item attempted to be processed
     * @param e    - exception thrown during processing.
     */
    @Override
    public void onProcessError(Product item, Exception e) {
        log.info("MyItemProcessListener:onProcessError - {}. Exception msg : {}", item.getProductName(), e.getMessage());
    }
}
