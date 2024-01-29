package ${package}.springdatajpa;

import ${package}.model.dao.git.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long>{


}
