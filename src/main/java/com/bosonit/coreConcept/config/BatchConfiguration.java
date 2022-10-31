package com.bosonit.coreConcept.config;

import com.bosonit.coreConcept.processor.InMemItemProcessor;
import com.bosonit.coreConcept.reader.InMemReader;
import com.bosonit.coreConcept.writer.ConsoleItemWriter;
import com.bosonit.coreConcept.listener.HwJobExecutionListener;
import com.bosonit.coreConcept.listener.HwStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@EnableBatchProcessing
@Configuration
public class BatchConfiguration {
    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    HwJobExecutionListener hwJobExecutionListener;

    @Autowired
    HwStepExecutionListener hwStepExecutionListener;

    @Autowired
    InMemItemProcessor inMemItemProcessor;

    private Tasklet helloWorldTasklet() {
        return  new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Hello world");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .listener(hwStepExecutionListener)
                .tasklet(helloWorldTasklet())
                .build();
    }
    @Bean
    public InMemReader reader(){
        return new InMemReader();
    }
    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .<Integer, Integer>chunk(3)
                .reader(reader())
                .processor(inMemItemProcessor)
                .writer(new ConsoleItemWriter())
                .build();
    }


    @Bean
    public Job helloWorldJob(){
        return jobBuilderFactory.get("HelloWorldJob")
                .incrementer(new RunIdIncrementer())
                .listener(hwJobExecutionListener)
                .start(step1())
                .next(step2())
                .build();
    }
}
