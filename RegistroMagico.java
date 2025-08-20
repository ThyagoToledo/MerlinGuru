import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroMagico {
    private int id;
    private String pergunta;
    private String resposta;
    private String categoria;
    private String emocaoMerlin;
    private LocalDateTime dataConsulta;
    
    // Construtor completo
    public RegistroMagico(int id, String pergunta, String resposta, String categoria, String emocaoMerlin) {
        this.id = id;
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.categoria = categoria;
        this.emocaoMerlin = emocaoMerlin;
        this.dataConsulta = LocalDateTime.now();
    }
    
    // Construtor para nova consulta (sem resposta ainda)
    public RegistroMagico(int id, String pergunta, String categoria) {
        this.id = id;
        this.pergunta = pergunta;
        this.categoria = categoria;
        this.resposta = "";
        this.emocaoMerlin = "NEUTRO";
        this.dataConsulta = LocalDateTime.now();
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPergunta() { return pergunta; }
    public void setPergunta(String pergunta) { this.pergunta = pergunta; }
    
    public String getResposta() { return resposta; }
    public void setResposta(String resposta) { this.resposta = resposta; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getEmocaoMerlin() { return emocaoMerlin; }
    public void setEmocaoMerlin(String emocaoMerlin) { this.emocaoMerlin = emocaoMerlin; }
    
    public LocalDateTime getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDateTime dataConsulta) { this.dataConsulta = dataConsulta; }
    
    // Método para converter para string do arquivo TXT
    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return id + "|" + pergunta + "|" + resposta + "|" + categoria + "|" + 
               emocaoMerlin + "|" + dataConsulta.format(formatter);
    }
    
    // Método para criar objeto a partir de linha do arquivo
    public static RegistroMagico fromFileString(String linha) {
        String[] partes = linha.split("\\|");
        if (partes.length != 6) return null;
        
        RegistroMagico registro = new RegistroMagico(
            Integer.parseInt(partes[0]), // id
            partes[1], // pergunta
            partes[2], // resposta
            partes[3], // categoria
            partes[4]  // emocao
        );
        
        // Parse da data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        registro.setDataConsulta(LocalDateTime.parse(partes[5], formatter));
        
        return registro;
    }
    
    @Override
    public String toString() {
        return "Consulta #" + id + " - " + categoria + ": " + pergunta;
    }
}
