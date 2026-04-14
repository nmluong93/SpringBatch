package com.springbatch.simplespringbatch.listener;

import com.springbatch.simplespringbatch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductReadListener implements ItemReadListener<Product> {
    /**
     * Called if an error occurs while trying to read.
     *
     * @param ex thrown from {@link ItemReader}
     */
    @Override
    public void onReadError(Exception ex) {
        log.info("MyReadListener:onReadError - {}", ex.getMessage());
    }

    /**
     * Called before {@link ItemReader#read()}
     */
    @Override
    public void beforeRead() {
        log.info("MyReadListener:beforeRead");
    }

    /**
     * Called after {@link ItemReader#read()}. This method is called only for actual items
     * (that is, it is not called when the reader returns {@code null}).
     *
     * @param item returned from read()
     */
    @Override
    public void afterRead(Product item) {
        log.info("MyReadListener:afterRead - {}", item.getProductName());
    }
}
