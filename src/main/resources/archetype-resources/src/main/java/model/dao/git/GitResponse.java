
package ${package}.model.dao.git;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.barmanyrober.model.Links;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitResponse {
    private String name;
    private String path;
    private String sha;
    private String url;
    private String htmlUrl;
    private String gitUrl;
    private String downloadUrl;
    private String type;
    private String content;
    private String encoding;
    private Links links;


}