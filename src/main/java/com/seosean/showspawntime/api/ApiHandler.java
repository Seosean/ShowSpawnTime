package com.seosean.showspawntime.api;

import java.util.HashMap;
import java.util.Map;

public class ApiHandler {
    private static final ApiHandler INSTANCE = new ApiHandler();
    private Map<Class<?>, Object> apiInstances = new HashMap<>();

    private ApiHandler() {
    }

    public static ApiHandler getInstance() {
        return INSTANCE;
    }

    public void registerApiInstance(Class<?> apiClass, Object apiInstance) {
        apiInstances.put(apiClass, apiInstance);
    }

    public <T> T getApiInstance(Class<T> apiClass) {
        Object apiInstance = apiInstances.get(apiClass);
        if (apiInstance != null && apiClass.isInstance(apiInstance)) {
            return apiClass.cast(apiInstance);
        }
        return null;
    }
}