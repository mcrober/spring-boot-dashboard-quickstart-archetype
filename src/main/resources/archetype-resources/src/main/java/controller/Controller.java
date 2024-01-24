package ${package}.controller;


import com.fasterxml.jackson.databind.JsonNode;
import ${package}.service.OcpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class Controller {
    @Autowired
    private OcpService ocpService;



    @GetMapping("/deployments")
    JsonNode deployments( @RequestHeader String token) throws IOException {
        try {
            return ocpService.getAllDeployments(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/services")
    JsonNode services(@RequestHeader String token ) throws IOException {
        try {
            return ocpService.getAllDeployments(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
