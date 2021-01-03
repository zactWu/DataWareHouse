package com.example.dw_backend.controller;

import com.example.dw_backend.dao.RelationQuery;
import com.example.dw_backend.model.RelationReturn;
import com.example.dw_backend.service.RelationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/neo4j/getCooperation", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RelationController {

    final private RelationService relationService = new RelationService(
            new RelationQuery("bolt://localhost:7687", "neo4j", "123")
    );


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public RelationReturn getRelation(@RequestParam String type, @RequestParam String name, @RequestParam int limit) {
        return this.relationService.generateRelation(type, name, limit);
    }
}
