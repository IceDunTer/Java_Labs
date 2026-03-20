package core;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<Integer> readNumbersFromFile(String filename) throws IOException {
        List<Integer> numbers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        try {
                            numbers.add(Integer.parseInt(part));
                        } catch (NumberFormatException e) {
                            System.err.println("Пропущено нечисловое значение: '" + part + "'");
                        }
                    }
                }
            }
        }
        return numbers;
    }

    public static void writeNumbersToFile(List<Integer> numbers, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < numbers.size(); i++) {
                writer.write(numbers.get(i).toString());
                if (i < numbers.size() - 1) {
                    writer.write(" ");
                }
            }
            writer.newLine();
        }
    }

    public static void writeLinesToFile(List<String> lines, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}