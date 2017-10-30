package myPack;
import java.util.ArrayList;
import java.util.List;

/** I use a list to calculate final median. To findMedian,
    simpliy use quickSelect to find the median of the data set.
**/
public class MedianList {
    private List<Double> list;
    double sum;
    public MedianList() {
        list = new ArrayList<>();
        sum = 0;
    }

    public void addNum(Double num) {
        list.add(num);
        sum += num;
    }

    public int findMedian() {
        int n = list.size();
        if ((n&1) == 1) return (int) Math.round(quickSelect(list, 0, n-1, n/2));
        return (int) Math.round((quickSelect(list, 0, n-1, n/2) + quickSelect(list, 0, n-1, n/2 -1)) / 2);

    }

    public int getCount() {
        return list.size();
    }

    public int getSum() {
        return (int) sum;
    }

    private double quickSelect(List<Double> nums, int start, int end, int k){
        int left = start, right = end;
        if(left == right) return list.get(left);
        double pivot = list.get((left + right) / 2);

        while(left <= right){
            while(left <= right && list.get(left) > pivot){
                left++;
            }
            while(left <= right && list.get(right) < pivot){
                right--;
            }
            if(left <= right){
                double tmp = list.get(right);
                list.set(right, list.get(left));
                list.set(left, tmp);
                left++;
                right--;
            }
        }

        if(k >= start && k <= right) return quickSelect(nums,start,right,k);
        else if(k >= left && k <= end) return quickSelect(nums,left,end,k);
        return list.get(right + 1);
    }
}
