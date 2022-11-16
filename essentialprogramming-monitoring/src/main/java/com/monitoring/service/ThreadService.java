package com.monitoring.service;

import com.monitoring.mapper.ThreadMapper;
import com.monitoring.output.ThreadInfoJSON;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadService {

    public List<ThreadInfoJSON> createThreadDump() {

        ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(false, false);

        return Arrays.stream(threads)
                .map(ThreadMapper::threadInfoToJSON)
                .collect(Collectors.toList());
    }
}
