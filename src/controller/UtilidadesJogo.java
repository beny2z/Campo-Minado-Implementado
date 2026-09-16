package controller;

import java.awt.Toolkit;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Properties;

/** Serviços pequenos de persistência, mantidos fora da View e do Model. */
public final class UtilidadesJogo {
    private static final Path DIR=Paths.get(System.getProperty("user.home"),".campo-minado");
    private UtilidadesJogo() { }
    public static Properties carregarConfiguracoes(){Properties p=new Properties();try(InputStream in=Files.newInputStream(DIR.resolve("config.properties"))){p.load(in);}catch(IOException ignored){}return p;}
    public static void salvarConfiguracoes(Properties p){try{Files.createDirectories(DIR);try(OutputStream out=Files.newOutputStream(DIR.resolve("config.properties"))){p.store(out,"Configurações do Campo Minado");}}catch(IOException ignored){}}
    public static void registrarHistorico(String perfil,String dificuldade,boolean venceu,long segundos,int jogadas){try{Files.createDirectories(DIR);String s=LocalDateTime.now()+","+perfil+","+dificuldade+","+(venceu?"VITORIA":"DERROTA")+","+segundos+","+jogadas+System.lineSeparator();Files.write(DIR.resolve("historico.csv"),s.getBytes(StandardCharsets.UTF_8),StandardOpenOption.CREATE,StandardOpenOption.APPEND);}catch(IOException ignored){}}
    public static void exportarCSV(Path destino){try{Files.createDirectories(DIR);if(Files.exists(DIR.resolve("historico.csv")))Files.copy(DIR.resolve("historico.csv"),destino,StandardCopyOption.REPLACE_EXISTING);}catch(IOException ignored){}}
    public static void beep(){Toolkit.getDefaultToolkit().beep();}
}
