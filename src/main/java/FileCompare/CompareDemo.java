package FileCompare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;

public class CompareDemo {
    private static final Logger log = LoggerFactory.getLogger(CompareDemo.class);
    private static diff_match_patch diffMatchPatch = new diff_match_patch();

    public static void main(String[] args) {


        String newFilePath = "E:\\compare\\new.txt";
        String oldFilePath = "E:\\compare\\old.txt";


        String newStr = readFile(newFilePath);
        String oldStr = readFile(oldFilePath);


        LinkedList<diff_match_patch.Diff> diffLinkedList = diffMatchPatch.diff_main(oldStr, newStr, true);

        for (diff_match_patch.Diff diff : diffLinkedList) {
            log.info(diff.text);
            diff.operation = diff_match_patch.Operation.INSERT;
        }

        LinkedList<diff_match_patch.Patch> patches = diffMatchPatch.patch_make(oldStr, diffLinkedList);

        for (diff_match_patch.Patch patch : patches) {

            log.info(patch.toString());
        }
        log.info(newStr);

    }

    private static String readFile(String newFilePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(newFilePath);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("utf-8"));

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
