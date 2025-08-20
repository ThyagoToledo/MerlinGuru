@echo off
echo =====================================================
echo 🧙‍♂️ GRIMÓRIO DIGITAL DO MAGO MERLIN 🧙‍♂️
echo =====================================================
echo.
echo Compilando arquivos Java...
javac *.java

if %ERRORLEVEL% EQU 0 (
    echo ✅ Compilação bem-sucedida!
    echo.
    echo Iniciando o Grimório do Merlin...
    echo.
    java MerlinGUI
) else (
    echo ❌ Erro na compilação!
    echo Verifique se o JDK está instalado corretamente.
)

echo.
echo Pressione qualquer tecla para sair...
pause > nul
