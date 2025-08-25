/**
 * Executável principal do jogo - MerlinGuru
 * Classe única para executar o jogo completo
 */
public class jogo {
    public static void main(String[] args) {
        // Configurar look and feel
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Erro ao configurar aparência: " + e.getMessage());
        }

        // Exibir splash screen
        System.out.println("====================================================");
        System.out.println("        🧙‍♂️ MERLIN GURU - GRIMÓRIO DIGITAL");
        System.out.println("====================================================");
        System.out.println("⚡ Iniciando o jogo...");
        System.out.println("✨ Carregando recursos mágicos...");

        // Executar em thread de interface gráfica
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new MerlinGUI();
                System.out.println("🎮 Jogo iniciado com sucesso!");
            } catch (Exception e) {
                System.err.println("❌ Erro ao iniciar o jogo: " + e.getMessage());
                e.printStackTrace();

                // Fallback em caso de erro
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Erro ao iniciar o jogo!\n" + e.getMessage(),
                        "Erro - MerlinGuru",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
