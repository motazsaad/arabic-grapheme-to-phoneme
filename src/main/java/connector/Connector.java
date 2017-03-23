package connector;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Connector {
    private Connector(){}
    private static final String port = "8080";
    private static final String url = "http://localhost:" + port + "/letters";
    public static Map<Character, String> getGraphemes(){
        Map<Character, String> result = new HashMap<>();
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                builder.append(line);
            }
            JSONObject obj = new JSONObject(builder.toString());
            JSONArray arr = obj.getJSONArray("letters");
            int size = arr.length();
            for (int i = 0; i < size; i++) {
                JSONObject obj = arr.getJSONObject(i);
                Character letter = obj.getString("letter");
                String phoneme = obj.getString("phoneme");
                result.put(letter, phoneme);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
