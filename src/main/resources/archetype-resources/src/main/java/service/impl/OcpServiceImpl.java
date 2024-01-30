package org.barmanyrober.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import ${package}.model.dao.git.Deployment;
import ${package}.utils.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import io.fabric8.openshift.api.model.DeploymentConfigList;

/**
 * Class to obtein info from Openshift API
 */
@Getter
@Setter
@Service
@Slf4j
public class OcpServiceImpl implements OcpService {

    /**
     * AUTHORIZATION
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * BEARER BEARER
     */
    public static final String BEARER = "Bearer ";
    /**
     * JSON
     */
    public static final String JSON = "application/json";

    public static final String ACCEPT = "Accept";
    public static final String NAMESPACES = "/api/v1/namespaces/";
    public static final String NAMESPACES2 = "/api/v1/namespaces";
    public static final String ROUTE ="/apis/route.openshift.io/v1/namespaces/";
    public static final String PODS = "/pods";
    public static final String DEPLOYMENTCONFIG = "/deploymentconfigs";
    public static final String DEPLOYMENTS = "/deployments";
    public static final String STATEFULSETS = "/statefulsets";
    public static final String JOBS = "/jobs";
    public static final String METADATA = "metadata";
    public static final String CONTAINERS = "containers";
    private static final Pattern myRegex = Pattern.compile("\"");


    /**
     * RestTemplate
     * restTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private Map<String, Map<String, Map<String, List<String>>>> ocpTokenClusterUrls;


    public OcpServiceImpl (){ }

    /**
     * Returns all deployments from a project in openshift
     * This method infers the pass uri from parameter envCluster
     *
     * @param token token
     * @param paas  paas
     * @return namespace namespace
     */
    public DeploymentConfigList getAllDeploymentsConfig (String token, String paas, String namespace) {
        String ocpApiBuilder="/apis/apps.openshift.io/v1/namespaces/";
        String uri = paas +ocpApiBuilder+namespace+DEPLOYMENTCONFIG;

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<DeploymentConfigList> response = restTemplate().exchange(uri,
                HttpMethod.GET,  httpEntity, DeploymentConfigList.class);

        DeploymentConfigList responseData = response.getBody();

        return responseData;

    }


    /**
     * Returns all deployments from a project in openshift
     * This method infers the pass uri from parameter envCluster
     *
     * @param token token
     * @return projects projects
     * @throws IOException IOException
     */
    public List<Deployment> getAllDeployments (String token, String paas, String namespace)
            throws IOException {

        String ocpApiBuilder="/apis/apps/v1/namespaces/";

        String uri = paas +ocpApiBuilder
                +namespace+DEPLOYMENTS;
        List ocpDeploymentList = new ArrayList();
        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate().exchange(uri,
                HttpMethod.GET,  httpEntity, String.class);

        String responseDataRaw;
        responseDataRaw = response.getBody();

        JsonNode itemsDeployments = JsonTools.convertToJson(responseDataRaw).get("items");

        for (final JsonNode items : itemsDeployments) {

            Deployment ocpDeployment = extractOpenshiftDeploymentConfigTo(items, "");
            ocpDeploymentList.add(ocpDeployment);
        }
        return ocpDeploymentList;
    }

    public Deployment extractOpenshiftDeploymentConfigTo(JsonNode item,  String projectName) {

        Deployment deployment = new Deployment();

        String deploymentName = myRegex.matcher(item.get(METADATA).get("name").toString()).replaceAll("");

        deployment.setProjectName(projectName);
        deployment.setEnvironment("dev");
        deployment.setDate(LocalDate.now());
        deployment.setDeployName(deploymentName);

        JsonNode containers = item.get("spec").get("template").get("spec").get("containers");
        String imageName="";
        String readinessPath = null;
        String livenessPath = null;
        Map<String,String> envVarList = new HashMap<>();
        for (final JsonNode container : containers) {
            imageName = myRegex.matcher(container.get("image").toString()).replaceAll("");
            //get environment variables
            JsonNode envVariables = container.get("env");
            //get liveness path
            JsonNode livenessProbe = container.get("livenessProbe");
            //get readiness path
            JsonNode readinessProbe = container.get("readinessProbe");
            if(livenessProbe != null && livenessProbe.get("httpGet") != null){
                livenessPath = livenessProbe.get("httpGet").get("path").asText();
            }
            if(readinessProbe != null && readinessProbe.get("httpGet") != null){
                readinessPath = readinessProbe.get("httpGet").get("path").asText();
            }

            envVarList =  PodUtils.extractEnvironmentVariables(envVariables);
            deployment.setEnvironmentVars(PodUtils.convertToDBFormatEnvironmentVariables(envVariables));
        }
        if (item.get("status").get("replicas") != null) {
            deployment.setReplicas(item.get("status").get("replicas").asInt());
        }
        deployment.setReadinessPath(readinessPath);
        deployment.setLivenessPath(livenessPath);
        deployment.setMicroName(deploymentName);
        deployment.setDeployImage(imageName);

        //set url
        if (envVarList.get("BUILD_URL") == null) {
            //url is "none" if the field is null
            deployment.setBuildUrl("none");
        } else {
            //set the url if is not null
            deployment.setBuildUrl(envVarList.get("BUILD_URL"));
        }
        if (envVarList.get("ARTIFACT_URL") == null) {
            //url is "none" if the field is null
            deployment.setArtifactUrl("none");
        } else {
            //set the artifact if is not null
            deployment.setArtifactUrl(envVarList.get("ARTIFACT_URL"));
        }

        return deployment;
    }

    /**
     * Returns all services from a project
     *
     * @param token token
     * @return services services
     * @throws IOException IOException
     */
    public JsonNode getAllRoutesFromProject (String token, String paas, String namespace) throws IOException {

        String uri = paas + ROUTE+namespace+"/routes";

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate().exchange(uri,
                HttpMethod.GET,  httpEntity, String.class);
        String responseData;
        responseData = response.getBody();

        return JsonTools.convertToJson(responseData);
    }




    /**
     * Returns all getAllStatefulSets from a project in openshift
     * This method infers the pass uri from parameter envCluster
     *
     * @param token  token
     * @return projects projects
     */
    public JsonNode getAllStatefulSets (String token, String paas, String namespace) throws IOException {

        String ocpApiBuilder="/apis/apps/v1/namespaces/";

        String uri = paas +ocpApiBuilder+namespace+STATEFULSETS;

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate().exchange(uri,
                HttpMethod.GET,  httpEntity, String.class);
        String responseData;
        responseData = response.getBody();

        return JsonTools.convertToJson(responseData);

    }


    /**
     * Returns all deployments from a project in openshift
     * This method infers the pass uri from parameter envCluster
     *
     * @param token token
     * @return projects projects
     * @throws IOException IOException
     */
    public JsonNode getAllJobs (String token, String paas, String namespace)
            throws IOException {

        //String ocpApiBuilder="/apis/batch/v1/namespaces/";
        String ocpApiBuilder = "/oapi/v1/namespaces/";

        String uri = paas +ocpApiBuilder+namespace+JOBS;

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate().exchange(uri,
                HttpMethod.GET,  httpEntity, String.class);
        String responseData;
        responseData = response.getBody();

        return JsonTools.convertToJson(responseData);

    }

    /**
     * Returns all projects in openshift
     * The method is prepared to access any paas uri
     *
     * @param token token
     * @param paas  paas
     * @return proyectos proyectos
     * @throws IOException IOException
     */
    public JsonNode getAllProjects(String token, String paas) throws IOException {

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate().exchange(paas+"/apis/project.openshift.io/v1/projects",
                HttpMethod.GET,  httpEntity, String.class);

        String responseData;
        responseData = response.getBody();

        return JsonTools.convertToJson(responseData);
    }

}

