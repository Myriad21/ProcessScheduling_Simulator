import java.util.*;

class Proc implements Cloneable {
    final int pid;
    final int arrival;
    final int burst;
    final int priority; // lower number = higher priority

    int remaining;
    int startTime = -1; // first time it gets CPU
    int completionTime = -1;

    Proc(int pid, int arrival, int burst, int priority) {
        this.pid = pid;
        this.arrival = arrival;
        this.burst = burst;
        this.priority = priority;
        this.remaining = burst;
    }

    //Calculate Waiting time
    int waitingTime() {
        if (completionTime < 0) return -1;
        return turnaroundTime() - burst;
    }

    //Calculate TAT
    int turnaroundTime() {
        if (completionTime < 0) return -1;
        return completionTime - arrival;
    }


    // If needed copies
    @Override
    public Proc clone() {
        try {
            Proc copy = (Proc) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    static List<Proc> deepCopy(List<Proc> src) {
        List<Proc> out = new ArrayList<>();
        for (Proc p : src) out.add(p.clone());
        return out;
    }


}