import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.google.gson.Gson;

public class ConversorMoneda {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String API_KEY = "c51aaa2bd1253c751c9ee852";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1 -> convertir("USD", "CLP");
                case 2 -> convertir("CLP", "USD");
                case 3 -> convertir("USD", "MXN");
                case 4 -> convertir("MXN", "USD");
                case 5 -> convertir("CLP", "MXN");
                case 6 -> convertir("MXN", "CLP");
                case 7 -> convertir("CLP", "EUR");
                case 8 -> convertir("EUR", "CLP");
                case 9 -> System.out.println("9) Salir");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 9);
    }

    private static void mostrarMenu() {
        System.out.println("***************************************");
        System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
        System.out.println("1) Dólar =>> Peso chileno");
        System.out.println("2) Peso chileno =>> Dólar");
        System.out.println("3) Dólar =>> Peso mexicano");
        System.out.println("4) Peso mexicano =>> Dólar");
        System.out.println("5) Peso chileno =>> Peso Mexicano");
        System.out.println("6) Peso Mexicano =>> Peso Chileno");
        System.out.println("7) Peso Chileno =>> Euro");
        System.out.println("8) Euro =>> Peso Chileno");
        System.out.println("9) Salir");
        System.out.print("Elija una opción válida: ");
    }

    private static void convertir(String from, String to) {
        System.out.print("Ingrese la cantidad a convertir: ");
        double cantidad = scanner.nextDouble();

        try {
            String urlStr = API_URL + from + "/" + to;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlStr))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            ExchangeResponse exchange = gson.fromJson(response.body(), ExchangeResponse.class);

            if ("success".equals(exchange.result)) {
                double resultado = cantidad * exchange.conversion_rate;
                System.out.printf("Resultado: %.2f %s = %.2f %s\n", cantidad, from, resultado, to);
            } else {
                System.out.println("Error: No se pudo obtener el tipo de cambio.");
            }
        } catch (Exception e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }
    }

    static class ExchangeResponse {
        String result;
        String base_code;
        String target_code;
        double conversion_rate;
    }
}

