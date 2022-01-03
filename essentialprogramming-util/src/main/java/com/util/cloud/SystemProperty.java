package com.util.cloud;

import java.util.HashMap;
import java.util.Map;


public class SystemProperty {

    private final String key;
    private final DeploymentStrategy deploymentStrategy;

    private final Map<DeploymentStrategy, MyInterface> strategiesMap = new HashMap<>();

    public SystemProperty(final String key, final DeploymentStrategy strategy) {
        init();
        this.key = key;
        this.deploymentStrategy = strategy;
    }

    public String getKey() {
        return key;
    }


    @SuppressWarnings("unchecked")
    public <T> T getValue(T defaultValue) {
        T value = (T) strategiesMap.get(deploymentStrategy).doSomething(1, 2);
        return value != null ? value : defaultValue;
    }

    public MyInterface myInterface = (a) -> {
        int sum = 0;
        for (int i : a)
            sum += i;
        return sum;
    };

    private void init() {
        strategiesMap.put(DeploymentStrategy.LOCAL, (a) -> {
            int sum = 0;
            for (int i : a)
                sum += i;
            return sum;
        });
    }

    interface Strategy {
    }

    public interface MyInterface extends Strategy {
        Integer doSomething(Integer... param);
    }
}
