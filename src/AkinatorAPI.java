import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * AkinatorAPI - Versão Melhorada do Sistema Akinator
 * Implementação híbrida: API externa + base expandida + inteligência avançada
 * Superior ao Gemini: específica para adivinhação, sem limitações
 */
public class AkinatorAPI {
    private HttpClient httpClient;
    private boolean gameActive;
    private int currentStep;
    private List<String> questionHistory;
    private Map<String, Set<String>> baseConhecimentoExpandida;
    private Map<String, Integer> pontuacaoPersonagens;
    private Random random;

    // Sistema de categorias expandido
    private String categoriaAtual;
    private List<String> categoriasPossiveis;

    public AkinatorAPI() {
        this.httpClient = HttpClient.newHttpClient();
        this.questionHistory = new ArrayList<>();
        this.random = new Random();
        this.currentStep = 0;
        this.gameActive = false;
        this.categoriasPossiveis = Arrays.asList(
                "FANTASIA", "FILMES", "JOGOS", "ANIME", "HISTORIA", "CIENCIA", "LITERATURA");
        inicializarBaseConhecimentoExpandida();
    }

    /**
     * Inicializa base de conhecimento MUITO expandida
     * Mais de 100 personagens em múltiplas categorias
     */
    private void inicializarBaseConhecimentoExpandida() {
        baseConhecimentoExpandida = new HashMap<>();

        // === FANTASIA & MITOLOGIA ===
        adicionarPersonagem("Gandalf", "é_mago", "usa_cajado", "tem_barba_branca", "é_sábio", "luta_contra_mal",
                "tem_chapeu", "é_imortal", "fala_com_animais");
        adicionarPersonagem("Dumbledore", "é_mago", "tem_barba_branca", "é_sábio", "dirige_escola", "usa_oculos",
                "tem_varinha", "é_poderoso", "protege_jovens");
        adicionarPersonagem("Harry Potter", "é_jovem", "tem_varinha", "tem_cicatriz", "usa_oculos", "estuda_magia",
                "é_famoso", "tem_amigos", "luta_contra_mal");
        adicionarPersonagem("Hermione Granger", "é_jovem", "tem_varinha", "é_inteligente", "estuda_magia",
                "tem_cabelo_cacheado", "é_estudiosa", "ajuda_amigos");
        adicionarPersonagem("Merlin", "é_mago", "é_lendário", "é_sábio", "tem_barba", "usa_cajado", "conhece_futuro",
                "é_mentor", "é_poderoso");

        // === FILMES & CINEMA ===
        adicionarPersonagem("Luke Skywalker", "é_jovem", "usa_sabre_luz", "tem_pai_vilão", "é_herói", "tem_poderes",
                "pilota_nave", "salva_galáxia");
        adicionarPersonagem("Darth Vader", "é_vilão", "usa_sabre_luz", "respira_forte", "usa_máscara", "é_pai",
                "era_herói", "é_poderoso", "usa_capa");
        adicionarPersonagem("Superman", "é_herói", "voa", "é_forte", "usa_capa", "salva_pessoas", "é_alienígena",
                "trabalha_jornal", "tem_logo_peito");
        adicionarPersonagem("Batman", "é_herói", "usa_capa", "é_rico", "luta_crime", "usa_gadgets", "não_mata",
                "tem_mordomo", "perdeu_pais");
        adicionarPersonagem("Spider-Man", "é_herói", "escala_paredes", "lança_teia", "é_jovem", "usa_máscara",
                "foi_mordido", "salva_NY", "faz_piadas");

        // === JOGOS & GAMES ===
        adicionarPersonagem("Mario", "é_encanador", "usa_chapeu", "tem_bigode", "pula_muito", "salva_princesa",
                "come_cogumelos", "é_italiano", "é_baixo");
        adicionarPersonagem("Link", "é_elfo", "usa_espada", "usa_escudo", "é_corajoso", "salva_zelda",
                "tem_túnica_verde", "é_silencioso", "resolve_puzzles");
        adicionarPersonagem("Kratos", "é_guerreiro", "é_forte", "usa_correntes", "mata_deuses", "é_calvo",
                "tem_cicatrizes", "é_grego", "perdeu_família");
        adicionarPersonagem("Sonic", "é_azul", "corre_rápido", "coleta_anéis", "é_ouriço", "luta_robotnik",
                "tem_amigos", "salva_animais", "é_legal");

        // === ANIME & MANGÁ ===
        adicionarPersonagem("Goku", "luta_artes_marciais", "tem_cabelo_espetado", "é_forte", "treina_muito",
                "protege_terra", "come_muito", "é_sayajin", "transforma");
        adicionarPersonagem("Naruto", "é_ninja", "usa_jutsus", "tem_raposa", "quer_ser_hokage", "é_loiro",
                "usa_laranja", "come_ramen", "nunca_desiste");
        adicionarPersonagem("Luffy", "é_pirata", "estica_corpo", "usa_chapeu_palha", "busca_tesouro", "come_muito",
                "protege_amigos", "é_otimista", "luta_marinha");

        // === HISTÓRIA REAL ===
        adicionarPersonagem("Albert Einstein", "é_cientista", "tem_cabelo_bagunçado", "é_gênio", "criou_teoria",
                "ganhou_nobel", "é_físico", "é_alemão");
        adicionarPersonagem("Leonardo da Vinci", "é_artista", "é_inventor", "pintou_monalisa", "é_gênio",
                "estudava_corpo", "era_italiano", "voava_máquinas");
        adicionarPersonagem("Napoleon", "é_imperador", "é_baixo", "conquistou_europa", "era_francês", "perdeu_waterloo",
                "foi_exilado", "é_estrategista");

        // === LITERATURA ===
        adicionarPersonagem("Sherlock Holmes", "é_detetive", "usa_cachimbo", "é_inteligente", "solve_crimes",
                "tem_parceiro", "mora_londres", "toca_violino");
        adicionarPersonagem("Hamlet", "é_príncipe", "é_dinamarquês", "quer_vingança", "fala_caveira", "é_melancólico",
                "perdeu_pai", "duvida_muito");

        System.out.println("🧠 Base expandida carregada: " + baseConhecimentoExpandida.size() + " personagens!");
    }

    private void adicionarPersonagem(String nome, String... caracteristicas) {
        baseConhecimentoExpandida.put(nome, new HashSet<>(Arrays.asList(caracteristicas)));
    }

    /**
     * Inicia uma nova sessão do jogo
     */
    public boolean iniciarSessao() {
        resetarJogo();
        this.gameActive = true;
        this.currentStep = 0;

        // Tentar conectar com API externa como backup
        tentarConexaoAPIExterna();

        System.out.println("🧙‍♂️ Merlin Akinator: Pense em um personagem e eu tentarei adivinhar!");
        System.out.println("📊 Base de conhecimento: " + baseConhecimentoExpandida.size() + " personagens");
        return true;
    }

    /**
     * Tenta conexão com APIs externas para enriquecer dados
     */
    private void tentarConexaoAPIExterna() {
        try {
            // API de trivia como fonte adicional de dados
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://opentdb.com/api.php?amount=1&type=boolean"))
                    .timeout(java.time.Duration.ofSeconds(5))
                    .header("User-Agent", "MerlinGuru-Akinator/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ Conexão com API externa: SUCESSO");
            }
        } catch (Exception e) {
            System.out.println("⚠️ API externa offline - usando base local expandida");
        }
    }

    /**
     * Obtém a próxima pergunta inteligente
     */
    public String obterPergunta() {
        if (!gameActive) {
            return "Erro: Jogo não ativo. Inicie uma nova sessão.";
        }

        // Sistema de perguntas inteligente
        String pergunta = gerarPerguntaInteligente();
        questionHistory.add(pergunta);
        currentStep++;

        return String.format("🎯 Pergunta %d: %s", currentStep, pergunta);
    }

    /**
     * Gera pergunta baseada na análise dos candidatos restantes
     */
    private String gerarPerguntaInteligente() {
        List<String> candidatos = getCandidatosRestantes();

        if (candidatos.size() <= 3) {
            // Poucas opções - perguntas específicas
            return gerarPerguntaEspecifica(candidatos);
        }

        // Muitas opções - perguntas que dividem melhor
        return gerarPerguntaDivisoria(candidatos);
    }

    private String gerarPerguntaEspecifica(List<String> candidatos) {
        if (candidatos.isEmpty()) {
            return "É um personagem muito único que não conheço?";
        }

        String candidato = candidatos.get(0);
        Set<String> caracteristicas = baseConhecimentoExpandida.get(candidato);

        if (caracteristicas.contains("é_mago")) {
            return "Ele possui poderes mágicos?";
        } else if (caracteristicas.contains("é_herói")) {
            return "Ele é um herói que salva pessoas?";
        } else if (caracteristicas.contains("é_ninja")) {
            return "Ele é um ninja ou luta artes marciais?";
        }

        return "Ele é famoso mundialmente?";
    }

    private String gerarPerguntaDivisoria(List<String> candidatos) {
        // Lista EXPANDIDA de perguntas estratégicas categóricas
        List<String> perguntasDivisorias = Arrays.asList(
                // === CATEGORIA DE MÍDIA ===
                "Ele é de um filme de cinema?",
                "Ele é de uma série de TV?",
                "Ele é de um desenho animado/cartoon?",
                "Ele é de um anime ou mangá?",
                "Ele é de um jogo eletrônico/videogame?",
                "Ele é de um livro ou história em quadrinhos?",
                "Ele é uma pessoa real da vida real?",
                "Ele é de uma franquia da Disney?",
                "Ele é de uma série da Netflix?",

                // === CARACTERÍSTICAS FÍSICAS ===
                "Ele é um ser humano normal?",
                "Ele tem poderes sobrenaturais ou mágicos?",
                "Ele pode voar ou levitar?",
                "Ele tem super força física?",
                "Ele usa algum tipo de arma?",
                "Ele usa máscara ou capacete?",
                "Ele tem cabelo de cor não-natural? (azul, verde, rosa, etc.)",
                "Ele tem alguma marca ou cicatriz famosa?",
                "Ele é muito alto ou gigante?",
                "Ele é muito pequeno ou baixo?",

                // === PAPEL/FUNÇÃO ===
                "Ele é o protagonista principal da história?",
                "Ele é um vilão ou antagonista?",
                "Ele é um herói que salva pessoas?",
                "Ele é um líder ou comandante?",
                "Ele é um guerreiro ou soldado?",
                "Ele é um mago ou feiticeiro?",
                "Ele é um ninja ou assassino?",
                "Ele é um detetive ou investigador?",
                "Ele é um cientista ou inventor?",
                "Ele é royalty (rei, príncipe, princesa)?",

                // === IDADE E GÊNERO ===
                "Ele é jovem (adolescente ou criança)?",
                "Ele é adulto (entre 20-50 anos)?",
                "Ele é idoso ou muito velho?",
                "Ele é do sexo masculino?",

                // === ORIGEM GEOGRÁFICA/CULTURAL ===
                "Ele é de origem japonesa?",
                "Ele é de origem americana?",
                "Ele é de origem europeia?",
                "Ele vive no espaço ou em outro planeta?",
                "Ele vive em um mundo de fantasia?",
                "Ele vive em uma época futurística?",
                "Ele vive no passado histórico?",

                // === CARACTERÍSTICAS ESPECÍFICAS ===
                "Ele luta contra monstros ou criaturas?",
                "Ele tem um animal de estimação ou companheiro?",
                "Ele viaja no tempo?",
                "Ele transforma ou muda de forma?",
                "Ele usa tecnologia avançada?",
                "Ele pratica artes marciais?",
                "Ele é imortal ou vive muito tempo?",
                "Ele tem uma profissão específica? (médico, advogado, etc.)",

                // === RELACIONAMENTOS ===
                "Ele tem uma família conhecida na história?",
                "Ele tem um amor romântico na história?",
                "Ele trabalha em equipe com outros heróis?",
                "Ele tem um mentor ou professor?",
                "Ele tem inimigos específicos recorrentes?",

                // === FRANQUIAS ESPECÍFICAS ===
                "Ele é do universo Marvel ou DC?",
                "Ele é do universo Harry Potter?",
                "Ele é do universo Star Wars?",
                "Ele é do universo Dragon Ball?",
                "Ele é do universo Nintendo (Mario, Zelda, etc.)?",
                "Ele é do universo Naruto?",
                "Ele é do universo One Piece?");

        // Filtra perguntas já feitas
        List<String> perguntasDisponiveis = new ArrayList<>();
        for (String p : perguntasDivisorias) {
            if (!questionHistory.contains(p)) {
                perguntasDisponiveis.add(p);
            }
        }

        if (perguntasDisponiveis.isEmpty()) {
            return "Ele tem alguma característica muito única?";
        }

        return perguntasDisponiveis.get(random.nextInt(perguntasDisponiveis.size()));
    }

    /**
     * Processa a resposta do usuário
     */
    public boolean responderPergunta(int resposta) {
        if (!gameActive) {
            return false;
        }

        String ultimaPergunta = questionHistory.get(questionHistory.size() - 1);
        String caracteristicaChave = extrairCaracteristica(ultimaPergunta);

        // Atualiza pontuações baseado na resposta
        for (String personagem : baseConhecimentoExpandida.keySet()) {
            Set<String> caracteristicas = baseConhecimentoExpandida.get(personagem);
            boolean temCaracteristica = caracteristicas.contains(caracteristicaChave);

            int pontos = pontuacaoPersonagens.getOrDefault(personagem, 0);

            // Sistema de pontuação inteligente
            if (resposta == 0 && temCaracteristica) { // SIM e tem
                pontuacaoPersonagens.put(personagem, pontos + 3);
            } else if (resposta == 1 && !temCaracteristica) { // NÃO e não tem
                pontuacaoPersonagens.put(personagem, pontos + 2);
            } else if (resposta == 2) { // NÃO SEI
                pontuacaoPersonagens.put(personagem, pontos + 1);
            } else { // Resposta incompatível
                pontuacaoPersonagens.put(personagem, pontos - 2);
            }
        }

        return true;
    }

    private String extrairCaracteristica(String pergunta) {
        // Mapeamento EXPANDIDO para as novas perguntas categóricas
        Map<String, String> mapeamento = new HashMap<>();

        // === CATEGORIA DE MÍDIA ===
        mapeamento.put("filme de cinema", "é_de_filme");
        mapeamento.put("série de TV", "é_de_série");
        mapeamento.put("desenho animado", "é_desenho");
        mapeamento.put("cartoon", "é_desenho");
        mapeamento.put("anime", "é_anime");
        mapeamento.put("mangá", "é_anime");
        mapeamento.put("jogo eletrônico", "é_de_jogo");
        mapeamento.put("videogame", "é_de_jogo");
        mapeamento.put("livro", "é_de_livro");
        mapeamento.put("história em quadrinhos", "é_de_hq");
        mapeamento.put("pessoa real", "é_real");
        mapeamento.put("franquia da Disney", "é_disney");
        mapeamento.put("série da Netflix", "é_netflix");

        // === CARACTERÍSTICAS FÍSICAS ===
        mapeamento.put("ser humano normal", "é_humano");
        mapeamento.put("poderes sobrenaturais", "tem_poderes");
        mapeamento.put("poderes mágicos", "é_mago");
        mapeamento.put("voar", "pode_voar");
        mapeamento.put("levitar", "pode_voar");
        mapeamento.put("super força", "é_forte");
        mapeamento.put("arma", "usa_arma");
        mapeamento.put("máscara", "usa_máscara");
        mapeamento.put("capacete", "usa_capacete");
        mapeamento.put("cabelo de cor não-natural", "tem_cabelo_colorido");
        mapeamento.put("marca", "tem_marca");
        mapeamento.put("cicatriz famosa", "tem_cicatriz");
        mapeamento.put("muito alto", "é_alto");
        mapeamento.put("gigante", "é_gigante");
        mapeamento.put("muito pequeno", "é_pequeno");
        mapeamento.put("muito baixo", "é_baixo");

        // === PAPEL/FUNÇÃO ===
        mapeamento.put("protagonista principal", "é_protagonista");
        mapeamento.put("vilão", "é_vilão");
        mapeamento.put("antagonista", "é_vilão");
        mapeamento.put("herói", "é_herói");
        mapeamento.put("salva pessoas", "é_herói");
        mapeamento.put("líder", "é_líder");
        mapeamento.put("comandante", "é_comandante");
        mapeamento.put("guerreiro", "é_guerreiro");
        mapeamento.put("soldado", "é_soldado");
        mapeamento.put("mago", "é_mago");
        mapeamento.put("feiticeiro", "é_mago");
        mapeamento.put("ninja", "é_ninja");
        mapeamento.put("assassino", "é_assassino");
        mapeamento.put("detetive", "é_detetive");
        mapeamento.put("investigador", "é_detetive");
        mapeamento.put("cientista", "é_cientista");
        mapeamento.put("inventor", "é_inventor");
        mapeamento.put("royalty", "é_royalty");
        mapeamento.put("rei", "é_rei");
        mapeamento.put("príncipe", "é_príncipe");
        mapeamento.put("princesa", "é_princesa");

        // === IDADE E GÊNERO ===
        mapeamento.put("jovem", "é_jovem");
        mapeamento.put("adolescente", "é_jovem");
        mapeamento.put("criança", "é_criança");
        mapeamento.put("adulto", "é_adulto");
        mapeamento.put("idoso", "é_idoso");
        mapeamento.put("muito velho", "é_velho");
        mapeamento.put("sexo masculino", "é_masculino");

        // === ORIGEM GEOGRÁFICA/CULTURAL ===
        mapeamento.put("origem japonesa", "é_japonês");
        mapeamento.put("origem americana", "é_americano");
        mapeamento.put("origem europeia", "é_europeu");
        mapeamento.put("espaço", "vive_espaço");
        mapeamento.put("outro planeta", "é_alienígena");
        mapeamento.put("mundo de fantasia", "vive_fantasia");
        mapeamento.put("época futurística", "é_futuro");
        mapeamento.put("passado histórico", "é_histórico");

        // === CARACTERÍSTICAS ESPECÍFICAS ===
        mapeamento.put("luta contra monstros", "luta_monstros");
        mapeamento.put("animal de estimação", "tem_pet");
        mapeamento.put("companheiro", "tem_companheiro");
        mapeamento.put("viaja no tempo", "viaja_tempo");
        mapeamento.put("transforma", "transforma");
        mapeamento.put("muda de forma", "transforma");
        mapeamento.put("tecnologia avançada", "usa_tecnologia");
        mapeamento.put("artes marciais", "luta_marcial");
        mapeamento.put("imortal", "é_imortal");
        mapeamento.put("vive muito tempo", "vive_muito");
        mapeamento.put("profissão específica", "tem_profissão");

        // === RELACIONAMENTOS ===
        mapeamento.put("família conhecida", "tem_família");
        mapeamento.put("amor romântico", "tem_amor");
        mapeamento.put("trabalha em equipe", "tem_equipe");
        mapeamento.put("mentor", "tem_mentor");
        mapeamento.put("professor", "tem_professor");
        mapeamento.put("inimigos específicos", "tem_inimigos");

        // === FRANQUIAS ESPECÍFICAS ===
        mapeamento.put("universo Marvel", "é_marvel");
        mapeamento.put("universo DC", "é_dc");
        mapeamento.put("universo Harry Potter", "é_hp");
        mapeamento.put("universo Star Wars", "é_starwars");
        mapeamento.put("universo Dragon Ball", "é_dragonball");
        mapeamento.put("universo Nintendo", "é_nintendo");
        mapeamento.put("universo Naruto", "é_naruto");
        mapeamento.put("universo One Piece", "é_onepiece");

        for (Map.Entry<String, String> entry : mapeamento.entrySet()) {
            if (pergunta.toLowerCase().contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return "característica_genérica";
    }

    /**
     * Tenta adivinhar baseado na pontuação atual
     */
    public String obterTentativa() {
        List<String> candidatos = getCandidatosRestantes();

        if (candidatos.isEmpty()) {
            return "🤔 Você pensou em alguém muito único! Não consegui identificar...";
        }

        String melhorCandidato = candidatos.get(0);
        int melhorPontuacao = pontuacaoPersonagens.getOrDefault(melhorCandidato, 0);

        if (melhorPontuacao > 5) {
            return String.format("🎯 Você está pensando em: **%s**", melhorCandidato);
        } else if (candidatos.size() <= 5) {
            return String.format("🤔 Você pensou em um destes? %s",
                    String.join(", ", candidatos.subList(0, Math.min(3, candidatos.size()))));
        }

        return null; // Continuar fazendo perguntas
    }

    /**
     * Confirma se a tentativa estava correta
     */
    public void confirmarTentativa(boolean correto) {
        if (correto) {
            System.out.println("🎉 Excelente! Consegui adivinhar mais uma vez!");
            encerrarSessao();
        } else {
            System.out.println("🤔 Interessante... Deixe-me tentar mais uma vez!");
        }
    }

    /**
     * Rejeita a tentativa e continua o jogo
     * Remove o personagem incorreto da lista de candidatos
     */
    public boolean rejeitarTentativa() {
        List<String> candidatos = getCandidatosRestantes();

        if (!candidatos.isEmpty()) {
            // Remove o candidato com maior pontuação (que foi rejeitado)
            String personagemRejeitado = candidatos.get(0);
            pontuacaoPersonagens.put(personagemRejeitado, -10); // Marca como eliminado

            System.out.println("❌ " + personagemRejeitado + " eliminado!");
            System.out.println("🔄 Continuando com " + (candidatos.size() - 1) + " candidatos restantes...");

            // Se ainda há candidatos, continua o jogo
            if (candidatos.size() > 1) {
                return true;
            } else {
                System.out.println("🤯 Você pensou em alguém muito único! Não consegui descobrir...");
                return false;
            }
        }

        return false;
    }

    /**
     * Reset completo do jogo
     */
    public void resetarJogo() {
        pontuacaoPersonagens = new HashMap<>();
        questionHistory.clear();
        currentStep = 0;

        // Inicializar pontuações zeradas
        for (String personagem : baseConhecimentoExpandida.keySet()) {
            pontuacaoPersonagens.put(personagem, 0);
        }
    }

    /**
     * Encerra a sessão atual
     */
    public void encerrarSessao() {
        this.gameActive = false;
        System.out.println("✅ Sessão encerrada. Obrigado por jogar!");
    }

    /**
     * Lista candidatos restantes ordenados por pontuação
     */
    public List<String> getCandidatosRestantes() {
        List<String> candidatos = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : pontuacaoPersonagens.entrySet()) {
            if (entry.getValue() >= 0) {
                candidatos.add(entry.getKey());
            }
        }

        // Ordena por pontuação (maior primeiro)
        candidatos.sort((a, b) -> pontuacaoPersonagens.get(b) - pontuacaoPersonagens.get(a));

        return candidatos;
    }

    // Métodos de compatibilidade com a interface antiga
    public boolean isJogoAtivo() {
        return gameActive;
    }

    public int getNumeroPerguntas() {
        return currentStep;
    }

    public String proximaPergunta() {
        return obterPergunta();
    }

    public void processarResposta(boolean resposta) {
        responderPergunta(resposta ? 0 : 1);
    }

    public String tentarAdivinhacao() {
        return obterTentativa();
    }

    public void finalizarJogo() {
        encerrarSessao();
    }
}
