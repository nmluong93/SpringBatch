package com.springbatch.simplespringbatch.listener;

import com.springbatch.simplespringbatch.domain.OSProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ItemWriteListener;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductWriteListener implements ItemWriteListener<OSProduct> {

    /**
     * Called after {@link ItemWriter#write(Chunk)}. This is called before any transaction
     * is committed, and before {@link ChunkListener#afterChunk(ChunkContext)}.
     *
     * @param items written items
     */
    @Override
    public void afterWrite(Chunk<? extends OSProduct> items) {
        log.info("MyItemWriterListener:afterWrite - Product count {}", items.size());
    }

    /**
     * Called before {@link ItemWriter#write(Chunk)}
     *
     * @param items to be written
     */
    @Override
    public void beforeWrite(Chunk<? extends OSProduct> items) {
        log.info("MyItemWriterListener:beforeWrite - Product count {}", items.size());
    }

    /**
     * Called if an error occurs while trying to write. Called inside a transaction, but
     * the transaction will normally be rolled back. There is no way to identify from this
     * callback which of the items (if any) caused the error.
     *
     * @param exception thrown from {@link ItemWriter}
     * @param items     attempted to be written.
     */
    @Override
    public void onWriteError(Exception exception, Chunk<? extends OSProduct> items) {
        log.info("MyItemWriterListener:onWriteError - Error {}", exception.getMessage());
    }
}
