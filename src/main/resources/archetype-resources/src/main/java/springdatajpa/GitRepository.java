package ${package}.springdatajpa;

import ${package}.model.dao.git.GitRepos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitRepository extends JpaRepository<GitRepos, Long>{


}
