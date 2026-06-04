import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiagnosticoAPI {
    private static final String API_KEY = "AIzaSyB3Q1h7ltCU-kuGg4dtkuzpCJnvHxWHO_w";

    public static void main(String[] args) {
        System.out.println("🔍 Diagnóstico completo da API do Gemini...\n");

        // Teste 1: Verificar se a API key está válida
        testarChaveAPI();

        // Teste 2: Listar modelos disponíveis
        listarModelosDisponiveis();

        // Teste 3: Testar diferentes endpoints
        testarEndpoints();

        // Teste 4: Testar requisição simples
        testarRequisicaoSimples();
    }

    private static void testarChaveAPI() {
        System.out.println("=== TESTE 1: Validação da API Key ===");
        System.out.println("API Key: " + API_KEY.substring(0, 10) + "...");
        System.out.println("Fonte: AI Studio Google (aistudio.google.com)");
        System.out.println();
    }

    private static void listarModelosDisponiveis() {
        System.out.println("=== TESTE 2: Modelos Disponíveis ===");

        String[] versoes = { "v1beta", "v1" };

        for (String versao : versoes) {
            try {
                System.out.println("Testando versão: " + versao);
                String url = "https://generativelanguage.googleapis.com/" + versao + "/models?key=" + API_KEY;

                String resposta = fazerRequisicaoGET(url);

                if (resposta != null) {
                    System.out.println("✅ Sucesso na versão " + versao);

                    // Extrair modelos gemini
                    if (resposta.contains("gemini")) {
                        System.out.println("📋 Modelos Gemini encontrados:");
                        extrairModelosGemini(resposta);
                    }
                } else {
                    System.out.println("❌ Erro na versão " + versao);
                }

            } catch (Exception e) {
                System.out.println("❌ Erro na versão " + versao + ": " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void testarEndpoints() {
        System.out.println("=== TESTE 3: Diferentes Endpoints ===");

        String[] modelos = {
                "gemini-2.5-flash",
                "gemini-2.5-pro",
                "gemini-1.5-flash",
                "gemini-1.5-pro",
                "gemini-pro"
        };

        String[] versoes = { "v1", "v1beta" };

        for (String versao : versoes) {
            for (String modelo : modelos) {
                String url = "https://generativelanguage.googleapis.com/" + versao + "/models/" + modelo
                        + ":generateContent?key=" + API_KEY;

                try {
                    String resposta = testarEndpoint(url);
                    if (resposta != null && !resposta.contains("NOT_FOUND")) {
                        System.out.println("✅ " + versao + "/" + modelo + " - Disponível");
                    } else {
                        System.out.println("❌ " + versao + "/" + modelo + " - Indisponível");
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ " + versao + "/" + modelo + " - Erro: " + e.getMessage());
                }
            }
        }
        System.out.println();
    }

    private static void testarRequisicaoSimples() {
        System.out.println("=== TESTE 4: Requisição de Teste ===");

        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key="
                + API_KEY;

        String jsonRequest = "{\n" +
                "  \"contents\": [{\n" +
                "    \"parts\": [{\n" +
                "      \"text\": \"Diga apenas 'Olá' em português.\"\n" +
                "    }]\n" +
                "  }],\n" +
                "  \"generationConfig\": {\n" +
                "    \"temperature\": 0.1,\n" +
                "    \"maxOutputTokens\": 50\n" +
                "  }\n" +
                "}";

        try {
            String resposta = fazerRequisicaoPOST(url, jsonRequest);

            if (resposta != null) {
                System.out.println("✅ Requisição bem-sucedida!");
                System.out.println("📋 Resposta da API:");
                System.out.println(resposta.substring(0, Math.min(500, resposta.length())));

                if (resposta.contains("\"text\"")) {
                    System.out.println("🎉 API está funcionando corretamente!");
                }
            } else {
                System.out.println("❌ Falha na requisição");
            }

        } catch (Exception e) {
            System.out.println("❌ Erro na requisição de teste: " + e.getMessage());
        }
    }

    private static String fazerRequisicaoGET(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            int responseCode = conn.getResponseCode();

            InputStream inputStream = responseCode >= 200 && responseCode < 300 ? conn.getInputStream()
                    : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                System.out.println("❌ Código de erro: " + responseCode);
                System.out.println("📋 Erro: " + response.toString());
                return null;
            }

        } catch (Exception e) {
            System.out.println("❌ Exceção: " + e.getMessage());
            return null;
        }
    }

    private static String fazerRequisicaoPOST(String urlStr, String jsonRequest) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(30000);

            // Enviar dados
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            InputStream inputStream = responseCode >= 200 && responseCode < 300 ? conn.getInputStream()
                    : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("📊 Código de resposta: " + responseCode);

            if (responseCode >= 200 && responseCode < 300) {
                return response.toString();
            } else {
                System.out.println("❌ Erro na API: " + response.toString());
                return response.toString(); // Retornar mesmo com erro para análise
            }

        } catch (Exception e) {
            System.out.println("❌ Exceção na requisição: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static String testarEndpoint(String urlStr) {
        try {
            // Fazer uma requisição HEAD para verificar se o endpoint existe
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Requisição mínima
            String minJson = "{\"contents\":[{\"parts\":[{\"text\":\"test\"}]}]}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(minJson.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 404) {
                return "NOT_FOUND";
            } else if (responseCode >= 200 && responseCode < 500) {
                return "AVAILABLE";
            }

            return "UNKNOWN";

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    private static void extrairModelosGemini(String resposta) {
        try {
            // Busca simples por nomes de modelos gemini
            String[] linhas = resposta.split(",");
            for (String linha : linhas) {
                if (linha.contains("\"name\"") && linha.contains("gemini")) {
                    int start = linha.indexOf("models/") + 7;
                    int end = linha.indexOf("\"", start);
                    if (start > 7 && end > start) {
                        String modelo = linha.substring(start, end);
                        System.out.println("  - " + modelo);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("  (Erro ao extrair modelos)");
        }
    }
}