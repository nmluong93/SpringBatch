package com.springbatch.simplespringbatch.listener;

import com.springbatch.simplespringbatch.domain.OSProduct;
import com.springbatch.simplespringbatch.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.batch.infrastructure.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Component
public class MySkipListener implements SkipListener<Product, OSProduct> {
    @Override
    public void onSkipInRead(Throwable t) {
        if (t instanceof FlatFileParseException exp) {
            log.info("Cannot parse line with error {}", exp.getMessage());
            // print line which cannot be parsed e.g with redundant comma
            writeToFile(exp.getInput());
        }
    }

    /**
     * This item failed on write with the given exception, and a skip was called for.
     *
     * @param item the failed item
     * @param t    the cause of the failure
     */
    @Override
    public void onSkipInWrite(OSProduct item, Throwable t) {
        SkipListener.super.onSkipInWrite(item, t);
    }

    /**
     * This item failed on processing with the given exception, and a skip was called for.
     *
     * @param item the failed item
     * @param t    the cause of the failure
     */
    @Override
    public void onSkipInProcess(Product item, Throwable t) {
        log.warn("Skipping item {} due to {}", item, t.getMessage());
        writeToFile(item.toString());
    }

    public void writeToFile(String data) {
        try (FileWriter fileWriter = new FileWriter("rejected/Product_Details_Rejected.txt", true)) {
            fileWriter.write(data + "\n");
            log.info("Successfully wrote to the file");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
