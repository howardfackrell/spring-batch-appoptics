spring-batch-appoptics
--


This repo demonstrates an issue using the AppOptics java agent
in combination with Spring Batch's @JobScoped steps and late binding

You can find information about installing and using the AppOptics java
agent on the [AppOptics site](https://documentation.solarwinds.com/en/success_center/appoptics/content/kb/apm_tracing/java/install.htm)

You can find information on Spring Batch's late binding feature 
in the [spring batch documentation](https://docs.spring.io/spring-batch/docs/current/reference/html/step.html#late-binding)

## Building the jar 
* use java 17 sdk

```bash
./mvnw clean package
```

## Running the Jar (without AppOptics)
If the jar was successfully built you should be able to run it
with this command
```bash
java -jar ./target/spring-batch-appoptics-0.0.1-SNAPSHOT.jar
```

and the output should look a lot like this:
```bash
java -jar ./target/spring-batch-appoptics-0.0.1-SNAPSHOT.jar                                                       10s  temurin-17.0.6+10 20:06:44
2023-04-25 20:18:28.485  INFO 22212 --- [           main] c.e.s.SpringBatchAppopticsApplication    : Starting SpringBatchAppopticsApplication v0.0.1-SNAPSHOT using Java 17.0.6 on i36383-IS2 with PID 22212 (/Users/howard.fackrell/src/github/octanner/spring-batch-appoptics/target/spring-batch-appoptics-0.0.1-SNAPSHOT.jar started by howard.fackrell in /Users/howard.fackrell/src/github/octanner/spring-batch-appoptics)
2023-04-25 20:18:28.486  INFO 22212 --- [           main] c.e.s.SpringBatchAppopticsApplication    : No active profile set, falling back to 1 default profile: "default"
2023-04-25 20:18:28.813  INFO 22212 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2023-04-25 20:18:28.924  INFO 22212 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2023-04-25 20:18:28.976  INFO 22212 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2023-04-25 20:18:29.000  INFO 22212 --- [           main] o.s.b.c.r.s.JobRepositoryFactoryBean     : No database type set, using meta data indicating: H2
2023-04-25 20:18:29.077  INFO 22212 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : No TaskExecutor has been set, defaulting to synchronous executor.
2023-04-25 20:18:29.103  INFO 22212 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=jobOne]] launched with the following parameters: [{jobParam=2023-04-26T02:18:28.985371Z}]
2023-04-25 20:18:29.143  INFO 22212 --- [           main] o.s.batch.core.job.SimpleStepHandler     : Executing step: [stepOne]
stepOne - with jobParam: 2023-04-26T02:18:28.985371Z Processing 1
stepOne - with jobParam: 2023-04-26T02:18:28.985371Z Processing 2
stepOne Processed 2 items
stepOne - with jobParam: 2023-04-26T02:18:28.985371Z Processing 3
stepOne - with jobParam: 2023-04-26T02:18:28.985371Z Processing 4
stepOne Processed 2 items
stepOne - with jobParam: 2023-04-26T02:18:28.985371Z Processing 5
stepOne Processed 1 items
2023-04-25 20:18:29.683  INFO 22212 --- [           main] o.s.batch.core.step.AbstractStep         : Step: [stepOne] executed in 540ms
2023-04-25 20:18:29.688  INFO 22212 --- [           main] o.s.b.c.l.support.SimpleJobLauncher      : Job: [SimpleJob: [name=jobOne]] completed with the following parameters: [{jobParam=2023-04-26T02:18:28.985371Z}] and the following status: [COMPLETED] in 575ms
2023-04-25 20:18:29.757  INFO 22212 --- [           main] c.e.s.SpringBatchAppopticsApplication    : Started SpringBatchAppopticsApplication in 1.476 seconds (JVM running for 1.685)
2023-04-25 20:18:29.761  INFO 22212 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2023-04-25 20:18:29.762  INFO 22212 --- [ionShutdownHook] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
```

## Demonstrating the problem
Set up the AppOptics agent as described on the AppOptics site
and run the jar again with the agent (no need to recompile the spring-batch-appoptics jar file)
```bash
java -jar  -javaagent:./appoptics-agent.jar ./target/spring-batch-appoptics-0.0.1-SNAPSHOT.jar
```

This time you'll get a java.lang.VerifyError
```text
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'jobLaunchComponent': Unsatisfied dependency expressed through field 'jobOne'; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'jobOne' defined in class path resource [com/example/springbatchappoptics/BatchConfig.class]: Unsatisfied dependency expressed through method 'jobOne' parameter 1; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'stepOne' defined in BeanDefinition defined in class path resource [com/example/springbatchappoptics/BatchConfig.class]: Initialization of bean failed; nested exception is java.lang.VerifyError: Bad type on operand stack
Exception Details:
  Location:
    jdk/proxy2/$Proxy56.execute(Lorg/springframework/batch/core/StepExecution;)V @112: invokevirtual
  Reason:
    Type 'java/lang/Object' (current frame, stack[0]) is not assignable to 'org/springframework/batch/core/StepExecution'
  Current Frame:
    bci: @112
    flags: { }
    locals: { 'jdk/proxy2/$Proxy56', 'java/lang/Object', top, top, top, null, 'java/lang/Throwable' }
    stack: { 'java/lang/Object' }
```
