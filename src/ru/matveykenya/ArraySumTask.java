package ru.matveykenya;

import java.util.concurrent.RecursiveTask;

public class ArraySumTask extends RecursiveTask<Integer> {

    int indexStart;
    int indexEnd;
    int[] array;

    public ArraySumTask (int indexStart, int indexEnd, int[] array) {
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
        this.array = array;
    }

    @Override
    protected Integer compute() {
        final int diff = indexEnd - indexStart;
        return switch (diff) {
            case 0 -> 0;
            case 1 -> array[indexStart];
            case 2 -> array[indexStart] + array[indexStart + 1];
            default -> forkTasksAndGetResult();
        };
    }

    private Integer forkTasksAndGetResult() {
        final int middle = (indexEnd - indexStart)/2 + indexStart;

        // Создаем задачу для левой части диапазона
        ArraySumTask task1 =new ArraySumTask(indexStart, middle, array);

        // Создаем задачу для правой части диапазона
        ArraySumTask task2 =new ArraySumTask(middle, indexEnd, array);

        //Запускаем обе задачи в пуле
        invokeAll(task1, task2);

        // Суммируем результаты выполнения обоих задач
        return task1.join()+ task2.join();
    }
}
