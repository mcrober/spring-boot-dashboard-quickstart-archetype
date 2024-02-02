package ${package}.jpa;

import ${package}.model.dao.git.GitRepos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitJpaRepository extends JpaRepository<GitRepos, Long>{

    GitRepos findByRepoName ( String repoName);
}