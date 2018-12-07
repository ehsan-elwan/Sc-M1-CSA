
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.Field;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;

public class main {

    // Function that permits computing the length of the Fields of MFCC (number of MFCC in a Field) 
    static int FieldLength(String fileName) throws IOException {
        int counter = 0;
        File file = new File(System.getProperty("user.dir") + fileName);
        for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
            counter++;
        }
        return 2 * Math.floorDiv(counter, 512);
    }

    public static Object[][] compareWithConfusionMatrix(String sourceRef, String[] inputRef, String[] listOfCommands) throws IOException, InterruptedException {

        int n = listOfCommands.length;
        int[][] confusionMatrix = new int[n][n];
        myDTW dtw = new myDTW();
        Extractor extractor = Extractor.getExtractor();
        WindowMaker windowMaker;
        List<String> files;
        float[] distancsBetweenCmds;
        int MFCCLength;
        for (int i = 0; i < n; i++) {
            files = new ArrayList<>();
            files.add(sourceRef + listOfCommands[i]);
            windowMaker = new MultipleFileWindowMaker(files);
            // Step 2. Recovery of MFCCs from the word Alpha
            MFCCLength = FieldLength(sourceRef + listOfCommands[i]);
            MFCC[] mfccs1 = new MFCC[MFCCLength];

            for (int k = 0; k < mfccs1.length; k++) {
                mfccs1[k] = extractor.nextMFCC(windowMaker);
            }
            distancsBetweenCmds = new float[n];

            for (String inputRef1 : inputRef) {
                for (int j = 0; j < n; j++) {
                    // Step 3. Construction of the alpha Field (MFCC set)

                    Field oneField = new Field(mfccs1);
                    ///File 2
                    files = new ArrayList<>();
                    files.add(inputRef1 + listOfCommands[j]);
                    windowMaker = new MultipleFileWindowMaker(files);
                    MFCCLength = FieldLength(inputRef1 + listOfCommands[j]);
                    MFCC[] mfccs2 = new MFCC[MFCCLength];
                    for (int k = 0; k < mfccs2.length; k++) {
                        mfccs2[k] = extractor.nextMFCC(windowMaker);
                    }
                    // Step 3. Construction of the alpha Field (MFCC set)
                    Field twoField = new Field(mfccs2);
                    float distance2 = dtw.DTWDistance(oneField, twoField); //our helper
                    distancsBetweenCmds[j] = distance2;
                    //  System.out.println("distance value " + sourceRef + listOfCommands[i] + " - " + inputRef1 + listOfCommands[j] + " = : " + distance2);
                    //  System.out.println("");
                }
                confusionMatrix[i][dtw.getIndexOfSmallest(distancsBetweenCmds)] += 1;
            }

        }
        // Copy the confusion matrix to new labeled matrix for better visual results
        Object[][] LabeledConfusionMatrix = new Object[n + 1][n + 1];
        LabeledConfusionMatrix[0][0] = "Word/Com";
        for (int i = 1; i <= n; i++) {
            LabeledConfusionMatrix[i][0] = listOfCommands[i - 1];
            LabeledConfusionMatrix[0][i] = listOfCommands[i - 1];

            for (int j = 1; j <= n; j++) {
                LabeledConfusionMatrix[i][j] = confusionMatrix[i - 1][j - 1];
                //System.out.print(confusionMatrix[i][j] + "  ");
                //System.out.print("\t");
            }
            //System.out.println();
        }
        return LabeledConfusionMatrix;
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String[] commands = {"do_a_flip.csv", "forward.csv", "left_turn.csv", "left.csv",
            "rev_pend.csv", "right_turn.csv", "right.csv", "stop.csv"};
        String[] inputsRef = {"/test/csv/Joey/", "/test/csv/Brain/", "/test/csv/ibrahim/"};// 3 differents inputs
        String soucre = "/test/csv/Amy/";  // our base reference / 
        // Compare the source with list of inputs through list of commands
        Object[][] o = compareWithConfusionMatrix(soucre, inputsRef, commands);
        //Show the confusion matrix
        for (Object[] o1 : o) {
            for (int j = 0; j < o.length; j++) {
                System.out.print(o1[j] + "\t");
            }
            System.out.println();
        }
    }
}
