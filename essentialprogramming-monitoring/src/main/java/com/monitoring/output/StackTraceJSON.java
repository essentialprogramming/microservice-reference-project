package com.monitoring.output;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StackTraceJSON {

    private String className;
    private String methodName;
    private String fileName;
    private Integer lineNumber;
    private Boolean nativeMethod;
}
