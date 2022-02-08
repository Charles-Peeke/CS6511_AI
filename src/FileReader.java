import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileReader {
    List<String> text = new ArrayList<String>();
    public int[] pitchers;
    public int goal;

    public void readFile(String name) throws Exception{
        ClassLoader cL = getClass().getClassLoader();
        InputStream iS = cL.getResourceAsStream(name);
        try(InputStreamReader sR = new InputStreamReader(iS, StandardCharsets.UTF_8);
            BufferedReader bR = new BufferedReader(sR)) {
            String str;

            // file -> string list
            while ((str = bR.readLine()) != null) {
                text.add(str);
            }


            // list -> array
            String[] arr = new String[text.size()];
            arr = text.toArray(arr);    // arr[0] = pitchers, arr[1] = goal

            String[] numArr = arr[0].split(",");
            goal = Integer.valueOf(arr[1]);
            pitchers = new int[numArr.length];
            for (int i = 0; i < numArr.length; i++) {
                pitchers[i] = Integer.valueOf(numArr[i]);
            }
        }
    }
}
