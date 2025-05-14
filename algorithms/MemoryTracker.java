package algorithms;

import java.text.NumberFormat;

public class MemoryTracker {
    public static class State {
        public int lastMemoryPrintStep;
        public int memoryUsageCount;
        public State(int lastMemoryPrintStep, int memoryUsageCount) {
            this.lastMemoryPrintStep = lastMemoryPrintStep;
            this.memoryUsageCount = memoryUsageCount;
        }
    }

    public static void trackMemoryUsage(int steps, State state, long[] memoryUsages) {
        if ((steps <= 100 && steps > state.lastMemoryPrintStep) || (steps > 100 && steps / 1000 > state.lastMemoryPrintStep / 1000)) {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            // System.out.println("Memory used: " + NumberFormat.getInstance().format(usedMemory / 1024) + " KB at step " + steps);
            if (state.memoryUsageCount < memoryUsages.length) {
                memoryUsages[state.memoryUsageCount++] = usedMemory;
            }
            state.lastMemoryPrintStep = steps;
        }
    }
}