package ${package}.service;

import ${package}.model.dao.git.GitReposResponse;
import ${package}.model.dao.git.GitResponse;

import org.springframework.http.ResponseEntity;

/**
 * Service for git and deployments utility
 */
public interface GitService {

    /**
     * getRepos
     * @param token
     * @param paas
     * @return
     */
    public GitReposResponse[] getRepos(String token,
                           String paas);

    /**
     * Get pom from git and set data
     * @param gitUriPom pom uri
     * @param gitUriPackage package uri
     * @param darwinParentModel darwin model
     * @param nuarParentModel nuar model
     * @param realTimeParenModel realtime model
     */
    void getDataGit(String gitUriPom, String gitUriPackage, String darwinParentModel,
                    String nuarParentModel, String realTimeParenModel);

    /**
     * Call git and retry with develop branch if response is reference error
     * @param gitUri uri to call
     * @return response entity
     */
    ResponseEntity<GitResponse> callGitRetryingOnReferenceError(String gitUri);
}
