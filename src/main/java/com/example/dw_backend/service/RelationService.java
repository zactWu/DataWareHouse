package com.example.dw_backend.service;
import com.example.dw_backend.dao.RelationQuery;
import com.example.dw_backend.model.RelationReturn;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RelationService {

    private final RelationQuery relationQuery;

    public RelationService(RelationQuery relationQuery){
        this.relationQuery = relationQuery;
    }

    public RelationReturn generateRelation(final String type, final String startName, final int limit){

        List<HashMap<String, String>> ans= new ArrayList<>();

        long startTime = System.currentTimeMillis();    //获取开始时间
        List<Record> recordList = relationQuery.findRelation(startName, type);
        long endTime = System.currentTimeMillis();    //获取开始时间
        long time = endTime - startTime;

        for (Record record: recordList) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", record.get("name").toString());
            item.put("count", record.get("count").toString());
            System.out.println(item);
            ans.add(item);
        }

        RelationReturn relationReturn = new RelationReturn(time, ans);
        return relationReturn;
    }

    public static void main(String[] args) {
        RelationQuery query = new RelationQuery("bolt://localhost:7687", "neo4j", "123");
        RelationService service = new RelationService(query);
        service.generateRelation("Avery Brooks", "a-d",5);
    }
}
