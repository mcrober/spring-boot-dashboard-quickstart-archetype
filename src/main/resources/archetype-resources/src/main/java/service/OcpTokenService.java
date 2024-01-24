package ${package}.service;

import ${package}.utils.Util;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import java.net.URI;
import java.util.Map;



/**
 * Service Class to get tokens from openshift
 *
 * @author Santander Technology
 */
@Getter
@Setter
public class OcpTokenService {


    /**
     * OAUTHSUFFIX
     */
    private static final String OAUTHSUFFIX = "/oauth/authorize?client_id=openshift-challenging-client&response_type=token";

    /**
     * RestTemplate
     * restTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final String username;

    private final String nassword;




    /**
     * OcpTokenService constructor
     *
     * @param username username
     * @param nassword nassword

     */
    public OcpTokenService(String username, String nassword) {

        this.username = username;
        this.nassword = nassword;


    }




    /**
     * getToken: gets token from openshift
     *
     * @param paas paas
     * @return token
     */
    public String getToken(String paas) {

        String token ="";
        String url = paas + OAUTHSUFFIX;
        ResponseEntity<String> response = restTemplate().exchange(url, HttpMethod.GET, buildHttpEntity(), String.class);

        URI location = response.getHeaders().getLocation();
        if (location != null) {
            token = location.getFragment().split("=")[1].split("&")[0];
        }
        return token;
    }



    /**
     * getTokenFromEnvLocationProvider
     * @param tokens tokens
     * @return token
     */
    public String getTokenFromEnvLocationProvider( Map<String,String> tokens ) {

        String ocToken =tokens.get("DEV-BO1"+"-");

        return ocToken;
    }

    /**
     * getTokenFromEnvLocationProvider
     * @param ocpProvider ocpProvider
     * @param tokens tokens
     * @return token
     */
    public String getTokenFromEnvLocationProvider(String ocpProvider,
                                                  Map<String,String> tokens ) {

        String ocToken =tokens.get("DEV-SAN"+"-"+ocpProvider);

        return ocToken;
    }


    /**
     * buildHttpEntity
     * @return httpentity httpentity
     */
    private HttpEntity<String[]> buildHttpEntity (){

        HttpHeaders authHeaders = Util.createAuthorizationHeaders(username, nassword);
        HttpEntity<String[]> httpEntity = new HttpEntity<>(authHeaders);
        return httpEntity;
    }




}




