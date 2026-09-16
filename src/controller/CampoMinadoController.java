package controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.Timer;
import model.Tabuleiro;
import view.CampoMinadoView;

/** Controller central: preserva o fluxo MVC original e concentra as novas opções. */
public class CampoMinadoController implements AcoesJogador {
    private final GerenciadorRecordes gerenciadorRecordes=new GerenciadorRecordes();
    private final CampoMinadoView view;
    private Tabuleiro tabuleiro; private String dificuldadeAtual="Personalizado", perfil="Jogador";
    private int totalMinas,totalCelulas,celulasReveladas,jogadas,vidas=3,dicasRestantes=3; private boolean jogoIniciado;
    private long tempoInicio; private int limiteSegundos; private Timer timerJogo;
    private static final Path SAVE=Paths.get(System.getProperty("user.home"),".campo-minado-partida.ser");
    public CampoMinadoController(CampoMinadoView v){view=v;v.setOuvinte(this);}
    public void iniciar(){view.mostrarTelaInicial();view.setVisible(true);}
    private String identificar(int l,int c,int m){if(l==9&&c==9&&m==10)return "Iniciante";if(l==16&&c==16&&m==40)return "Intermediário";if(l==16&&c==30&&m==99)return "Avançado";return "Personalizado";}
    @Override public void aoEscolherDificuldade(int l,int c,int m){novoTabuleiro(new Tabuleiro(l,c,m),l,c,m);}
    private void novoTabuleiro(Tabuleiro t,int l,int c,int m){pararTimer();tabuleiro=t;totalMinas=m;totalCelulas=l*c-m;celulasReveladas=0;jogadas=0;vidas=3;dicasRestantes=3;jogoIniciado=false;dificuldadeAtual=identificar(l,c,m);t.setVidas(3);t.setSemCascata(view.isModoSemCascata());t.setToroidal(view.isModoToroidal());t.setModoPergunta(view.isModoPergunta());t.setMinasVisiveis(view.isModoMinasVisiveis());if(view.isModoRelampago())t.revelarRelampago(Math.max(1,(l*c)/30));celulasReveladas=contarReveladas();view.aplicarTemaSelecionado();limiteSegundos=view.getTempoLimiteSegundosSelecionado();view.iniciarTelaDeJogo(l,c,m,totalCelulas,limiteSegundos);view.atualizarTabuleiro(t);view.atualizarEstatisticas(m,celulasReveladas,totalCelulas,0);}
    @Override public void aoPedirNovoJogo(){pararTimer();view.mostrarTelaInicial();}
    @Override public void aoMarcarCelula(int l,int c){if(tabuleiro==null||tabuleiro.isJogoEncerrado())return;tabuleiro.alternarMarcacao(l,c);view.atualizarCelula(l,c,tabuleiro);atualizar();UtilidadesJogo.beep();}
    @Override public void aoUsarChording(int l,int c){if(tabuleiro==null||tabuleiro.isJogoEncerrado())return;List<int[]> reveladas=tabuleiro.chord(l,c);celulasReveladas=contarReveladas();animar(reveladas,0,12);}
    @Override public void aoRevelarCelula(int l,int c){if(tabuleiro==null||tabuleiro.isJogoEncerrado())return;if(!jogoIniciado){jogoIniciado=true;tempoInicio=System.currentTimeMillis();iniciarTimer();}if(limiteSegundos>0&&passados()>=limiteSegundos){encerrarPorTempo();return;}jogadas++;List<int[]> reveladas=tabuleiro.revelar(l,c);celulasReveladas=contarReveladas();animar(reveladas,0,reveladas.size()>50?3:12);UtilidadesJogo.beep();}
    private void animar(List<int[]>l,int i,int atraso){if(i>=l.size()){finalizar();return;}int[]p=l.get(i);view.atualizarCelula(p[0],p[1],tabuleiro);Timer t=new Timer(atraso,e->animar(l,i+1,atraso));t.setRepeats(false);t.start();}
    @Override public void aoPedirDica(){if(dicasRestantes<=0||tabuleiro==null||tabuleiro.isJogoEncerrado())return;for(int i=0;i<tabuleiro.getLinhas();i++)for(int j=0;j<tabuleiro.getColunas();j++)if(!tabuleiro.isMinada(i,j)&&!tabuleiro.isRevelada(i,j)){dicasRestantes--;aoRevelarCelula(i,j);return;}}
    @Override public void aoAlternarModo(String modo){if("tela-cheia".equals(modo))view.alternarTelaCheia();}
    @Override public void aoSalvarPartida(){if(tabuleiro==null)return;try{Files.createDirectories(SAVE.getParent());try(ObjectOutputStream o=new ObjectOutputStream(Files.newOutputStream(SAVE))){o.writeObject(tabuleiro);o.writeObject(dificuldadeAtual);o.writeInt(jogadas);o.writeLong(jogoIniciado?passados():0);}}catch(IOException ignored){}}
    @Override public void aoRetomarPartida(){try(ObjectInputStream in=new ObjectInputStream(Files.newInputStream(SAVE))){Tabuleiro t=(Tabuleiro)in.readObject();String d=(String)in.readObject();int j=in.readInt();long s=in.readLong();novoTabuleiro(t,t.getLinhas(),t.getColunas(),t.getNumMinas());dificuldadeAtual=d;jogadas=j;if(s>0){jogoIniciado=true;tempoInicio=System.currentTimeMillis()-s*1000;iniciarTimer();}}catch(Exception ignored){}}
    @Override public void aoExportarEstatisticas(){Path p=Paths.get(System.getProperty("user.home"),"campo-minado-historico.csv");UtilidadesJogo.exportarCSV(p);view.mostrarAviso("Histórico exportado para " + p);}
    private int contarReveladas(){int n=0;for(int i=0;i<tabuleiro.getLinhas();i++)for(int j=0;j<tabuleiro.getColunas();j++)if(tabuleiro.isRevelada(i,j)&&!tabuleiro.isMinada(i,j))n++;return n;}
    private int marcadas(){int n=0;for(int i=0;i<tabuleiro.getLinhas();i++)for(int j=0;j<tabuleiro.getColunas();j++)if(tabuleiro.isMarcada(i,j))n++;return n;}
    private void atualizar(){view.atualizarEstatisticas(totalMinas-marcadas(),celulasReveladas,totalCelulas,jogadas);}
    private void iniciarTimer(){timerJogo=new Timer(1000,e->atualizarTempo());timerJogo.start();}
    private void pararTimer(){if(timerJogo!=null)timerJogo.stop();}
    private long passados(){return (System.currentTimeMillis()-tempoInicio)/1000;}
    private void atualizarTempo(){long s=passados();if(limiteSegundos>0){long r=Math.max(0,limiteSegundos-s);view.atualizarTempo(String.format("-%02d:%02d",r/60,r%60));if(r<=0)encerrarPorTempo();}else view.atualizarTempo(String.format("%02d:%02d",s/60,s%60));}
    private void encerrarPorTempo(){pararTimer();if(tabuleiro!=null&&!tabuleiro.isJogoEncerrado()){tabuleiro.encerrarPorDerrota();view.atualizarTabuleiro(tabuleiro);}view.mostrarDerrota();view.iniciarAnimacaoExplosao();UtilidadesJogo.registrarHistorico(perfil,dificuldadeAtual,false,passados(),jogadas);}
    private void finalizar(){atualizar();if(!tabuleiro.isJogoEncerrado())return;pararTimer();boolean venceu=!tabuleiro.isDerrota();if(venceu){view.mostrarVitoria(mensagemRecorde());view.iniciarAnimacaoVitoria();}else{view.mostrarDerrota();view.iniciarAnimacaoExplosao();}UtilidadesJogo.registrarHistorico(perfil,dificuldadeAtual,venceu,passados(),jogadas);}
    private String mensagemRecorde(){long s=passados();boolean n=gerenciadorRecordes.registrarSeForMelhor(dificuldadeAtual,s);return n?"Novo recorde!":"Tempo: "+String.format("%02d:%02d",s/60,s%60);}
}
