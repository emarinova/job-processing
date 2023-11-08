package com.sumup.jobprocessing.payload;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sumup.jobprocessing.payload.task.Task;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class JobResponse {
  @JsonValue
  private final List<Task> tasks;
}
