package ${package}.service.impl;


import com.google.gson.Gson;


import ${package}.model.dao.git.GitReposResponse;
import ${package}.model.dao.git.GitResponse;
import ${package}.model.PackageJson;
import ${package}.service.GitService;
import ${package}.utils.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Bean;


/**
 * Git and deployments utility service implementation
 */
@Service
@Slf4j
public class GitServiceImpl implements GitService {


    /**
     * Rest template
     */
    @Autowired
    public RestTemplate restTemplate;

    /**
     * PIP_FILE
     */
    public static final String PIP_FILE_PATH = "/Pipfile";
    /**
     * String
     * POM_ERROR
     */
    public static final String POM_ERROR = "error";
    /**
     * Pom not found in repo
     */
    public static final String POM_NOT_FOUND = "notFound";
    /**
     * String
     * NOT_JAR
     */
    public static final String NOT_JAR = "notJar";
    /**
     * Git message when reference is not found
     */
    public static final String BAD_REF_MESSAGE = "No commit found for the ref";
    /**
     * Develop branch reference
     */
    public static final String DEVELOP = "develop";
    /**
     * String
     * POM_PATH
     */
    public static final String POM_PATH = "/pom.xml";
    /**
     * String
     * PACKAGE_PATH
     */
    public static final String PACKAGE_PATH = "/package.json";
    /**
     * String
     * Dockerfile Path
     */
    public static final String DOCKERFILE_PATH = "/Dockerfile";

    /**
     * Git service impl constructor

     */
    public GitServiceImpl(  ){

    }

    /**
     * getRepos
     * @param token token
     * @param paas paas
     * @return
     */
    public GitReposResponse[] getRepos (String token,
                            String paas){

        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders(token);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);

        ResponseEntity<GitReposResponse[]> response = restTemplate.exchange(paas, HttpMethod.GET, httpEntity, GitReposResponse[].class);

        GitReposResponse[] reposPage = response.getBody();
        return reposPage;
    }

    /**
     * getDataGit
     *
     * @param token  token
     * @param gitUriPom  String
     * @param gitUriPackage   String
     */
    @Override
    public String getDataGit(String token, String gitUriPom, String gitUriPackage) {
        String result="";
        try {
            //Pom.xml case
            if (gitUriPom != null) {
                result = callGitUriAndRegisterPomInfo(gitUriPom,gitUriPackage,"");
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                //Try with package.json
                try {
                    PackageJson packageJson = callGitUriAndRegisterPackageInfo(gitUriPackage,token);
                    result = packageJson.getDependencies().toString();
                } catch(Exception exception){
                    log.debug("Error " + gitUriPackage, exception);

                }
            } else {
                log.error("Non-managed error from github ",e);
            }
        } catch (Exception e) {
            log.debug("Error " + gitUriPom, e);
        }
        return result;
    }

    /**
     * callGitUriAndRegisterPomInfo
     * @param gitUri gitUri
     * @param gitUriPackage gitUriPackage
     * @throws IOException IOException
     * @throws XmlPullParserException XmlPullParserException
     */
    public String callGitUriAndRegisterPomInfo(String gitUri,  String gitUriPackage, String token)
            throws IOException, XmlPullParserException {
        String parent ;
        ResponseEntity<GitResponse> response = callGitRetryingOnReferenceError(gitUri,token);

        if(response.getBody() == null){
            throw new IllegalArgumentException("Git response for pom is empty");
        } else{
            GitResponse responseBody = response.getBody();

            String rawPom = PomUtils.decodeString(responseBody.getContent());
            Model pomModel = PomUtils.readMavenPom(rawPom);

            parent = pomModel.getParent() == null ? "noParent" : pomModel.getParent().toString();
            System.out.println (parent);
        }
        return parent;
    }

    /**
     * Call main reference of pom or package.json file, if reference does not exist call upper case version,
     * else call develop reference
     * @param gitUri git file uri
     * @return response entity
     */
    @Override
    public ResponseEntity<GitResponse> callGitRetryingOnReferenceError(String gitUri, String token){
        HttpEntity<GitResponse> httpEntity = getHttpEntity(token);
        try {
            return restTemplate.exchange(gitUri, HttpMethod.GET, httpEntity, GitResponse.class);
        } catch (HttpClientErrorException e){
            //Tag may have been lower cased
            if(isReferenceNotFoundError(e)){
                String[] splitUri = gitUri.split("=");
                String upperCaseRefUri = splitUri[0] + "=" + splitUri[1].toUpperCase();
                String developRefUri = splitUri[0] + "=" + "develop";
                try{
                    return restTemplate.exchange(upperCaseRefUri, HttpMethod.GET, httpEntity, GitResponse.class);
                } catch (HttpClientErrorException exc){
                    //If upper case tag neither exists, try with develop branch
                    if(isReferenceNotFoundError(exc)){
                        //If this file neither exists in develop throw this exception and continue flow
                        return restTemplate.exchange(developRefUri, HttpMethod.GET, httpEntity, GitResponse.class);
                    } else{
                        throw exc;
                    }
                }

            } else{
                throw e;
            }
        }
    }


    /**
     * isReferenceNotFoundError
     * @param e
     * @return error
     */
    private boolean isReferenceNotFoundError(@Nonnull HttpClientErrorException e){
        String message = e.getMessage();
        if ( message == null){
            return false;
        } else {
            return e.getStatusCode() == HttpStatus.NOT_FOUND && message.contains(BAD_REF_MESSAGE);
        }
    }

    /**
     * callGitUriAndRegisterPackageInfo
     *
     * @param gitUriPackage gitUriPackage
     * @param token token
     * @return package.json
     */
    private PackageJson callGitUriAndRegisterPackageInfo(String gitUriPackage, String token) {

        ResponseEntity<GitResponse> response = callGitRetryingOnReferenceError(gitUriPackage,token);
        PackageJson packageJson;
        if(response.getBody() == null){
            throw new IllegalArgumentException("Git response for package is empty");
        } else{
            GitResponse responseBody = response.getBody();
            String encodedPom = responseBody == null ? "" : responseBody.getContent();
            String rawContent = new String(Base64.decodeBase64(encodedPom));

            packageJson = new Gson().fromJson(rawContent, PackageJson.class);
            Map<String,String> dependencies = packageJson.getDependencies();
            Map<String,String> devDependencies = packageJson.getDevDependencies();
            log.info(dependencies.toString());
        }
        return packageJson;

    }



    /**
     * getHttpEntity
     *
     * @returnHttpEntity
     */
    private HttpEntity<GitResponse> getHttpEntity(String token) {
        HttpHeaders authHeaders = Util.createTokenAuthorizationHeaders("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");
        HttpEntity<GitResponse> httpEntity = new HttpEntity<>(authHeaders);
        return httpEntity;
    }


}
