
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import fr.enseeiht.danck.voice_analyzer.DTWHelper;
import fr.enseeiht.danck.voice_analyzer.Extractor;
import fr.enseeiht.danck.voice_analyzer.Field;
import fr.enseeiht.danck.voice_analyzer.MFCC;
import fr.enseeiht.danck.voice_analyzer.WindowMaker;
import fr.enseeiht.danck.voice_analyzer.defaults.DTWHelperDefault;

public class Test2 {

    // Function that permits computing the length of the Fields of MFCC (number of MFCC in a Field) 
    static int FieldLength(String fileName) throws IOException {
        int counter = 0;
        File file = new File(System.getProperty("user.dir") + fileName);
        for (String line : Files.readAllLines(file.toPath(), Charset.defaultCharset())) {
            counter++;
        }
        return 2 * Math.floorDiv(counter, 512);
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        // If you want to add new words, just repeat steps 1 to 3
        // For example, we can test that the distance between alpha and alpha is 0
        // float mydistanceAlphaAlpha= myDTWHelper.DTWDistance(alphaField, alphaField);
        //float distanceAlphaAlphadefault = DTWHelperDefault.DTWDistance(alphaField, alphaField);
        // System.out.println("myDTW -  distance value Alpha-Alpha  : "+mydistanceAlphaAlpha);
        //System.out.println("DTWHelperDefault - distance value Alpha-Alpha  : " + distanceAlphaAlphadefault);
        // Distance between the words Alpha and Bravo
        // Step 1. Bravo Reading
        int MFCCLength;
        //DTWHelper myDTWHelper= new myDTW();
        DTWHelper DTWHelperDefault = new DTWHelperDefault();
        myMFCCdistance t = new myMFCCdistance();

        myDTW dtw = new myDTW();

        // Path to audio files
        String base = "/test/csv/fr/";

        // Calling the default extractor (MFCC calculation)
        Extractor extractor = Extractor.getExtractor();
        WindowMaker windowMaker;// = new MultipleFileWindowMaker(files);
        List<String> files;
        String[] filescvs = {"01droite.csv", "02droite.csv", "03droite.csv", "04droite.csv", "05droite.csv"};
        for (String str : filescvs) {

            for (String str2 : filescvs) {
                if (!str.equals(str2)) {
                    files = new ArrayList<>();
                    files.add(base + str);
                    windowMaker = new MultipleFileWindowMaker(files);
                    // Step 2. Recovery of MFCCs from the word Alpha
                    MFCCLength = FieldLength(base + str);
                    MFCC[] mfccs1 = new MFCC[MFCCLength];

                    for (int i = 0; i < mfccs1.length; i++) {
                        mfccs1[i] = extractor.nextMFCC(windowMaker);
                    }

                    // Step 3. Construction of the alpha Field (MFCC set)
                    Field oneField = new Field(mfccs1);

                    ///File 2
                    files = new ArrayList<>();
                    files.add(base + str2);
                    windowMaker = new MultipleFileWindowMaker(files);
                    MFCCLength = FieldLength(base + str2);
                    MFCC[] mfccs2 = new MFCC[MFCCLength];

                    for (int i = 0; i < mfccs2.length; i++) {
                        mfccs2[i] = extractor.nextMFCC(windowMaker);
                    }

                    // Step 3. Construction of the alpha Field (MFCC set)
                    Field twoField = new Field(mfccs2);
                    //float distance1 = DTWHelperDefault.DTWDistance(oneField, twoField);
                    float distance2 = dtw.DTWDistance(oneField, twoField);

                    System.out.println("first file: " + str + "  Second file: " + str2);
                    System.out.println("Our helper - valeur distance " + str + " - " + str2 + " calculee : " + distance2);
                    //System.out.println("DTWHelperDefault -  distance value "+str+" - "+str2+" : " + distance1);
                    System.out.println("");
                    for (int i = 0; i < 100; i++) {
                        System.out.print("-");
                    }
                    System.out.println();
                    //System.exit(0);
                   
                }
            }
        }

    }
}
