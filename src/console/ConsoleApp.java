package console;

import core.DataProcessor;

public class ConsoleApp {
    public static void main(String[] args) {
        // Проверка аргументов командной строки
        if (args.length < 1) {
            System.err.println("Использование: java ConsoleApp <имя_файла_данных> [имя_файла_результата]");
            System.exit(1);
        }

        String inputFile = args[0];
        String outputFile = args.length > 1 ? args[1] : null; // Необязательный аргумент

        DataProcessor dp = new DataProcessor(inputFile);

        // Обработка данных
        String result = dp.processPipeline();

        // Вывод в консоль
        System.out.println("Результат обработки:");
        System.out.println(result);

        // Сохранение в файл, если указан
        if (outputFile != null) {
            dp.saveResult(result, outputFile);
            System.out.println("Результат сохранён в файл: " + outputFile);
        }
    }
}