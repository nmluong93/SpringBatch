package com.springbatch.simplespringbatch.reader;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemReader;

import java.util.Iterator;
import java.util.List;

public class ProductNameItemReader implements ItemReader<String> {


    private final Iterator<String> productListIterator;

    public ProductNameItemReader(List<String> productListIterator) {
        this.productListIterator = productListIterator.iterator();
    }

    /**
     * Reads a piece of input data and advance to the next one. Implementations
     * <strong>must</strong> return <code>null</code> at the end of the input data set. In
     * a transactional setting, caller might get the same item twice from successive calls
     * (or otherwise), if the first call was in a transaction that rolled back.
     *
     * @return T the item to be processed or {@code null} if the data source is exhausted
     */
    @Override
    public @Nullable String read() {
        return productListIterator.hasNext() ? productListIterator.next() : null;
    }
}
