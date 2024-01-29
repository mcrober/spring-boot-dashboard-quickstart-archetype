package ${package}.controller;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import ${package}.model.dao.git.Deployment;
import ${package}.springdatajpa.DeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.fabric8.openshift.api.model.DeploymentConfigList;

import java.io.IOException;

@RestController
class OcpController {

    @Autowired
    private OcpService ocpService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @GetMapping("/deployments")
    JsonNode deployments( @RequestHeader String token,
                          @RequestHeader String paas,
                          @RequestHeader String namespace
    ) throws IOException {
        try {
            return ocpService.getAllDeployments(token,paas,namespace);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/deploymentsConfig")
    DeploymentConfigList deploymentsConfig( @RequestHeader String token,
                                            @RequestHeader String paas,
                                            @RequestHeader String namespace
    )  {
        DeploymentConfigList deploymentsConfig = ocpService.getAllDeploymentsConfig(token,paas,namespace);
        Deployment deployment = new Deployment();
        deployment.setRepoName(deploymentsConfig.getItems().get(0).getMetadata().getName());
        deploymentRepository.save(deployment);
        return deploymentsConfig;
    }

    @GetMapping("/statefulSets")
    JsonNode services(@RequestHeader String token,
                      @RequestHeader String paas,
                      @RequestHeader String namespace
    ) throws IOException {
        try {
            return ocpService.getAllStatefulSets(token,paas,namespace);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
