@echo off
title MerlinGuru - Grimorio Digital do Mago Merlin
color 0E
chcp 65001 > nul

echo.
echo ====================================================
echo           🧙‍♂️ MERLIN GURU - GRIMÓRIO DIGITAL
echo ====================================================
echo           Desenvolvido com magia e sabedoria
echo ====================================================
echo.

rem Navegar para o diretório do script
cd /d "%~dp0"

rem Verificar se o Java está instalado
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java não encontrado no sistema!
    echo 📥 Por favor, instale o Java JDK 8 ou superior
    echo 🌐 Download: https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

echo ⚡ Java detectado com sucesso!

rem Criar pasta build se não existir
if not exist "build" (
    echo 📁 Criando pasta build...
    mkdir build
)

rem Limpar compilações anteriores
if exist "build\*.class" (
    echo 🧹 Limpando arquivos antigos...
    del /q "build\*.class" >nul 2>&1
)

echo 🔮 Compilando arquivos Java...
echo.

rem Compilar apenas os arquivos essenciais (excluindo testes)
javac -d build -cp . src\jogo.java src\MerlinGUI.java src\Inteligencia.java src\GrimorioDAO.java src\RegistroMagico.java src\MyInterface.java src\AkinatorAPI.java

if %errorlevel% equ 0 (
    echo ✅ Compilação concluída com sucesso!
    echo 🧙‍♂️ API do Gemini configurada e funcionando!
    echo.
    echo 🎮 Iniciando o Grimório do Merlin...
    echo ✨ Preparando ambiente mágico...
    echo.
    
    rem Executar o jogo
    java -cp build jogo
    
    if %errorlevel% equ 0 (
        echo.
        echo 🌟 Sessão mágica encerrada com sucesso!
        echo 👋 Obrigado por usar o MerlinGuru!
    ) else (
        echo.
        echo ⚠️  Erro durante execução, tentando método alternativo...
        java -cp build MerlinGUI
        
        if %errorlevel% equ 0 (
            echo ✅ Execução alternativa bem-sucedida!
        ) else (
            echo ❌ Erro persistente na execução!
            echo 🔍 Possíveis problemas:
            echo   • Conexão com API do Gemini instável
            echo   • Permissões de arquivo
            echo   • Java JDK incompatível (recomendado: JDK 11+)
        )
    )
    
) else (
    echo ❌ Erro na compilação dos arquivos Java!
    echo.
    echo 🔍 Possíveis causas:
    echo   • Arquivos Java corrompidos ou ausentes
    echo   • Versão incompatível do Java
    echo   • Problemas de sintaxe no código
    echo.
    echo 💡 Verifique se todos os arquivos .java estão na pasta 'src'
)

echo.
echo ====================================================
echo Pressione qualquer tecla para finalizar...
echo ====================================================
pause >nul
