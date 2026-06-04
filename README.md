# GRIMÓRIO DIGITAL DO MAGO MERLIN

<p align="center">
  <img src="resources/Img/Merlin/Normal.png" alt="Mago Merlin Logo" width="120px" style="border-radius: 24px; box-shadow: 0 8px 30px rgba(0, 0, 0, 0.3);" />
</p>

<p align="center">
  <strong>Aplicação interativa Java Swing com persistência CRUD em arquivos e inteligência artificial real integrada via Google Gemini API.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-JDK_8+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java JDK 8+" />
  <img src="https://img.shields.io/badge/Interface-Java_Swing-007396?style=for-the-badge" alt="Java Swing" />
  <img src="https://img.shields.io/badge/Inteligência_Artificial-Google_Gemini-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Gemini API" />
  <img src="https://img.shields.io/badge/Persistência-TXT_CRUD-FF6B35?style=for-the-badge" alt="Persistência TXT" />
</p>

---

## Índice de Tópicos

* [Sobre o Projeto](#sobre-o-projeto)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Funcionalidades de Destaque](#funcionalidades-de-destaque)
* [Estrutura do Projeto](#estrutura-do-projeto)
* [Sistema de Emoções](#sistema-de-emoções)
* [Como Compilar e Executar](#como-compilar-e-executar)
* [Como Usar](#como-usar)
* [Formato de Persistência](#formato-de-persistência)
* [Validações e Tratamento de Erros](#validações-e-tratamento-de-erros)
* [Autor](#autor)

---

## Sobre o Projeto

O **Grimório Digital do Mago Merlin** é um sistema completo desenvolvido em Java que simula uma interface interativa de diálogo com o personagem Mago Merlin (baseado no universo de lendas e RPG de fantasia). O projeto vai além de um simples chat: ele incorpora uma lógica CRUD completa (Create, Read, Update, Delete) que persiste as consultas mágicas em arquivos de texto locais, valida as entradas e faz consultas de inteligência artificial reais integrando-se com a **Google Gemini API**.

Durante as interações, a inteligência artificial gera respostas contextualizadas com base na categoria escolhida (como feitiços ou batalhas), e analisa o tom das perguntas para determinar de forma dinâmica as expressões e emoções do Mago Merlin, alterando sua imagem na interface gráfica (Swing).

---

## Tecnologias Utilizadas

* **Java Standard Edition (JDK 8+)**: Linguagem principal e infraestrutura de execução estável e portável.
* **Java Swing**: Framework integrado de componentes para construção de uma interface rica, responsiva e customizada.
* **Google Gemini API**: IA generativa integrada via requisições HTTP para obter respostas inteligentes e mapeamento de emoções do mago.
* **File I/O (Persistência)**: Leitura e escrita otimizada no arquivo local `grimorio_merlin.txt` garantindo a persistência completa do histórico.

---

## Funcionalidades de Destaque

* **Interface Gráfica Temática**: Design e painéis em tons de roxo/azul escuro remetendo a uma atmosfera mística, com terminal de log com fundo preto e texto verde clássico.
* **Integração Real com IA**: Classe de inteligência artificial que consome a API do Gemini com prompts contextuais especializados para o personagem.
* **Sistema de Emoções**: O mago expressa sentimentos (Feliz, Sábio, Irritado, Misterioso e Neutro) atualizando o seu retrato em tempo real com base no sentimento detectado na resposta gerada.
* **Persistência CRUD Robusta**:
  * **Create**: Criação de novas consultas e perguntas associadas a categorias específicas.
  * **Read**: Exibição em tempo real na tabela de logs com suporte a ordenações e atualizações automáticas.
  * **Update**: Edição de qualquer registro inserido diretamente selecionando a linha na tabela.
  * **Delete**: Exclusão definitiva de registros com caixa de diálogo de confirmação.
* **Modo de Backup (Fallback Offline)**: Caso a conexão com a API do Google Gemini falhe ou esteja indisponível, o sistema ativa um simulador offline que responde de forma textual e gerencia as emoções locais.

---

## Estrutura do Projeto

Abaixo está o mapeamento dos arquivos que compõem o repositório estruturado:

```
MerlinGuru/
├── build/                 # Diretório de compilação local (ignorado pelo Git)
├── resources/             # Mídias e assets do projeto
│   └── Img/
│       ├── Cenario/
│       │   └── Fundo.png
│       └── Merlin/
│           ├── Bravo.png  # Merlin com emoção irritado
│           ├── Normal.png # Merlin com emoção neutro/sábio
│           └── Rindo.png  # Merlin com emoção feliz/rindo
├── src/                   # Arquivos de código-fonte Java
│   ├── Akinator.java
│   ├── AkinatorAPI.java
│   ├── DiagnosticoAPI.java
│   ├── GrimorioDAO.java   # Responsável pelo controle CRUD no arquivo TXT
│   ├── Inteligencia.java  # Gerenciamento da API Gemini e fallbacks
│   ├── MerlinGUI.java     # Tela principal gráfica construída em Swing
│   ├── MyInterface.java   # Interface com contratos do sistema
│   ├── RegistroMagico.java# Modelo de dados da consulta
│   └── jogo.java          # Lógica auxiliar de controle
├── .gitignore             # Arquivos ignorados no versionamento
├── grimorio_exemplo.txt   # Amostra de histórico de consultas
├── grimorio_merlin.txt    # Arquivo de persistência (gerado automaticamente)
├── MerlinGuru.bat         # Script automatizado de compilação/execução no Windows
├── O que e o jogo.txt     # Descritivo conceptual do jogo
├── Roteiro                # Roteiro auxiliar de testes e apresentação do projeto
└── README.md              # Documentação principal
```

---

## Sistema de Emoções

O comportamento gráfico do Merlin reage às sentenças analisadas cromática e semânticamente pela IA:

* **FELIZ**: Acionado por palavras de agradecimento, elogios ou respostas leves (Exibe a imagem Rindo.png).
* **SABIO**: Acionado por perguntas teóricas sobre magia, feitiços ou explicações complexas (Exibe a imagem Normal.png).
* **IRRITADO**: Acionado por termos chulos, ofensas ou perguntas que insultem o mago (Exibe a imagem Bravo.png).
* **MISTERIOSO**: Acionado por lendas, criaturas mitológicas ou dragões (Exibe a imagem Normal.png).
* **NEUTRO**: Estado padrão para saudações gerais e perguntas iniciais (Exibe a imagem Normal.png).

---

## Como Compilar e Executar

### Pré-requisitos
* Java Development Kit (JDK) 8 ou superior instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
* Terminal de comando (Prompt de Comando ou PowerShell).

### Procedimento de Compilação
1. Abra o terminal e navegue até a raiz do projeto:
   ```bash
   cd MerlinGuru-main
   ```
2. Compile as classes Java a partir da pasta de código-fonte:
   ```bash
   javac -d build src/*.java
   ```

### Execução da Aplicação
Após compilar, você pode rodar o programa apontando para o classpath da pasta de build:
```bash
java -cp build MerlinGUI
```

> **Dica para Windows**: Você também pode executar a compilação e abertura automática clicando duas vezes no script executável [MerlinGuru.bat](MerlinGuru.bat).

---

## Como Usar

1. **Abrir o Grimório**: Execute o programa. A interface gráfica com o cenário de Merlin será exibida.
2. **Formular Pergunta (Create)**:
   * Digite a sua dúvida ou conversa no campo de pergunta.
   * Selecione a categoria apropriada (Feitiços, Criaturas, Batalha, História ou Outros).
   * Clique em **Consultar Merlin**. A resposta gerada pela API do Gemini e a emoção detectada aparecerão no chat e a tabela de histórico abaixo registrará a consulta.
3. **Atualizar Histórico (Read)**: A tabela na área inferior carrega os dados diretamente do arquivo TXT. Clique em **Atualizar Lista** para reordenar ou recarregar os dados.
4. **Editar Consulta (Update)**:
   * Selecione a linha que deseja alterar na tabela.
   * Clique em **Editar Selecionada**.
   * Insira a nova pergunta ou altere a categoria e salve.
5. **Apagar Registro (Delete)**:
   * Selecione a linha na tabela.
   * Clique em **Excluir Selecionada** e confirme o aviso de confirmação.

---

## Formato de Persistência

Os dados do CRUD são salvos localmente na raiz do projeto no arquivo `grimorio_merlin.txt` utilizando delimitadores de barra vertical (pipe) para leitura eficiente:

```
# GRIMÓRIO DIGITAL DO MAGO MERLIN
# Formato: ID|PERGUNTA|RESPOSTA|CATEGORIA|EMOÇÃO|DATA
1|Como fazer uma poção de cura?|Ah, jovem aprendiz! Esse feitiço requer muita concentração.|FEITICOS|SABIO|20/08/2025 14:30:15
2|Onde vivem os dragões?|Nas montanhas mais distantes, onde o fogo e a pedra se encontram.|CRIATURAS|MISTERIOSO|20/08/2025 14:35:22
```

---

## Validações e Tratamento de Erros

* **Validação de Entrada**: O formulário impede o envio de perguntas vazias, campos contendo apenas espaços em branco ou categorias inválidas.
* **Segurança de Ações CRUD**: O sistema exige a seleção de um registro na tabela e confirmação por janela de diálogo antes de prosseguir com alterações ou exclusões.
* **Integridade do Histórico**: A manipulação de arquivos possui mecanismos de tratamento (`try-catch`) para evitar que dados sejam perdidos ou corrompidos em caso de falha de gravação ou falta de permissão de escrita.

---

## Autor

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/ThyagoToledo">
        <img src="https://github.com/ThyagoToledo.png" width="100px;" alt="Thyago Toledo"/>
        <br />
        <sub><b>Thyago Toledo</b></sub>
      </a>
    </td>
  </tr>
</table>
