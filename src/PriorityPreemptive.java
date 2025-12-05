import  java.util.*;

/** Preemptive Priority Scheduler
 * Highest priority runs first, but if while a process is running a higher priority process comes in
 * the running process is interrupted and sent back to the queue while the scheduler chooses the highest priority
 * process from the queue (Which would be the one that interrupted)
 * */

class PriorityPreemptive implements IScheduler {
    @Override
    public GanttResult run(List<Proc> original) {
        // Lower priority value = higher priority.
        List<Proc> all = Proc.deepCopy(original);
        all.sort(Comparator.comparingInt((Proc p) -> p.arrival).thenComparingInt(p -> p.pid));
        GanttResult gr = new GanttResult();
        int n = all.size();
        int completed = 0;
        int t = 0;
        int i = 0; // arrival pointer
        PriorityQueue<Proc> rq = new PriorityQueue<>(
                Comparator.comparingInt((Proc p) -> p.priority)
                        .thenComparingInt(p -> p.arrival)
                        .thenComparingInt(p -> p.pid)
        );
        while (completed < n) {
            // Adds arrivals at time t
            while (i < n && all.get(i).arrival <= t) rq.add(all.get(i++));
            if (rq.isEmpty()) {
                // Jumps to next arrival if idle
                int nextArr = (i < n ? all.get(i).arrival : t+1);
                gr.addSlice(-1, t, nextArr);
                t = nextArr;
                continue;
            }
            Proc cur = rq.poll();
            if (cur.startTime < 0) cur.startTime = t;
            // Runs for 1 time unit, then re-evaluates to check for a higher priority
            gr.addSlice(cur.pid, t, t+1);
            cur.remaining -= 1;
            t += 1;

            // Add any arrivals that occur at new time t before deciding preemption
            while (i < n && all.get(i).arrival <= t) rq.add(all.get(i++));
            if (cur.remaining == 0) {
                cur.completionTime = t;
                gr.recordFinal(cur);
                completed++;
            } else {
            // Preemption happens naturally here; PQ will pick highest priority next loop
                rq.add(cur);
            }
        }
        return gr;
    }
}