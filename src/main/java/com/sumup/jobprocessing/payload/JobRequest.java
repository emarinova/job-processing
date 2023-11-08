package com.sumup.jobprocessing.payload;

import com.sumup.jobprocessing.payload.task.Task;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class JobRequest {
  private final List<Task> tasks;
}
