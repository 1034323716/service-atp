package org.mahatma.atp.common.engine;


import java.util.HashMap;
import java.util.Map;

public class StackManager {

    private AutoTestEngine engine;

    private Map<Class<?>, StackSummary> stackMapping = new HashMap<>();

    protected StackManager(AutoTestEngine engine) {
        this.engine = engine;
    }

    public void init() {
        engine.getModuleManager().getStackMapping().values().forEach((StackSummary summary) -> {
            summary.build();
            Class<?> clazz = summary.getClazz();
            stackMapping.put(clazz, summary);
        });
    }

    public <T> T getStack(Class<T> tClass) {
        StackSummary stackSummary = stackMapping.get(tClass);
        if (stackSummary != null) {
            stackSummary.start();
            return (T) stackSummary.getStack();
        } else {
            return null;
        }
    }

}
