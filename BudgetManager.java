import java.util.Scanner;
import java.text.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BudgetManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DecimalFormat df = new DecimalFormat("0.00");

        try (PrintWriter writer = new PrintWriter(new FileWriter("expenses_history.txt", true))) {
            writer.println("Budget Manager Report");

            double initialBudget = getInitialBudget(scanner);
            double remainingBudget = initialBudget;
            int day = 1;

            while (true) {
                System.out.println("\nDay " + day);
                System.out.println("Remaining budget: " + df.format(remainingBudget) + " euro");
                System.out.println("Enter a category or type 'quit' to finish the day: ");
                String category = scanner.next();

                if (category.equalsIgnoreCase("quit")) {
                    break;
                }

                double dailyExpenses = enterExpensesForCategory(scanner, category, remainingBudget);

                remainingBudget -= dailyExpenses;

                printExpensesSummary(category, dailyExpenses, remainingBudget);
                writer.println(day + "\t" + category + "\t" + df.format(dailyExpenses) + "\t" + df.format(remainingBudget));

                System.out.println("Type 'next' to go to the next day or any other key to continue with the current day: ");
                String userInput = scanner.next();
                if (userInput.equalsIgnoreCase("next")) {
                    day++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static double getInitialBudget(Scanner scanner) {
        System.out.println("Please enter your budget (in euro): ");
        return scanner.nextDouble();
    }

    private static double enterExpensesForCategory(Scanner scanner, String category, double remainingBudget) {
        DecimalFormat df = new DecimalFormat("0.00");
        double dailyExpenses = 0;

        while (true) {
            System.out.println("Enter expenses for " + category + " or type 'next' to finish: ");
            String expenseInput = scanner.next();

            if (expenseInput.equalsIgnoreCase("next")) {
                break;
            }

            double expenses = Double.parseDouble(expenseInput);

            if (expenses < 0.0) {
                System.out.println("Invalid expense amount. Please enter a valid positive amount.");
                continue;
            }

            if (expenses > remainingBudget) {
                System.out.println("Expenses exceed remaining budget. Please enter a valid amount.");
                continue;
            }

            dailyExpenses += expenses;
        }

        return dailyExpenses;
    }

    private static void printExpensesSummary(String category, double dailyExpenses, double remainingBudget) {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Total expenses in " + category + ": " + df.format(dailyExpenses) + " euro.");
        System.out.println("Remaining budget: " + df.format(remainingBudget) + " euro.");
    }
}
