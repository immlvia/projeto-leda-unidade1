import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 Experimento de tempo dos algoritmos de ordenação.
 Para cada tamanho e cenário gera UMA entrada original e dá uma cópia dela
 para cada algoritmo. Cada algoritmo roda 25 vezes, as 5 primeiras são
 escartadas (warm-up) e as estatísticas usam só as 20 restantes.
 Resultado: resultados_ordenacao.csv (na pasta do projeto).
 */

public class Experimento {

    static final int[] TAMANHOS = {50_000, 150_000, 500_000};

    static final int EXECUCOES = 25;
    static final int WARMUP = 5;
    static final int MAX_VALOR = 100_000;
    static final double FRACAO_PARCIAL = 0.15;
    static final String ARQUIVO = "resultados_ordenacao500.csv";

    static final String[] CENARIOS = {"aleatorio", "ordenado", "inverso", "parcial"};

    static final Map<String, Consumer<int[]>> ALGORITMOS = new LinkedHashMap<>();

    static {
        ALGORITMOS.put("BubbleClassico", Ordenacao::bubbleSortClassico);
        ALGORITMOS.put("BubbleOtimizado", Ordenacao::bubbleSortOtimizado);
        ALGORITMOS.put("SelectionClassico", Ordenacao::selectionSortClassico);
        ALGORITMOS.put("SelectionEstavel", Ordenacao::selectionSortEstavel);
        ALGORITMOS.put("Insertion", Ordenacao::insertionSort);
        ALGORITMOS.put("Merge", Ordenacao::mergeSort);
        ALGORITMOS.put("QuickClassico", Ordenacao::quickSortClassico);
        ALGORITMOS.put("QuickShuffle", Ordenacao::quickSortShuffle);
        ALGORITMOS.put("Counting", Ordenacao::countingSort);
        ALGORITMOS.put("ArraysSort", a -> Arrays.sort(a));
    }

    public static void main(String[] args) throws InterruptedException {
        // Roda numa thread com pilha grande
        Thread t = new Thread(null, () -> {
            try {
                executar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "experimento", 512L * 1024 * 1024);
        t.start();
        t.join();
    }

    static void executar() throws IOException {
        Random rnd = new Random(42);

        try (PrintWriter csv = new PrintWriter(new FileWriter(ARQUIVO))) {
            csv.println("tamanho,cenario,algoritmo,media_ms,desvio_ms,min_ms,max_ms");

            for (int n : TAMANHOS) {
                for (String cenario : CENARIOS) {
                    int[] original = gerarEntrada(cenario, n, rnd);

                    for (Map.Entry<String, Consumer<int[]>> e : ALGORITMOS.entrySet()) {
                        String nome = e.getKey();
                        double[] ms = medir(e.getValue(), original, nome);

                        double media = media(ms);
                        double desvio = desvioPadrao(ms, media);
                        double min = Arrays.stream(ms).min().getAsDouble();
                        double max = Arrays.stream(ms).max().getAsDouble();

                        csv.printf(Locale.US, "%d,%s,%s,%.3f,%.3f,%.3f,%.3f%n",
                                n, cenario, nome, media, desvio, min, max);
                        csv.flush();

                        System.out.printf(Locale.US, "n=%d | %-9s | %-16s | media=%.3f ms%n",
                                n, cenario, nome, media);
                    }
                }
            }
        }
        System.out.println("Terminou. Resultados em " + ARQUIVO);
    }

    /* Roda o algoritmo EXECUCOES vezes e devolve os tempos (ms) sem o warm-up. */
    static double[] medir(Consumer<int[]> algoritmo, int[] original, String nome) {
        double[] tempos = new double[EXECUCOES - WARMUP];

        for (int r = 0; r < EXECUCOES; r++) {
            int[] copia = original.clone();

            long inicio = System.nanoTime();
            algoritmo.accept(copia);
            long fim = System.nanoTime();

            if (!Ordenacao.estaOrdenado(copia)) {
                throw new IllegalStateException(nome + " nao ordenou corretamente");
            }
            if (r >= WARMUP) {
                tempos[r - WARMUP] = (fim - inicio) / 1_000_000.0;
            }
        }
        return tempos;
    }


    // Geração das entradas
    static int[] gerarEntrada(String cenario, int n, Random rnd) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = 1 + rnd.nextInt(MAX_VALOR);
        }

        switch (cenario) {
            case "aleatorio":
                return v;
            case "ordenado":
                Arrays.sort(v);
                return v;
            case "inverso":
                Arrays.sort(v);
                for (int i = 0, j = n - 1; i < j; i++, j--) {
                    int temp = v[i];
                    v[i] = v[j];
                    v[j] = temp;
                }
                return v;
            case "parcial":
                Arrays.sort(v);
                embaralharPosicoesSorteadas(v, FRACAO_PARCIAL, rnd);
                return v;
            default:
                throw new IllegalArgumentException("Cenario desconhecido: " + cenario);
        }
    }


     // Sorteia ~fracao das posições e embaralha só os elementos dessas posições.
    static void embaralharPosicoesSorteadas(int[] v, double fracao, Random rnd) {
        int[] pos = new int[v.length];
        int m = 0;
        for (int i = 0; i < v.length; i++) {
            if (rnd.nextDouble() < fracao) {
                pos[m++] = i;
            }
        }
        for (int i = m - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int temp = v[pos[i]];
            v[pos[i]] = v[pos[j]];
            v[pos[j]] = temp;
        }
    }

    // Estatísticas
    static double media(double[] x) {
        double soma = 0;
        for (double d : x) {
            soma += d;
        }
        return soma / x.length;
    }

    static double desvioPadrao(double[] x, double media) {
        double soma = 0;
        for (double d : x) {
            soma += (d - media) * (d - media);
        }
        return Math.sqrt(soma / (x.length - 1));
    }
}