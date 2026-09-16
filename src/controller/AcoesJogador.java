package controller;

/** Eventos entre View e Controller. Os métodos default preservam compatibilidade com a API original. */
public interface AcoesJogador {
    void aoEscolherDificuldade(int linhas,int colunas,int minas);
    void aoRevelarCelula(int linha,int coluna);
    void aoMarcarCelula(int linha,int coluna);
    default void aoUsarChording(int linha,int coluna) { }
    void aoPedirNovoJogo();
    default void aoPedirDica() { }
    default void aoAlternarModo(String modo) { }
    default void aoSalvarPartida() { }
    default void aoRetomarPartida() { }
    default void aoExportarEstatisticas() { }
}
