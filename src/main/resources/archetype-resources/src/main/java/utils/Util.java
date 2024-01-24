package ${package}.utils;


import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
/**
 * Class of Utils
 * @author Santander Technology
 **/
public class Util {

    /**
     * AUTHORIZATION
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * BEARER BEARER
     */
    public static final String BEARER = "Bearer ";
    /**
     * JSON
     */
    public static final String JSON = "application/json";

    public static final String ACCEPT = "Accept";
    /**
     * Builder
     */
    private Util() {
    }

    /**
     * Create Authorization HttpHeaders to use in the RestTemplate call
     * @param username String
     * @param nassword String
     * @return HttpHeaders
     */
    public static HttpHeaders createAuthorizationHeaders(String username, String nassword){
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + nassword;
        String encodedAuth = Base64.encodeBase64String(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + encodedAuth;
        headers.set(AUTHORIZATION,authHeader);
        return headers;
    }

    /**
     * Create Authorization HttpHeaders to use in the RestTemplate call
     * @param token token
     * @return HttpHeaders
     */
    public static HttpHeaders createTokenAuthorizationHeaders(String token){
        HttpHeaders headers = new HttpHeaders();

        headers.set(AUTHORIZATION,BEARER + token);
        return headers;
    }

    /**
     * Return true if object is not null
     * @param o object
     * @return bool
     */
    public static boolean isNotNull(Object o){
        return o != null;
    }

    /**
     * Null safe is empty string
     * @param str string to check
     * @return boolean
     */
    public static boolean isEmptyString(String str){
        return str == null || "".equals(str);
    }


    /**
     * Read a script from the provided LineNumberReader, using the supplied comment prefixes and statement separator, and build a String containing the lines.
     * Lines beginning with one of the comment prefixes are excluded from the results; however, line comments anywhere else — for example, within a statement — will be included in the results.
     * @param lineNumberReader the LineNumberReader containing the script to be processed
     * @param commentPrefixes the prefixes that identify comments in the SQL script (typically "--")
     * @param separator the statement separator in the SQL script (typically ";")
     * @param blockCommentEndDelimiter the end block comment delimiter
     * @return a String containing the script lines
     * @throws IOException in case of I/O errors
     */
    public static String readScript(LineNumberReader lineNumberReader, @Nullable String[] commentPrefixes,
                                    @Nullable String separator, @Nullable String blockCommentEndDelimiter) throws IOException {

        String currentStatement = lineNumberReader.readLine();
        StringBuilder scriptBuilder = new StringBuilder();
        while (currentStatement != null) {
            if ((blockCommentEndDelimiter != null && currentStatement.contains(blockCommentEndDelimiter)) ||
                    (commentPrefixes != null && !startsWithAny(currentStatement, commentPrefixes, 0))) {
                if (scriptBuilder.length() > 0) {
                    scriptBuilder.append('\n');
                }
                scriptBuilder.append(currentStatement);
            }
            currentStatement = lineNumberReader.readLine();
        }
        appendSeparatorToScriptIfNecessary(scriptBuilder, separator);
        return scriptBuilder.toString();
    }

    /**
     * Removes ampersand from the given string
     * @param parameter the string to be filtered
     * @return the filtred string
     */
    public static String filterUserInput(String parameter) {
        return parameter == null ? null : parameter.replace("&","");
    }

    private static void appendSeparatorToScriptIfNecessary(StringBuilder scriptBuilder, @Nullable String separator) {
        if (separator == null) {
            return;
        }
        String trimmed = separator.trim();
        if (trimmed.length() == separator.length()) {
            return;
        }
        // separator ends in whitespace, so we might want to see if the script is trying
        // to end the same way
        if (scriptBuilder.lastIndexOf(trimmed) == scriptBuilder.length() - trimmed.length()) {
            scriptBuilder.append(separator.substring(trimmed.length()));
        }
    }

    private static boolean startsWithAny(String script, String[] prefixes, int offset) {
        for (String prefix : prefixes) {
            if (script.startsWith(prefix, offset)) {
                return true;
            }
        }
        return false;
    }
}

