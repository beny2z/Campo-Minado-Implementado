# Campo Minado

Este projeto é uma versão em Java do jogo Campo Minado com interface gráfica Swing e arquitetura MVC.

## Estrutura do projeto

- `src/main` - classes principais de execução.
  - `JogoCampoMinadoGUI.java` - entrada do jogo em modo gráfico.
  - `JogoCampoMinado.java` - alternativa de execução em console.
- `src/controller` - controlador MVC.
  - `CampoMinadoController.java` - lógica de jogo e sincronização entre View e Model.
  - `AcoesJogador.java` - interface de ações disparadas pela View.
- `src/view` - camada de interface gráfica.
  - `CampoMinadoView.java` - tela do jogo, tutorial, tema e estatísticas.
- `src/model` - modelo de domínio do jogo.
  - `Tabuleiro.java` - lógica do tabuleiro, minas, revelação e vitória.
  - `Celula.java` - estado de cada célula do tabuleiro.
  - `LeituraTabuleiro.java` - interface de leitura do estado do tabuleiro.
- `src/test` - testes unitários.
  - `CampoMinadoTest.java`

## Funcionalidades

- Escolha de dificuldade: Iniciante, Intermediário e Avançado.
- Estatísticas atualizadas em tempo real: tempo, minas restantes, células reveladas e jogadas.
- Tema de cores para o plano de fundo e tabuleiro.
- Tutorial integrado com instruções de jogo.
- Tempo limite selecionável (até 5 minutos) como modo rápido.
- Arquitetura MVC organizada em pastas.

## Compilação

Execute no terminal a partir da pasta do projeto:

```powershell
javac src\main\*.java src\controller\*.java src\view\*.java src\model\*.java
```

## Execução

Para iniciar a interface gráfica:

```powershell
java -cp src main.JogoCampoMinadoGUI
```

Para executar a versão em console:

```powershell
java -cp src main.JogoCampoMinado
```

## Testes

Se você tiver o JUnit configurado, execute os testes em `src/test/CampoMinadoTest.java` com seu ambiente de testes Java.

## Observações

- A interface gráfica usa Swing e respeita cores personalizadas graças ao LookAndFeel cross-platform.
- Antes de executar, certifique-se de compilar todos os arquivos do diretório `src`.
