package dk.itu.map.utility;

public class Sort extends Thread{
    private int[] aux;
    protected int[] arrayToSort;

    public Sort(int[] arr){
        this.arrayToSort = arr;
    }

    @Override
    public void run(){
        sort(arrayToSort);
    }

    protected void sort(int[] arr){
        int N = arr.length;
        aux = new int[N];
        for(int size = 1; size < N; size *= 2){
            for(int subIndex = 0; subIndex < N-size; subIndex += 2*size){
                merge(arr, subIndex, subIndex + size - 1, Math.min(subIndex + size + size -1, N-1));
            }
        }
    }

    private void merge(int[] a, int low, int middle, int high){
        int i = low, j = middle + 1;
        for(int k = low; k <= high; k++){
            aux[k] = a[k];
        }
        for(int k = low; k <= high; k++){
            if (i > middle) a[k] = aux[j++];
            else if (j > high) a[k] = aux[i++];
            else if (compare(aux[j], aux[i]) < 0) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    protected int compare(int i, int j){
        return Integer.compare(i, j);
    }

    public int[] getResult(){
        return arrayToSort;
    }
}