import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class BinaryMinHeap {

    private Reservation[] heap;
    private int size;
    private static final int MAX_SIZE = 20;

    //Constructor
    public BinaryMinHeap(){
        this.size = 0;
        heap = new Reservation[MAX_SIZE + 1];
        heap[0] = new Reservation(-1, Integer.MIN_VALUE, LocalDateTime.MIN); //Sentinel value
    }

    //Inserting a node into the minheap.
    public void insert(Reservation element){
        if(size == MAX_SIZE){
            //Heap is full
            return;
        }
        heap[++size] = element;
        int current = size;

        //Insert in the right position of min-heap
        while(heap[current].compareTo(heap[parent(current)]) < 0){
            swap(current, parent(current));
            current = parent(current);
        }
    }

    private int parent(int position) { return position/2; }
    private int leftChild( int position) { return (2*position); }
    private int rightChild(int position) { return (2*position) + 1;}
    private boolean isLeafNode(int position) { return position > (size/2) && position <= size;}

    private void swap(int fpos, int spos){
        Reservation tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }

    private void minHeapify(int position) {
        // Check if the node is a non-leaf node
        if (!isLeafNode(position)) {
            // Compare with left child
            int leftChildPos = leftChild(position);
            int rightChildPos = rightChild(position);
            boolean hasRightChild = rightChildPos <= size;

            if (heap[position] == null || (heap[leftChildPos] != null && heap[position].compareTo(heap[leftChildPos]) > 0)
                    || (hasRightChild && heap[rightChildPos] != null && heap[position].compareTo(heap[rightChildPos]) > 0)) {

                // Swap with the smaller child and heapify the child
                if (!hasRightChild || (heap[leftChildPos] != null && heap[leftChildPos].compareTo(heap[rightChildPos]) < 0)) {
                    swap(position, leftChildPos);
                    minHeapify(leftChildPos);
                } else {
                    swap(position, rightChildPos);
                    minHeapify(rightChildPos);
                }
            }
        }
    }

    //Return the min element from the heap.
    public Reservation extractMin(){
        Reservation minElement = heap[1];
        heap[1] = heap[size--];
        minHeapify(1);
        return minElement;
    }

    public List<Reservation> getAllReservations(){
        return Arrays.asList(Arrays.copyOfRange(heap, 1, size + 1));
    }


}
