package ${package}.utils;




import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class to handle openshift pods
 */
public class PodUtils {

    /**
     * myRegex myRegex
     */
    private static final Pattern myRegex = Pattern.compile("\"");

    private static final String VALUE = "value";


    /**
     * Constructor
     */
    private PodUtils() {
        throw new IllegalStateException("Utility class");
    }



    /**
     * extractEnvironmentVariables
     *
     * Gets environment variables from Openshift and converts it to a Map structure
     *
     * @param variablesEnCrudo variablesEnCrudo
     * @return lista de variables de entorno
     */
    public static Map<String,String> extractEnvironmentVariables(JsonNode variablesEnCrudo) {
        HashMap<String, String> environmentVariables = new HashMap<>();
        var key = "";
        var value = "";

        if (variablesEnCrudo != null && variablesEnCrudo.isArray()) {

            for (final JsonNode item : variablesEnCrudo) {

                if (item.get("name") != null) {
                    key = myRegex.matcher(item.get("name").toString()).replaceAll( "");
                }
                if (item.get(VALUE) != null) {
                    value = myRegex.matcher(item.get(VALUE).toString()).replaceAll("");
                }
                environmentVariables.put(key,value);
            }
        }
        return environmentVariables;
    }



    /**
     * convertToDataBaseFormatEnvironmentVariables
     * Converts env variables returned from oc in json format to the format:
     * {"clave":"valor","clave2":"valor2"}
     *
     * @param rawArrayVariables rawArrayVariables
     * @return String format {"clave":"valor","clave2":"valor2"}
     */
    public static String convertToDBFormatEnvironmentVariables (JsonNode rawArrayVariables) {
        var environmentVariables = "";

        if (rawArrayVariables != null && rawArrayVariables.isArray()) {
            environmentVariables = rawArrayVariables.toString();
        }
        return environmentVariables;
    }



}




