package ${package}.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Git response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepoOwner {

    // Git api response owner

    /**
     * login
     */
    private String login;

    /**
     * Repos url
     */
    @JsonProperty("repos_url")
    private String reposUrl;

}