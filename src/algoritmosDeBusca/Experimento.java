package algoritmosDeBusca;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

// Experimento de busca (vetor ordenado, alvo ausente = -1).

public class Experimento {

    private static final int[] TAMANHOS = {10_000, 50_000, 100_000};
    private static final int TOTAL_EXECUCOES = 25;
    private static final int WARMUP_EXECUCOES = 5;
    private static final int RODADAS_AQUECIMENTO_EXTRA = 20;
    private static final int ELEMENTO_BUSCA = -1; // Ausente
    private static final int VALOR_MIN = 1;
    private static final int VALOR_MAX = 100_000;
    private static final long SEED = 42L;
    private static final long TAMANHO_PILHA = 512L * 1024 * 1024;

    private static volatile long sorvedouro;

    private interface Busca {
        int buscar(int[] vetor, int alvo);
    }

    private static class Algoritmo {
        final String nome;
        final Busca busca;
        final int repeticoes; 

        Algoritmo(String nome, Busca busca, int repeticoes) {
            this.nome = nome;
            this.busca = busca;
            this.repeticoes = repeticoes;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(null, Experimento::executar, "experimento", TAMANHO_PILHA);
        t.start();
        t.join();
    }

    private static void executar() {
        String arquivoResumo = "resultados_busca_resumido_2.csv";
        String arquivoBruto = "resultados_busca_bruto_2.csv";

        try (PrintWriter writerResumo = new PrintWriter(new FileWriter(arquivoResumo));
             PrintWriter writerBruto = new PrintWriter(new FileWriter(arquivoBruto))) {

            writerResumo.println("Algoritmo,Tamanho,Repeticoes,Media_ns,Mediana_ns,DesvioPadrao_ns,Min_ns,Max_ns");
            writerBruto.println("Algoritmo,Tamanho,Execucao,Tempo_ns_por_busca");

            Random random = new Random(SEED);

            for (int tamanho : TAMANHOS) {
                System.out.println("Preparando vetor ordenado de tamanho: " + tamanho + "...");
                int[] vetorOrdenado = gerarVetorOrdenado(tamanho, random);

                
                List<Algoritmo> algoritmos = new ArrayList<>();
                algoritmos.add(new Algoritmo("BuscaLinearIterativa", AlgoritmosBusca::buscaLinearIterativa, 200));
                algoritmos.add(new Algoritmo("BuscaLinearRecursiva", AlgoritmosBusca::buscaLinearRecursiva, 200));
                algoritmos.add(new Algoritmo("BuscaLinearDuasPontas", AlgoritmosBusca::buscaLinearDuasPontas, 200));
                algoritmos.add(new Algoritmo("BuscaBinariaIterativa", AlgoritmosBusca::buscaBinariaIterativa, 200_000));
                algoritmos.add(new Algoritmo("BuscaBinariaRecursiva", AlgoritmosBusca::buscaBinariaRecursiva, 200_000));

                
                Collections.shuffle(algoritmos, random);

                for (Algoritmo alg : algoritmos) {
                    executarBateria(alg, vetorOrdenado, writerResumo, writerBruto);
                }
            }

            System.out.println("\nExperimento finalizado com sucesso!");
            System.out.println("Arquivos gerados: " + arquivoResumo + " e " + arquivoBruto);

        } catch (IOException e) {
            System.err.println("Erro ao gravar arquivos CSV: " + e.getMessage());
        }
    }

    private static void executarBateria(
            Algoritmo alg,
            int[] vetor,
            PrintWriter writerResumo,
            PrintWriter writerBruto) {

        int execucoesValidas = TOTAL_EXECUCOES - WARMUP_EXECUCOES;
        double[] temposValidos = new double[execucoesValidas];

        try {
            // Aquecimento extra (descartado)
            for (int r = 0; r < RODADAS_AQUECIMENTO_EXTRA; r++) {
                rodada(alg, vetor);
            }

            for (int i = 0; i < TOTAL_EXECUCOES; i++) {
                double nsPorBusca = rodada(alg, vetor);

                // As 5 primeiras execucoes sao warm-up e nao entram nas estatisticas
                if (i >= WARMUP_EXECUCOES) {
                    int idx = i - WARMUP_EXECUCOES;
                    temposValidos[idx] = nsPorBusca;
                    writerBruto.printf(Locale.US, "%s,%d,%d,%.2f%n",
                            alg.nome, vetor.length, idx + 1, nsPorBusca);
                }
            }

            double soma = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (double t : temposValidos) {
                soma += t;
                if (t < min) min = t;
                if (t > max) max = t;
            }
            double media = soma / execucoesValidas;

            double somaQuadrados = 0;
            for (double t : temposValidos) {
                somaQuadrados += (t - media) * (t - media);
            }
            double desvio = Math.sqrt(somaQuadrados / execucoesValidas);

            double[] ordenados = temposValidos.clone();
            Arrays.sort(ordenados);
            int n = ordenados.length;
            double mediana = (n % 2 == 1)
                    ? ordenados[n / 2]
                    : (ordenados[n / 2 - 1] + ordenados[n / 2]) / 2.0;

            writerResumo.printf(Locale.US, "%s,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                    alg.nome, vetor.length, alg.repeticoes, media, mediana, desvio, min, max);

            System.out.printf(Locale.US, "  [OK] %-24s | N = %7d | Media: %10.2f ns | Mediana: %10.2f ns%n",
                    alg.nome, vetor.length, media, mediana);

        } catch (StackOverflowError e) {
            System.err.printf("  [ERRO] %s estourou a pilha em N = %d!%n", alg.nome, vetor.length);
            writerResumo.printf(Locale.US, "%s,%d,%d,STACK_OVERFLOW,STACK_OVERFLOW,0,0,0%n",
                    alg.nome, vetor.length, alg.repeticoes);
        }
    }

    private static double rodada(Algoritmo alg, int[] vetor) {
        int reps = alg.repeticoes;
        long soma = 0;

        long inicio = System.nanoTime();
        for (int k = 0; k < reps; k++) {
            soma += alg.busca.buscar(vetor, ELEMENTO_BUSCA);
        }
        long fim = System.nanoTime();

        // Verificacao de correcao fora da medicao
        if (soma != -(long) reps) {
            throw new IllegalStateException(
                    "Erro: o elemento -1 nao deveria ser encontrado em " + alg.nome);
        }
        sorvedouro += soma;

        return (double) (fim - inicio) / reps;
    }

    private static int[] gerarVetorOrdenado(int tamanho, Random random) {
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = random.nextInt(VALOR_MAX - VALOR_MIN + 1) + VALOR_MIN;
        }
        Arrays.sort(vetor);
        return vetor;
    }
}