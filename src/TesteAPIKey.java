import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TesteAPIKey {
    private static final String API_KEY = "AIzaSyB3Q1h7ltCU-kuGg4dtkuzpCJnvHxWHO_w";

    public static void main(String[] args) {
        System.out.println("🔍 Testando chave de API do Gemini...\n");

        // Teste 1: Verificar se a chave tem formato válido
        verificarFormatoChave();

        // Teste 2: Testar endpoint de modelos (sem consumir quota)
        testarEndpointModelos();

        // Teste 3: Fazer uma requisição mínima
        testarRequisicaoMinima();

        System.out.println("\n✅ Diagnóstico concluído!");
    }

    private static void verificarFormatoChave() {
        System.out.println("=== TESTE 1: Formato da Chave ===");
        System.out.println("Chave: " + API_KEY.substring(0, 15) + "...");
        System.out.println("Tamanho: " + API_KEY.length() + " caracteres");
        System.out.println("Prefixo: " + (API_KEY.startsWith("AIza") ? "✅ Válido" : "❌ Inválido"));
        System.out.println("Origem: Google AI Studio");
        System.out.println();
    }

    private static void testarEndpointModelos() {
        System.out.println("=== TESTE 2: Endpoint de Modelos ===");

        String url = "https://generativelanguage.googleapis.com/v1/models?key=" + API_KEY;

        try {
            HttpURLConnection conn = criarConexao(url, "GET");

            int responseCode = conn.getResponseCode();
            System.out.println("Código de resposta: " + responseCode);

            String resposta = lerResposta(conn, responseCode);

            if (responseCode == 200) {
                System.out.println("✅ Chave de API válida!");
                System.out.println("📋 Resposta (primeiros 200 chars):");
                System.out.println(resposta.substring(0, Math.min(200, resposta.length())) + "...");
            } else if (responseCode == 403) {
                System.out.println("❌ Chave de API inválida ou sem permissões");
                System.out.println("📋 Erro: " + resposta);
            } else if (responseCode == 429) {
                System.out.println("⚠️ Muitas requisições - quota excedida");
                System.out.println("📋 Erro: " + resposta);
            } else {
                System.out.println("⚠️ Código inesperado: " + responseCode);
                System.out.println("📋 Resposta: " + resposta);
            }

        } catch (Exception e) {
            System.out.println("❌ Erro na requisição: " + e.getMessage());
        }

        System.out.println();
    }

    private static void testarRequisicaoMinima() {
        System.out.println("=== TESTE 3: Requisição Mínima ===");

        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key="
                + API_KEY;

        // JSON mínimo possível
        String jsonMinimo = "{\n" +
                "  \"contents\": [{\n" +
                "    \"parts\": [{\n" +
                "      \"text\": \"Diga 'OK'\"\n" +
                "    }]\n" +
                "  }],\n" +
                "  \"generationConfig\": {\n" +
                "    \"maxOutputTokens\": 10\n" +
                "  }\n" +
                "}";

        try {
            HttpURLConnection conn = criarConexao(url, "POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Enviar requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonMinimo.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            String resposta = lerResposta(conn, responseCode);

            System.out.println("Código de resposta: " + responseCode);

            if (responseCode == 200) {
                System.out.println("🎉 API funcionando perfeitamente!");

                // Extrair texto da resposta
                if (resposta.contains("\"text\"")) {
                    String texto = extrairTexto(resposta);
                    if (texto != null) {
                        System.out.println("📝 Resposta do Gemini: \"" + texto + "\"");
                    }
                }
            } else if (responseCode == 400) {
                System.out.println("❌ Requisição malformada");
                System.out.println("📋 Detalhes: " + resposta.substring(0, Math.min(300, resposta.length())));
            } else if (responseCode == 403) {
                System.out.println("❌ Acesso negado - verifique permissões da chave");
                System.out.println("📋 Detalhes: " + resposta.substring(0, Math.min(300, resposta.length())));
            } else if (responseCode == 429) {
                System.out.println("⚠️ Quota excedida");
                System.out.println("📋 Detalhes: " + resposta.substring(0, Math.min(300, resposta.length())));
            } else {
                System.out.println("⚠️ Código inesperado: " + responseCode);
                System.out.println("📋 Resposta: " + resposta.substring(0, Math.min(300, resposta.length())));
            }

        } catch (Exception e) {
            System.out.println("❌ Erro na requisição: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static HttpURLConnection criarConexao(String urlStr, String metodo) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(metodo);
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(30000);

        // Headers importantes para a API do Gemini
        conn.setRequestProperty("User-Agent", "MerlinGuru/1.0");

        return conn;
    }

    private static String lerResposta(HttpURLConnection conn, int responseCode) throws Exception {
        InputStream inputStream = responseCode >= 200 && responseCode < 300 ? conn.getInputStream()
                : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private static String extrairTexto(String jsonResponse) {
        try {
            int textIndex = jsonResponse.indexOf("\"text\":");
            if (textIndex == -1)
                return null;

            int startQuote = jsonResponse.indexOf("\"", textIndex + 7) + 1;
            int endQuote = jsonResponse.indexOf("\"", startQuote);

            if (startQuote > 0 && endQuote > startQuote) {
                return jsonResponse.substring(startQuote, endQuote)
                        .replace("\\n", "\n")
                        .replace("\\\"", "\"");
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}