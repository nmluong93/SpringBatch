package com.springbatch.simplespringbatch.listener;

import com.springbatch.simplespringbatch.domain.OSProduct;
import com.springbatch.simplespringbatch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyChunkListener implements ChunkListener<Product, OSProduct> {
    /**
     * Callback after the chunk is written, inside the transaction. <strong>This method is
     * not called in concurrent steps.</strong>
     *
     * @param chunk
     * @since 6.0
     */
    @Override
    public void afterChunk(Chunk<OSProduct> chunk) {
        log.info("MyChunkListener:afterChunk");
    }

    /**
     * Callback after a chunk is read but before it is processed, inside the transaction.
     * <strong>This method is not called in concurrent steps.</strong>
     *
     * @param chunk
     * @since 6.0
     */
    @Override
    public void beforeChunk(Chunk<Product> chunk) {
        log.info("MyChunkListener:beforeChunk");
    }

    /**
     * Callback if an exception occurs while processing or writing a chunk, inside the
     * transaction, which is about to be rolled back. <em>As a result, you should use
     * {@code PROPAGATION_REQUIRES_NEW} for any transactional operation that is called
     * here</em>. <strong>This method is not called in concurrent steps.</strong>
     *
     * @param exception the exception that caused the underlying rollback.
     * @param chunk     the processed chunk
     * @since 6.0
     */
    @Override
    public void onChunkError(Exception exception, Chunk<OSProduct> chunk) {
        log.info("MyChunkListener:onChunkError - {}", exception.getMessage());
    }
}
