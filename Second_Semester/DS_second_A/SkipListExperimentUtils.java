import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SkipListExperimentUtils {
    public static double measureLevels(double p, int x) {
        AbstractSkipList list = new IndexableSkipList(p);
        double sum = 0;
        for (int i = 0 ; i < x ; i++){
            int height = list.generateHeight();
            sum += height;
        }
        return (sum/x)+1;
       }
    

    /*
     * The experiment should be performed according to these steps:
     * 1. Create the empty Data-Structure.
     * 2. Generate a randomly ordered list (or array) of items to insert.
     *
     * 3. Save the start time of the experiment (notice that you should not
     *    include the previous steps in the time measurement of this experiment).
     * 4. Perform the insertions according to the list/array from item 2.
     * 5. Save the end time of the experiment.
     *
     * 6. Return the DS and the difference between the times from 3 and 5.
     */


    public static Pair<AbstractSkipList, Double> measureInsertions(double p, int size) {
        IndexableSkipList skipList = new IndexableSkipList(p);
        List<Integer> items = generateRandomOrder(size);

        double startTime = System.nanoTime();

        for (int item : items) {
            skipList.insert(item);
        }

        double endTime = System.nanoTime();
        double AvregeInsertTime = (endTime - startTime)/(size+1); //בהסבר כתוב לעשות סעיף 3 פחות סעיף 5 אבל זה לא הזמן הממוצע

        return new Pair<>(skipList, AvregeInsertTime);
    }

    public static double measureSearch(AbstractSkipList skipList, int size) {
        List<Integer> items = generateRandomOrderOdd(size);
        int searchedItems = 0;
        AbstractSkipList.Node curr = null;
        double startTime = System.nanoTime();

 
        for (int item : items){
            searchedItems ++;
            curr = skipList.search(item);
            if (curr != null) {
                break;
            }
        }

        double endTime = System.nanoTime();

        return (endTime - startTime)/searchedItems;
    }

    public static double measureDeletions(AbstractSkipList skipList, int size) {
        List<Integer> items = generateRandomOrder(size);

        double startTime = System.nanoTime();

        for (Integer item : items) {
            AbstractSkipList.Node node = skipList.find(item);
            if (node != null) {
                skipList.delete(node);
            }
        }

        double endTime = System.nanoTime();
        double AvregeInsertTime = (endTime - startTime)/(size+1); 

        return AvregeInsertTime;
    }

    private static List<Integer> generateRandomOrder(int size) {
        List<Integer> items = new ArrayList<>();

        for (int i = 0; i <= 2 * size; i += 2) {
            items.add(i);
        }

        Collections.shuffle(items);

        return items;
    }

    private static List<Integer> generateRandomOrderOdd(int size) {
        List<Integer> items = new ArrayList<>();

        for (int i = 0; i <= 2 * size; i++) {
            items.add(i);
        }

        Collections.shuffle(items);

        return items;
    }


    public static void main(String[] args) {
        LinkedList<Double> p = new LinkedList<Double>();
        p.add(0.33);
        p.add(0.5);
        p.add(0.75);
        p.add(0.9);

        LinkedList<Integer> x = new LinkedList<Integer>();
        x.add(10);
        x.add(100);
        x.add(1000);
        x.add(10000);

        LinkedList<Double> levels = new LinkedList<Double>();

        System.out.println("2.2 output:");


        for (int i = 0 ; i < p.size() ; i++){ 
            for (int j =0 ; j < x.size() ; j++){
                for (int l = 1 ; l < 6 ; l++){
                    double level = measureLevels(p.get(i), x.get(j));
                    levels.add(level);
                }
                double exeptedLevel = (1/(1-p.get(i)));
                double averageDelta = 0;
                for (int d=0 ; d < levels.size() ; d++){
                    averageDelta = averageDelta + (Math.abs(levels.get(d)-exeptedLevel));
                }
                averageDelta = (1.0/5)*averageDelta;
                System.out.println("probability: "+p.get(i)+", x: "+x.get(j));
                for (int d=0 ; d < levels.size() ; d++){
                    System.out.println("l"+(d+1)+": "+levels.get(d));
                }
                System.out.println("Exepted Level: "+exeptedLevel);
                System.out.println("Average Delta: "+averageDelta);
                levels.clear();
                System.out.println();



                System.out.println("2.6 output:");

                LinkedList<Integer> x2 = new LinkedList<Integer>();
                x2.add(1000);
                x2.add(2500);
                x2.add(5000);
                x2.add(10000);
                x2.add(15000);
                x2.add(20000);
                x2.add(50000);

                double totalInsertTime = 0;
                double totalSearchTime = 0;
                double totalDeletionTime = 0;
                for (int m = 0 ; m < p.size() ; m++){ 
                    for (int n = 0 ; n< x2.size() ; n++){
                        for (int q = 0; q < 30; q++) {
                            Pair<AbstractSkipList, Double> pair = measureInsertions(p.get(m), x2.get(n));
                            totalInsertTime = totalInsertTime + pair.second();
                             
                            double searchTime = measureSearch(pair.first(), x2.get(n));
                            totalSearchTime = totalSearchTime + searchTime;
                
                            double deletionTime = measureDeletions(pair.first(), x2.get(n));
                            totalDeletionTime = totalDeletionTime + deletionTime;
                        }
                   
                           double averageInsertionTime = totalDeletionTime / 30;
                           double averageSearchTime = totalSearchTime / 30;
                           double averageDeletionTime = totalDeletionTime / 30;
                           System.out.println("p: "+p.get(m)+"  x :"+x2.get(n));
                           System.out.println("Average Insertion: " + averageInsertionTime);
                           System.out.println("Average Search Time: " + averageSearchTime);
                           System.out.println("Average Deletion Time: " + averageDeletionTime);
                           System.out.println();

                    }
                }
            }

            System.out.println("2.7 output:");

            
        }
    }
    
}

