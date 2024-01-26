package ${package}.springdatajpa;

import ${package}.model.dao.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, Long>{


}
