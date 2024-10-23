import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;

public class SortingAnalysis {
    private static final int[] SIZES = {100, 500, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 
                                      10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000};
    private static final String DIRECTORY_PATH = "C:/Users/Usuario/Desktop/JavaTODO/"; 
    
    public static void main(String[] args) {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Error: El directorio especificado no existe o no es válido.");
            return;
        }

        System.out.println("Analizando archivos en: " + directory.getAbsolutePath());
        System.out.println("----------------------------------------");

        for (String algorithm : new String[]{"Bubble Sort", "Counting Sort", "Heap Sort", 
                                           "Insertion Sort", "Merge Sort", "Quick Sort", "Selection Sort"}) {
            System.out.print(algorithm + " :");
            for (int size : SIZES) {
                int[] arr = readFile(DIRECTORY_PATH + "file_" + size + ".txt");
                if (arr != null) {
                    long time = measureSortingTime(arr.clone(), algorithm);
                    System.out.print(" " + time + "ms");
                } else {
                    System.out.print(" ERROR");
                }
            }
            System.out.println();
        }
    }

    private static int[] readFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            if (line != null) {
                // Eliminar los corchetes del inicio y final
                line = line.trim().replaceAll("^\\[|\\]$", "");
                
                // Separar los números por coma y espacio
                String[] numberStrings = line.split(",\\s*");
                
                // Convertir cada string a número
                int[] numbers = new int[numberStrings.length];
                for (int i = 0; i < numberStrings.length; i++) {
                    numbers[i] = Integer.parseInt(numberStrings[i].trim());
                }
                return numbers;
            }
        } catch (IOException e) {
            System.err.println("Error leyendo archivo: " + filename);
        } catch (NumberFormatException e) {
            System.err.println("Error parseando números en archivo: " + filename);
        }
        return null;
    }

    private static long measureSortingTime(int[] arr, String algorithm) {
        long startTime = System.currentTimeMillis();
        switch (algorithm) {
            case "Bubble Sort":
                bubbleSort(arr);
                break;
            case "Counting Sort":
                arr = countingSort(arr);
                break;
            case "Heap Sort":
                heapSort(arr);
                break;
            case "Insertion Sort":
                insertionSort(arr);
                break;
            case "Merge Sort":
                mergeSort(arr, 0, arr.length - 1);
                break;
            case "Quick Sort":
                quickSort(arr, 0, arr.length - 1);
                break;
            case "Selection Sort":
                selectionSort(arr);
                break;
        }
        return System.currentTimeMillis() - startTime;
    }

    // [Resto de los métodos de ordenamiento permanecen igual...]
    // Implementación de Bubble Sort
    private static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    // Implementación de Counting Sort
    private static int[] countingSort(int[] arr) {
        int max = Arrays.stream(arr).max().getAsInt();
        int min = Arrays.stream(arr).min().getAsInt();
        int range = max - min + 1;
        int[] count = new int[range];
        int[] output = new int[arr.length];

        for (int value : arr) {
            count[value - min]++;
        }

        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        for (int i = arr.length - 1; i >= 0; i--) {
            output[count[arr[i] - min] - 1] = arr[i];
            count[arr[i] - min]--;
        }

        return output;
    }

    // Implementación de Heap Sort
    private static void heapSort(int[] arr) {
        int n = arr.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && arr[left] > arr[largest])
            largest = left;

        if (right < n && arr[right] > arr[largest])
            largest = right;

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }

    // Implementación de Insertion Sort
    private static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Implementación de Merge Sort
    private static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(arr, left, middle);
            mergeSort(arr, middle + 1, right);
            merge(arr, left, middle, right);
        }
    }

    private static void merge(int[] arr, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, left, L, 0, n1);
        System.arraycopy(arr, middle + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Implementación de Quick Sort
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        
        return i + 1;
    }

    // Implementación de Selection Sort
    private static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }
}
