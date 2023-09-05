package com.batch.steps;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.batch.model.Persona;
import com.batch.service.IPersonaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemWriterStep implements Tasklet {
	
	@Autowired
	private IPersonaService service;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("------------> Inicio del paso de ESCRITURA <------------");
		
		/** Traemos la lista hecha por el paso anterior a traves del contexto de Spring Batch */
		List<Persona> personaList = (List<Persona>) chunkContext.getStepContext()
				.getStepExecution()
				.getJobExecution()
				.getExecutionContext()
				.get("personaList");
		
		personaList.forEach(p ->{
			if(p != null){
                log.info(p.toString());
            }
		});
		
		log.info("Número de entidades agregadas: " + personaList.size());
	
		service.saveAll(personaList);
		
		log.info("------------> Fin del paso de ESCRITURA <------------");
		
		return RepeatStatus.FINISHED;
	}

}
