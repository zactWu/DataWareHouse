package com.example.dw_backend.controller;

import com.example.dw_backend.dao.SimpleQuery;
import com.example.dw_backend.dao.StatisticsQuery;
import com.example.dw_backend.dao.TimeQuery;
import com.example.dw_backend.service.SimpleQueryService;
import com.example.dw_backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/api/v1/neo4j/analysis", produces = {MediaType.APPLICATION_JSON_VALUE})
public class StatisticsController {


    final private SimpleQueryService simpleQueryService = new SimpleQueryService(
            new SimpleQuery("bolt://localhost:7687", "neo4j", "123"),
            new TimeQuery("bolt://localhost:7687", "neo4j", "123")
    );

    final private StatisticsService statisticsService = new StatisticsService(
            new StatisticsQuery("bolt://localhost:7687", "neo4j", "123")
    );

    @ResponseBody
    @RequestMapping(value = "/time/year", method = RequestMethod.GET)
    public HashMap<String, Object> getCountByYearInNeo4j(@RequestParam String time) {
        return simpleQueryService.getTimeCount(time,"year");
    }

    @ResponseBody
    @RequestMapping(value = "/time/season", method = RequestMethod.GET)
    public HashMap<String, Object> getCountBySeasonInNeo4j(@RequestParam String time) {
        return simpleQueryService.getTimeCount(time, "season");
    }

    @ResponseBody
    @RequestMapping(value = "/time/month", method = RequestMethod.GET)
    public HashMap<String, Object> getCountByMonthInNeo4j(@RequestParam String time) {
        return simpleQueryService.getTimeCount(time, "month");
    }

    @ResponseBody
    @RequestMapping(value = "/time/day", method = RequestMethod.GET)
    public HashMap<String, Object> getCountByDayInNeo4j(@RequestParam String time) {
        return simpleQueryService.getTimeCount(time, "day");
    }

    @ResponseBody
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    public HashMap<String, Object>  statisticsScoreInNeo4j() {
        return statisticsService.scoreStatistic("score");
    }

    @ResponseBody
    @RequestMapping(value = "/emotionScore", method = RequestMethod.GET)
    public HashMap<String, Object> statisticsEmotionScoreInNeo4j() {
        return statisticsService.scoreStatistic("emotion_score");
    }

    @ResponseBody
    @RequestMapping(value = "/actor", method = RequestMethod.GET)
    public HashMap<String, Object> statisticsActorInNeo4j(@RequestParam int limit) {
        return statisticsService.otherStatistic(limit, "actor");
    }

    @ResponseBody
    @RequestMapping(value = "/director", method = RequestMethod.GET)
    public HashMap<String, Object> statisticsDirectorInNeo4j(@RequestParam int limit) {
        return statisticsService.otherStatistic(limit, "director");
    }

    @ResponseBody
    @RequestMapping(value = "/label", method = RequestMethod.GET)
    public HashMap<String, Object> statisticsLabelInNeo4j(@RequestParam int limit) {
        return statisticsService.otherStatistic(limit, "label");
    }

//    @ResponseBody
//    @RequestMapping(value = "/score", method = RequestMethod.GET)
//    public HashMap<String, Object> getScoreCountInNeo4j(@RequestParam Integer score, @RequestParam String comparison) {
//        return statisticsService.getScoreCount(score,"score",comparison);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/emotion", method = RequestMethod.GET)
//    public HashMap<String, Object> getEmotionScoreCountInNeo4j(@RequestParam Integer score, @RequestParam String comparison) {
//        return statisticsService.getScoreCount(score,"emotion_score",comparison);
//    }

}
