package ${package}.service;


import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;
import io.fabric8.openshift.api.model.DeploymentConfigList;

/**
 * Class to obtein info from Openshift API
 */

public interface OcpService {


    DeploymentConfigList getAllDeploymentsConfig(String token, String paas, String namespace);

    JsonNode getAllStatefulSets(String token, String paas, String namespace) throws IOException;

    JsonNode getAllDeployments(String token, String paas, String namespace) throws IOException;

    DeploymentConfigList getAllDeployments2 (String token, String paas, String namespace) throws IOException;
}
