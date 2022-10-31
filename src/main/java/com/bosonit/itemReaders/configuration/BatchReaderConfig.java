package com.bosonit.itemReaders.configuration;

import com.bosonit.coreConcept.processor.InMemItemProcessor;
import com.bosonit.coreConcept.reader.InMemReader;
import com.bosonit.coreConcept.writer.ConsoleItemWriter;
import com.bosonit.itemReaders.model.Product;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@EnableBatchProcessing
@Configuration
public class BatchReaderConfig {


    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    InMemItemProcessor inMemItemProcessor;
    @StepScope
    @Bean
    public FlatFileItemReader flatFileItemReader(@Value("#{jobParameters['fileInput']}") FileSystemResource inputFile)
    {
        FlatFileItemReader reader = new FlatFileItemReader();
        //step 1. let reader know where is the file
        reader.setResource(inputFile);

        //step 2. create the line mapper
        reader.setLineMapper(
                new DefaultLineMapper<Product>()
                {
                    {
                        setLineTokenizer( new DelimitedLineTokenizer()
                        {
                            {
                            setNames("productID", "productName", "productDesc", "price", "unit");
                            setDelimiter("|");
                            }
                        });

                        setFieldSetMapper(new BeanWrapperFieldSetMapper<>(){
                            {
                                setTargetType(Product.class);
                            }
                        });
                    }
        });
        //step 3. tell reader to skip header.
        reader.setLinesToSkip(1);

        return reader;
    }

    @Bean
    public InMemReader reader2(){
        return new InMemReader();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .chunk(4)
                .reader(flatFileItemReader(null))
                .writer(new ConsoleItemWriter())
                .build();
    }

    @Bean
    public Job secondJobTest(){
        return jobBuilderFactory.get("SecondJobTest")
                .incrementer(new RunIdIncrementer())
                .start(step3())
                .build();
    }
}
