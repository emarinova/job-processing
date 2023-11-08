package com.sumup.jobprocessing.service;

import com.sumup.jobprocessing.payload.task.Task;

import java.util.List;

public interface JobProcessingService {
  List<Task> process(List<Task> request);
}
