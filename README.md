#Junjie's documentation

Name: Junjie Ouyang
Date: 10/30/2017
project: find-political-donors

I used java to do this project. It has passed the test_1 and my own test.

My source code has three java files: Main, MedianHeap and MedianList, and they are in the package named "myPack", which is in the folder "src/myPack".

#Main.java
It is the main class to do I/O task. Read input file line by line and maintained two HashMaps, to store id-zip-val and id-date-val, respectively.
I output medianvals_by_zip line by line while reading file, since this file requires calculating running median on streaming data.
After reading, output medianvals_by_date, since this file don't require running median so we can calculate it after reading data. In order to output medianvals_by_date with sorted data, I also maintained a TreeMap to sort keys.
More details are commented in my code.

#MedianHeap.java
For medianvals_by_zip.txt, I use two heaps to calculate running median. Left is a maxheap storing the smaller part and Right is a minheap storing the larger part of the data set. When findMedian, Left.peek() or average of Left.peek() and Right.peek(), depending on odd or even number of data set.
I also maintained a count and sum variables to keep track of data size and total sum.
More details are commented in my code.

#MedianList.java
For medianvals_by_date.txt, I use a list to calculate final median. To findMedian, simply use quickSelect to find the median of the data set.
I also maintained a sum variable to keep track of total sum. For data size, simply return list size.
More detail are commented in my code.

#Time complexity
In order to improve time complexity and space complexity, I first skip invalid raw data.
When calculating median for medianvals_by_zip, to improve performance of running median, I used two heaps instead of quickSelect. Assume a data stream of size N for a id-zip key, using heaps can take O(NlogN) time of one id-zip key, while quickSelect takes O(N^2) time and sorting takes O(N^2logN) time.
When calculating median for medianvals_by_date, since we only need to calculate medians for one id-date key for once, we can use quickSelect which takes linear time for one id-date key.
However, in this project I believe I/O steps are the slowest step.

#summary
This java project can still be imporved to run with cooperation with Hadoop and Spark in order to deal with larger data sets.
