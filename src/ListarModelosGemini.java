import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ListarModelosGemini {
    private static final String API_KEY = "AIzaSyCUoLD8mDMDzaTR9iYKZIJy4JzanGicxzA";

    public static void main(String[] args) {
        try {
            System.out.println("🔍 Listando modelos disponíveis na API do Gemini...");

            String[] versoes = { "v1beta", "v1" };

            for (String versao : versoes) {
                System.out.println("\n=== Testando versão " + versao + " ===");
                String apiUrl = "https://generativelanguage.googleapis.com/" + versao + "/models?key=" + API_KEY;

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(30000);

                int responseCode = connection.getResponseCode();
                InputStream inputStream = responseCode >= 200 && responseCode < 300 ? connection.getInputStream()
                        : connection.getErrorStream();

                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                if (responseCode >= 200 && responseCode < 300) {
                    System.out.println("✅ Resposta: "
                            + response.toString().substring(0, Math.min(500, response.length())) + "...");

                    // Extrair nomes dos modelos
                    String respStr = response.toString();
                    int index = 0;
                    while ((index = respStr.indexOf("\"name\":", index)) != -1) {
                        int start = respStr.indexOf("\"", index + 7) + 1;
                        int end = respStr.indexOf("\"", start);
                        if (start > 0 && end > start) {
                            String modelName = respStr.substring(start, end);
                            if (modelName.contains("gemini") && modelName.contains("generateContent")) {
                                System.out.println("📋 Modelo: " + modelName);
                            }
                        }
                        index = end;
                    }
                } else {
                    System.out.println("❌ Erro " + responseCode + ": " + response.toString());
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro: " + e.getMessage());
        }
    }
}