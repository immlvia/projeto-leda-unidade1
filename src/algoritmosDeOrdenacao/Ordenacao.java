import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

public class Ordenacao {

    /* usado pelo Counting Sort). */
    public static final int K = 100_000;

    private static final Random RNG = new Random();

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }


    // BUBBLE SORT
    /* Versão sem flag: sempre faz todas as passadas. */
    public static void bubbleSortClassico(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                }
            }
        }
    }

    /* Versão otimizada */
    public static void bubbleSortOtimizado(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            boolean trocou = false;
            for (int j = 0; j < n - 1 -i; j++) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1);
                    trocou = true;
                }
            }
            if (!trocou) {
                break;
            }
        }
    }


    // SELECTION SORT
    /* Versão não estável */
    public static void selectionSortClassico(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            swap(a, i, min);
        }
    }

    /**
     Versão estável: em vez de trocar, desloca os elementos entre i e min
     uma posição para a frente e insere o menor na posição i.
     */
    public static void selectionSortEstavel(int[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j] < a[min]) {
                    min = j;
                }
            }
            int menor = a[min];
            while (min > i) {
                a[min] = a[min - 1];
                min--;
            }
            a[i] = menor;
        }
    }


    // INSERTION SORT
    public static void insertionSort(int[] a) {
        int n = a.length;
        for (int j = 1; j < n; j++) {
            int key = a[j];
            int i = j - 1;
            while (i >= 0 && a[i] > key) {
                a[i + 1] = a[i];
                i--;
            }
            a[i + 1] = key;
        }
    }


    // MERGE SORT
    public static void mergeSort(int[] a) {
        mergeSort(a, 0, a.length - 1);
    }

    public static void mergeSort(int[] A, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            mergeSort(A, left, middle);
            mergeSort(A, middle + 1, right);

            merge(A, left, middle, right);
        }
    }

    private static void merge(int[] A, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = A[left + i];
        }
        for (int j = 0; j < n2; j++) {
            R[j] = A[middle + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                A[k] = L[i];
                i++;
            } else {
                A[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            A[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            A[k] = R[j];
            j++;
            k++;
        }
    }


    // QUICK SORT
    public static void quickSortClassico(int[] a) {
        quickSort(a, 0, a.length - 1);
    }

    /** Versão slide + shuffle: embaralha o vetor antes de ordenar. */
    public static void quickSortShuffle(int[] a) {
        shuffle(a);
        quickSort(a, 0, a.length - 1);
    }

    private static void quickSort(int[] a, int left, int right) {
        if (left < right) {
            int pivot = partition(a, left, right);
            quickSort(a, left, pivot - 1);
            quickSort(a, pivot + 1, right);
        }
    }

    private static int partition(int[] a, int left, int right) {
        int p = a[left];
        int i = left + 1;
        int j = right;
        while (i <= j) {
            if (a[i] <= p) {
                i++;
            } else if (a[j] > p) {
                j--;
            } else {
                swap(a, i, j);
            }
        }
        swap(a, left, j);
        return j;
    }

    /** Fisher-Yates. */
    private static void shuffle(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = RNG.nextInt(i + 1);
            swap(a, i, j);
        }
    }


    // COUNTING SORT
    public static void countingSort(int[] a) {
        int n = a.length;
        int[] c = new int[K + 1];
        int[] b = new int[n];

        for (int j = 0; j < n; j++) {
            c[a[j]]++;
        }
        for (int i = 1; i <= K; i++) {
            c[i] += c[i - 1];
        }
        for (int j = n - 1; j >= 0; j--) {
            b[c[a[j]] - 1] = a[j];
            c[a[j]]--;
        }
        System.arraycopy(b, 0, a, 0, n);
    }


    //  verificação e teste rápido de correção


    public static boolean estaOrdenado(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    private static void testar(String nome, Consumer<int[]> algoritmo) {
        Random r = new Random(42);
        int[] tamanhos = {0, 1, 2, 10, 1000};
        boolean ok = true;
        for (int n : tamanhos) {
            int[] original = new int[n];
            for (int i = 0; i < n; i++) {
                original[i] = 1 + r.nextInt(K);
            }
            int[] esperado = original.clone();
            Arrays.sort(esperado);

            int[] teste = original.clone();
            algoritmo.accept(teste);
            if (!Arrays.equals(teste, esperado)) {
                ok = false;
            }
        }
        System.out.println((ok ? "OK    " : "ERRO  ") + nome);
    }

    public static void main(String[] args) {
        testar("bubbleSortClassico", Ordenacao::bubbleSortClassico);
        testar("bubbleSortOtimizado", Ordenacao::bubbleSortOtimizado);
        testar("selectionSortClassico", Ordenacao::selectionSortClassico);
        testar("selectionSortEstavel", Ordenacao::selectionSortEstavel);
        testar("insertionSort", Ordenacao::insertionSort);
        testar("mergeSort", Ordenacao::mergeSort);
        testar("quickSortClassico", Ordenacao::quickSortClassico);
        testar("quickSortShuffle", Ordenacao::quickSortShuffle);
        testar("countingSort", Ordenacao::countingSort);
    }
}