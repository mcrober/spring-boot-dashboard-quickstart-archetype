package ${package}.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;


/**
 * Common methods to extract framework info
 */
@Slf4j
public class PomUtils {



    /**
     * regex
     */
    private static final Pattern myRegex =  Pattern.compile("\\^|\\~|\\ |((-|\\.)?(RELEASE)?$)",Pattern.CASE_INSENSITIVE);



    /**
     * getStringDescode
     *
     * @param encodedPom String
     * @return String
     */
    public static String decodeString(String encodedPom) {
        return new String(Base64.decodeBase64(encodedPom));
    }

    /**
     * readMavenPom
     *
     * @param rawPom String
     * @return Model
     * @throws IOException            IOException
     * @throws XmlPullParserException XmlPullParserException
     */
    public static Model readMavenPom(String rawPom) throws IOException, XmlPullParserException {
        Reader reader = new StringReader(rawPom);
        MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
        Model pomModel = xpp3Reader.read(reader);
        return pomModel;
    }


    /**
     * Get version from artifact url string
     * @param artifactUrl string
     * @return version
     */
    private String getVersionFromArtifact(String artifactUrl){
        String[] splitArtifact = artifactUrl.split("/");
        int versionIndex = splitArtifact.length - 2;
        if(versionIndex > 0){
            return splitArtifact[versionIndex];
        } else{
            return null;
        }
    }




    /**
     * Get java version or null from pom
     *
     * @param pomModel pom
     * @return version or null
     */
    public String getJavaVersionFromPom(Model pomModel) {
        Properties properties = pomModel.getProperties();
        if (properties == null) {
            return null;
        } else {
            return (String) properties.get("java.version");
        }
    }




    /**
     * getParentFromPom
     *
     * @param pomModel pomModel
     * @return parent label
     */
    public String getParentFromPom(Model pomModel) {
        return pomModel.getParent() == null ? "noParent" : pomModel.getParent().toString();
    }

    /**
     * get version of parent
     *
     * @param pomModel pom model
     * @return parent version
     */
    public String getParentVersion(Model pomModel) {
        return pomModel.getParent() == null ? null : pomModel.getParent().getVersion();
    }



    /**
     * getCleanVersion:
     * remove ^, ~, release|release and last .- ej in ^8.0.0.RELEASE we return 8.0.0
     * remove .RELEASE or -RELEASE
     * @param version version
     * @return cleanVersionNumber
     */
    public String getCleanVersion(String version) {
        if (version.isEmpty()){
            return "";
        }
        String newVersion = myRegex.matcher(version).replaceAll("");

        //clean datagrid version
        if (version.toLowerCase().contains("ga") || (version.length() > 9 && !version.toLowerCase().contains("release")
                && !version.toLowerCase().contains("beta") && !version.toLowerCase().contains("rc"))) {
            if(version.contains("-")){
                version = version.split("-")[1];
            }
            newVersion =version.substring(0,3)+"."+version.substring(version.length()-1);
        }

        return (newVersion);
    }

}