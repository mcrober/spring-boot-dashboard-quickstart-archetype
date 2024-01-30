package ${package}.controller;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import ${package}.model.dao.git.Deployment;
import ${package}.springdatajpa.DeploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.fabric8.openshift.api.model.DeploymentConfigList;
import java.time.LocalDate;
import java.io.IOException;
import java.util.List;

@RestController
class OcpController {

    @Autowired
    private OcpService ocpService;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @GetMapping("/deployments")
    List<Deployment> deployments(@RequestHeader String token,
                                 @RequestHeader String paas,
                                 @RequestHeader String namespace
    ) throws IOException {
        try {
            List<Deployment> deployments = ocpService.getAllDeployments(token,paas,namespace);

            for (int i=0;i<deployments.size();i++) {
                deploymentRepository.save(deployments.get(i));
            }
            return deployments;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * deploymentsConfig
     * @param token token
     * @param paas paas
     * @param namespace namespace
     * @return
     */
    @GetMapping("/deploymentsConfig")
    DeploymentConfigList deploymentsConfig( @RequestHeader String token,
                                            @RequestHeader String paas,
                                            @RequestHeader String namespace
    )  {
        DeploymentConfigList deploymentsConfig = ocpService.getAllDeploymentsConfig(token,paas,namespace);
        for (int i=0;i<=deploymentsConfig.getItems().size();i++) {
            Deployment deployment = new Deployment();
            deployment.setDeployName(deploymentsConfig.getItems().get(i).getMetadata().getName());
            LocalDate date = LocalDate.now();
            deployment.setDate(date);
            deploymentRepository.save(deployment);
        }
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
