package ${package}.controller;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import ${package}.service.GitService;
import ${package}.model.dao.git.Deployment;
import ${package}.springdatajpa.DeploymentRepository;
import ${package}.springdatajpa.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.fabric8.openshift.api.model.DeploymentConfigList;
import java.time.LocalDate;
import java.io.IOException;
import java.util.List;

@RestController
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


        for ( int i=0; i <deployments.size();i++){

            GitRepos gitRepos = gitRepository.findByRepoName(deployments.get(i).getDeployName());
            if (gitRepos != null) {
                gitService.getDataGit("", gitRepos.getReposUrl() + pomSuffix, "");
            }
        }

    }
}
