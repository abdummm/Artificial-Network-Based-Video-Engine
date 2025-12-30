package com.example.quranfx;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;

public class CrashLog {
    private static Path logFile;

    public static void install() {
        try {
            logFile = Path.of(System.getProperty("user.home"), "Desktop", "YourApp-crash.log");
            if (Files.notExists(logFile)) Files.createFile(logFile);

            // Redirect stdout/stderr to the log (append mode)
            FileOutputStream fos = new FileOutputStream(logFile.toFile(), true);
            PrintStream ps = new PrintStream(fos, true, StandardCharsets.UTF_8);

            System.setOut(ps);
            System.setErr(ps);

            System.out.println("\n=== APP STARTED === " + LocalDateTime.now());
            System.out.println("java.version=" + System.getProperty("java.version"));
            System.out.println("java.home=" + System.getProperty("java.home"));
            System.out.println("user.dir=" + System.getProperty("user.dir"));

        } catch (Exception e) {
            // If logging can't initialize, there's nowhere reliable to report it.
        }

        // Uncaught exceptions on ANY thread that reaches the JVM uncaught handler
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            try {
                System.err.println("\n=== UNCAUGHT EXCEPTION === " + LocalDateTime.now());
                System.err.println("Thread: " + t.getName());
                e.printStackTrace(System.err);
                System.err.flush();
            } catch (Exception ignored) {}
        });
    }
}
