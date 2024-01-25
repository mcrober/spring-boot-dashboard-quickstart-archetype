
package ${package}.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GitReposResponse {

    /**
     *  Repo name
     */
    private String name;

    /**
     * org + repo
     */
    @JsonProperty("full_name")
    private String fullName;

    /**
     * Repo owner
     */
    private String owner;

    /**
     * Repo url
     */
    private String url;
}
