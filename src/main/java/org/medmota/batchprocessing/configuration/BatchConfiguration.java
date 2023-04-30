package org.medmota.batchprocessing.configuration;

import javax.sql.DataSource;

import org.medmota.batchprocessing.configuration.processor.EmployeeDtoFieldSetMapper;
import org.medmota.batchprocessing.configuration.processor.EmployeeItemProcessor;
import org.medmota.batchprocessing.configuration.processor.JobCompletionListener;
import org.medmota.batchprocessing.dtos.EmployeeDto;
import org.medmota.batchprocessing.models.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	private String[] format = new String[] { "employeeId", "firstName", "lastName", "jobTitle", "email", "companyId" };

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private EmployeeDtoFieldSetMapper employeeDtoFieldSetMapper;

	@Bean
	public Job processJob(Step step) {
		return jobBuilderFactory.get("processJob").incrementer(new RunIdIncrementer()).listener(listener()).flow(step)
				.end().build();
	}

	@Bean
	public JobExecutionListener listener() {
		return new JobCompletionListener();
	}

	@Bean
	public Step orderStep1(JdbcBatchItemWriter<Employee> writer) {
		return stepBuilderFactory.get("orderStep1").<EmployeeDto, Employee>chunk(10).reader(flatFileItemReader())
				.processor(employeeItemProcessor()).writer(writer).build();
	}

	@Bean
	public FlatFileItemReader<EmployeeDto> flatFileItemReader() {
		return new FlatFileItemReaderBuilder<EmployeeDto>().name("flatFileItemReader")
				.resource(new ClassPathResource("input/employeedata.csv")).delimited().names(format).linesToSkip(1)
				.lineMapper(lineMapper()).fieldSetMapper(new BeanWrapperFieldSetMapper<EmployeeDto>() {
					{
						setTargetType(EmployeeDto.class);
					}
				}).build();
	}

	@Bean
	public LineMapper<EmployeeDto> lineMapper() {
		final DefaultLineMapper<EmployeeDto> defaultLineMapper = new DefaultLineMapper<>();
		final DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames(format);

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(employeeDtoFieldSetMapper);

		return defaultLineMapper;
	}

	@Bean
	public EmployeeItemProcessor employeeItemProcessor() {
		return new EmployeeItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Employee> writer(final DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Employee>()
				.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
				.sql("INSERT INTO employee(employeeId, firstName, lastName, jobTitle, email, "
						+ "companyId) VALUES(:employeeId, :firstName, :lastName, :jobTitle, :email," + " "
						+ ":companyId)")
				.dataSource(dataSource).build();
	}

}
