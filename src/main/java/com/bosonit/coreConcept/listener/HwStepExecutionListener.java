package com.bosonit.coreConcept.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HwStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("this is before StepExecution" + stepExecution.getJobExecution().getExecutionContext());
        System.out.println("In side step - print job parameters: " + stepExecution.getJobExecution().getJobParameters());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("this is after StepExecution" + stepExecution.getJobExecution().getExecutionContext());
        return null;
    }
}
