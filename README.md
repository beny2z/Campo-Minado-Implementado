# Campo Minado

Este projeto é uma versão em Java do jogo Campo Minado com interface gráfica Swing e arquitetura MVC. A estrutura original foi preservada: `src/main` continua contendo os mesmos pontos de entrada e o comando de compilação não foi alterado.

## Funcionalidades implementadas

Além das dificuldades padrão, o jogo inclui temas escuro, claro, campo, terminal retrô, cyberpunk neon, Halloween, oceano tropical, alto contraste e automático; skins de bandeira; marcação em três estados (bandeira, interrogação e vazio); dificuldade personalizada; modo sem cascata; minas visíveis; modo relâmpago; bordas toroidais; dicas limitadas; cronômetro regressivo; aviso de excesso de bandeiras; zoom e atalhos/acessibilidade de teclado; animações de explosão e vitória; recordes por dificuldade; histórico em CSV; perfis compatíveis com o histórico; configurações persistentes; salvamento e retomada serializada; exportação de estatísticas; tela cheia; e feedback sonoro simples.

O Model também fornece as operações `chord`, `revelarRelampago`, `revelarTodasMinas`, `setSemCascata`, `setToroidal`, `setMinasVisiveis` e `setModoPergunta`, permitindo testes e futuras extensões sem acoplar a View.

## Estrutura

- `src/main` — classes principais de execução.
- `src/controller` — Controller, recordes e utilidades de persistência.
- `src/view` — interface gráfica Swing, temas, acessibilidade e feedback.
- `src/model` — `Celula`, `Tabuleiro` e contrato de leitura.
- `src/test` — testes unitários existentes.

## Compilação

A partir da pasta do projeto:

```powershell
javac src\main\*.java src\controller\*.java src\view\*.java src\model\*.java
```

Em Linux, o mesmo comando pode ser escrito com barras normais:

```bash
javac src/main/*.java src/controller/*.java src/view/*.java src/model/*.java
```

## Execução

```powershell
java -cp src main.JogoCampoMinadoGUI
java -cp src main.JogoCampoMinado
```

Os dados do usuário são gravados em `~/.campo-minado/`, os recordes no arquivo original `campo_minado_recordes.properties` e a partida atual em `.campo-minado-partida.ser` dentro da pasta pessoal.
