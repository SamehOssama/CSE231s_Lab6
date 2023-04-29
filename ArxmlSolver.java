import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ArxmlSolver {
    public static void main(String[] args) {
        try {
            // Check if the file is of the correct format and not empty.
            String fileName = args[0];
            final String extension = ".arxml";
            if (!fileName.endsWith(extension)) {
                throw new NotVaildAutosarFileException();
            }
            // Get the file as string
            Path filePath = Path.of(fileName);
            String str = Files.readString(filePath);
            if (str.length() == 0) {
                throw new EmptyAutosarFileException();
            }
            
            // Create a template for the array of container properties.
            String[] wordStart = { "<SHORT-NAME>", "UUID=\"", "<LONG-NAME>" };
            String[] wordEnd = { "</SHORT-NAME>", "\"", "</LONG-NAME>" };

            // Get the container count and create a 2D array of containers.
            int containerCount = 0;
            for (int i = str.indexOf("<CONTAINER"); i >= 0; i = str.indexOf("<CONTAINER", i + 1)){
                containerCount++;
            }
            String arr[][] = new String[containerCount][wordStart.length];

            // Get every container's attributes in an array.
            int index = 0;
            for (int i = str.indexOf("<CONTAINER"); i >= 0; i = str.indexOf("<CONTAINER", i + 1)){
                for (int j = 0; j < wordStart.length; j++) {
                    int start = str.indexOf(wordStart[j], i);
                    int end = str.indexOf(wordEnd[j], start + wordStart[j].length());
                    arr[index][j] = str.substring(start + wordStart[j].length(), end);
                }
                index++;
            }
            
            // Sort the container arrays by "SHORT-NAME" attribute.
            StringBuilder newString = new StringBuilder(str);
            Arrays.sort(arr, (o1, o2)-> o1[0].compareTo(o2[0]));

            // Replace the old containers with the new ones.
            index = 0;
            for (int i = str.indexOf("<CONTAINER"); i >= 0; i = str.indexOf("<CONTAINER", i + 1)) {
                for (int j = 0; j < wordStart.length; j++) {
                    int start = str.indexOf(wordStart[j], i);
                    int end = str.indexOf(wordEnd[j], start + wordStart[j].length());
                    newString.replace(start + wordStart[j].length(), end, arr[index][j]);
                }
                index++;
            }

            // Output the rearranged string to the modified file.
            int idx = fileName.lastIndexOf('.');
            String outputFile = fileName.substring(0, idx) + "_mod" + extension;
            Path modifiedFilePath = Path.of(outputFile);
            Files.writeString(modifiedFilePath, newString.toString());
            System.out.println("Done creating the ARXML File");
        } catch (NotVaildAutosarFileException | IOException e) {
            e.printStackTrace();
        }
    }
}
