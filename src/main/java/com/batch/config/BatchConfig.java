package com.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.steps.ItemDescompressStep;
import com.batch.steps.ItemProcessorStep;
import com.batch.steps.ItemReaderStep;
import com.batch.steps.ItemWriterStep;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	/** Tasklets */
	@Bean
	@JobScope
	ItemDescompressStep itemDescompressStep() {
		return new ItemDescompressStep();
	}

	@Bean
	@JobScope
	ItemReaderStep itemReaderStep() {
		return new ItemReaderStep();
	}

	@Bean
	@JobScope
	ItemProcessorStep itemProcessorStep() {
		return new ItemProcessorStep();
	}

	@Bean
	@JobScope
	ItemWriterStep itemWriterStep() {
		return new ItemWriterStep();
	}

	/** Steps */
	@Bean
	Step descompressFileStep() {
		return stepBuilderFactory.get("itemDescompressStep").tasklet(itemDescompressStep()).build();
	}
	
	@Bean
	Step readerFileStep() {
		return stepBuilderFactory.get("itemReaderStep").tasklet(itemReaderStep()).build();
	}
	
	@Bean
	Step processDataStep() {
		return stepBuilderFactory.get("itemProcessorStep").tasklet(itemProcessorStep()).build();
	}
	
	@Bean
	Step writerDataStep() {
		return stepBuilderFactory.get("itemWriterStep").tasklet(itemWriterStep()).build();
	}
	
	/** Job */
	/** Se llaman a los Steps */
	@Bean
	Job readCsvJob(){
		return jobBuilderFactory.get("readCsvJob")
				.start(descompressFileStep())
				.next(readerFileStep())
				.next(processDataStep())
				.next(writerDataStep())
				.build();
	}
}
