class GanttEntry {
    final int pid; // -1 is default
    final int start;
    final int end;

    GanttEntry(int pid, int start, int end) {
        this.pid = pid;
        this.start = start;
        this.end = end;
    }
}
