# 🧙‍♂️ Grimório Digital do Mago Merlin

## 📋 Descrição do Projeto

O **Grimório Digital do Mago Merlin** é uma aplicação Java que combina:
- 🎮 **Jogo interativo**: Conversa com o Mago Merlin sobre RPG, magia e fantasia
- 💾 **Sistema CRUD**: Gerenciamento completo de consultas em arquivo TXT
- 🖥️ **Interface gráfica**: GUI moderna usando Swing
- 🤖 **IA Real**: Integração com Google Gemini API para respostas inteligentes

## 🎯 Requisitos Atendidos

### ✅ Funcionalidades Obrigatórias
- [x] Interface Gráfica (Swing)
- [x] CRUD completo (Create, Read, Update, Delete)
- [x] Persistência em arquivo de texto
- [x] Validação de entradas
- [x] Interface intuitiva e organizada

### ✅ Tema Criativo
- Grimório de consultas mágicas
- Personagem carismático (Mago Merlin)
- Sistema de emoções e reações
- Categorização temática

## 🏗️ Estrutura do Projeto

```
Java/
├── MyInterface.java          # Interface principal do sistema
├── MerlinGUI.java           # Interface gráfica principal
├── RegistroMagico.java      # Modelo de dados
├── GrimorioDAO.java         # Manipulação do arquivo TXT
├── Inteligencia.java        # Integração com Google Gemini API
├── MerlinSimulator.java     # Simulador offline (backup)
├── grimorio_merlin.txt      # Arquivo de persistência (gerado automaticamente)
├── Fundo.png               # Imagem de fundo
├── Merlin.png              # Imagem do Merlin
└── README.md               # Este arquivo
```

## 🚀 Como Compilar e Executar

### Pré-requisitos
- Java Development Kit (JDK) 8 ou superior
- Sistema operacional: Windows, Linux ou macOS

### Compilação
```bash
# Navegar até o diretório do projeto
cd "c:\Users\aluno.LabLenovo06\Desktop\dev\Java"

# Compilar todos os arquivos Java
javac *.java
```

### Execução
```bash
# Executar o programa principal
java MerlinGUI
```

## 🎮 Como Usar

### 1. Iniciando o Programa
- Execute `java MerlinGUI`
- A interface será aberta em tela cheia
- Merlin aparecerá com emoção NEUTRO inicialmente

### 2. Fazendo uma Consulta (CREATE)
1. Digite sua pergunta no campo "Pergunta"
2. Selecione uma categoria:
   - **FEITICOS**: Perguntas sobre magias e encantamentos
   - **CRIATURAS**: Perguntas sobre monstros e seres mágicos
   - **BATALHA**: Perguntas sobre combate e estratégia
   - **HISTORIA**: Perguntas sobre lendas e eventos antigos
   - **OUTROS**: Perguntas gerais
3. Clique em "🔮 Consultar Merlin"
4. Aguarde a resposta (2-5 segundos da API Google Gemini)
5. A consulta será salva automaticamente no grimório

### 🤖 **Integração com IA Real**
O projeto utiliza a **classe Inteligencia** que integra com a **Google Gemini API**:
- **Respostas Inteligentes**: IA real do Google gera respostas contextuais
- **Prompts Personalizados**: Cada categoria tem contexto específico para Merlin
- **Detecção de Emoções**: IA indica automaticamente a emoção do Mago
- **Sistema de Backup**: Fallback offline se a API estiver indisponível

### 3. Visualizando Consultas (READ)
- Todas as consultas aparecem na tabela inferior
- Informações mostradas: ID, Pergunta, Categoria, Emoção, Data
- Use "🔄 Atualizar Lista" para recarregar

### 4. Editando Consultas (UPDATE)
1. Selecione uma linha na tabela
2. Clique em "✏️ Editar Selecionada"
3. Modifique a pergunta e/ou categoria
4. Confirme as alterações

### 5. Excluindo Consultas (DELETE)
1. Selecione uma linha na tabela
2. Clique em "🗑️ Excluir Selecionada"
3. Confirme a exclusão

## 📁 Formato do Arquivo TXT

O arquivo `grimorio_merlin.txt` é gerado automaticamente com o formato:

```
# GRIMÓRIO DIGITAL DO MAGO MERLIN
# Formato: ID|PERGUNTA|RESPOSTA|CATEGORIA|EMOÇÃO|DATA
# Categorias: FEITICOS, CRIATURAS, BATALHA, HISTORIA, OUTROS
# Emoções: FELIZ, SABIO, IRRITADO, MISTERIOSO, NEUTRO

1|Como fazer uma poção de cura?|Ah, jovem aprendiz! Esse feitiço requer muita concentração e uma pitada de pó de estrelas!|FEITICOS|SABIO|20/08/2025 14:30:15
2|Que criatura é o dragão?|Essa criatura é fascinante! São conhecidas por sua inteligência aguçada.|CRIATURAS|MISTERIOSO|20/08/2025 14:35:22
```

## 🎭 Sistema de Emoções

O Merlin reage às perguntas com diferentes emoções:

- 😊 **FELIZ**: Quando agradecido ou satisfeito
- 🤔 **SABIO**: Para perguntas complexas sobre feitiços
- 😠 **IRRITADO**: Para perguntas ofensivas
- 🔮 **MISTERIOSO**: Para assuntos históricos ou criaturas
- 😐 **NEUTRO**: Estado padrão

## 🛠️ Validações Implementadas

### Entrada de Dados
- ❌ Pergunta não pode estar vazia
- ❌ Categoria deve ser uma das opções válidas
- ❌ Campos não podem conter apenas espaços

### Operações CRUD
- ✅ Verificação de ID existente antes de editar/excluir
- ✅ Confirmação antes de excluir registros
- ✅ Tratamento de erros de arquivo
- ✅ Validação de formato do arquivo TXT

## 🎨 Características Visuais

### Tema Mágico
- 🌌 Fundo azul escuro (cor mágica)
- 💜 Painéis em tons de roxo
- ✨ Textos dourados para destaque
- 🖥️ Chat estilo terminal (fundo preto, texto verde)

### Organização
- 📱 Interface responsiva
- 🔄 Atualização automática da tabela
- ⏳ Indicadores de carregamento
- 📊 Tabela com scroll automático

## 🚨 Tratamento de Erros

### Erros Comuns e Soluções
1. **Erro de compilação**: Verifique se o JDK está instalado
2. **Arquivo não encontrado**: O arquivo TXT é criado automaticamente
3. **Interface não abre**: Verifique se há suporte ao Swing
4. **Dados não salvam**: Verifique permissões de escrita na pasta

### Log de Erros
- Erros são exibidos em pop-ups informativos
- Console mostra detalhes técnicos
- Sistema continua funcionando mesmo com erros pontuais

## 🎬 Demonstração para Vídeo

### Roteiro Sugerido (5+ minutos)

1. **Introdução** (30s)
   - "Bem-vindos ao Grimório Digital do Mago Merlin!"
   - Explicar o conceito: chat + CRUD + arquivo TXT

2. **Tour da Interface** (60s)
   - Mostrar área de chat
   - Explicar formulário de consulta
   - Apresentar tabela de histórico

3. **Demonstração CREATE** (90s)
   - Fazer pergunta sobre feitiços
   - Mostrar resposta do Merlin e mudança de emoção
   - Verificar registro na tabela

4. **Demonstração READ** (30s)
   - Mostrar histórico na tabela
   - Usar botão atualizar

5. **Demonstração UPDATE** (60s)
   - Editar uma consulta existente
   - Mostrar alteração na tabela

6. **Demonstração DELETE** (30s)
   - Excluir uma consulta
   - Confirmar remoção

7. **Arquivo TXT** (90s)
   - Abrir arquivo no Bloco de Notas
   - Explicar formato dos dados
   - Mostrar como dados persistem

8. **Elementos Criativos** (60s)
   - Diferentes emoções do Merlin
   - Categorias temáticas
   - Interface mágica

## 👥 Créditos

- **Desenvolvedor**: [Seu Nome]
- **Disciplina**: Programação Java
- **Tema**: Sistema CRUD com Interface Gráfica
- **Personagem**: Mago Merlin (inspiração fantástica)

---

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique se todos os arquivos .java estão no mesmo diretório
2. Confirme que o JDK está corretamente instalado
3. Execute sempre a partir do diretório do projeto

**🧙‍♂️ Que a magia do código esteja com você!**
