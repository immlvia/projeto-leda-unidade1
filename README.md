# projeto-leda-unidade1
# 📊 Análise de Algoritmos de Busca e Ordenação

Projeto prático da Unidade 1 da disciplina de **Estrutura de Dados / Laboratório de Estrutura de Dados (UEPB)**, voltado à implementação e análise experimental de desempenho de algoritmos de busca e ordenação em Java.

## O que tem neste repositório?
* **Algoritmos de Ordenação (Java):** Implementações de *BubbleSort* (clássico e otimizado), *SelectionSort* (clássico e estável), *InsertionSort*, *MergeSort*, *QuickSort* (pivô fixo e com *shuffle*), *CountingSort* e comparativo com o `Arrays.sort()` nativo.
* **Algoritmos de Busca (Java):** Implementações de *Busca Linear* (iterativa, recursiva e de duas pontas) e *Busca Binária* (iterativa e recursiva).
* **Scripts de Experimentos:** Testes automatizados medindo o tempo via `System.nanoTime()` em vetores de `10.000`, `50.000` e `100.000` elementos (25 execuções por cenário, descartando as 5 primeiras como *warm-up* da JVM).
* **Cenários Avaliados:** Vetores aleatórios, ordenados, inversamente ordenados e parcialmente ordenados (15% embaralhado) na ordenação; e avaliação de pior caso (busca pelo elemento ausente `-1`) nas buscas.
* **Resultados e Gráficos:** Scripts em Python utilizados para gerar os gráficos comparativos, prints das visualizações e tabelas consolidadas com os resultados obtidos.

## Autoras
* **Héllida Louize Lima Duarte Gomes**
* **Lívia Albuquerque Oliveira**
