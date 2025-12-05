import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


class ProcessParser {
    static List<Proc> readFromFile(Path path) throws IOException {
        List<Proc> list = new ArrayList<>();
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;
            if (!Character.isDigit(line.charAt(0))) continue; // skip header or comments
            String[] parts = line.split("\\s+");
            if (parts.length < 4) throw new IOException("Bad line: " + line);
            int pid = Integer.parseInt(parts[0]);
            int arr = Integer.parseInt(parts[1]);
            int bur = Integer.parseInt(parts[2]);
            int pri = Integer.parseInt(parts[3]);
            list.add(new Proc(pid, arr, bur, pri));
        }
        // Verifies unique PIDs
        Set<Integer> seen = new HashSet<>();
        for (Proc p : list) if (!seen.add(p.pid)) throw new IOException("Duplicate PID: " + p.pid);
        return list;
    }
}