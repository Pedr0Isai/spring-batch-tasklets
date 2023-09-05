package com.batch.steps;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.batch.model.Persona;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemProcessorStep implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("------------> Inicio del paso de PROCESAMIENTO <------------");

		/** Traemos la lista hecha por el paso anterior a traves del contexto de Spring Batch */
		List<Persona> personaList = (List<Persona>) chunkContext.getStepContext()
				.getStepExecution()
				.getJobExecution()
				.getExecutionContext()
				.get("personaList");
		
		List<Persona> personaListMod = personaList.stream().map(persona -> {
			DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			persona.setFechaAlta(format.format(LocalDateTime.now()));
			return persona;
		}).collect(Collectors.toList());
		
		/** Guardamos lista en el contexto de Spring Batch, al ser la misma llave la sobreescribe */
		chunkContext.getStepContext()
					.getStepExecution()
					.getJobExecution()
					.getExecutionContext()
					.put("personaList", personaListMod);
		
		log.info("------------> Fin del paso de PROCESAMIENTO <------------");
		
		return RepeatStatus.FINISHED;
	}

}
