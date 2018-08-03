import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

/**
 * @author jaydeep
 */
public class JSONProcessor {
    private static final String lead = "leads";

    public JSONArray parseData(String fileName) {
        Object object;
        JSONObject jsonObject;
        JSONArray jsonArray = null;
        try {
            object = new JSONParser().parse(new FileReader(fileName));
            jsonObject = (JSONObject) object;
            jsonArray = (JSONArray) jsonObject.get(lead);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}