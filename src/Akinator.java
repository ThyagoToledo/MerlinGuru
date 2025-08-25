import java.util.*;

/**
 * Classe Akinator - Sistema de adivinhação mágica do Merlin
 * Funcionalidade adicional: Merlin tenta adivinhar em que você está pensando
 */
public class Akinator {
    private Map<String, Set<String>> baseConhecimento;
    private Map<String, Integer> pontuacaoPersonagens;
    private List<String> perguntasFeitas;
    private boolean jogoAtivo;

    public Akinator() {
        inicializarBaseConhecimento();
        resetarJogo();
    }

    private void inicializarBaseConhecimento() {
        baseConhecimento = new HashMap<>();

        // Personagens de fantasia e suas características
        baseConhecimento.put("Gandalf", new HashSet<>(Arrays.asList(
                "é_mago", "usa_cajado", "tem_barba_branca", "é_sábio", "luta_contra_mal", "tem_chapeu")));

        baseConhecimento.put("Dumbledore", new HashSet<>(Arrays.asList(
                "é_mago", "tem_barba_branca", "é_sábio", "dirige_escola", "usa_oculos", "tem_varinha")));

        baseConhecimento.put("Harry Potter", new HashSet<>(Arrays.asList(
                "é_jovem", "tem_varinha", "tem_cicatriz", "usa_oculos", "estuda_magia", "é_famoso")));

        baseConhecimento.put("Merlin", new HashSet<>(Arrays.asList(
                "é_mago", "é_lendário", "é_sábio", "tem_barba", "usa_cajado", "conhece_futuro")));

        baseConhecimento.put("Aragorn", new HashSet<>(Arrays.asList(
                "é_guerreiro", "é_rei", "usa_espada", "é_corajoso", "protege_outros", "tem_barba")));

        baseConhecimento.put("Legolas", new HashSet<>(Arrays.asList(
                "é_elfo", "usa_arco", "é_ágil", "tem_cabelo_loiro", "vive_muito", "é_preciso")));

        baseConhecimento.put("Gimli", new HashSet<>(Arrays.asList(
                "é_anão", "usa_machado", "tem_barba", "é_forte", "vive_montanhas", "é_teimoso")));

        baseConhecimento.put("Smaug", new HashSet<>(Arrays.asList(
                "é_dragão", "é_grande", "é_perigoso", "guarda_tesouro", "cospe_fogo", "é_ganancioso")));

        baseConhecimento.put("Sauron", new HashSet<>(Arrays.asList(
                "é_vilão", "é_poderoso", "tem_anel", "é_sombrio", "comanda_exército", "quer_domínio")));

        baseConhecimento.put("Gollum", new HashSet<>(Arrays.asList(
                "é_estranho", "tem_anel", "vive_cavernas", "é_obcecado", "é_magro", "fala_sozinho")));
    }

    public void resetarJogo() {
        pontuacaoPersonagens = new HashMap<>();
        perguntasFeitas = new ArrayList<>();
        jogoAtivo = true;

        // Inicializar pontuações
        for (String personagem : baseConhecimento.keySet()) {
            pontuacaoPersonagens.put(personagem, 0);
        }
    }

    public boolean isJogoAtivo() {
        return jogoAtivo;
    }

    public String proximaPergunta() {
        if (!jogoAtivo) {
            return null;
        }

        // Lista de todas as características possíveis
        List<String> todasCaracteristicas = Arrays.asList(
                "é_mago", "é_guerreiro", "é_jovem", "é_elfo", "é_anão", "é_dragão", "é_vilão",
                "usa_cajado", "usa_espada", "usa_arco", "usa_machado", "tem_varinha",
                "tem_barba", "tem_barba_branca", "tem_cabelo_loiro", "tem_cicatriz", "tem_anel",
                "usa_oculos", "tem_chapeu", "é_sábio", "é_corajoso", "é_forte", "é_ágil",
                "é_preciso", "é_teimoso", "é_grande", "é_perigoso", "é_ganancioso", "é_poderoso",
                "é_sombrio", "é_estranho", "é_magro", "é_lendário", "é_famoso", "é_obcecado",
                "é_rei", "luta_contra_mal", "dirige_escola", "estuda_magia", "conhece_futuro",
                "protege_outros", "vive_muito", "vive_montanhas", "vive_cavernas", "guarda_tesouro",
                "cospe_fogo", "comanda_exército", "quer_domínio", "fala_sozinho");

        // Encontrar a melhor pergunta (que mais divide os candidatos)
        String melhorPergunta = null;
        double melhorScore = 0;

        for (String caracteristica : todasCaracteristicas) {
            if (perguntasFeitas.contains(caracteristica)) {
                continue; // Já perguntamos isso
            }

            int candidatosCom = 0;
            int candidatosSem = 0;

            for (String personagem : baseConhecimento.keySet()) {
                if (pontuacaoPersonagens.get(personagem) >= 0) { // Ainda é candidato
                    if (baseConhecimento.get(personagem).contains(caracteristica)) {
                        candidatosCom++;
                    } else {
                        candidatosSem++;
                    }
                }
            }

            // Score baseado em quão bem divide os candidatos
            double score = Math.min(candidatosCom, candidatosSem) / (double) Math.max(candidatosCom, candidatosSem);

            if (score > melhorScore) {
                melhorScore = score;
                melhorPergunta = caracteristica;
            }
        }

        if (melhorPergunta != null) {
            perguntasFeitas.add(melhorPergunta);
            return converterCaracteristicaParaPergunta(melhorPergunta);
        }

        return null; // Não há mais perguntas
    }

    private String converterCaracteristicaParaPergunta(String caracteristica) {
        Map<String, String> perguntas = new HashMap<>();
        perguntas.put("é_mago", "Ele é um mago?");
        perguntas.put("é_guerreiro", "Ele é um guerreiro?");
        perguntas.put("é_jovem", "Ele é jovem?");
        perguntas.put("é_elfo", "Ele é um elfo?");
        perguntas.put("é_anão", "Ele é um anão?");
        perguntas.put("é_dragão", "Ele é um dragão?");
        perguntas.put("é_vilão", "Ele é um vilão?");
        perguntas.put("usa_cajado", "Ele usa cajado?");
        perguntas.put("usa_espada", "Ele usa espada?");
        perguntas.put("usa_arco", "Ele usa arco e flecha?");
        perguntas.put("usa_machado", "Ele usa machado?");
        perguntas.put("tem_varinha", "Ele tem uma varinha mágica?");
        perguntas.put("tem_barba", "Ele tem barba?");
        perguntas.put("tem_barba_branca", "Ele tem barba branca?");
        perguntas.put("tem_cabelo_loiro", "Ele tem cabelo loiro?");
        perguntas.put("tem_cicatriz", "Ele tem uma cicatriz famosa?");
        perguntas.put("tem_anel", "Ele possui um anel especial?");
        perguntas.put("usa_oculos", "Ele usa óculos?");
        perguntas.put("tem_chapeu", "Ele usa chapéu?");
        perguntas.put("é_sábio", "Ele é conhecido por sua sabedoria?");
        perguntas.put("é_corajoso", "Ele é muito corajoso?");
        perguntas.put("é_forte", "Ele é muito forte fisicamente?");
        perguntas.put("é_ágil", "Ele é muito ágil?");
        perguntas.put("é_preciso", "Ele é muito preciso em seus ataques?");
        perguntas.put("é_teimoso", "Ele é teimoso?");
        perguntas.put("é_grande", "Ele é de tamanho gigantesco?");
        perguntas.put("é_perigoso", "Ele é muito perigoso?");
        perguntas.put("é_ganancioso", "Ele é ganancioso?");
        perguntas.put("é_poderoso", "Ele possui grande poder?");
        perguntas.put("é_sombrio", "Ele tem uma natureza sombria?");
        perguntas.put("é_estranho", "Ele tem aparência estranha?");
        perguntas.put("é_magro", "Ele é muito magro?");
        perguntas.put("é_lendário", "Ele é uma figura lendária?");
        perguntas.put("é_famoso", "Ele é muito famoso?");
        perguntas.put("é_obcecado", "Ele é obcecado por algo?");
        perguntas.put("é_rei", "Ele é um rei?");
        perguntas.put("luta_contra_mal", "Ele luta contra o mal?");
        perguntas.put("dirige_escola", "Ele dirige uma escola?");
        perguntas.put("estuda_magia", "Ele estuda magia?");
        perguntas.put("conhece_futuro", "Ele pode ver o futuro?");
        perguntas.put("protege_outros", "Ele protege outras pessoas?");
        perguntas.put("vive_muito", "Ele tem uma vida muito longa?");
        perguntas.put("vive_montanhas", "Ele vive nas montanhas?");
        perguntas.put("vive_cavernas", "Ele vive em cavernas?");
        perguntas.put("guarda_tesouro", "Ele guarda um tesouro?");
        perguntas.put("cospe_fogo", "Ele cospe fogo?");
        perguntas.put("comanda_exército", "Ele comanda um exército?");
        perguntas.put("quer_domínio", "Ele quer dominar o mundo?");
        perguntas.put("fala_sozinho", "Ele fala sozinho?");

        return perguntas.getOrDefault(caracteristica, "Sobre " + caracteristica + "?");
    }

    public void processarResposta(boolean resposta) {
        if (perguntasFeitas.isEmpty()) {
            return;
        }

        String ultimaCaracteristica = perguntasFeitas.get(perguntasFeitas.size() - 1);

        for (String personagem : baseConhecimento.keySet()) {
            Set<String> caracteristicas = baseConhecimento.get(personagem);
            boolean personagemTemCaracteristica = caracteristicas.contains(ultimaCaracteristica);

            if (personagemTemCaracteristica == resposta) {
                // Resposta compatível - aumentar pontuação
                pontuacaoPersonagens.put(personagem, pontuacaoPersonagens.get(personagem) + 1);
            } else {
                // Resposta incompatível - diminuir pontuação
                pontuacaoPersonagens.put(personagem, pontuacaoPersonagens.get(personagem) - 1);
            }
        }
    }

    public String tentarAdivinhacao() {
        String melhorCandidato = null;
        int melhorPontuacao = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : pontuacaoPersonagens.entrySet()) {
            if (entry.getValue() > melhorPontuacao) {
                melhorPontuacao = entry.getValue();
                melhorCandidato = entry.getKey();
            }
        }

        if (melhorPontuacao > 0) {
            return melhorCandidato;
        }

        return "Não consegui adivinhar... Você pensou em alguém muito único!";
    }

    public void finalizarJogo() {
        jogoAtivo = false;
    }

    public int getNumeroPerguntas() {
        return perguntasFeitas.size();
    }

    public List<String> getCandidatosRestantes() {
        List<String> candidatos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : pontuacaoPersonagens.entrySet()) {
            if (entry.getValue() > 0) {
                candidatos.add(entry.getKey());
            }
        }
        return candidatos;
    }
}
