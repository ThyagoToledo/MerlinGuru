import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MerlinGUI extends JFrame implements MyInterface {
    private GrimorioDAO grimorio;
    private Inteligencia inteligencia;
    
    // Componentes da interface
    private JTextArea areaChat;
    private JTextField campoPergunta;
    private JComboBox<String> comboCategoria;
    private JTable tabelaConsultas;
    private DefaultTableModel modeloTabela;
    private JLabel labelEmocaoMerlin;
    private JButton btnPerguntar, btnEditar, btnExcluir, btnAtualizar;
    
    // Cores temáticas
    private final Color COR_FUNDO = new Color(25, 25, 112);      // Azul escuro mágico
    private final Color COR_PAINEL = new Color(72, 61, 139);     // Roxo escuro
    private final Color COR_TEXTO = new Color(255, 215, 0);      // Dourado
    private final Color COR_BOTAO = new Color(138, 43, 226);     // Violeta
    
    public MerlinGUI() {
        this.grimorio = new GrimorioDAO();
        this.inteligencia = new Inteligencia();
        inicializarInterface();
    }
    
    @Override
    public void inicializarInterface() {
        setTitle("🧙‍♂️ Grimório Digital do Mago Merlin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COR_FUNDO);
        
        // Layout principal
        setLayout(new BorderLayout());
        
        // Painel superior - Título e emoção do Merlin
        add(criarPainelTitulo(), BorderLayout.NORTH);
        
        // Painel central - Chat e formulário
        add(criarPainelCentral(), BorderLayout.CENTER);
        
        // Painel inferior - Tabela de consultas
        add(criarPainelTabela(), BorderLayout.SOUTH);
        
        // Carregar dados iniciais
        lerConsultas();
        
        setVisible(true);
    }
    
    private JPanel criarPainelTitulo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("🧙‍♂️ GRIMÓRIO DIGITAL DO MAGO MERLIN 🧙‍♂️", JLabel.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 28));
        titulo.setForeground(COR_TEXTO);
        
        labelEmocaoMerlin = new JLabel("😐 Merlin está NEUTRO", JLabel.CENTER);
        labelEmocaoMerlin.setFont(new Font("SansSerif", Font.ITALIC, 16));
        labelEmocaoMerlin.setForeground(Color.LIGHT_GRAY);
        
        painel.add(titulo, BorderLayout.CENTER);
        painel.add(labelEmocaoMerlin, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Área de chat (esquerda)
        JPanel painelChat = criarPainelChat();
        
        // Painel de consulta (direita)
        JPanel painelConsulta = criarPainelConsulta();
        
        painel.add(painelChat, BorderLayout.CENTER);
        painel.add(painelConsulta, BorderLayout.EAST);
        
        return painel;
    }
    
    private JPanel criarPainelChat() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TEXTO, 2), 
            "💬 Conversa com Merlin", 
            0, 0, new Font("SansSerif", Font.BOLD, 14), COR_TEXTO));
        
        areaChat = new JTextArea(20, 40);
        areaChat.setBackground(Color.BLACK);
        areaChat.setForeground(Color.GREEN);
        areaChat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaChat.setEditable(false);
        areaChat.setText("🧙‍♂️ Merlin: Bem-vindo ao meu grimório digital, jovem aprendiz!\n" +
                        "Faça suas perguntas sobre magia, criaturas, batalhas e história.\n" +
                        "Todas as consultas serão registradas para a posteridade...\n\n");
        
        JScrollPane scrollChat = new JScrollPane(areaChat);
        painel.add(scrollChat, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelConsulta() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(COR_PAINEL);
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TEXTO, 2),
            "✨ Nova Consulta",
            0, 0, new Font("SansSerif", Font.BOLD, 14), COR_TEXTO));
        painel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo pergunta
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPergunta = new JLabel("Pergunta:");
        lblPergunta.setForeground(COR_TEXTO);
        painel.add(lblPergunta, gbc);
        
        gbc.gridy = 1;
        campoPergunta = new JTextField(20);
        campoPergunta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        painel.add(campoPergunta, gbc);
        
        // Combo categoria
        gbc.gridy = 2;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setForeground(COR_TEXTO);
        painel.add(lblCategoria, gbc);
        
        gbc.gridy = 3;
        String[] categorias = {"FEITICOS", "CRIATURAS", "BATALHA", "HISTORIA", "OUTROS"};
        comboCategoria = new JComboBox<>(categorias);
        painel.add(comboCategoria, gbc);
        
        // Botão perguntar
        gbc.gridy = 4;
        btnPerguntar = new JButton("🔮 Consultar Merlin");
        btnPerguntar.setBackground(COR_BOTAO);
        btnPerguntar.setForeground(Color.WHITE);
        btnPerguntar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnPerguntar.addActionListener(e -> processarNovaConsulta());
        painel.add(btnPerguntar, gbc);
        
        // Botões de edição
        gbc.gridy = 5;
        btnEditar = new JButton("✏️ Editar Selecionada");
        btnEditar.setBackground(Color.ORANGE);
        btnEditar.setForeground(Color.BLACK);
        btnEditar.addActionListener(e -> editarConsultaSelecionada());
        painel.add(btnEditar, gbc);
        
        gbc.gridy = 6;
        btnExcluir = new JButton("🗑️ Excluir Selecionada");
        btnExcluir.setBackground(Color.RED);
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirConsultaSelecionada());
        painel.add(btnExcluir, gbc);
        
        gbc.gridy = 7;
        btnAtualizar = new JButton("🔄 Atualizar Lista");
        btnAtualizar.setBackground(Color.BLUE);
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.addActionListener(e -> lerConsultas());
        painel.add(btnAtualizar, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_TEXTO, 2),
            "📜 Histórico de Consultas",
            0, 0, new Font("SansSerif", Font.BOLD, 14), COR_TEXTO));
        painel.setPreferredSize(new Dimension(0, 200));
        
        String[] colunas = {"ID", "Pergunta", "Categoria", "Emoção Merlin", "Data"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaConsultas = new JTable(modeloTabela);
        tabelaConsultas.setBackground(Color.WHITE);
        tabelaConsultas.setSelectionBackground(COR_BOTAO);
        tabelaConsultas.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollTabela = new JScrollPane(tabelaConsultas);
        painel.add(scrollTabela, BorderLayout.CENTER);
        
        return painel;
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
        btnPerguntar.setEnabled(false);
        btnPerguntar.setText("🔮 Consultando...");
        
        // Processar em thread separada para não travar a interface
        SwingWorker<Inteligencia.RespostaMerlin, Void> worker = 
            new SwingWorker<Inteligencia.RespostaMerlin, Void>() {
            
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
                    btnPerguntar.setEnabled(true);
                    btnPerguntar.setText("🔮 Consultar Merlin");
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
        
        // Atualizar emoção visual
        String emoji = obterEmojiPorEmocao(emocao);
        labelEmocaoMerlin.setText(emoji + " Merlin está " + emocao);
        
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
                registro.getPergunta().length() > 50 ? 
                    registro.getPergunta().substring(0, 47) + "..." : registro.getPergunta(),
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
    
    // Métodos auxiliares da interface
    private void processarNovaConsulta() {
        String pergunta = campoPergunta.getText().trim();
        String categoria = (String) comboCategoria.getSelectedItem();
        
        if (!pergunta.isEmpty()) {
            criarConsulta(pergunta, categoria);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Digite uma pergunta primeiro!");
        }
    }
    
    private void editarConsultaSelecionada() {
        int linha = tabelaConsultas.getSelectedRow();
        if (linha >= 0) {
            int id = (Integer) modeloTabela.getValueAt(linha, 0);
            RegistroMagico registro = grimorio.obterRegistroPorId(id);
            
            if (registro != null) {
                String novaPergunta = JOptionPane.showInputDialog(this,
                    "Nova pergunta:", registro.getPergunta());
                    
                if (novaPergunta != null && !novaPergunta.trim().isEmpty()) {
                    String[] categorias = {"FEITICOS", "CRIATURAS", "BATALHA", "HISTORIA", "OUTROS"};
                    String novaCategoria = (String) JOptionPane.showInputDialog(this,
                        "Nova categoria:", "Categoria", JOptionPane.QUESTION_MESSAGE,
                        null, categorias, registro.getCategoria());
                        
                    if (novaCategoria != null) {
                        atualizarConsulta(id, novaPergunta, novaCategoria);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "❌ Selecione uma consulta para editar!");
        }
    }
    
    private void excluirConsultaSelecionada() {
        int linha = tabelaConsultas.getSelectedRow();
        if (linha >= 0) {
            int id = (Integer) modeloTabela.getValueAt(linha, 0);
            excluirConsulta(id);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Selecione uma consulta para excluir!");
        }
    }
    
    private String obterEmojiPorEmocao(String emocao) {
        switch (emocao.toUpperCase()) {
            case "FELIZ": return "😊";
            case "SABIO": return "🤔";
            case "IRRITADO": return "😠";
            case "MISTERIOSO": return "🔮";
            default: return "😐";
        }
    }
    
    // Método main para executar o programa
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MerlinGUI();
        });
    }
}
