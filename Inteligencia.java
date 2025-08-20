import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Inteligencia {
    private static final String API_KEY = "AIzaSyCUoLD8mDMDzaTR9iYKZIJy4JzanGicxzA";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;
    
    public static class RespostaMerlin {
        private String resposta;
        private String emocao;
        
        public RespostaMerlin(String resposta, String emocao) {
            this.resposta = resposta;
            this.emocao = emocao;
        }
        
        public String getResposta() { return resposta; }
        public String getEmocao() { return emocao; }
    }
    
    public RespostaMerlin processarPergunta(String pergunta, String categoria) {
        try {
            // Criar prompt contextualizado para o Mago Merlin
            String prompt = criarPromptMerlin(pergunta, categoria);
            
            // Fazer requisição para a API
            String respostaAPI = chamarGeminiAPI(prompt);
            
            // Processar resposta
            return processarResposta(respostaAPI, pergunta, categoria);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar pergunta: " + e.getMessage());
            // Fallback para resposta offline
            return gerarRespostaOffline(pergunta, categoria);
        }
    }
    
    private String criarPromptMerlin(String pergunta, String categoria) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Você é o Mago Merlin, um sábio e poderoso feiticeiro com conhecimento ancestral sobre magia e fantasia. ");
        prompt.append("Responda como Merlin responderia, com sabedoria, mistério e personalidade única. ");
        prompt.append("Use linguagem elaborada mas compreensível. ");
        
        // Contexto específico por categoria
        switch (categoria.toUpperCase()) {
            case "FEITICOS":
                prompt.append("A pergunta é sobre FEITIÇOS: Fale sobre magias, encantamentos e rituais com conhecimento prático e detalhes místicos. ");
                break;
            case "CRIATURAS":
                prompt.append("A pergunta é sobre CRIATURAS: Descreva criaturas mágicas e fantásticas com detalhes vividos, suas características e comportamentos. ");
                break;
            case "BATALHA":
                prompt.append("A pergunta é sobre BATALHA: Dê conselhos de estratégia e combate mágico com sabedoria militar e tática. ");
                break;
            case "HISTORIA":
                prompt.append("A pergunta é sobre HISTÓRIA: Conte histórias antigas, lendas e eventos místicos com tom nostálgico e misterioso. ");
                break;
            default:
                prompt.append("A pergunta é GERAL: Responda with a sabedoria de um mago experiente sobre qualquer tópico. ");
        }
        
        prompt.append("Pergunta: ").append(pergunta).append("\\n\\n");
        prompt.append("INSTRUÇÕES IMPORTANTES: ");
        prompt.append("1. Responda de forma elaborada mas não muito longa (máximo 3 frases). ");
        prompt.append("2. Termine sua resposta com EXATAMENTE uma das seguintes emoções entre colchetes: [FELIZ], [SABIO], [IRRITADO], [MISTERIOSO] ou [NEUTRO]. ");
        prompt.append("3. Escolha a emoção baseada no tom da sua resposta e contexto da pergunta. ");
        prompt.append("4. Use linguagem mística apropriada para um mago.");
        
        return prompt.toString();
    }
    
    private String chamarGeminiAPI(String prompt) throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Configurar conexão
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);
        connection.setConnectTimeout(10000); // 10 segundos
        connection.setReadTimeout(30000);    // 30 segundos
        
        // Criar JSON da requisição manualmente
        String jsonRequest = criarJsonRequest(prompt);
        
        // Enviar requisição
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonRequest.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Ler resposta
        int responseCode = connection.getResponseCode();
        InputStream inputStream = responseCode >= 200 && responseCode < 300 ? 
                                connection.getInputStream() : connection.getErrorStream();
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        if (responseCode >= 200 && responseCode < 300) {
            return response.toString();
        } else {
            throw new Exception("Erro na API: " + responseCode + " - " + response.toString());
        }
    }
    
    private String criarJsonRequest(String prompt) {
        // Escape do texto para JSON
        String promptEscapado = prompt.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"contents\": [");
        json.append("{");
        json.append("\"parts\": [");
        json.append("{");
        json.append("\"text\": \"").append(promptEscapado).append("\"");
        json.append("}");
        json.append("]");
        json.append("}");
        json.append("],");
        json.append("\"generationConfig\": {");
        json.append("\"temperature\": 0.8,");
        json.append("\"topP\": 0.9,");
        json.append("\"maxOutputTokens\": 512");
        json.append("}");
        json.append("}");
        
        return json.toString();
    }
    
    private RespostaMerlin processarResposta(String respostaJSON, String pergunta, String categoria) {
        try {
            // Parser JSON simples para extrair o texto da resposta
            String textoResposta = extrairTextoResposta(respostaJSON);
            
            if (textoResposta != null && !textoResposta.trim().isEmpty()) {
                // Extrair emoção e limpar resposta
                String emocao = extrairEmocao(textoResposta);
                String resposta = limparResposta(textoResposta);
                
                return new RespostaMerlin(resposta, emocao);
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao processar resposta JSON: " + e.getMessage());
        }
        
        // Fallback para resposta offline
        return gerarRespostaOffline(pergunta, categoria);
    }
    
    private String extrairTextoResposta(String jsonResponse) {
        try {
            // Buscar o padrão "text":"conteúdo" na resposta
            int textIndex = jsonResponse.indexOf("\"text\":");
            if (textIndex == -1) return null;
            
            int startQuote = jsonResponse.indexOf("\"", textIndex + 7);
            if (startQuote == -1) return null;
            
            int endQuote = startQuote + 1;
            int escapeCount = 0;
            
            // Encontrar o final da string considerando escape de caracteres
            while (endQuote < jsonResponse.length()) {
                char c = jsonResponse.charAt(endQuote);
                if (c == '\\') {
                    escapeCount++;
                } else if (c == '"' && escapeCount % 2 == 0) {
                    break;
                } else {
                    escapeCount = 0;
                }
                endQuote++;
            }
            
            if (endQuote < jsonResponse.length()) {
                String texto = jsonResponse.substring(startQuote + 1, endQuote);
                // Desfazer escape de caracteres básicos
                return texto.replace("\\\"", "\"")
                          .replace("\\n", "\n")
                          .replace("\\r", "")
                          .replace("\\t", " ");
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao extrair texto da resposta: " + e.getMessage());
        }
        
        return null;
    }
    
    private String extrairEmocao(String texto) {
        String[] emocoesPossiveis = {"FELIZ", "SABIO", "IRRITADO", "MISTERIOSO", "NEUTRO"};
        
        // Buscar emoção explícita entre colchetes
        for (String emocao : emocoesPossiveis) {
            if (texto.toUpperCase().contains("[" + emocao + "]")) {
                return emocao;
            }
        }
        
        // Determinar emoção por análise de conteúdo
        String textoUpper = texto.toUpperCase();
        
        if (textoUpper.contains("EXCELENTE") || textoUpper.contains("MARAVILHOSO") || 
            textoUpper.contains("PERFEITO") || textoUpper.contains("ALEGRIA") ||
            textoUpper.contains("BEM-VINDO")) {
            return "FELIZ";
        } else if (textoUpper.contains("ANTIGO") || textoUpper.contains("SEGREDO") || 
                   textoUpper.contains("MISTÉRIO") || textoUpper.contains("OCULTO") ||
                   textoUpper.contains("LENDA")) {
            return "MISTERIOSO";
        } else if (textoUpper.contains("SABEDORIA") || textoUpper.contains("CONHECIMENTO") || 
                   textoUpper.contains("ESTUDE") || textoUpper.contains("APRENDA") ||
                   textoUpper.contains("CUIDADO")) {
            return "SABIO";
        } else if (textoUpper.contains("NÃO POSSO") || textoUpper.contains("IMPOSSÍVEL") ||
                   textoUpper.contains("ABSURDO") || textoUpper.contains("IRRITANTE")) {
            return "IRRITADO";
        }
        
        return "NEUTRO";
    }
    
    private String limparResposta(String texto) {
        // Remover as tags de emoção da resposta
        String resposta = texto.replaceAll("\\[(?:FELIZ|SABIO|IRRITADO|MISTERIOSO|NEUTRO)\\]", "").trim();
        
        // Remover quebras de linha excessivas
        resposta = resposta.replaceAll("\n+", " ");
        
        // Garantir que começa com maiúscula e termina com pontuação
        if (!resposta.isEmpty()) {
            resposta = Character.toUpperCase(resposta.charAt(0)) + resposta.substring(1);
            if (!resposta.endsWith(".") && !resposta.endsWith("!") && !resposta.endsWith("?")) {
                resposta += ".";
            }
        }
        
        return resposta;
    }
    
    // Fallback para quando a API não está disponível
    private RespostaMerlin gerarRespostaOffline(String pergunta, String categoria) {
        String resposta = "";
        String emocao = "MISTERIOSO";
        
        switch (categoria.toUpperCase()) {
            case "FEITICOS":
                resposta = "As artes arcanas que você questiona requerem profundo estudo e concentração, jovem aprendiz.";
                emocao = "SABIO";
                break;
            case "CRIATURAS":
                resposta = "Ah, as criaturas místicas... cada uma possui segredos que apenas os mais sábios podem compreender.";
                emocao = "MISTERIOSO";
                break;
            case "BATALHA":
                resposta = "Em combate, lembre-se: a mente afiada vale mais que a lâmina mais cortante.";
                emocao = "NEUTRO";
                break;
            case "HISTORIA":
                resposta = "Nos tempos antigos, quando a magia fluía livremente... ah, tantas histórias para contar.";
                emocao = "MISTERIOSO";
                break;
            default:
                resposta = "Sua questão ecoa pelos corredores do tempo, jovem buscador de conhecimento.";
                emocao = "SABIO";
        }
        
        return new RespostaMerlin(resposta, emocao);
    }
}
