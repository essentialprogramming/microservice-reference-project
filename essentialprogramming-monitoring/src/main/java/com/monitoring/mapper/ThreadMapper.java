package com.monitoring.mapper;

import com.monitoring.output.StackTraceJSON;
import com.monitoring.output.ThreadInfoJSON;

import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadMapper {

    public static ThreadInfoJSON threadInfoToJSON(ThreadInfo threadInfo) {

        return ThreadInfoJSON.builder()
                .threadId(threadInfo.getThreadId())
                .threadName(threadInfo.getThreadName())
                .threadState(String.valueOf(threadInfo.getThreadState()))
                .blockedTime(threadInfo.getBlockedTime())
                .blockedCount(threadInfo.getBlockedCount())
                .waitedTime(threadInfo.getWaitedTime())
                .waitedCount(threadInfo.getWaitedCount())
                .lockName(threadInfo.getLockName())
                .lockOwnerId(threadInfo.getLockOwnerId())
                .lockOwnerName(threadInfo.getLockOwnerName())
                .inNative(threadInfo.isInNative())
                .suspended(threadInfo.isSuspended())
                .stackTrace(stackTraceElementsToJSON(threadInfo.getStackTrace()))
                .build();
    }

    private static List<StackTraceJSON> stackTraceElementsToJSON(StackTraceElement[] stackTraceElements) {
        return Arrays.stream(stackTraceElements).map(ThreadMapper::stackTraceElementToJSON)
                .collect(Collectors.toList());
    }

    private static StackTraceJSON stackTraceElementToJSON(StackTraceElement stackTraceElement) {
        return StackTraceJSON.builder()
                .className(stackTraceElement.getClassName())
                .methodName(stackTraceElement.getMethodName())
                .fileName(stackTraceElement.getFileName())
                .lineNumber(stackTraceElement.getLineNumber())
                .nativeMethod(stackTraceElement.isNativeMethod())
                .build();
    }
}
