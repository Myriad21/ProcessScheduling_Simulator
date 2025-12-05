import java.util.*;

class GanttResult {
    final List<GanttEntry> timeline = new ArrayList<>();
    final Map<Integer, Proc> byPid = new LinkedHashMap<>(); // final states after run

    void addSlice(int pid, int start, int end) {
        if (!timeline.isEmpty()) {
            GanttEntry last = timeline.get(timeline.size()-1);
            if (last.pid == pid && last.end == start) { // merge adjacent same-PID slices
                timeline.set(timeline.size()-1, new GanttEntry(pid, last.start, end));
                return;
            }
        }
        timeline.add(new GanttEntry(pid, start, end));
    }

    void recordFinal(Proc p) {
        byPid.put(p.pid, p);
    }

    void printChart() {
        final int SCALE = 3; // chars per time unit (tune if you want wider/narrower boxes)
        StringBuilder boxes = new StringBuilder();
        List<int[]> tickPos = new ArrayList<>();

        boxes.setLength(0);
        for (GanttEntry e : timeline) {
            String label = (e.pid < 0) ? "IDLE" : ("P" + e.pid);
            int dur = Math.max(1, e.end - e.start);
            int innerWidth = Math.max(label.length() + 2, dur * SCALE); // keep label legible

            boxes.append('|');
            int startCol = boxes.length();
            boxes.append(center(label, innerWidth));
            int endCol = boxes.length();

            // record tick columns for start and end
            if (tickPos.isEmpty() || tickPos.get(tickPos.size()-1)[1] != e.start) {
                tickPos.add(new int[]{startCol, e.start});
            }
            tickPos.add(new int[]{endCol, e.end});
        }
        boxes.append('|');

        char[] ticks = new char[boxes.length()];
        Arrays.fill(ticks, ' ');
        for (int i = 0; i < tickPos.size(); i++) {
            int[] tp = tickPos.get(i);
            int col = Math.min(tp[0], ticks.length - 1);
            String s = String.valueOf(tp[1]);
            int start = (i == 0) ? 0 : Math.max(0, Math.min(col - s.length() / 2, ticks.length - s.length()));
            for (int j = 0; j < s.length(); j++) ticks[start + j] = s.charAt(j);
        }
        System.out.println(boxes);
        System.out.println(new String(ticks));
    }

    // Simple helpers for the  GanttResult
    private static String center(String s, int width) {
        if (s.length() >= width) return s;
        int pad = width - s.length();
        int left = pad / 3, right = pad / 3;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    void printMetrics() {
        double sumWT = 0, sumTAT = 0;
        List<Integer> sorted = new ArrayList<>(byPid.keySet());
        Collections.sort(sorted);
        System.out.println("\nPer-process metrics:");
        System.out.println("PID WT TAT");
        for (int pid : sorted) {
            Proc p = byPid.get(pid);
            System.out.printf("%3d %2d %3d%n", pid, p.waitingTime(), p.turnaroundTime());
            sumWT += p.waitingTime();
            sumTAT += p.turnaroundTime();
        }
        int n = byPid.size();
        System.out.printf("Avg WT = %.2f, Avg TAT = %.2f%n", sumWT/n, sumTAT/n);
    }


    void printCpuUtilization() {
        if (timeline.isEmpty()) return;
        int start = timeline.get(0).start;
        int end = timeline.get(timeline.size()-1).end;
        int busy = 0;
        for (GanttEntry e : timeline) if (e.pid >= 0) busy += (e.end - e.start);
        double util = 100.0 * busy / Math.max(1, (end - start));
        System.out.printf("CPU Utilization = %.2f%%%n", util);
    }
}