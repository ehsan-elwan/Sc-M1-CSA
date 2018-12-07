
import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Field;

public class myDTW extends DTWHelper {

    @Override
    public float DTWDistance(Field unknown, Field known) {
        // Method which computes the DTW score between two sets of MFCC
        int row, col;
        row = unknown.getLength();
        col = known.getLength();
        double[][] d = new double[row + 1][col + 1];
        myMFCCdistance t = new myMFCCdistance();
        for (int i = 1; i <= row; i++) {
            d[i][0] = Double.POSITIVE_INFINITY;
        }
        for (int i = 1; i <= col; i++) {
            d[0][i] = Double.POSITIVE_INFINITY;
        }
        d[0][0] = 0;
        double w0 = 1;
        double w1 = 1;
        double w2 = 1;
        float dist;
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                dist = t.distance(unknown.getMFCC(i - 1), known.getMFCC(j - 1));
                double m1 = d[i - 1][j] + (w0 * dist);
                double m2 = d[i - 1][j - 1] + (w1 * dist);
                double m3 = d[i][j - 1] + (w2 * dist);
                d[i][j] = Math.min(m3, Math.min(m1, m2));
            }
        }
        return (float) d[row][col] / (row + col);
    }

    public void showMatrix(double[][] d) {
        int row, col;
        row = d.length;
        col = d[0].length;
        for (int i = 0; i < 100; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.printf("The distance matrix => row: %d  , col: %d  ", row, col);
        System.out.println();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.printf("%.2f     ", d[i][j]);
                System.out.print("\t");
            }
            System.out.println();
        }
    }
    

    public int getIndexOfSmallest(float[] array) {
        if (array == null || array.length == 0) {
            return -1; // null or empty
        }
        int small = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[small]) {
                small = i;
            }
        }
        return small; // position of the first smallest found
    }

}
