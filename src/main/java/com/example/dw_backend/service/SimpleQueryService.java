package com.example.dw_backend.service;

import com.example.dw_backend.dao.SimpleQuery;
import com.example.dw_backend.dao.TimeQuery;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimpleQueryService {

    private final SimpleQuery simpleQuery;
    private final TimeQuery timeQuery;

    public SimpleQueryService (SimpleQuery simpleQuery, TimeQuery timeQuery){
        this.simpleQuery = simpleQuery;
        this.timeQuery = timeQuery;
    }

    public HashMap<String, Object> generateMovieListByScore(Integer score, int limit, String scoreType, String cmp){
        List<HashMap<String, String>> ans= new ArrayList<>();
        List<Record> recordList;
        long startTime = System.currentTimeMillis();    //获取开始时间
        recordList = simpleQuery.queryMovieByScore(score, limit, scoreType, cmp);
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        for (Record record: recordList) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("productId", record.get("product_id").toString());
            item.put("score", record.get("score").toString());
            item.put("emotionScore", record.get("emotion_score").toString());
            item.put("title", record.get("title").toString());
            item.put("versionCount", record.get("versionCount").toString());
            ans.add(item);
        }
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("time", time);
        ret.put("movieList", ans);
        return ret;
    }

    public HashMap<String, Object> generateMovieList(String data, int limit, String queryType){

        List<HashMap<String, String>> ans= new ArrayList<>();
        List<Record> recordList;
        long startTime = System.currentTimeMillis();    //获取开始时间
        switch (queryType) {
            case "title":
            case "Title":
                recordList = simpleQuery.queryByTitle(data, limit);
                break;
            case "actor":
            case "Actor":
                recordList = simpleQuery.queryMovieByActor(data, limit);
                break;
            case "director":
            case "Director":
                recordList = simpleQuery.queryMovieByDirector(data, limit);
                break;
            case "label":
            case "Label":
                recordList = simpleQuery.queryMovieByLabel(data, limit);
                break;
            default:
                System.out.println("error");
                return new HashMap<String, Object>();
        }

        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;

        for (Record record: recordList) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("productId", record.get("product_id").toString());
            item.put("score", record.get("score").toString());
            item.put("emotionScore", record.get("emotion_score").toString());
            item.put("title", record.get("title").toString());
            item.put("versionCount", record.get("versionCount").toString());
            ans.add(item);
        }
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("time", time);
        ret.put("movieList", ans);
        return ret;
    }

    public HashMap<String, Object> getTimeCount(String timeData, String timeType){

        List<HashMap<String, String>> ans= new ArrayList<>();
        List<Record> recordList;
        List<String> dataList;
        long startTime;    //获取开始时间
        switch (timeType){
            case "year":
                startTime = System.currentTimeMillis();
                recordList = timeQuery.queryTimeByYear(timeData);
                break;
            case "season":
                dataList = Arrays.asList(timeData.split("-"));
                System.out.println(dataList);
                int month = Integer.parseInt(dataList.get(1));
                Integer season;
                if (month<=3){
                    season = 1;
                } else if (month<=6){
                    season = 2;
                } else if (month<=9) {
                    season = 3;
                } else if (month<=12){
                    season = 4;
                } else {
                    System.out.println("error");
                    season = -1;
                }
                startTime = System.currentTimeMillis();
                recordList = timeQuery.queryTimeBySeason(dataList.get(0), season.toString());
                break;
            case "month":
                dataList = Arrays.asList(timeData.split("-"));
                startTime = System.currentTimeMillis();
                recordList = timeQuery.queryTimeByMonth(dataList.get(0), dataList.get(1));
                break;
            case "day":
                dataList = Arrays.asList(timeData.split("-"));
                startTime = System.currentTimeMillis();
                recordList = timeQuery.queryTimeByDay(dataList.get(0), dataList.get(1), dataList.get(2));
                break;
            default:
                System.out.println("error");
                return new HashMap<>();
        }
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;
        HashMap<String, Object> ret = new HashMap<String, Object>();
        ret.put("time", time);
        ret.put("count", recordList.get(0).get("count").toString());
        return ret;
    }

    public static void main(String[] args) {
        SimpleQuery query = new SimpleQuery("bolt://localhost:7687", "neo4j", "your_password");
        TimeQuery timeQuery = new TimeQuery("bolt://localhost:7687", "neo4j", "your_password");
        SimpleQueryService service = new SimpleQueryService(query, timeQuery);
        System.out.println(service.getTimeCount("2000-10","month"));
        System.out.println(service.generateMovieList("Dr. Seuss - The Cat in the Hat",5, "title"));
    }
}
