package algoritmosDeBusca;
public class AlgoritmosBusca {

    // 1. Busca Linear Iterativa - O(n)
    public static int buscaLinearIterativa(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // 2. Busca Linear Recursiva - O(n)
    public static int buscaLinearRecursiva(int[] arr, int target) {
        return buscaLinearRecursivaHelper(arr, target, 0);
    }

    private static int buscaLinearRecursivaHelper(int[] arr, int target, int index) {
        if (index >= arr.length) {
            return -1;
        }
        if (arr[index] == target) {
            return index;
        }
        return buscaLinearRecursivaHelper(arr, target, index + 1);
    }

    // 3. Busca Binária Iterativa - O(log n)
    public static int buscaBinariaIterativa(int[] arr, int target) {
        int inicio = 0;
        int fim = arr.length - 1;

        while (inicio <= fim) {
            int meio = inicio + (fim - inicio) / 2;

            if (arr[meio] == target) {
                return meio;
            }
            if (arr[meio] < target) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    // 4. Busca Binária Recursiva - O(log n)
    public static int buscaBinariaRecursiva(int[] arr, int target) {
        return buscaBinariaRecursivaHelper(arr, target, 0, arr.length - 1);
    }

    private static int buscaBinariaRecursivaHelper(int[] arr, int target, int inicio, int fim) {
        if (inicio > fim) {
            return -1;
        }

        int meio = inicio + (fim - inicio) / 2;

        if (arr[meio] == target) {
            return meio;
        }
        if (arr[meio] < target) {
            return buscaBinariaRecursivaHelper(arr, target, meio + 1, fim);
        } else {
            return buscaBinariaRecursivaHelper(arr, target, inicio, meio - 1);
        }
    }

    // 5. Busca Linear Iterativa Duas Pontas - O(n)
    public static int buscaLinearDuasPontas(int[] arr, int target) {
        int inicio = 0;
        int fim = arr.length - 1;

        while (inicio <= fim) {
            if (arr[inicio] == target) {
                return inicio;
            }
            if (arr[fim] == target) {
                return fim;
            }
            inicio++;
            fim--;
        }
        return -1;
    }
}