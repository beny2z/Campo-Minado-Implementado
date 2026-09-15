package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GerenciadorRecordes {

    private static final String ARQUIVO_RECORDES =
            System.getProperty("user.home") + java.io.File.separator + "campo_minado_recordes.properties";

    private final Properties recordes = new Properties();

    public GerenciadorRecordes() {
        carregar();
    }

    private void carregar() {
        try (FileInputStream entrada = new FileInputStream(ARQUIVO_RECORDES)) {
            recordes.load(entrada);
        } catch (IOException ignorado) {
            // primeira execução: arquivo ainda não existe
        }
    }

    private void salvar() {
        try (FileOutputStream saida = new FileOutputStream(ARQUIVO_RECORDES)) {
            recordes.store(saida, "Recordes do Campo Minado (segundos por dificuldade)");
        } catch (IOException ignorado) {
            // sem permissão de escrita: só não persiste desta vez
        }
    }

    public long obterMelhorTempo(String dificuldade) {
        String valor = recordes.getProperty(dificuldade);
        if (valor == null) return -1;
        try {
            return Long.parseLong(valor);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean registrarSeForMelhor(String dificuldade, long segundos) {
        long recordeAtual = obterMelhorTempo(dificuldade);
        if (recordeAtual == -1 || segundos < recordeAtual) {
            recordes.setProperty(dificuldade, String.valueOf(segundos));
            salvar();
            return true;
        }
        return false;
    }
}