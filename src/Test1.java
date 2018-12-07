
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

public class Test1 {

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

        int MFCCLength;
        //DTWHelper myDTWHelper= new myDTW();
        DTWHelper DTWHelperDefault = new DTWHelperDefault();
        myMFCCdistance t = new myMFCCdistance();
        // Path to audio files
        String base = "/test/audio/";

        // Calling the default extractor (MFCC calculation)
        Extractor extractor = Extractor.getExtractor();

        // Step 1. Reading Alpha file
        List<String> files = new ArrayList<>();
        files.add(base + "Alpha.csv");
        WindowMaker windowMaker = new MultipleFileWindowMaker(files);

        // Step 2. Recovery of MFCCs from the word Alpha
        MFCCLength = FieldLength(base + "Alpha.csv");
        MFCC[] mfccsAlpha = new MFCC[MFCCLength];

        for (int i = 0; i < mfccsAlpha.length; i++) {
            mfccsAlpha[i] = extractor.nextMFCC(windowMaker);
        }

        // Step 3. Construction of the alpha Field (MFCC set)
        Field alphaField = new Field(mfccsAlpha);

        // Field and each MFCC display
        System.out.println(alphaField.toString());
        for (int i = 0; i < alphaField.getLength(); i++) {
            System.out.println(i + ": " + alphaField.getMFCC(i).toString());
        }

        // If you want to add new words, just repeat steps 1 to 3
        // For example, we can test that the distance between alpha and alpha is 0
        // float mydistanceAlphaAlpha= myDTWHelper.DTWDistance(alphaField, alphaField);
        //float distanceAlphaAlphadefault = DTWHelperDefault.DTWDistance(alphaField, alphaField);
        // System.out.println("myDTW -  distance value Alpha-Alpha  : "+mydistanceAlphaAlpha);
        //System.out.println("DTWHelperDefault - distance value Alpha-Alpha  : " + distanceAlphaAlphadefault);
        // Distance between the words Alpha and Bravo
        // Step 1. Bravo Reading
        files = new ArrayList<>();
        files.add(base + "Bravo.csv");
        MFCCLength = FieldLength(base + "Bravo.csv");
        windowMaker = new MultipleFileWindowMaker(files);

        // Step 2. Recovery of MFCCs from the word Bravo
        MFCC[] mfccsBravo = new MFCC[MFCCLength];
        for (int i = 0; i < mfccsBravo.length; i++) {
            mfccsBravo[i] = extractor.nextMFCC(windowMaker);
        }

        // Step 3. Construction of the Bravo Field (MFCC set)
        Field bravoField = new Field(mfccsBravo);

        //again
           
        files = new ArrayList<>();
        files.add(base + "Charlie.csv");
        
        windowMaker = new MultipleFileWindowMaker(files);

        // Step 2. Recovery of MFCCs from the word Alpha
        MFCCLength = FieldLength(base + "Charlie.csv");
        
        MFCC[] mfccsCharlie = new MFCC[MFCCLength];
        for (int i = 0; i < mfccsCharlie.length; i++) {
            mfccsCharlie[i] = extractor.nextMFCC(windowMaker);
        }

        // Step 3. Construction of the alpha Field (MFCC set)
        Field charlieField = new Field(mfccsCharlie);

        //
     
        //float mydistanceAlphaBravo= myDTWHelper.DTWDistance(alphaField, bravoField);
        float distanceAlphaBravodefault = DTWHelperDefault.DTWDistance(alphaField, bravoField);
        float distanceAlphaCharliefault = DTWHelperDefault.DTWDistance(alphaField, charlieField);
        myDTW dtw = new myDTW();

        float alphabravo = dtw.DTWDistance(alphaField, bravoField);
        float alphaCharlie = dtw.DTWDistance(alphaField, charlieField);

        //System.out.println("myDTW - valeur distance Alpha-Bravo calculee : "+mydistanceAlphaBravo);
        System.out.println("DTWHelperDefault -  distance value Alpha-Bravo : " + distanceAlphaBravodefault);
        System.out.println("myDTW -  distance value Alpha-Bravo : " + alphabravo);

        System.out.println("DTWHelperDefault -  distance value Alpha-Bravo : " + distanceAlphaCharliefault);
        System.out.println("myDTW -  distance value Alpha-Charlie : " + alphaCharlie);

    }
}
