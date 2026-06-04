public class TesteGeminiAPI {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Testando conectividade com a API do Gemini...");

            Inteligencia inteligencia = new Inteligencia();
            Inteligencia.RespostaMerlin resposta = inteligencia.processarPergunta(
                    "O que é magia?", "FEITICOS");

            System.out.println("✅ API funcionando!");
            System.out.println("Resposta: " + resposta.getResposta());
            System.out.println("Emoção: " + resposta.getEmocao());

        } catch (Exception e) {
            System.err.println("❌ Erro ao testar API: " + e.getMessage());
            e.printStackTrace();
        }
    }
}