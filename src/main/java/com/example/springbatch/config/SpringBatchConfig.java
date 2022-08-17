package com.example.springbatch.config;


import com.example.springbatch.entity.PersonsDetails;
import com.example.springbatch.repository.PersonDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;


@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private PersonDetailsRepository personDetailsRepository;

    @Bean
    public FlatFileItemReader<PersonsDetails> reader(){
        FlatFileItemReader<PersonsDetails> itemReader= new FlatFileItemReader<PersonsDetails>();
        itemReader.setResource(new FileSystemResource("src/main/resources/Details.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<PersonsDetails> lineMapper() {
        DefaultLineMapper<PersonsDetails> lineMapper= new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer= new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id","firstName","lastName","emailId","gender","contactNo","country");

        BeanWrapperFieldSetMapper<PersonsDetails> fieldSetMapper= new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(PersonsDetails.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public PersonDetailsProcessor personDetailsProcessor() {
        return new PersonDetailsProcessor();

    }

    @Bean
    public RepositoryItemWriter<PersonsDetails> writer(){
        RepositoryItemWriter<PersonsDetails> writer = new RepositoryItemWriter<>();
        writer.setRepository(personDetailsRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("csv-step").<PersonsDetails,PersonsDetails>chunk(10)
                .reader(reader())
                .processor(personDetailsProcessor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importPersonDetails")
                .flow(step1()).end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

}