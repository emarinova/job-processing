package com.sumup.jobprocessing.service;

import com.sumup.jobprocessing.payload.task.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class JobProcessingServiceImpl implements JobProcessingService {

  /**
   * Reorders requested tasks in execution order
   * @param requestedTasks tasks passed by the user with request
   * @return tasks in execution order
   */
  @Override
  public List<Task> process(List<Task> requestedTasks) {
    if (requestedTasks == null) {
      return new ArrayList<>();
    }

    Map<String, Task> processedTasks = new LinkedHashMap<>();
    List<Task> waitingTasks = new LinkedList<>();

    for (Task task : requestedTasks) {
      if (isTaskReadyForProcessing(processedTasks, task)) {
        processTask(processedTasks, waitingTasks, task);
      } else {
        putTaskInWaitingList(waitingTasks, task);
      }
    }

    return new ArrayList<>(processedTasks.values());
  }

  private boolean isTaskReadyForProcessing(Map<String, Task> processedTasks, Task task) {
    return noRequiredTasks(task) || requiredTasksAreProcessed(task, processedTasks);
  }

  private static boolean noRequiredTasks(Task task) {
    return task.getRequires() == null;
  }

  private boolean requiredTasksAreProcessed(Task task, Map<String, Task> processedTasks) {
    for (String requiredTask : task.getRequires()) {
      if (!processedTasks.containsKey(requiredTask)) {
        return false;
      }
    }
    return true;
  }

  private void processTask(Map<String, Task> processedTasks, List<Task> waitingTasks, Task task) {
    processedTasks.put(task.getName(), Task.builder().name(task.getName()).command(task.getCommand()).build());

    // after processing a task, check if waiting list for tasks ready to process
    processWaitingTask(processedTasks, waitingTasks);
  }

  private void processWaitingTask(Map<String, Task> processedTasks, List<Task> waitingTasks) {
    for (Task waitingTask : waitingTasks) {
      if (requiredTasksAreProcessed(waitingTask, processedTasks)) {
        prepareTaskForProcessing(waitingTasks, waitingTask);
        processTask(processedTasks, waitingTasks, waitingTask);
      }
    }
  }

  private static void prepareTaskForProcessing(List<Task> waitingTasks, Task waitingTask) {
    waitingTasks.remove(waitingTask);
  }

  private static void putTaskInWaitingList(List<Task> waitingTasks, Task task) {
    waitingTasks.add(task);
  }
}
