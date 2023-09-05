package com.batch.steps;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.aspose.cells.Cell;
import com.aspose.cells.Cells;
import com.aspose.cells.Row;
import com.aspose.cells.RowCollection;
import com.aspose.cells.TxtLoadOptions;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.batch.model.Persona;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemReaderStep implements Tasklet {

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		log.info("------------> Inicio del paso de LECTURA <------------");

		Resource resource = resourceLoader.getResource("classpath:files/destino/personas.csv");
		String filePath = resource.getFile().getAbsolutePath();
		
		TxtLoadOptions opts = new TxtLoadOptions();
		opts.setSeparator(',');
		opts.setConvertNumericData(true);
		
		/** Crea un libro con el archivo csv y los parametros de opts */
		/** La notación classpath devuelve automaticamente la ruta de resource */
		Workbook workbook = new Workbook(filePath, opts);

		/** Accede a la primera Hoja de trabajo */
		Worksheet sheet = workbook.getWorksheets().get(0);
		
		List<Persona> personaList = new ArrayList<>();
		for (int i = 1; i <= sheet.getCells().getMaxDataRow(); i++) {
			
			/** Accede a la fila i de la hoja de trabajo */
            Row fila = sheet.getCells().getRows().get(i);
            Persona persona = new Persona();
            
            /** Accede a la columna N de la hoja de trabajo */
			persona.setNombre(fila.get(0).getStringValue());
			persona.setApellido(fila.get(1).getStringValue());
			persona.setEdad(fila.get(2).getStringValue());
			personaList.add(persona);
        }
		sheet.closeAccessCache(0);
		workbook.closeAccessCache(0);

		/** Guardamos lista en el contexto de Spring Batch */
		chunkContext.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext()
			.put("personaList", personaList);
		
		log.info("------------> Fin del paso de LECTURA <------------");

		return RepeatStatus.FINISHED;
	}

}
