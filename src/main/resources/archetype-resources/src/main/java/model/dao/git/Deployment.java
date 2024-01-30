package ${package}.model.dao.git;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.LocalDate;
import java.io.Serializable;
import java.time.LocalDate;


/**
 *
 * Entity for table
 * INPUT_OCP_DEPLOYS
 * @author Santander Technology
 */
@Entity
@Table(name = "INPUT_OCP_DEPLOYS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(Deployment.DeploymentKey.class)
@Builder
public class Deployment implements Persistable<Deployment.DeploymentKey> {

    /**
     * Id
     * Column DATA_DATE
     */
    @Id
    @Column(name = "DATA_DATE", nullable = false)
    private LocalDate date;

    /**
     * Id
     * Column OCP_PROJECT
     */
    @Id
    @Column(name = "OCP_PROJECT")
    private String projectName;

    /**
     * Id
     * Column OCP_ENVIRONMENT
     */
    @Id
    @Column(name = "OCP_ENVIRONMENT")
    private String environment;

    /**
     * Id
     * Column OCP_DEPLOY_NAME
     */
    @Id
    @Column(name = "OCP_DEPLOY_NAME")
    private String deployName;

    /**
     * Microservice (block) name
     * Column OCP_MICRO_NAME
     */
    @Column(name = "OCP_MICRO_NAME")
    private String microName;


    /**
     * Column OCP_DEPLOY_IMAGE
     */
    @Column(name = "OCP_DEPLOY_IMAGE")
    private String deployImage;

    /**
     * Column OCP_DEPLOY_REPLICAS
     */
    @Column(name = "OCP_DEPLOY_REPLICAS")
    private Integer replicas;

    /**
     * Column OCP_ARTIFACT_URL
     */
    @Column(name = "OCP_ARTIFACT_URL")
    private String artifactUrl;

    /**
     * Technology version
     */
    @Column(name = "TECH_VERSION")
    private String techVersion;

    /**
     * Docker base image
     */
    @Column(name ="BASE_IMAGE")
    private String baseImage;



    /**
     * Column FRAMEWORK
     */
    @Column(name = "FRAMEWORK_VERSION")
    private String frameworkVersion;



    /**
     * Column OCP_DEPLOY_POM
     */
    @Column(name = "OCP_DEPLOY_POM")
    private String encodedPom;

    /**
     * Column OCP_DEPLOY_PARENT
     */
    @Column(name = "OCP_DEPLOY_PARENT")
    private String deployParent;


    /**
     *  Column ENV_VAR
     */
    @Column(name = "ENV_VARS")
    private String environmentVars;

    /**
     *  Column OCP_DEPLOY_BUILD_URL
     */
    @Column(name = "OCP_DEPLOY_BUILD_URL")
    private String buildUrl;


    /**
     *  Column OCP_DEPLOY_BUILD_URL
     */
    @Column(name = "GIT_REPO")
    private String gitRepo;

    /**
     *  Column HC_READINESS
     */
    @Column(name = "HC_READINESS")
    private String readinessPath;

    /**
     *  Column HC_LIVENESS
     */
    @Column(name = "HC_LIVENESS")
    private String livenessPath;


    @Override
    public DeploymentKey getId() {
        return new DeploymentKey(projectName, environment, deployName,  date);
    }

    @Override
    public boolean isNew() {
        return true;
    }


    /**
     * Primary key for table OcpDeploymentKey
     */
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class DeploymentKey implements Serializable {

        private static final long serialVersionUID = -4006713129667541714L;
        private String projectName;

        private String environment;

        private String deployName;

        private LocalDate date;

    }
}