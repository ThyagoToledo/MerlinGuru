public interface MyInterface {
    // Interface principal para o sistema do Mago Merlin
    void display();
    
    // Operações CRUD para o grimório
    void criarConsulta(String pergunta, String categoria);
    void lerConsultas();
    void atualizarConsulta(int id, String novaPergunta, String novaCategoria);
    void excluirConsulta(int id);
    
    // Funcionalidades específicas do Merlin
    void inicializarInterface();
    void processarPergunta(String pergunta, String categoria);
    void exibirResposta(String resposta, String emocao);
    void salvarNoGrimorio(String pergunta, String resposta, String categoria, String emocao);
}
