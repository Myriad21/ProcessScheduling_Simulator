import java.util.*;

/** First Come, First Served Scheduler
 * First Process to arrive runs, runs till completion then next available process with
 * the earliest arrival time runs
*/

class FCFS implements IScheduler {
    @Override
    public GanttResult run(List<Proc> original) {

        // Get independent copy to avoid mutating original list
        List<Proc> procs = Proc.deepCopy(original);
        procs.sort(Comparator.comparingInt((Proc p) -> p.arrival).thenComparingInt(p -> p.pid));
        GanttResult gr = new GanttResult();
        int t = 0;
        for (Proc p : procs) {

            //If CPU is idle before p arrives add an IDLE Slice
            if (t < p.arrival) {
                gr.addSlice(-1, t, p.arrival); t = p.arrival;
            }

            p.startTime = t;
            t += p.burst;
            p.remaining = 0;
            p.completionTime = t;
            gr.addSlice(p.pid, p.startTime, t);
            gr.recordFinal(p);
        }
        return gr;
    }
}