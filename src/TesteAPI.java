public class TesteAPI {
    public static void main(String[] args) {
        System.out.println("🔍 Testando nova API Key com Gemini 2.5 Flash...");

        Inteligencia inteligencia = new Inteligencia();
        Inteligencia.RespostaMerlin resposta = inteligencia.processarPergunta(
                "O que é um dragão?", "CRIATURAS");

        System.out.println("✅ Resposta: " + resposta.getResposta());
        System.out.println("😊 Emoção: " + resposta.getEmocao());
    }
}