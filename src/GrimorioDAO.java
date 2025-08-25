import java.io.*;
import java.util.*;

public class GrimorioDAO {
    private static final String ARQUIVO_GRIMORIO = "grimorio_merlin.txt";
    private List<RegistroMagico> registros;
    
    public GrimorioDAO() {
        this.registros = new ArrayList<>();
        carregarRegistros();
    }
    
    // CREATE - Adicionar novo registro
    public boolean adicionarRegistro(RegistroMagico registro) {
        try {
            // Define ID automaticamente
            registro.setId(obterProximoId());
            registros.add(registro);
            salvarNoArquivo();
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao adicionar registro: " + e.getMessage());
            return false;
        }
    }
    
    // READ - Obter todos os registros
    public List<RegistroMagico> obterTodosRegistros() {
        return new ArrayList<>(registros);
    }
    
    // READ - Obter registro por ID
    public RegistroMagico obterRegistroPorId(int id) {
        return registros.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    // READ - Obter registros por categoria
    public List<RegistroMagico> obterRegistrosPorCategoria(String categoria) {
        return registros.stream()
                .filter(r -> r.getCategoria().equalsIgnoreCase(categoria))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    // UPDATE - Atualizar registro existente
    public boolean atualizarRegistro(int id, String novaPergunta, String novaCategoria) {
        try {
            RegistroMagico registro = obterRegistroPorId(id);
            if (registro != null) {
                registro.setPergunta(novaPergunta);
                registro.setCategoria(novaCategoria);
                salvarNoArquivo();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar registro: " + e.getMessage());
            return false;
        }
    }
    
    // UPDATE - Atualizar resposta e emoção do Merlin
    public boolean atualizarResposta(int id, String resposta, String emocao) {
        try {
            RegistroMagico registro = obterRegistroPorId(id);
            if (registro != null) {
                registro.setResposta(resposta);
                registro.setEmocaoMerlin(emocao);
                salvarNoArquivo();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao atualizar resposta: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Excluir registro
    public boolean excluirRegistro(int id) {
        try {
            boolean removido = registros.removeIf(r -> r.getId() == id);
            if (removido) {
                salvarNoArquivo();
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Erro ao excluir registro: " + e.getMessage());
            return false;
        }
    }
    
    // Salvar todos os registros no arquivo TXT
    private void salvarNoArquivo() throws IOException {
        try (FileWriter writer = new FileWriter(ARQUIVO_GRIMORIO)) {
            writer.write("# GRIMÓRIO DIGITAL DO MAGO MERLIN\n");
            writer.write("# Formato: ID|PERGUNTA|RESPOSTA|CATEGORIA|EMOÇÃO|DATA\n");
            writer.write("# Categorias: FEITICOS, CRIATURAS, BATALHA, HISTORIA, OUTROS\n");
            writer.write("# Emoções: FELIZ, SABIO, IRRITADO, MISTERIOSO, NEUTRO\n\n");
            
            for (RegistroMagico registro : registros) {
                writer.write(registro.toFileString() + "\n");
            }
        }
    }
    
    // Carregar registros do arquivo TXT
    private void carregarRegistros() {
        File arquivo = new File(ARQUIVO_GRIMORIO);
        if (!arquivo.exists()) {
            return; // Arquivo ainda não existe, lista vazia
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Ignorar comentários e linhas vazias
                if (linha.trim().isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                
                RegistroMagico registro = RegistroMagico.fromFileString(linha);
                if (registro != null) {
                    registros.add(registro);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar registros: " + e.getMessage());
        }
    }
    
    // Obter próximo ID disponível
    private int obterProximoId() {
        return registros.stream()
                .mapToInt(RegistroMagico::getId)
                .max()
                .orElse(0) + 1;
    }
    
    // Validar entrada
    public boolean validarEntrada(String pergunta, String categoria) {
        if (pergunta == null || pergunta.trim().isEmpty()) {
            return false;
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            return false;
        }
        
        // Validar categorias permitidas
        String[] categoriasValidas = {"FEITICOS", "CRIATURAS", "BATALHA", "HISTORIA", "OUTROS"};
        return Arrays.asList(categoriasValidas).contains(categoria.toUpperCase());
    }
    
    // Obter estatísticas do grimório
    public String obterEstatisticas() {
        int total = registros.size();
        Map<String, Long> porCategoria = new HashMap<>();
        Map<String, Long> porEmocao = new HashMap<>();
        
        for (RegistroMagico registro : registros) {
            porCategoria.merge(registro.getCategoria(), 1L, Long::sum);
            porEmocao.merge(registro.getEmocaoMerlin(), 1L, Long::sum);
        }
        
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTATÍSTICAS DO GRIMÓRIO ===\n");
        stats.append("Total de consultas: ").append(total).append("\n\n");
        stats.append("Por categoria:\n");
        porCategoria.forEach((cat, count) -> 
            stats.append("- ").append(cat).append(": ").append(count).append("\n"));
        stats.append("\nPor emoção do Merlin:\n");
        porEmocao.forEach((emo, count) -> 
            stats.append("- ").append(emo).append(": ").append(count).append("\n"));
        
        return stats.toString();
    }
}
