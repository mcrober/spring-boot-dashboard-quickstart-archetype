package ${package}.controller;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import ${package}.service.GitService;
import ${package}.model.dao.git.Deployment;
import ${package}.model.dao.git.GitRepos;
import ${package}.springdatajpa.DeploymentRepository;
import ${package}.springdatajpa.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
class DashboardController {

    @Autowired
    private OcpService ocpService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private GitService gitService;

    @Autowired
    private GitRepository gitRepository;

    @GetMapping("/framework")
    void framework (   ) throws IOException {

        List<Deployment> deployments = deploymentRepository.findAll();
        String pomSuffix = "/contents/pom.xml";

        for (Deployment deployment : deployments) {
            log.info(deployment.getDeployName());
            GitRepos gitRepos = gitRepository.findByRepoName(deployment.getDeployName());
            if (gitRepos != null) {
                gitService.getDataGit("", gitRepos.getReposUrl() + pomSuffix,
                        gitRepos.getReposUrl()+"/contents/package.json");
            }
        }

    }
}
