package com.bosonit.coreConcept.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HwJobExecutionListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before starting the Job - Job name: " + jobExecution.getJobInstance().getJobName());
        System.out.println("before starting the Job" + jobExecution.getExecutionContext());
        jobExecution.getExecutionContext().put("my name", "marcos");
        System.out.println("before starting the Job - after set: " + jobExecution.getExecutionContext());

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after starting the Job - Job execution context: " + jobExecution.getExecutionContext());

    }
}
