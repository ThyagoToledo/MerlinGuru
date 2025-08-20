import java.util.*;

public class MerlinSimulator {
    private Random random;
    private Map<String, List<String>> respostasPorCategoria;
    private Map<String, String> emocoesAssociadas;
    
    public MerlinSimulator() {
        this.random = new Random();
        inicializarRespostas();
        inicializarEmocoes();
    }
    
    // Simula chamada para API Gemini
    public RespostaMerlin processarPergunta(String pergunta, String categoria) {
        try {
            // Simula delay da API
            Thread.sleep(1000 + random.nextInt(2000));
            
            String resposta = gerarResposta(pergunta, categoria);
            String emocao = determinarEmocao(pergunta, categoria);
            
            return new RespostaMerlin(resposta, emocao);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new RespostaMerlin("*Merlin está meditando... tente novamente*", "MISTERIOSO");
        }
    }
    
    private String gerarResposta(String pergunta, String categoria) {
        List<String> respostas = respostasPorCategoria.getOrDefault(categoria.toUpperCase(), 
                                 respostasPorCategoria.get("OUTROS"));
        
        String respostaBase = respostas.get(random.nextInt(respostas.size()));
        
        // Personaliza a resposta com elementos da pergunta
        if (pergunta.toLowerCase().contains("como")) {
            respostaBase = "Ah, jovem aprendiz! " + respostaBase;
        } else if (pergunta.toLowerCase().contains("por que") || pergunta.toLowerCase().contains("porque")) {
            respostaBase = "Excelente pergunta! " + respostaBase;
        } else if (pergunta.toLowerCase().contains("quando")) {
            respostaBase = "No tempo certo, " + respostaBase.toLowerCase();
        }
        
        return respostaBase;
    }
    
    private String determinarEmocao(String pergunta, String categoria) {
        // Emoção baseada no conteúdo da pergunta
        String perguntaLower = pergunta.toLowerCase();
        
        if (perguntaLower.contains("obrigado") || perguntaLower.contains("valeu")) {
            return "FELIZ";
        } else if (perguntaLower.contains("difícil") || perguntaLower.contains("complicado")) {
            return "SABIO";
        } else if (perguntaLower.contains("burro") || perguntaLower.contains("idiota")) {
            return "IRRITADO";
        } else if (categoria.equals("HISTORIA") || perguntaLower.contains("antigo")) {
            return "MISTERIOSO";
        } else {
            // Emoção aleatória baseada na categoria
            return emocoesAssociadas.getOrDefault(categoria.toUpperCase(), "NEUTRO");
        }
    }
    
    private void inicializarRespostas() {
        respostasPorCategoria = new HashMap<>();
        
        // Respostas para FEITICOS
        respostasPorCategoria.put("FEITICOS", Arrays.asList(
            "Esse feitiço requer muita concentração e uma pitada de pó de estrelas!",
            "Cuidado! Esse tipo de magia pode ter efeitos colaterais inesperados...",
            "Ah, um clássico! Pratique os movimentos de varinha primeiro.",
            "Esse encantamento é poderoso. Use com sabedoria, jovem mago!",
            "Para dominar essa magia, você deve primeiro compreender os elementos."
        ));
        
        // Respostas para CRIATURAS
        respostasPorCategoria.put("CRIATURAS", Arrays.asList(
            "Essa criatura é fascinante! São conhecidas por sua inteligência aguçada.",
            "Tenha muito cuidado ao se aproximar. São territoriais por natureza.",
            "Interessante escolha! Essas criaturas são raras de se encontrar.",
            "Ah, sim! Já enfrentei uma dessas em minhas aventuras. São formidáveis!",
            "Criaturas assim requerem respeito e uma abordagem cautelosa."
        ));
        
        // Respostas para BATALHA
        respostasPorCategoria.put("BATALHA", Arrays.asList(
            "Em combate, a estratégia é mais importante que a força bruta!",
            "Lembre-se: conhecer seu oponente é metade da vitória.",
            "A paciência é uma virtude essencial em batalhas mágicas.",
            "Nunca subestime o poder da defesa! Um bom escudo vale por mil ataques.",
            "O verdadeiro guerreiro luta apenas quando necessário."
        ));
        
        // Respostas para HISTORIA
        respostasPorCategoria.put("HISTORIA", Arrays.asList(
            "Ah, os tempos antigos... quando a magia fluía mais livremente pelo mundo.",
            "Essa história remonta aos primórdios da magia. Deixe-me contar...",
            "Nos pergaminhos antigos está escrito sobre esse evento histórico.",
            "Era uma época de grandes magos e terríveis ameaças...",
            "A história se repete, jovem. O que foi, voltará a ser."
        ));
        
        // Respostas para OUTROS
        respostasPorCategoria.put("OUTROS", Arrays.asList(
            "Hmm, interessante pergunta. Deixe-me consultar meus grimórios...",
            "Essa é uma questão que requer reflexão profunda.",
            "Cada caminho tem suas próprias recompensas e desafios.",
            "A sabedoria vem com o tempo e a experiência, jovem aprendiz.",
            "Algumas perguntas têm múltiplas respostas. Qual você prefere ouvir?"
        ));
    }
    
    private void inicializarEmocoes() {
        emocoesAssociadas = new HashMap<>();
        emocoesAssociadas.put("FEITICOS", "SABIO");
        emocoesAssociadas.put("CRIATURAS", "MISTERIOSO");
        emocoesAssociadas.put("BATALHA", "NEUTRO");
        emocoesAssociadas.put("HISTORIA", "MISTERIOSO");
        emocoesAssociadas.put("OUTROS", "FELIZ");
    }
    
    // Classe interna para encapsular resposta
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
}
