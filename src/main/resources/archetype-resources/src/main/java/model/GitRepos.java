package ${package}.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Table INPUT_GIT_REPOSITORIES object
 */
@Entity
@Table(name = "INPUT_GIT_REPOSITORIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GitRepos.GitReposKey.class)
@Builder
public class GitRepos implements Persistable<GitRepos.GitReposKey> {
    // All git repos and organizations for matching with deployment configs

    /**
     * org + repo
     */
    @Column(name="FULL_NAME")
    private String fullName;

    /**
     * repo Name
     */
    @Id
    @Column(name="REPO_NAME", nullable = false)
    private String repoName;

    /**
     * organization
     */
    @Id
    @Column(name="ORGANIZATION", nullable = false)
    private String organization;

    /**
     * Git repos api url
     */
    @Column(name="REPOS_URL")
    private String reposUrl;

    /**
     * Date
     */
    @Column(name="DATA_DATE")
    private LocalDate date;

    /**
     * Object id
     * @return GitReposKey
     */
    @Override
    public GitRepos.GitReposKey getId() {
        return new GitReposKey(repoName, organization);
    }

    /**
     * optimize persistable write not checking before commit
     * @return true
     */
    @Override
    public boolean isNew() {
        return true;
    }

    /**
     * Primary Key
     */
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GitReposKey implements Serializable {

        /**
         * UID
         */
        private static final long serialVersionUID = -8184842077565979243L;

        /**
         * Repo name
         */
        private String repoName;

        /**
         * Organization
         */
        private String organization;
    }
}