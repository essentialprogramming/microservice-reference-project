package com.monitoring.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ThreadInfoJSON {

    private Long threadId;
    private String threadName;
    private String threadState;
    private Long blockedTime;
    private Long blockedCount;
    private Long waitedTime;
    private Long waitedCount;
    private String lockName;
    private Long lockOwnerId;
    private String lockOwnerName;
    private Boolean inNative;
    private Boolean suspended;
    private List<StackTraceJSON> stackTrace;
}
