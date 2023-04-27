package com.example.springbatchappoptics;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Map;

@Component
public class JobLaunchComponent {

    @Autowired
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    private Job jobOne;

    @PostConstruct
    public void postContruct() throws Exception {
        Map<String, JobParameter> jobParamMap =
                Map.of("jobParam", new JobParameter(Instant.now().toString()));
        simpleJobLauncher.run(jobOne, new JobParameters(jobParamMap));
    }

}
