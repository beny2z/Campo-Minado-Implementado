package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Model do Campo Minado, mantendo os construtores originais e adicionando variantes opcionais. */
public class Tabuleiro implements LeituraTabuleiro, Serializable {
    private static final long serialVersionUID = 1L;
    private final int linhas, colunas, numMinas;
    private final Celula[][] grade;
    private boolean jogoEncerrado, derrota;
    private int vidas = 1;
    private boolean semCascata, toroidal, minasVisiveis, modoPergunta;
    private int revelacoesRelampago;

    public Tabuleiro(int linhas, int colunas, int numMinas) { this(linhas, colunas, numMinas, new Random().nextLong()); }
    public Tabuleiro(int linhas, int colunas, int numMinas, long seed) {
        validar(linhas, colunas, numMinas); this.linhas=linhas; this.colunas=colunas; this.numMinas=numMinas; grade=new Celula[linhas][colunas]; inicializar();
        Random r=new Random(seed); int n=0; while(n<numMinas){ int i=r.nextInt(linhas), j=r.nextInt(colunas); if(!grade[i][j].isMinada()){grade[i][j].setMinada(true);n++;} } calcular();
    }
    public Tabuleiro(int linhas, int colunas, int[][] posicoesMinas) {
        validar(linhas,colunas,posicoesMinas.length); this.linhas=linhas;this.colunas=colunas;numMinas=posicoesMinas.length;grade=new Celula[linhas][colunas];inicializar();
        for(int[] p:posicoesMinas) if(dentro(p[0],p[1])) grade[p[0]][p[1]].setMinada(true); calcular();
    }
    private static void validar(int l,int c,int m){ if(l<=0||c<=0||m<0||m>=l*c) throw new IllegalArgumentException("Dimensões ou minas inválidas."); }
    private void inicializar(){for(int i=0;i<linhas;i++)for(int j=0;j<colunas;j++)grade[i][j]=new Celula();}
    private void calcular(){for(int i=0;i<linhas;i++)for(int j=0;j<colunas;j++)grade[i][j].setMinasVizinhas(contar(i,j));}
    private int[] normalizar(int i,int j){ if(toroidal)return new int[]{(i+linhas)%linhas,(j+colunas)%colunas}; return dentro(i,j)?new int[]{i,j}:null; }
    private boolean dentro(int i,int j){return i>=0&&i<linhas&&j>=0&&j<colunas;}
    private int contar(int i,int j){int n=0;for(int di=-1;di<=1;di++)for(int dj=-1;dj<=1;dj++)if(di!=0||dj!=0){int[]p=normalizar(i+di,j+dj);if(p!=null&&grade[p[0]][p[1]].isMinada())n++;}return n;}
    public void setSemCascata(boolean v){semCascata=v;} public boolean isSemCascata(){return semCascata;}
    public void setToroidal(boolean v){toroidal=v;calcular();} public boolean isToroidal(){return toroidal;}
    public void setMinasVisiveis(boolean v){minasVisiveis=v;} public boolean isMinasVisiveis(){return minasVisiveis;}
    public void setModoPergunta(boolean v){modoPergunta=v;}
    public void setVidas(int quantidade){vidas=Math.max(1, quantidade);}
    public int getVidas(){return vidas;}
    public int revelarRelampago(int quantidade){int feitos=0;Random r=new Random();while(feitos<quantidade){int i=r.nextInt(linhas),j=r.nextInt(colunas);if(!grade[i][j].isMinada()&&!grade[i][j].isRevelada()){grade[i][j].revelar();feitos++;}}return feitos;}
    public List<int[]> revelar(int linha,int coluna){List<int[]> ordem=new ArrayList<>();if(jogoEncerrado||!dentro(linha,coluna))return ordem;Celula inicial=grade[linha][coluna];if(inicial.isRevelada()||inicial.isMarcada()||inicial.isQuestionada())return ordem;if(inicial.isMinada()){if(vidas>1){vidas--;return ordem;}inicial.revelar();ordem.add(new int[]{linha,coluna});jogoEncerrado=true;derrota=true;return ordem;}
        List<int[]> fila=new ArrayList<>();fila.add(new int[]{linha,coluna});while(!fila.isEmpty()){int[]p=fila.remove(fila.size()-1);Celula c=grade[p[0]][p[1]];if(c.isRevelada()||c.isMarcada()||c.isQuestionada()||c.isMinada())continue;c.revelar();ordem.add(p);if(!semCascata&&c.getMinasVizinhas()==0)for(int di=-1;di<=1;di++)for(int dj=-1;dj<=1;dj++)if(di!=0||dj!=0){int[]q=normalizar(p[0]+di,p[1]+dj);if(q!=null)fila.add(q);}}
        if(verificarVitoria())jogoEncerrado=true;return ordem; }
    public List<int[]> chord(int linha,int coluna){List<int[]>r=new ArrayList<>();if(!dentro(linha,coluna)||!grade[linha][coluna].isRevelada())return r;int f=0;for(int[]p:vizinhos(linha,coluna))if(grade[p[0]][p[1]].isMarcada())f++;if(f==grade[linha][coluna].getMinasVizinhas())for(int[]p:vizinhos(linha,coluna))r.addAll(revelar(p[0],p[1]));return r;}
    private List<int[]> vizinhos(int i,int j){List<int[]>r=new ArrayList<>();for(int di=-1;di<=1;di++)for(int dj=-1;dj<=1;dj++)if(di!=0||dj!=0){int[]p=normalizar(i+di,j+dj);if(p!=null)r.add(p);}return r;}
    public void alternarMarcacao(int i,int j){if(!jogoEncerrado&&dentro(i,j)){if(modoPergunta)grade[i][j].alternarEstadoMarcacao();else grade[i][j].alternarMarcacao();}}
    public boolean verificarVitoria(){for(int i=0;i<linhas;i++)for(int j=0;j<colunas;j++)if(!grade[i][j].isMinada()&&!grade[i][j].isRevelada())return false;return true;}
    public boolean isDerrota(){return derrota;} public boolean isJogoEncerrado(){return jogoEncerrado;} public int getLinhas(){return linhas;} public int getColunas(){return colunas;} public int getNumMinas(){return numMinas;}
    public boolean isRevelada(int i,int j){return grade[i][j].isRevelada();} public boolean isMarcada(int i,int j){return grade[i][j].isMarcada();} public boolean isQuestionada(int i,int j){return grade[i][j].isQuestionada();} public boolean isMinada(int i,int j){return grade[i][j].isMinada();} public int getMinasVizinhas(int i,int j){return grade[i][j].getMinasVizinhas();} public Celula getCelula(int i,int j){return grade[i][j];}
    public void revelarTodasMinas(){for(Celula[]l:grade)for(Celula c:l)if(c.isMinada())c.revelar();}
    public void encerrarPorDerrota(){jogoEncerrado=true;derrota=true;revelarTodasMinas();}
    public void imprimir(boolean tudo){for(int i=0;i<linhas;i++){for(int j=0;j<colunas;j++)System.out.print((tudo&&grade[i][j].isMinada()?"*":grade[i][j])+" ");System.out.println();}}
}
