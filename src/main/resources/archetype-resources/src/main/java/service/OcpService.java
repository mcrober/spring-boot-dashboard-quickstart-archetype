package ${package}.service;


import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.fabric8.openshift.api.model.DeploymentConfigList;
import org.barmanyrober.model.dao.git.Deployment;

/**
 * Class to obtein info from Openshift API
 */

public interface OcpService {



    DeploymentConfigList getAllDeploymentsConfig(String token, String paas, String namespace);

    JsonNode getAllStatefulSets(String token, String paas, String namespace) throws IOException;


    List<Deployment> getAllDeployments (String token, String paas, String namespace) throws IOException;
}
