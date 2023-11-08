package com.sumup.jobprocessing.service;

import com.sumup.jobprocessing.payload.task.Task;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JobProcessingServiceImplTest {

  public static final String TASK_1 = "task-1";
  public static final String TASK_3 = "task-3";
  public static final String TASK_2 = "task-2";
  public static final String TASK_4 = "task-4";
  public static final Task.TaskBuilder TASK_1_BUILDER = Task.builder().name(TASK_1).command("touch /tmp/file1");
  public static final Task.TaskBuilder TASK_3_BUILDER = Task.builder().name(TASK_3).command("echo 'Hello World!' > /tmp/file1");
  public static final Task.TaskBuilder TASK_2_BUILDER = Task.builder().name(TASK_2).command("cat /tmp/file1");
  public static final Task.TaskBuilder TASK_4_BUILDER = Task.builder().name(TASK_4).command("rm /tmp/file1");

  private final JobProcessingService jobProcessingService = new JobProcessingServiceImpl();

  @Test
  public void processTest() {
    List<Task> expected = getExpectedProcessedTasks();
    List<Task> actual = this.jobProcessingService.process(getRequestedTasks());
    assertEquals(expected, actual);
  }

  @Test
  public void processNullTasksTest() {
    List<Task> expected = Collections.emptyList();
    List<Task> actual = this.jobProcessingService.process(null);
    assertEquals(expected, actual);
  }

  private List<Task> getExpectedProcessedTasks() {
    return new ArrayList<>() {{
      add(TASK_1_BUILDER.build());
      add(TASK_3_BUILDER.build());
      add(TASK_2_BUILDER.build());
      add(TASK_4_BUILDER.build());
    }};
  }

  private List<Task> getRequestedTasks() {
    return new ArrayList<>() {{
      add(TASK_1_BUILDER.build());
      add(TASK_2_BUILDER.requires(List.of(TASK_3)).build());
      add(TASK_3_BUILDER.requires(List.of(TASK_1)).build());
      add(TASK_4_BUILDER.requires(List.of(TASK_2, TASK_3)).build());
    }};
  }

}
