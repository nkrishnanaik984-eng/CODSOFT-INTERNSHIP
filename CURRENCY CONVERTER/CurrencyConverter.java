import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===============================");
        System.out.println("Welcome to Currency Converter!");
        System.out.println("===============================");

        // 1. Currency Selection
        System.out.print("Enter the base currency (e.g., USD, EUR, INR): ");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the target currency (e.g., USD, EUR, INR): ");
        String targetCurrency = scanner.nextLine().trim().toUpperCase();

        // 3. Amount Input
        System.out.print("Enter the amount you want to convert: ");
        double amount = 0;
        try {
            amount = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please run the program again and enter a valid number.");
            System.exit(0);
        }

        System.out.println("\nFetching real-time exchange rates...");

        // 2. Fetch Currency Rates
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate != -1) {
            // 4. Currency Conversion
            double convertedAmount = amount * exchangeRate;
            
            String symbol = targetCurrency + " ";
            try {
                Currency currency = Currency.getInstance(targetCurrency);
                symbol = currency.getSymbol();
            } catch (IllegalArgumentException e) {
                // If the target currency's symbol isn't found in Java's Locale, we simply append the currency code
            }

            // 5. Display Result
            System.out.println("\n----------------------------------------");
            System.out.printf("Exchange Rate: 1 %s = %.4f %s%n", baseCurrency, exchangeRate, targetCurrency);
            System.out.printf("Converted Amount: %s%.2f%n", symbol, convertedAmount);
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Conversion failed. Please check the currency codes and your internet connection.");
        }

        scanner.close();
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            // Using a reliable public exchange rate API
            String urlString = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();

                // Simple JSON parsing using Regex to find the rate for the target currency (without extra libraries)
                String regex = "\"" + targetCurrency + "\"\\s*:\\s*([0-9.]+)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(jsonResponse);

                if (matcher.find()) {
                    return Double.parseDouble(matcher.group(1));
                } else {
                    System.out.println("Error: Target currency '" + targetCurrency + "' not found in the API response.");
                    return -1;
                }
            } else if (responseCode == 404) {
                System.out.println("Error: Base currency '" + baseCurrency + "' not found.");
                return -1;
            } else {
                System.out.println("Error: Unable to fetch data from API. HTTP Response Code: " + responseCode);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }
}
