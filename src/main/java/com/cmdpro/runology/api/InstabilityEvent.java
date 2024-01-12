package com.cmdpro.runology.api;

public class InstabilityEvent {
    public InstabilityEventRunnable run;
    public float minInstability;
    public InstabilityEvent(InstabilityEventRunnable run, float minInstability) {
        this.run = run;
        this.minInstability = minInstability;
    }
}
