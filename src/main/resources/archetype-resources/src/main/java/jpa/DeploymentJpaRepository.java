package ${package}.jpa;

import ${package}.model.dao.Deployment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class DeploymentJpaRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void insert(Deployment deployment) {
		entityManager.merge(deployment);
	}
	
	public Deployment findById(long id) {
		return entityManager.find(Deployment.class, id);
	}

	public void deleteById(long id) {
		Deployment deployment = entityManager.find(Deployment.class, id);
		entityManager.remove(deployment);
	}

}
