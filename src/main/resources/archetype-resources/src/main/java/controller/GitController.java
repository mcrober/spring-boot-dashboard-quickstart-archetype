package org.barmanyrober.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.barmanyrober.service.GitService;
import org.barmanyrober.service.OcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
class GitController {

    @Autowired
    private GitService gitService;


    @GetMapping("/repos")
    String deployments(    ) throws IOException {
        return gitService.getRepos();
    }
}