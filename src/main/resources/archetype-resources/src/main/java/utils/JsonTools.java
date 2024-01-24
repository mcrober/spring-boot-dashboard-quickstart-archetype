package ${package}.utils;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;


/**
 * Class to deal with json
 */
public class JsonTools {

    private static final Pattern myRegex = Pattern.compile("\"");
    private static final Pattern myRegexN = Pattern.compile("\\\\n");

    /**
     * Constructor
     */
    private JsonTools() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Convertir a json
     *
     * @param data data
     * @return JsonNode JsonNode
     * @throws IOException IOException
     */
    public static JsonNode convertToJson(String data) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(data);
    }

    /**
     * readPackageParentJsonDocument
     * @param jsonFile jsonFile
     * @param nodo nodo
     * @return nodo nodo
     * @throws IOException IOException
     */
    public static JsonNode readPackageParentJsonDocument(JsonNode jsonFile, String nodo) throws IOException {

        JsonNode jsonFileContent = jsonFile.get("content");

        String jsonTxt = myRegexN.matcher(myRegex.matcher(jsonFileContent.toString()).replaceAll("")).replaceAll("");

        Base64 decoder = new Base64();
        byte[] decodedBytes = decoder.decode(jsonTxt);
        String s = new String(decodedBytes, StandardCharsets.UTF_8);

        return convertToJson(s).get(nodo);
    }



}

