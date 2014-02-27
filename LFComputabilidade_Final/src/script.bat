@ Echo off
echo -------------------------------------------------------------
echo Compilacao Trabalho 1 de Linguagens Formais e Computabilidade
echo -------------------------------------------------------------
javac ExpressaoRegular.java
echo Compilacao completada.
pause
Set /p arq1=Digite o caminho completo do arquivo de entrada:
Set /p arq2=Digite o caminho completo do arquivo de saida:
java ExpressaoRegular "%arq1%" "%arq2%"
pause
