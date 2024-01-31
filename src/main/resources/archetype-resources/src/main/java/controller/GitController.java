package ${package}.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.GitService;
import ${package}.model.dao.git.GitReposResponse;
import ${package}.springdatajpa.GitRepository;
import ${package}.model.dao.git.GitRepos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;


@RestController
class GitController {

    @Autowired
    private GitService gitService;

    @Autowired
    private GitRepository gitRepository;

    @GetMapping("/repos")
    GitReposResponse[] repos ( @RequestHeader String token,
                               @RequestHeader String paas   ) throws IOException {

        GitReposResponse[] gitResponse = gitService.getRepos(token, paas);

        LocalDate date = LocalDate.now();

        for (int i = 0; i < gitResponse.length; i++){
            GitRepos gitRepos = new GitRepos();
            gitRepos.setFullName(gitResponse[i].getFull_name());
            gitRepos.setRepoName(gitResponse[i].getName());
            gitRepos.setReposUrl(gitResponse[i].getUrl());
            gitRepos.setOrganization("barmanyrober");
            gitRepos.setDate(date);
            gitRepository.save(gitRepos);
        }
        return gitResponse;
    }

    @GetMapping("/pom")
    void pom ( @RequestHeader String token,
                             @RequestHeader String paas   ) throws IOException {

        gitService.getDataGit(token,paas,paas);
        String pomSuffix = "/contents/pom.xml";

    }



}