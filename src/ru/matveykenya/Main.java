package ru.matveykenya;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        final int ARRAY_SIZE = 200000000; // 100_000_000
        final int MAX_INT = 2;
        long time1, time2, timeDelta;
        long sum; // сумма всех членов массива

        int[] array = getArrayInt(ARRAY_SIZE, MAX_INT);

        //System.out.println(Arrays.toString(array));
        System.out.println("Максимум в массиве " + Arrays.stream(array).max().getAsInt());
        System.out.println("Сумма элементов массива " + Arrays.stream(array).sum());

        // Рассчитываем время для 1 потока  *************************************
        time1 = System.currentTimeMillis();
        System.out.println("\nВремя начала " + time1);
        //------------------------------
        sum = sumArrayOneThread(array);
        //------------------------------
        time2 = System.currentTimeMillis();
        System.out.println("Время конца " + time2);
        timeDelta = time2 - time1;
        System.out.println("Расчет суммы в 1 потоке " + sum + " - затраченное время - " + timeDelta);


        //Рассчитываем время в многопоточном режиме *****************************
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ArraySumTask task = new ArraySumTask(0, array.length, array);

        time1 = System.currentTimeMillis();
        System.out.println("\nВремя начала " + time1);
        //------------------------------
        sum = forkJoinPool.invoke(task); // вынес из метода sumArrayManyThreads, думал времени меньше будет тратится
        //------------------------------
        time2 = System.currentTimeMillis();
        System.out.println("Время конца " + time2);
        timeDelta = time2 - time1;
        System.out.println("Расчет суммы методом ForkJoinPool " + sum + " - затраченное время - " + timeDelta);

    }

    private static long sumArrayOneThread (int[] array){
        long sum = 0;
        for (int i : array) {
            sum += i;
        }
        return sum;
    }

    private static long sumArrayManyThreads (int[] array){
        // создаем ForkJoinPool
        //ExecutorService pool = Executors.newWorkStealingPool();
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // Создаем рекурсивную задачу
        ArraySumTask task = new ArraySumTask(0, array.length, array);

        // Отправляем задачу на выполнение в пул потоков
        return forkJoinPool.invoke(task);
    }


    private static int[] getArrayInt(int count, int maxInt){
        Random random = new Random();
        int[] array = new int[count];
        for (int i = 0; i < count; i++){
            array[i] = random.nextInt(maxInt);
        }
        return array;
    }
}
