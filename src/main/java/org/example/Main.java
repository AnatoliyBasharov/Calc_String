import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите операцию со строками: ");
        String expression = scanner.nextLine();

        if (expression.isEmpty() || expression.charAt(0) != '\"') {
            System.out.println("Ошибка: Первым аргументом должна быть строка");
            return;
        }

        String insideQuotes = extractStringInsideQuotes(expression);

        if (insideQuotes.length() > 10) {
            System.out.println("Ошибка: Строка в кавычках должна содержать не более 10 символов");
            return;
        }

        try {
            Calculator calculator = new Calculator();
            String result = calculator.calculate(expression);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String extractStringInsideQuotes(String expression) {
        if (expression.startsWith("\"")) {
            int endIndex = expression.indexOf("\"", 1);

            if (endIndex != -1) {
                return expression.substring(1, endIndex);
            }
        }

        return "";
    }


}

class Calculator {
    public String calculate(String expression) throws Exception {
        char action;
        String[] data;

        if (expression.contains(" + ")) {
            data = expression.split(" \\+ ");
            action = '+';
        } else if (expression.contains(" - ")) {
            data = expression.split(" - ");
            action = '-';
        } else if (expression.contains(" * ")) {
            data = expression.split(" \\* ");
            action = '*';
        } else if (expression.contains(" / ")) {
            data = expression.split(" / ");
            action = '/';
        } else {
            throw new Exception("Некорректный знак действия");
        }

        validateData(action, data);

        for (int i = 0; i < data.length; i++) {
            data[i] = data[i].replace("\"", "");
        }

        String result = performAction(action, data);
        return wrapInQuotes(result);
    }

    private void validateData(char action, String[] data) throws Exception {
        if (action == '*' || action == '/') {
            if (data[1].contains("\"")) {
                throw new Exception("Строчку можно делить или умножать только на число");
            }

            int number = Integer.parseInt(data[1]);
            if (number < 1 || number > 10) {
                throw new Exception("Число должно быть от 1 до 10 включительно");
            }
        }
    }

    private String performAction(char action, String[] data) {
        switch (action) {
            case '+':
                return data[0] + data[1];
            case '*':
                int multiplier = Integer.parseInt(data[1]);
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < multiplier; i++) {
                    result.append(data[0]);
                }
                return result.toString();
            case '-':
                int index = data[0].indexOf(data[1]);
                if (index == -1) {
                    return data[0];
                } else {
                    return data[0].substring(0, index) + data[0].substring(index + data[1].length());
                }
            case '/':
                int newLen = data[0].length() / Integer.parseInt(data[1]);
                return data[0].substring(0, newLen);
            default:
                throw new IllegalArgumentException("Неподдерживаемая операция");
        }
    }

    private String wrapInQuotes(String text) {
        return "\"" + (text.length() > 40 ? text.substring(0, 40) + "..." : text) + "\"";
    }
}
