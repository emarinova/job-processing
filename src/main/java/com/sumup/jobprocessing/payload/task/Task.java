package com.sumup.jobprocessing.payload.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Task {
  private String name;
  private String command;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<String> requires;
}
