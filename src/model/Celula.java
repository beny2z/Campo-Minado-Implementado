package model;

import java.io.Serializable;

/** Estado de uma célula do Campo Minado. */
public class Celula implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean minada;
    private boolean revelada;
    private boolean marcada;
    private boolean questionada;
    private int minasVizinhas;

    public Celula() { }
    public boolean isMinada() { return minada; }
    public void setMinada(boolean valor) { minada = valor; }
    public boolean isRevelada() { return revelada; }
    public void revelar() { if (!marcada && !questionada) revelada = true; }
    public boolean isMarcada() { return marcada; }
    public boolean isQuestionada() { return questionada; }
    public void alternarMarcacao() { if (!revelada) { marcada = !marcada; if (marcada) questionada = false; } }
    public void alternarEstadoMarcacao() { if (!revelada) { if (!marcada && !questionada) marcada = true; else if (marcada) { marcada = false; questionada = true; } else questionada = false; } }
    public void setQuestionada(boolean valor) { if (!revelada && !marcada) questionada = valor; }
    public int getMinasVizinhas() { return minasVizinhas; }
    public void setMinasVizinhas(int valor) { minasVizinhas = valor; }
    @Override public String toString() { if (marcada) return "F"; if (questionada) return "?"; if (!revelada) return "."; if (minada) return "*"; return minasVizinhas == 0 ? " " : String.valueOf(minasVizinhas); }
}
