import java.nio.file.*;
import java.util.*;


public class Main {
    enum Algo { FCFS, PRIORITY }

    static void runOnce(Algo algo, List<Proc> base) {
        IScheduler s = (algo == Algo.FCFS) ? new FCFS() : new PriorityPreemptive();
        System.out.println(" === " + algo + " ===");
        GanttResult gr = s.run(base);
        gr.printChart();
        gr.printMetrics();
        gr.printCpuUtilization();
    }


    private static int showMenuAndGetChoice(Scanner sc) {
        System.out.println("Choose Algorithm:");
        System.out.println("[1] - First Come First Serve\n[2] - Priority Scheduling (Preemptive)");
        System.out.print("\nChoose (integer): ");
        while (true) {
            String line = sc.nextLine().trim();
            try {
                int c = Integer.parseInt(line);
                if (c == 1 || c == 2) return c;
            } catch (NumberFormatException ignored) { }
            System.out.print("Invalid choice. Enter 1 or 2: ");
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int choice = showMenuAndGetChoice(sc);
        Algo algo = (choice == 1) ? Algo.FCFS : Algo.PRIORITY;

        Path file = Path.of("src","processes.txt");
        List<Proc> procs = ProcessParser.readFromFile(file);
        runOnce(algo, procs);
    }
}