package ${package}.model;

import lombok.Data;

import java.util.Map;

/**
 * PackageJson
 * Response for
 * Json
 *
 * @author Santander Technology
 */
@Data
public class PackageJson {

    private String name;
    private String version;
    private String license;
    private Map<String, String> scripts;
    private Map<String, String> dependencies;
    private Map<String, String> devDependencies;

}