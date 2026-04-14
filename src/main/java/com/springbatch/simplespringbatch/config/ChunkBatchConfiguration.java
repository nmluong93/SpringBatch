package com.springbatch.simplespringbatch.config;//package com.springbatch.simplespringbatch.config;

import com.springbatch.simplespringbatch.domain.OSProduct;
import com.springbatch.simplespringbatch.domain.Product;
import com.springbatch.simplespringbatch.domain.ProductFieldSetMapper;
import com.springbatch.simplespringbatch.domain.ProductRowMapper;
import com.springbatch.simplespringbatch.processor.MyProductItemProcessor;
import com.springbatch.simplespringbatch.reader.ProductNameItemReader;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableBatchProcessing
public class ChunkBatchConfiguration {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(ChunkBatchConfiguration.class);

    @Bean
    public ItemReader<String> itemReader() {
        List<String> productList = Arrays.asList(new String[]{
                "product_1", "product_2", "product_3", "product_4", "product_5", "product_6", "product_7", "product_8",
                "product_9", "product_10", "product_11", "product_12",
                "product_13", "product_14", "product_15", "product_16", "product_17", "product_18",
        });
        return new ProductNameItemReader(productList);
    }

    @Bean
    public ItemReader<Product> flatFileItemReader() {
        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("product_id", "product_name", "product_category", "product_price");
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new ProductFieldSetMapper());

        return new FlatFileItemReaderBuilder<Product>()
                .linesToSkip(1)
                .resource(new ClassPathResource("/data/Product_Details.csv"))
                .lineMapper(lineMapper)
                .name("ProductDetail_ItemReader")
                .build();
    }

    @Bean
    public ItemReader<Product> jdbcCursorItemReader() {

        return new JdbcCursorItemReaderBuilder<Product>()
                .dataSource(dataSource)
                // ordering is very important
                .sql("select * from PRODUCT_DETAILS order by product_id asc")
                .rowMapper(new ProductRowMapper())
                .name("JDBCProduct_ItemReader")
                .build();
    }

    /**
     * This enables reading data in page by page, it is suitable for multi-threaded env b/c it is thread-safe
     * @return
     */
    @Bean
    public ItemReader<Product> jdbcPagingItemReader() throws Exception {

        SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
        sqlPagingQueryProviderFactoryBean.setSelectClause("select product_id, product_name, product_category, product_price");
        sqlPagingQueryProviderFactoryBean.setFromClause("from PRODUCT_DETAILS");
        sqlPagingQueryProviderFactoryBean.setSortKey("product_id");

        return new JdbcPagingItemReaderBuilder<Product>()
                .dataSource(dataSource)
                // ordering is very important
                .queryProvider(sqlPagingQueryProviderFactoryBean.getObject())
                .rowMapper(new ProductRowMapper())
                .name("JDBCPagingProduct_ItemReader")
                // This number should be equal to chunk size
                .pageSize(10)
                .build();
    }

    @Bean
    public ItemWriter<Product> flatFileItemWriter() {
        DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"productId", "productName", "productCategory", "productPrice"});

        lineAggregator.setFieldExtractor(fieldExtractor);

        return new FlatFileItemWriterBuilder<Product>()
                .name("FlatFileItemWriter")
                .resource(new FileSystemResource("src/main/resources/data/Product_Details_FromDB.csv"))
                .lineAggregator(lineAggregator)
                .build();
    }

    @Bean
    public ItemWriter<OSProduct> jdbcBatchItemWriter() throws Exception {
        return new JdbcBatchItemWriterBuilder<OSProduct>()
                .dataSource(dataSource)
                .sql("insert into OS_PRODUCT_DETAILS_OUTPUT values(:productId, :productName, :productCategory, :productPrice, :taxPercent, :sku, :shippingRate)")
//                .itemPreparedStatementSetter(new ProductItemPrepareStatementSetter())
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

    @Bean
    public ItemProcessor<Product, OSProduct> myProductItemProcessor() {
        return new MyProductItemProcessor();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(10)
                .reader(itemReader())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(Chunk<? extends String> chunk) throws Exception {
                        log.info("Processing chunk in item writer with size: {}", chunk == null ? 0 : chunk.getItems().size());
                        chunk.getItems().forEach(item -> {
                            log.info("Item Writer: {}", item);
                        });
                        log.info("Finish processing chunk");
                    }
                })
                .build();
    }

    @Bean
    public Step productStep() throws Exception {
        return new StepBuilder("productStep", jobRepository)
                .<Product, Product>chunk(3)
                .reader(jdbcPagingItemReader())
                .writer(new ItemWriter<Product>() {
                    @Override
                    public void write(Chunk<? extends Product> chunk) throws Exception {
                        log.info("Processing chunk in item writer with size: {}", chunk == null ? 0 : chunk.getItems().size());
                        chunk.getItems().forEach(item -> {
                            log.info("Item Writer: {}", item);
                        });
                        log.info("Finish processing chunk");
                    }
                })
                .build();
    }

    @Bean
    public Step productStepFromJdbcPagingItemReaderToFlatFileWriter() throws Exception {
        return new StepBuilder("productStep2", jobRepository)
                .<Product, OSProduct>chunk(3)
                .reader(jdbcPagingItemReader())
                .processor(myProductItemProcessor())
                .writer(jdbcBatchItemWriter())
                .build();
    }


    @Bean
    public Job firstJob(JobRepository jobRepository) throws Exception {
        return new JobBuilder("simpleJobWithNextTransition", jobRepository)
                .start(productStepFromJdbcPagingItemReaderToFlatFileWriter())
                .build();
    }

    public static class CustomizedTaskLet implements Tasklet {
        private final String stepInfo;

        public CustomizedTaskLet(String logMsg) {
            this.stepInfo = logMsg;
        }

        @Override
        public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
            log.info("Executing: " + stepInfo);
            return RepeatStatus.FINISHED;
        }
    }
}
