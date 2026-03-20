package core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    private String inputFile;

    public DataProcessor(String inputFile) {
        this.inputFile = inputFile;
    }

    // Основной пайплайн обработки: чтение чисел -> обработка -> строка
    public String processPipeline() {
        try {
            List<Integer> numbers = FileUtils.readNumbersFromFile(inputFile);
            List<Integer> processed = processData(numbers);
            return convertResultToString(processed);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return "";
        }
    }

    // Перегруженная версия для прямой передачи строк (например, из аргументов)
    public String processPipeline(String[] stringNumbers) {
        List<Integer> numbers = convertStringsToIntegers(stringNumbers);
        List<Integer> processed = processData(numbers);
        return convertResultToString(processed);
    }

    // Основная логика обработки данных: убираем последовательные дубликаты
    private List<Integer> processData(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        if (numbers.isEmpty()) return result;

        result.add(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (!numbers.get(i).equals(numbers.get(i - 1))) {
                result.add(numbers.get(i));
            }
        }
        return result;
    }

    // Преобразует массив строк в список чисел с обработкой ошибок
    private List<Integer> convertStringsToIntegers(String[] stringNumbers) {
        List<Integer> numbers = new ArrayList<>();
        for (String str : stringNumbers) {
            if (str == null || str.trim().isEmpty()) continue;
            try {
                numbers.add(Integer.parseInt(str.trim()));
            } catch (NumberFormatException e) {
                System.err.println("Не удалось преобразовать в число: '" + str + "' — пропущено.");
            }
        }
        return numbers;
    }

    // Преобразует результат (список чисел) в строку через пробел
    private String convertResultToString(List<Integer> result) {
        if (result.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Integer num : result) {
            sb.append(num).append(" ");
        }
        return sb.toString().trim();
    }

    // Сохранение результата в файл
    public void saveResult(String result, String filename) {
        try {
            List<String> lines = new ArrayList<>();
            lines.add(result);
            FileUtils.writeLinesToFile(lines, filename);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл " + filename + ": " + e.getMessage());
        }
    }

    // Устаревший метод — оставлен для совместимости
    @Deprecated
    public static List<Integer> processVariant3() {
        return processVariant3("input.txt", "output.txt");
    }

    @Deprecated
    public static List<Integer> processVariant3(String inputFilename, String outputFilename) {
        try {
            List<Integer> numbers = FileUtils.readNumbersFromFile(inputFilename);
            List<Integer> result = processDataStatic(numbers);
            FileUtils.writeNumbersToFile(result, outputFilename);
            return result;
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Статический метод для удаления последовательных дубликатов (для совместимости)
    @Deprecated
    private static List<Integer> processDataStatic(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        if (numbers.isEmpty()) return result;

        result.add(numbers.get(0));
        for (int i = 1; i < numbers.size(); i++) {
            if (!numbers.get(i).equals(numbers.get(i - 1))) {
                result.add(numbers.get(i));
            }
        }
        return result;
    }

    // Пустой метод — возможно, будет реализован позже
    void saveResults(List<String> results) {
        // TODO: Реализовать при необходимости
    }
}