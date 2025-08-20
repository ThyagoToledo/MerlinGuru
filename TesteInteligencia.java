/**
 * Classe de teste para verificar a integração com a API Google Gemini
 */
public class TesteInteligencia {
    public static void main(String[] args) {
        System.out.println("🧪 TESTE DA CLASSE INTELIGENCIA");
        System.out.println("===============================\n");
        
        Inteligencia ia = new Inteligencia();
        
        // Teste 1: Pergunta sobre feitiços
        System.out.println("🔮 Teste 1 - Categoria FEITICOS:");
        testarPergunta(ia, "Como fazer uma poção de cura?", "FEITICOS");
        
        // Teste 2: Pergunta sobre criaturas
        System.out.println("\n🐉 Teste 2 - Categoria CRIATURAS:");
        testarPergunta(ia, "O que são dragões vermelhos?", "CRIATURAS");
        
        // Teste 3: Pergunta sobre batalha
        System.out.println("\n⚔️ Teste 3 - Categoria BATALHA:");
        testarPergunta(ia, "Como vencer um orc?", "BATALHA");
        
        System.out.println("\n✅ Testes concluídos!");
    }
    
    private static void testarPergunta(Inteligencia ia, String pergunta, String categoria) {
        System.out.println("Pergunta: " + pergunta);
        System.out.println("Categoria: " + categoria);
        System.out.println("Processando...");
        
        try {
            Inteligencia.RespostaMerlin resposta = ia.processarPergunta(pergunta, categoria);
            System.out.println("✅ Resposta: " + resposta.getResposta());
            System.out.println("😊 Emoção: " + resposta.getEmocao());
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }
}
