package com.sumup.jobprocessing.web;

import com.sumup.jobprocessing.payload.JobRequest;
import com.sumup.jobprocessing.payload.JobResponse;
import com.sumup.jobprocessing.payload.task.Task;
import com.sumup.jobprocessing.service.JobProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobProcessingController {

  public static final String SHEBANG_LINE = "#!/usr/bin/env bash\n\n";
  private final JobProcessingService jobProcessingService;

  @Autowired
  public JobProcessingController(JobProcessingService jobProcessingService) {
    this.jobProcessingService = jobProcessingService;
  }

  /**
   * This endpoint processes the collection of tasks and returns them in execution order in json format
   * It can be tested with swagger <a href="http://localhost:8080/swagger-ui/index.html">...</a>
   * @param job request containing tasks to be processed
   * @return tasks in execution order
   */
  @PostMapping(value = "/process")
  @Operation(summary = "Process collection of tasks and returns them in proper execution order in json format")
  public JobResponse process(@RequestBody JobRequest job) {
    return JobResponse.builder().tasks(this.jobProcessingService.process(job.getTasks())).build();
  }

  /**
   * This endpoint allows us to run the processed commands directly from shell with the following command
   * $ curl -H "Content-Type: application/json" -d @mytasks.json http://localhost:8080/process-to-script | bash
   * @param job request containing tasks to be processed
   * @return commands in execution order
   */
  @PostMapping(value = "/process-to-script")
  @Operation(summary = "Process collection of tasks and returns them in order ready to execute")
  public String processToScriptRepresentation(@RequestBody JobRequest job) {
    StringBuilder sb = new StringBuilder(SHEBANG_LINE);
    List<Task> tasks = this.jobProcessingService.process(job.getTasks());
    for (Task task : tasks) {
      sb.append(task.getCommand()).append("\n");
    }
    return sb.toString();
  }
}
