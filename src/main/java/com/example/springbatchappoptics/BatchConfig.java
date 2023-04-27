package com.example.springbatchappoptics;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobOne(JobBuilderFactory jobBuilderFactory, Step stepOne) {
        return jobBuilderFactory.get("jobOne")
                .start(stepOne)
                .build();
    }

    @Bean
    @JobScope
    public Step stepOne(StepBuilderFactory stepBuilderFactory,
                        @Value("#{jobParameters[jobParam]}") String jobParam)  {
        return stepBuilderFactory.get("stepOne")
                .<Integer, Integer>chunk(2)
                .reader(new IntReader())
                .processor(new IntProcessor("stepOne - with jobParam: " + jobParam ))
                .writer(new IntWriter("stepOne"))
                .build();
    }

//    @Bean
//    public Step stepOne(StepBuilderFactory stepBuilderFactory)  {
//        return stepBuilderFactory.get("stepOne")
//                .<Integer, Integer>chunk(2)
//                .reader(new IntReader())
//                .processor(new IntProcessor("stepOne - without @JobScope & late binding"))
//                .writer(new IntWriter("stepOne"))
//                .build();
//    }

    @Bean
    public SimpleJobLauncher simpleJobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }

    public static class IntReader extends ListItemReader<Integer> {
        public IntReader() {
            super(IntStream.range(1, 6).boxed().toList());
        }

    }

    public static class IntProcessor implements ItemProcessor<Integer, Integer> {
        private final String context;

        public IntProcessor(String context) {
            this.context = context;
        }

        @Override
        public Integer process(Integer item) throws Exception {
            System.out.println(context + " Processing " + item);
            Thread.sleep(100);
            return item;
        }
    }

    public static class IntWriter implements ItemWriter<Integer> {
        private final String context;

        public IntWriter(String context) {
            this.context = context;
        }

        @Override
        public void write(List<? extends Integer> list) throws Exception {
            System.out.println(context + " Processed " + list.size() +" items");
        }
    }
}
