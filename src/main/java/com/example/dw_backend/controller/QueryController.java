package com.example.dw_backend.controller;

import com.example.dw_backend.dao.SimpleQuery;
import com.example.dw_backend.dao.TimeQuery;
import com.example.dw_backend.service.SimpleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


/**
 * @author xuedixuedi
 * 返回值都是MovieList
 */
@RestController
@RequestMapping(value = "/api/v1/neo4j/getMovie", produces = {MediaType.APPLICATION_JSON_VALUE})
public class QueryController {

    final private SimpleQueryService simpleQueryService = new SimpleQueryService(
            new SimpleQuery("bolt://localhost:7687", "neo4j", "123"),
            new TimeQuery("bolt://localhost:7687", "neo4j", "123")
    );


    @ResponseBody
    @RequestMapping(value = "/title", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByTitle(@RequestParam String title, @RequestParam int limit){
        return simpleQueryService.generateMovieList(title, limit, "title");
    }

    @ResponseBody
    @RequestMapping(value = "/actor", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByActor(@RequestParam String actor, @RequestParam int limit){
        return simpleQueryService.generateMovieList(actor, limit, "Actor");
    }

    @ResponseBody
    @RequestMapping(value = "/director", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByDirector(@RequestParam String director, @RequestParam int limit){
        return simpleQueryService.generateMovieList(director, limit, "Director");
    }

    @ResponseBody
    @RequestMapping(value = "/label", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByLabel(@RequestParam String label, @RequestParam int limit){
        return simpleQueryService.generateMovieList(label, limit, "Label");
    }

    @ResponseBody
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByScore(@RequestParam Integer score, @RequestParam int limit, @RequestParam String comparison){
        System.out.println("1");
        return simpleQueryService.generateMovieListByScore(score, limit,"score", comparison );
    }

    @ResponseBody
    @RequestMapping(value = "/emotionScore", method = RequestMethod.GET)
    public HashMap<String, Object> getMovieListByEmotion(@RequestParam Integer emotion, @RequestParam int limit, @RequestParam String comparison){
        return simpleQueryService.generateMovieListByScore(emotion, limit,"emotion_score", comparison );
    }

}
