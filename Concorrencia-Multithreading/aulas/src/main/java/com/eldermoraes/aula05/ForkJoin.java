package com.eldermoraes.aula05;

import com.eldermoraes.aula05.Ex1ForkJoinSumExample.SumTask;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoin {
  static class SumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10_000;
    private final long[] array;
    private final int start;
    private final int end;

    public SumTask(long[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    protected Long compute() {
      int length = end - start;
      if (length <= THRESHOLD) {
        // Caso base: o problema é pequeno o suficiente para resolver diretamente.
        long sum = 0;
        for (int i = start; i < end; i++) {
          sum += array[i];
        }
        return sum;
      }

      // Caso recursivo: divide o problema.
      int mid = start + length / 2;
      Ex1ForkJoinSumExample.SumTask leftTask = new Ex1ForkJoinSumExample.SumTask(array, start, mid);
      Ex1ForkJoinSumExample.SumTask rightTask = new Ex1ForkJoinSumExample.SumTask(array, mid, end);

      // Otimização: executa a segunda subtarefa diretamente na thread atual
      // enquanto a primeira é enviada para ser executada por outra thread (ou roubada).
      leftTask.fork(); // Envia a tarefa da esquerda para execução assíncrona.
      long rightResult = rightTask.compute(); // Executa a tarefa da direita diretamente.
      long leftResult = leftTask.join(); // Aguarda o resultado da tarefa da esquerda.

      return leftResult + rightResult;
    }
  }

  public static void main(String[] args) {
    long[] numbers = LongStream.rangeClosed(1, 1_000_000).toArray();
    try (ForkJoinPool pool = ForkJoinPool.commonPool()) {
      long startTime = System.currentTimeMillis();
      long result = pool.invoke(new Ex1ForkJoinSumExample.SumTask(numbers, 0, numbers.length));
      long endTime = System.currentTimeMillis();

      System.out.println("Resultado da soma: " + result);
      System.out.println("Tempo de execução: " + (endTime - startTime) + " ms");
    }
  }
}
