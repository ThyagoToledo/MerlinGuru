import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

public class MerlinGUI extends JFrame implements MyInterface {
    private GrimorioDAO grimorio;
    private Inteligencia inteligencia;
    private AkinatorAPI akinatorAPI; // Nova API melhorada

    // Componentes da interface
    private JTextArea areaChat;
    private JTextField campoPergunta;
    private JComboBox<String> comboCategoria;
    private JTable tabelaConsultas;
    private DefaultTableModel modeloTabela;
    private JLabel labelMerlin;
    private JButton btnConsultar, btnHistorico, btnAkinator; // Novo botão

    // Estados do jogo
    private boolean modoAkinator = false;
    private boolean estadoAdivinhandoAkinator = false;

    // Imagens
    private BufferedImage imagemFundo;
    private BufferedImage imagemMerlinNormal;
    private BufferedImage imagemMerlinRindo;
    private BufferedImage imagemMerlinBravo;

    // Cores temáticas para o chat
    private final Color COR_CHAT_FUNDO = new Color(0, 0, 0, 150); // Preto semi-transparente
    private final Color COR_CHAT_TEXTO = new Color(255, 255, 255); // Branco
    private final Color COR_INPUT_FUNDO = new Color(40, 40, 40, 200); // Cinza escuro semi-transparente
    private final Color COR_BOTAO = new Color(184, 134, 11); // Dourado

    public MerlinGUI() {
        this.grimorio = new GrimorioDAO();
        this.inteligencia = new Inteligencia();
        this.akinatorAPI = new AkinatorAPI(); // Inicializar nova API
        carregarImagens();
        inicializarInterface();
    }

    private void carregarImagens() {
        try {
            imagemFundo = ImageIO.read(new File("resources/Img/Cenario/Fundo.png"));
            imagemMerlinNormal = ImageIO.read(new File("resources/Img/Merlin/Normal.png"));
            imagemMerlinRindo = ImageIO.read(new File("resources/Img/Merlin/Rindo.png"));
            imagemMerlinBravo = ImageIO.read(new File("resources/Img/Merlin/Bravo.png"));
        } catch (IOException e) {
            System.err.println("Erro ao carregar imagens: " + e.getMessage());
        }
    }

    @Override
    public void inicializarInterface() {
        setTitle("🧙‍♂️ Grimório Digital do Mago Merlin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Criar painel principal com imagem de fundo
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagemFundo != null) {
                    // Desenhar a imagem de fundo redimensionada para cobrir toda a tela
                    g.drawImage(imagemFundo, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        painelPrincipal.setLayout(null); // Layout absoluto para posicionamento preciso

        // Adicionar Merlin no centro
        criarMerlinCentral(painelPrincipal);

        // Criar área de chat na parte inferior
        criarAreaChat(painelPrincipal);

        // Criar campo de input na parte inferior
        criarCampoInput(painelPrincipal);

        // Criar botões
        criarBotoes(painelPrincipal);

        // Inicializar tabela para histórico (inicialmente oculta)
        criarTabelaHistorico(painelPrincipal);

        // Listener único para reposicionar todos os componentes
        painelPrincipal.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                reposicionarComponentes(painelPrincipal);
            }
        });

        setContentPane(painelPrincipal);

        // Carregar dados iniciais
        lerConsultas();

        setVisible(true);

        // Forçar reposicionamento inicial após a janela estar visível
        SwingUtilities.invokeLater(() -> reposicionarComponentes(painelPrincipal));
    }

    private void criarMerlinCentral(JPanel painelPrincipal) {
        labelMerlin = new JLabel();
        labelMerlin.setHorizontalAlignment(JLabel.CENTER);
        labelMerlin.setVerticalAlignment(JLabel.CENTER);

        atualizarImagemMerlin("NORMAL");
        painelPrincipal.add(labelMerlin);
    }

    private void criarAreaChat(JPanel painelPrincipal) {
        areaChat = new JTextArea();
        // Usar cor sólida ao invés de transparente para evitar bugs visuais
        areaChat.setBackground(new Color(20, 20, 30)); // Azul escuro sólido
        areaChat.setForeground(COR_CHAT_TEXTO);
        areaChat.setFont(new Font("Consolas", Font.PLAIN, 14)); // Font monospace para melhor renderização
        areaChat.setEditable(false);
        areaChat.setOpaque(true);
        areaChat.setWrapStyleWord(true);
        areaChat.setLineWrap(true);
        // Adicionar margem interna
        areaChat.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Texto inicial mais simples para evitar bugs
        areaChat.setText("🧙‍♂️ Merlin: Bem-vindo ao meu grimório digital, jovem aprendiz!\n" +
                "Faça suas perguntas sobre magia, criaturas, batalhas e história.\n\n");

        JScrollPane scrollChat = new JScrollPane(areaChat);
        // Configurações para evitar bugs de renderização
        scrollChat.setOpaque(true);
        scrollChat.getViewport().setOpaque(true);
        scrollChat.setBackground(new Color(20, 20, 30)); // Mesmo fundo do chat
        scrollChat.getViewport().setBackground(new Color(20, 20, 30));
        scrollChat.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        scrollChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Personalizar scrollbar para evitar conflitos visuais
        scrollChat.getVerticalScrollBar().setBackground(new Color(40, 40, 50));
        scrollChat.getVerticalScrollBar().setOpaque(true);

        painelPrincipal.add(scrollChat);
    }

    private void criarCampoInput(JPanel painelPrincipal) {
        // Painel para o input
        JPanel painelInput = new JPanel();
        painelInput.setBackground(new Color(40, 40, 40, 220));
        painelInput.setLayout(new BorderLayout(10, 10));
        painelInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        campoPergunta = new JTextField();
        campoPergunta.setFont(new Font("Arial", Font.PLAIN, 16));
        campoPergunta.setBackground(new Color(60, 60, 60));
        campoPergunta.setForeground(Color.WHITE);
        campoPergunta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        campoPergunta.addActionListener(e -> processarNovaConsulta());

        String[] categorias = { "FEITICOS", "CRIATURAS", "BATALHA", "HISTORIA", "OUTROS" };
        comboCategoria = new JComboBox<>(categorias);
        comboCategoria.setPreferredSize(new Dimension(120, 35));

        painelInput.add(campoPergunta, BorderLayout.CENTER);
        painelInput.add(comboCategoria, BorderLayout.EAST);

        painelPrincipal.add(painelInput);
    }

    private void criarBotoes(JPanel painelPrincipal) {
        // Botão Consultar Merlin
        btnConsultar = new JButton("🔮 CONSULTAR MERLIN");
        btnConsultar.setBackground(COR_BOTAO);
        btnConsultar.setForeground(Color.BLACK);
        btnConsultar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConsultar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnConsultar.setFocusPainted(false);
        btnConsultar.addActionListener(e -> processarNovaConsulta());

        // Botão Ver Histórico
        btnHistorico = new JButton("📜 VER HISTÓRICO");
        btnHistorico.setBackground(new Color(70, 130, 180));
        btnHistorico.setForeground(Color.WHITE);
        btnHistorico.setFont(new Font("Arial", Font.BOLD, 14));
        btnHistorico.setBorder(BorderFactory.createRaisedBevelBorder());
        btnHistorico.setFocusPainted(false);
        btnHistorico.addActionListener(e -> mostrarHistorico());

        // Botão Akinator - Nova funcionalidade
        btnAkinator = new JButton("🧠 JOGO AKINATOR");
        btnAkinator.setBackground(new Color(148, 0, 211)); // Roxo
        btnAkinator.setForeground(Color.WHITE);
        btnAkinator.setFont(new Font("Arial", Font.BOLD, 14));
        btnAkinator.setBorder(BorderFactory.createRaisedBevelBorder());
        btnAkinator.setFocusPainted(false);
        btnAkinator.addActionListener(e -> processarBotaoAkinator());

        // Botões CRUD para o histórico
        JButton btnEditar = new JButton("✏️ EDITAR");
        btnEditar.setBackground(new Color(255, 165, 0)); // Laranja
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEditar.setBorder(BorderFactory.createRaisedBevelBorder());
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> editarConsultaSelecionada());

        JButton btnExcluir = new JButton("🗑️ EXCLUIR");
        btnExcluir.setBackground(new Color(220, 20, 60)); // Vermelho
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 12));
        btnExcluir.setBorder(BorderFactory.createRaisedBevelBorder());
        btnExcluir.setFocusPainted(false);
        btnExcluir.addActionListener(e -> excluirConsultaSelecionada());

        JButton btnLimparHistorico = new JButton("🧹 LIMPAR TUDO");
        btnLimparHistorico.setBackground(new Color(139, 0, 0)); // Vermelho escuro
        btnLimparHistorico.setForeground(Color.WHITE);
        btnLimparHistorico.setFont(new Font("Arial", Font.BOLD, 11));
        btnLimparHistorico.setBorder(BorderFactory.createRaisedBevelBorder());
        btnLimparHistorico.setFocusPainted(false);
        btnLimparHistorico.addActionListener(e -> limparHistoricoCompleto());

        painelPrincipal.add(btnConsultar);
        painelPrincipal.add(btnHistorico);
        painelPrincipal.add(btnAkinator);
        painelPrincipal.add(btnEditar);
        painelPrincipal.add(btnExcluir);
        painelPrincipal.add(btnLimparHistorico);
    }

    private void criarTabelaHistorico(JPanel painelPrincipal) {
        String[] colunas = { "ID", "Pergunta", "Categoria", "Emoção", "Data" };
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaConsultas = new JTable(modeloTabela);
        tabelaConsultas.setBackground(new Color(255, 255, 255, 230));
        tabelaConsultas.setSelectionBackground(new Color(184, 134, 11));
        tabelaConsultas.setSelectionForeground(Color.WHITE);

        JScrollPane scrollTabela = new JScrollPane(tabelaConsultas);
        scrollTabela.setVisible(false); // Inicialmente oculto

        painelPrincipal.add(scrollTabela);
    }

    // Método único para reposicionar todos os componentes
    private void reposicionarComponentes(JPanel painelPrincipal) {
        Dimension tamanhoTela = painelPrincipal.getSize();
        if (tamanhoTela.width <= 0 || tamanhoTela.height <= 0)
            return;

        // Posicionar Merlin no centro
        if (labelMerlin != null) {
            int x = (tamanhoTela.width - 200) / 2;
            int y = (tamanhoTela.height - 300) / 2 - 50;
            labelMerlin.setBounds(x, y, 200, 300);
        }

        // Posicionar área de chat
        Component scrollChat = encontrarScrollChat(painelPrincipal);
        if (scrollChat != null) {
            int largura = 650;
            int altura = 200;
            int x = 30;
            int y = tamanhoTela.height - altura - 150;
            scrollChat.setBounds(x, y, largura, altura);
        }

        // Posicionar campo de input
        Component painelInput = encontrarPainelInput(painelPrincipal);
        if (painelInput != null) {
            int largura = 650;
            int altura = 55;
            int x = 30;
            int y = tamanhoTela.height - altura - 80;
            painelInput.setBounds(x, y, largura, altura);
        }

        // Posicionar botões
        if (btnConsultar != null) {
            int larguraBotao = 200;
            int alturaBotao = 40;
            int x = 700;
            int y = tamanhoTela.height - alturaBotao - 130;
            btnConsultar.setBounds(x, y, larguraBotao, alturaBotao);
        }

        if (btnHistorico != null) {
            int larguraBotao = 200;
            int alturaBotao = 40;
            int x = 700;
            int y = tamanhoTela.height - alturaBotao - 80;
            btnHistorico.setBounds(x, y, larguraBotao, alturaBotao);
        }

        if (btnAkinator != null) {
            int larguraBotao = 200;
            int alturaBotao = 40;
            int x = 700;
            int y = tamanhoTela.height - alturaBotao - 30;
            btnAkinator.setBounds(x, y, larguraBotao, alturaBotao);
        }

        // Posicionar botões CRUD
        Component btnEditar = encontrarBotaoPorTexto(painelPrincipal, "✏️ EDITAR");
        if (btnEditar != null) {
            int larguraBotao = 120;
            int alturaBotao = 35;
            int x = 950;
            int y = tamanhoTela.height - alturaBotao - 80;
            btnEditar.setBounds(x, y, larguraBotao, alturaBotao);
        }

        Component btnExcluir = encontrarBotaoPorTexto(painelPrincipal, "🗑️ EXCLUIR");
        if (btnExcluir != null) {
            int larguraBotao = 120;
            int alturaBotao = 35;
            int x = 950;
            int y = tamanhoTela.height - alturaBotao - 30;
            btnExcluir.setBounds(x, y, larguraBotao, alturaBotao);
        }

        Component btnLimparHistorico = encontrarBotaoPorTexto(painelPrincipal, "🧹 LIMPAR TUDO");
        if (btnLimparHistorico != null) {
            int larguraBotao = 140;
            int alturaBotao = 35;
            int x = 1080;
            int y = tamanhoTela.height - alturaBotao - 55; // Entre os outros dois botões
            btnLimparHistorico.setBounds(x, y, larguraBotao, alturaBotao);
        }

        // Posicionar tabela de histórico
        Component scrollTabela = encontrarScrollTabela(painelPrincipal);
        if (scrollTabela != null) {
            int largura = tamanhoTela.width - 100;
            int altura = 300;
            int x = 50;
            int y = 50;
            scrollTabela.setBounds(x, y, largura, altura);
        }
    }

    private Component encontrarScrollChat(JPanel painel) {
        for (Component comp : painel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                if (scroll.getViewport().getView() instanceof JTextArea) {
                    return comp;
                }
            }
        }
        return null;
    }

    private Component encontrarPainelInput(JPanel painel) {
        for (Component comp : painel.getComponents()) {
            if (comp instanceof JPanel && comp != painel) {
                JPanel p = (JPanel) comp;
                if (p.getLayout() instanceof BorderLayout) {
                    return comp;
                }
            }
        }
        return null;
    }

    private Component encontrarScrollTabela(JPanel painel) {
        for (Component comp : painel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) comp;
                if (scroll.getViewport().getView() instanceof JTable) {
                    return comp;
                }
            }
        }
        return null;
    }

    private Component encontrarBotaoPorTexto(JPanel painel, String texto) {
        for (Component comp : painel.getComponents()) {
            if (comp instanceof JButton) {
                JButton botao = (JButton) comp;
                if (botao.getText().equals(texto)) {
                    return comp;
                }
            }
        }
        return null;
    }

    private void atualizarImagemMerlin(String emocao) {
        BufferedImage imagem = imagemMerlinNormal;

        switch (emocao.toUpperCase()) {
            case "FELIZ":
            case "SABIO":
                imagem = imagemMerlinRindo;
                break;
            case "IRRITADO":
                imagem = imagemMerlinBravo;
                break;
            default:
                imagem = imagemMerlinNormal;
                break;
        }

        if (imagem != null && labelMerlin != null) {
            // Redimensionar a imagem se necessário
            int novaLargura = 200;
            int novaAltura = 300;
            Image imagemRedimensionada = imagem.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
            labelMerlin.setIcon(new ImageIcon(imagemRedimensionada));
            labelMerlin.setSize(novaLargura, novaAltura);
        }
    }

    private void mostrarHistorico() {
        // Se está no modo Akinator e o jogo está ativo, processar como "NÃO"
        if (modoAkinator && akinatorAPI.isJogoAtivo()) {
            processarRespostaAkinator(false); // Botão "NÃO"
            return;
        }

        // Funcionamento normal do histórico
        Component scrollTabela = null;
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JScrollPane && ((JScrollPane) comp).getViewport().getView() instanceof JTable) {
                scrollTabela = comp;
                break;
            }
        }

        if (scrollTabela != null) {
            scrollTabela.setVisible(!scrollTabela.isVisible());
            if (scrollTabela.isVisible()) {
                if (!modoAkinator) {
                    btnHistorico.setText("📜 OCULTAR HISTÓRICO");
                }
                lerConsultas();
            } else {
                if (!modoAkinator) {
                    btnHistorico.setText("📜 VER HISTÓRICO");
                }
            }
        }
    }

    @Override
    public void display() {
        setVisible(true);
    }

    @Override
    public void criarConsulta(String pergunta, String categoria) {
        if (!grimorio.validarEntrada(pergunta, categoria)) {
            JOptionPane.showMessageDialog(this,
                    "❌ Entrada inválida! Verifique os campos.",
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        RegistroMagico registro = new RegistroMagico(0, pergunta, categoria);
        if (grimorio.adicionarRegistro(registro)) {
            areaChat.append("👤 Você: " + pergunta + " [" + categoria + "]\n");
            processarPergunta(pergunta, categoria);
        }
    }

    @Override
    public void processarPergunta(String pergunta, String categoria) {
        // Desabilitar botão durante processamento
        btnConsultar.setEnabled(false);
        btnConsultar.setText("🔮 Consultando...");

        // Processar em thread separada para não travar a interface
        SwingWorker<Inteligencia.RespostaMerlin, Void> worker = new SwingWorker<Inteligencia.RespostaMerlin, Void>() {

            @Override
            protected Inteligencia.RespostaMerlin doInBackground() throws Exception {
                return inteligencia.processarPergunta(pergunta, categoria);
            }

            @Override
            protected void done() {
                try {
                    Inteligencia.RespostaMerlin resultado = get();

                    // Encontrar o registro recém-criado e atualizar com a resposta
                    List<RegistroMagico> registros = grimorio.obterTodosRegistros();
                    if (!registros.isEmpty()) {
                        RegistroMagico ultimoRegistro = registros.get(registros.size() - 1);
                        grimorio.atualizarResposta(ultimoRegistro.getId(),
                                resultado.getResposta(),
                                resultado.getEmocao());
                    }

                    exibirResposta(resultado.getResposta(), resultado.getEmocao());
                    lerConsultas(); // Atualizar tabela

                } catch (Exception e) {
                    areaChat.append("🧙‍♂️ Merlin: *Algo deu errado na magia...* \n\n");
                } finally {
                    btnConsultar.setEnabled(true);
                    btnConsultar.setText("🔮 CONSULTAR MERLIN");
                }
            }
        };

        worker.execute();
    }

    @Override
    public void exibirResposta(String resposta, String emocao) {
        // Atualizar chat
        areaChat.append("🧙‍♂️ Merlin: " + resposta + "\n\n");
        areaChat.setCaretPosition(areaChat.getDocument().getLength());

        // Atualizar imagem do Merlin baseada na emoção
        atualizarImagemMerlin(emocao);

        // Limpar campos
        campoPergunta.setText("");
        comboCategoria.setSelectedIndex(0);
    }

    @Override
    public void lerConsultas() {
        modeloTabela.setRowCount(0);
        List<RegistroMagico> registros = grimorio.obterTodosRegistros();

        for (RegistroMagico registro : registros) {
            Object[] linha = {
                    registro.getId(),
                    registro.getPergunta().length() > 50 ? registro.getPergunta().substring(0, 47) + "..."
                            : registro.getPergunta(),
                    registro.getCategoria(),
                    registro.getEmocaoMerlin(),
                    registro.getDataConsulta().format(
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"))
            };
            modeloTabela.addRow(linha);
        }
    }

    @Override
    public void atualizarConsulta(int id, String novaPergunta, String novaCategoria) {
        if (grimorio.atualizarRegistro(id, novaPergunta, novaCategoria)) {
            JOptionPane.showMessageDialog(this, "✅ Consulta atualizada com sucesso!");
            lerConsultas();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Erro ao atualizar consulta!");
        }
    }

    @Override
    public void excluirConsulta(int id) {
        int confirmacao = JOptionPane.showConfirmDialog(this,
                "🗑️ Deseja realmente excluir esta consulta do grimório?",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (grimorio.excluirRegistro(id)) {
                JOptionPane.showMessageDialog(this, "✅ Consulta excluída com sucesso!");
                lerConsultas();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erro ao excluir consulta!");
            }
        }
    }

    @Override
    public void salvarNoGrimorio(String pergunta, String resposta, String categoria, String emocao) {
        // Este método é chamado internamente por processarPergunta
    }

    private void editarConsultaSelecionada() {
        int linhaSelecionada = tabelaConsultas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "❌ Selecione uma consulta na tabela primeiro!");
            return;
        }

        // Obter dados da linha selecionada
        int id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
        String perguntaAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 1);
        String categoriaAtual = (String) modeloTabela.getValueAt(linhaSelecionada, 2);

        // Criar painel de edição
        JPanel painelEdicao = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField campoNovaPergunta = new JTextField(perguntaAtual);
        JComboBox<String> comboNovaCategoria = new JComboBox<>(
                new String[] { "FEITICOS", "CRIATURAS", "BATALHA", "HISTORIA", "OUTROS" });
        comboNovaCategoria.setSelectedItem(categoriaAtual);

        painelEdicao.add(new JLabel("Nova pergunta:"));
        painelEdicao.add(campoNovaPergunta);
        painelEdicao.add(new JLabel("Nova categoria:"));
        painelEdicao.add(comboNovaCategoria);

        int resultado = JOptionPane.showConfirmDialog(this, painelEdicao,
                "✏️ Editar Consulta #" + id, JOptionPane.OK_CANCEL_OPTION);

        if (resultado == JOptionPane.OK_OPTION) {
            String novaPergunta = campoNovaPergunta.getText().trim();
            String novaCategoria = (String) comboNovaCategoria.getSelectedItem();

            if (novaPergunta.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ A pergunta não pode estar vazia!");
                return;
            }

            if (grimorio.atualizarRegistro(id, novaPergunta, novaCategoria)) {
                JOptionPane.showMessageDialog(this, "✅ Consulta editada com sucesso!");
                lerConsultas(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erro ao editar consulta!");
            }
        }
    }

    private void excluirConsultaSelecionada() {
        int linhaSelecionada = tabelaConsultas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "❌ Selecione uma consulta na tabela primeiro!");
            return;
        }

        // Obter ID da linha selecionada
        int id = (Integer) modeloTabela.getValueAt(linhaSelecionada, 0);
        String pergunta = (String) modeloTabela.getValueAt(linhaSelecionada, 1);

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "🗑️ Deseja realmente excluir esta consulta do grimório?\n\n\"" + pergunta + "\"",
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

        if (confirmacao == JOptionPane.YES_OPTION) {
            if (grimorio.excluirRegistro(id)) {
                JOptionPane.showMessageDialog(this, "✅ Consulta excluída com sucesso!");
                lerConsultas(); // Atualizar tabela
            } else {
                JOptionPane.showMessageDialog(this, "❌ Erro ao excluir consulta!");
            }
        }
    }

    private void limparHistoricoCompleto() {
        // Verificar se há consultas no histórico
        List<RegistroMagico> registros = grimorio.obterTodosRegistros();
        if (registros.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ℹ️ O grimório já está vazio!");
            return;
        }

        // Confirmar ação com aviso mais forte
        String mensagem = String.format(
                "⚠️ ATENÇÃO! Esta ação irá excluir TODAS as %d consultas do grimório!\n\n" +
                        "🗑️ Todo o histórico de conversas com Merlin será perdido permanentemente.\n" +
                        "📜 O arquivo 'grimorio_merlin.txt' será limpo completamente.\n\n" +
                        "Tem certeza de que deseja continuar?",
                registros.size());

        int confirmacao = JOptionPane.showConfirmDialog(this, mensagem,
                "⚠️ LIMPAR TODO HISTÓRICO", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            // Segunda confirmação para ações destrutivas
            int segundaConfirmacao = JOptionPane.showConfirmDialog(this,
                    "🚨 ÚLTIMA CONFIRMAÇÃO!\n\nEsta ação NÃO pode ser desfeita!\nExcluir todo o histórico?",
                    "Confirmar Exclusão Total", JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);

            if (segundaConfirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = true;
                int totalExcluido = 0;

                // Excluir todos os registros
                for (RegistroMagico registro : registros) {
                    if (grimorio.excluirRegistro(registro.getId())) {
                        totalExcluido++;
                    } else {
                        sucesso = false;
                        break;
                    }
                }

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "✅ Histórico limpo com sucesso!\n\n" +
                                    "🗑️ " + totalExcluido + " consultas foram excluídas.\n" +
                                    "📜 O grimório está agora vazio e pronto para novas consultas!",
                            "Limpeza Concluída", JOptionPane.INFORMATION_MESSAGE);

                    // Limpar chat visual também
                    areaChat.setText(
                            "🧙‍♂️ Merlin: O grimório foi limpo! Estou pronto para novas consultas mágicas!\n\n");
                    atualizarImagemMerlin("NORMAL");

                    // Atualizar tabela
                    lerConsultas();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "❌ Erro ao limpar histórico!\n\n" +
                                    "Algumas consultas podem não ter sido excluídas.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void processarBotaoAkinator() {
        if (!modoAkinator) {
            // Iniciar novo jogo
            iniciarAkinator();
        } else if (akinatorAPI.isJogoAtivo()) {
            // Finalizar jogo atual
            finalizarAkinator();
        } else {
            // Novo jogo após finalização
            iniciarAkinator();
        }
    }

    // ========== MÉTODOS DO AKINATOR ==========

    private void iniciarAkinator() {
        modoAkinator = true;
        akinatorAPI.iniciarSessao();

        areaChat.setText(""); // Limpar chat
        areaChat.append("🧠 JOGO AKINATOR INICIADO!\n\n");
        areaChat.append("🧙‍♂️ Merlin: Olá, jovem! Vamos jogar um jogo mágico!\n");
        areaChat.append("Pense em um personagem de fantasia e eu tentarei adivinhar quem é!\n\n");
        areaChat.append(
                "📝 Personagens que conheço: Gandalf, Dumbledore, Harry Potter, Merlin, Aragorn, Legolas, Gimli, Smaug, Sauron, Gollum\n\n");
        areaChat.append("✨ Responda apenas 'sim' ou 'não' às minhas perguntas!\n\n");

        // Alterar interface para modo Akinator
        btnConsultar.setText("✅ SIM");
        btnHistorico.setText("❌ NÃO");
        btnAkinator.setText("🏁 FINALIZAR JOGO");

        comboCategoria.setEnabled(false);
        campoPergunta.setEnabled(false);

        atualizarImagemMerlin("MISTERIOSO");

        // Fazer primeira pergunta
        proximaPerguntaAkinator();
    }

    private void proximaPerguntaAkinator() {
        if (!akinatorAPI.isJogoAtivo()) {
            return;
        }

        String pergunta = akinatorAPI.proximaPergunta();

        if (pergunta != null && akinatorAPI.getNumeroPerguntas() <= 15) {
            areaChat.append("🤔 Pergunta " + akinatorAPI.getNumeroPerguntas() + ": " + pergunta + "\n\n");
            areaChat.setCaretPosition(areaChat.getDocument().getLength());
        } else {
            // Tentar adivinhação final
            tentarAdivinhacaoFinal();
        }
    }

    private void processarRespostaAkinator(boolean resposta) {
        if (!modoAkinator || !akinatorAPI.isJogoAtivo()) {
            return;
        }

        akinatorAPI.processarResposta(resposta);

        String respostaTexto = resposta ? "Sim" : "Não";
        areaChat.append("👤 Você: " + respostaTexto + "\n\n");

        // Verificar se deve tentar adivinhação
        if (akinatorAPI.getNumeroPerguntas() >= 8) {
            List<String> candidatos = akinatorAPI.getCandidatosRestantes();
            if (candidatos.size() == 1) {
                tentarAdivinhacaoFinal();
                return;
            }
        }

        if (akinatorAPI.getNumeroPerguntas() >= 15) {
            tentarAdivinhacaoFinal();
        } else {
            proximaPerguntaAkinator();
        }
    }

    private void tentarAdivinhacaoFinal() {
        String personagem = akinatorAPI.tentarAdivinhacao();

        if (personagem == null || personagem.contains("muito único")) {
            // Não conseguiu adivinhar
            areaChat.append("🤯 Merlin: Caramba! Você pensou em alguém muito único!\n");
            areaChat.append("� Não consegui descobrir... Você me venceu desta vez!\n\n");
            finalizarAkinator();
            return;
        }

        areaChat.append("�🔮 Merlin: Eureka! Eu acho que sei!\n");
        areaChat.append("✨ Você estava pensando em: **" + personagem + "** !\n\n");

        areaChat.append("❓ Acertei? Escolha uma opção:\n");
        areaChat.append("   ✅ SIM - Acertou!\n");
        areaChat.append("   ❌ NÃO - Continue tentando\n");
        areaChat.append("   🏁 FINALIZAR - Encerrar jogo\n\n");

        // Desabilitar campo de texto durante adivinhação
        campoPergunta.setEnabled(false);

        // Configurar botões para resposta à adivinhação
        btnConsultar.setText("✅ SIM");
        btnHistorico.setText("❌ NÃO");
        btnAkinator.setText("🏁 FINALIZAR");

        atualizarImagemMerlin("FELIZ");

        // Marcar estado especial de adivinhação
        estadoAdivinhandoAkinator = true;
    }

    private void finalizarAkinator() {
        modoAkinator = false;

        // Restaurar interface normal
        btnConsultar.setText("🔮 CONSULTAR MERLIN");
        btnHistorico.setText("📜 VER HISTÓRICO");
        btnAkinator.setText("🧠 JOGO AKINATOR");

        comboCategoria.setEnabled(true);
        campoPergunta.setEnabled(true);
        campoPergunta.setText("");

        areaChat.append("🎮 Jogo finalizado! Obrigado por jogar!\n");
        areaChat.append("🧙‍♂️ Merlin: Voltamos ao modo de consultas normal.\n\n");

        atualizarImagemMerlin("NORMAL");
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }

    private void finalizarJogoAkinator() {
        modoAkinator = false;
        estadoAdivinhandoAkinator = false;

        // Restaurar interface normal
        btnConsultar.setText("🔮 CONSULTAR MERLIN");
        btnHistorico.setText("📜 VER HISTÓRICO");
        btnAkinator.setText("🧠 JOGO AKINATOR");

        comboCategoria.setEnabled(true);
        campoPergunta.setEnabled(true);
        campoPergunta.setText("");

        areaChat.append("🎮 Jogo finalizado! Obrigado por jogar!\n\n");

        atualizarImagemMerlin("NORMAL");
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }

    private void iniciarNovoJogoAkinator() {
        if (akinatorAPI == null) {
            akinatorAPI = new AkinatorAPI();
        }

        akinatorAPI.resetarJogo();
        estadoAdivinhandoAkinator = false;

        areaChat.append("🎮 Iniciando novo jogo Akinator!\n");
        areaChat.append("🧙‍♂️ Merlin: Pense em um personagem e eu tentarei adivinhar!\n\n");

        String pergunta = akinatorAPI.proximaPergunta();
        if (pergunta != null) {
            areaChat.append("🔮 Merlin: " + pergunta + "\n");
            areaChat.append("💬 Responda: ✅SIM | ❌NÃO | 🤷‍♂️NÃO SEI\n\n");
        }

        atualizarImagemMerlin("SABIO");
        areaChat.setCaretPosition(areaChat.getDocument().getLength());
    }

    // Métodos auxiliares da interface
    private void processarNovaConsulta() {
        // Verificar se está no modo Akinator
        if (modoAkinator) {
            if (estadoAdivinhandoAkinator) {
                // Estado de adivinhação - processar resposta do usuário
                String resposta = campoPergunta.getText().trim().toLowerCase();
                if (resposta.equals("sim") || resposta.equals("s")) {
                    // Usuário confirmou que é o personagem correto
                    areaChat.append("🎉 Merlin: Excelente! Mais uma vez minha sabedoria prevalece!\n\n");
                    atualizarImagemMerlin("RINDO");
                    finalizarJogoAkinator();
                } else if (resposta.equals("não") || resposta.equals("nao") || resposta.equals("n")) {
                    // Usuário disse que não é o personagem - continuar jogo
                    areaChat.append("🤔 Merlin: Hmm... deixe-me pensar em outras possibilidades...\n\n");
                    atualizarImagemMerlin("NORMAL");

                    // Rejeitar tentativa e continuar
                    estadoAdivinhandoAkinator = false;
                    akinatorAPI.rejeitarTentativa();

                    // Verificar se ainda há candidatos
                    if (akinatorAPI.getCandidatosRestantes().size() > 0) {
                        // Fazer próxima pergunta
                        String proximaPergunta = akinatorAPI.proximaPergunta();
                        if (proximaPergunta != null) {
                            areaChat.append("🔮 Merlin: " + proximaPergunta + "\n");
                            areaChat.append("💬 Responda: ✅SIM | ❌NÃO | �‍♂️NÃO SEI\n\n");
                            atualizarImagemMerlin("SABIO");
                        } else {
                            tentarAdivinhacaoFinal();
                        }
                    } else {
                        // Não há mais candidatos
                        areaChat.append("😔 Merlin: Você me derrotou! Em que personagem estava pensando?\n\n");
                        atualizarImagemMerlin("BRAVO");
                        finalizarJogoAkinator();
                    }
                } else if (resposta.equals("finalizar") || resposta.equals("fim")) {
                    finalizarJogoAkinator();
                }

                campoPergunta.setText("");
                areaChat.setCaretPosition(areaChat.getDocument().getLength());
                return;
            }

            if (akinatorAPI.isJogoAtivo()) {
                // Modo pergunta/resposta do Akinator
                processarRespostaAkinator(true); // Botão "SIM"
            } else {
                // Jogo não ativo - inicializar novo jogo
                iniciarNovoJogoAkinator();
            }
            return;
        }

        // Modo normal de consultas
        String pergunta = campoPergunta.getText().trim();
        String categoria = (String) comboCategoria.getSelectedItem();

        if (!pergunta.isEmpty()) {
            criarConsulta(pergunta, categoria);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Digite uma pergunta primeiro!");
        }
    }

    // Removido método main - usar classe 'jogo' para executar
}
