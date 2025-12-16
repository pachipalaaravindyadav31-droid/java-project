import java.io.*;
import java.util.*;

public class CodeMetrics {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==== CODE METRICS TOOL ====");
        System.out.print("Enter Java file or folder path: ");
        String path = sc.nextLine();

        File f = new File(path);

        if (!f.exists()) {
            System.out.println("Error: Path does not exist!");
            return;
        }

        List<File> javaFiles = new ArrayList<>();

        if (f.isFile() && f.getName().endsWith(".java")) {
            javaFiles.add(f);
        } else if (f.isDirectory()) {
            javaFiles = getJavaFiles(f);
        } else {
            System.out.println("Invalid file or folder.");
            return;
        }

        System.out.println("\nFound " + javaFiles.size() + " Java file(s).\n");

        for (File file : javaFiles) {
            analyzeFile(file);
        }

        System.out.println("==== DONE ====");
    }

    // Collect all .java files inside directory
    public static List<File> getJavaFiles(File folder) {
        List<File> list = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files == null) return list;

        for (File f : files) {
            if (f.isDirectory()) {
                list.addAll(getJavaFiles(f));
            } else if (f.getName().endsWith(".java")) {
                list.add(f);
            }
        }
        return list;
    }

    public static void analyzeFile(File file) {
        int total = 0, code = 0, comment = 0, blank = 0;
        int complexity = 1;

        List<String> keys = Arrays.asList("if", "for", "while", "case", "catch", "&&", "||", "?");

        boolean blockComment = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                total++;
                String t = line.trim();

                if (t.isEmpty()) {
                    blank++;
                    continue;
                }

                // Check block comments
                if (t.startsWith("/*")) blockComment = true;

                if (blockComment) {
                    comment++;
                    if (t.endsWith("*/")) blockComment = false;
                    continue;
                }

                // Single line comment
                if (t.startsWith("//")) {
                    comment++;
                    continue;
                }

                // Code line
                code++;

                // Complexity
                for (String k : keys) {
                    if (t.contains(k)) complexity++;
                }
            }

            System.out.println("File: " + file.getName());
            System.out.println("Total Lines: " + total);
            System.out.println("Code Lines: " + code);
            System.out.println("Comment Lines: " + comment);
            System.out.println("Blank Lines: " + blank);
            System.out.println("Cyclomatic Complexity: " + complexity);
            System.out.println("--------------------------------------");

        } catch (Exception e) {
            System.out.println("Error reading: " + file.getName());
        }
    }
}