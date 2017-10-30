package myPack;

import java.util.Collections;
import java.util.PriorityQueue;

/** I use two heaps to calculate runing median. Left is a maxheap storing
    the smaller part and Right is a minheap storing the larger part of the
    data set. When findMedian, Left.peek() or avg of Left.peek() and Right.peek(),
    depending on odd or even number of data set.
**/
public class MedianHeap {
    private PriorityQueue<Double> left;
    private PriorityQueue<Double> right;
    int count;
    double sum;
    public MedianHeap() {
        left = new PriorityQueue<>(Collections.reverseOrder());
        right = new PriorityQueue<>();
        count = 0;
        sum = 0;
    }

    public void addNum(Double num) {
        if (left.isEmpty() || num <= left.peek()) left.add(num);
        else right.add(num);

        if (left.size() > right.size()+1) right.add(left.poll());
        else if (right.size() > left.size()) left.add(right.poll());
        count++;
        sum += num;
    }

    public int findMedian() {
        if (left.size() - right.size() ==1) return (int) Math.round(left.peek());
        else return (int) Math.round((left.peek()+right.peek())/2.0);
    }

    public int getCount() {
        return count;
    }

    public int getSum() {
        return (int) sum;
    }
}
