package com.example.dw_backend.dao;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class TimeQuery implements AutoCloseable{
    private final Driver driver;

    public TimeQuery(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    /**
     * ① 按照时间进行查询及统计
     * 粒度：年份
     * 某年的电影数
     */
    public List<Record> queryTimeByYear(final String year){
        try(Session session = driver.session()){
            List<Record> cnt = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result;
                    result = transaction.run("match(t:Time) where t.year=$year return sum(t.year_count) as count",
                            parameters("year", Integer.valueOf(year)));
                    List<Record> resultList = result.list();
                    return resultList;
//                    if(resultList.size()==0){
//                        return "";
//                    } else {
//                        return resultList.get(0).get(result.keys().get(0)).toString();
//                    }
                }
            });
            return cnt;
        }
    }

    /**
     * ① 按照时间进行查询及统计
     * 粒度：季度
     * 某年某季度的电影数
     * cmp: 0 相等 1 after -1 before
     */
    public List<Record> queryTimeBySeason(final String year, final String season){
        try(Session session = driver.session()){
            List<Record> cnt = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result;
                    result = transaction.run("match(t:Time) where t.year=$year and t.season=$season return sum(t.season_count) as count",
                            parameters("year", Integer.valueOf(year), "season", Integer.valueOf(season)));
                    List<Record> resultList = result.list();
                    return resultList;
//                    if(resultList.size()==0){
//                        return "";
//                    } else {
//                        return resultList.get(0).get(result.keys().get(0)).toString();
//                    }
                }
            });
            return cnt;
        }
    }

    /**
     * ① 按照时间进行查询及统计
     * 粒度：月份
     * 某年某月的电影数
     * cmp: 0 相等 1 after -1 before
     */
    public List<Record> queryTimeByMonth(final String year, final String month){
        try(Session session = driver.session()){
            List<Record> cnt = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result;
                    result = transaction.run("match(t:Time) where t.year=$year and t.month=$month return sum(t.month_count) as count",
                            parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month)));
                    List<Record> resultList = result.list();
                    return resultList;
                }
            });
            return cnt;
        }
    }

    /**
     * ① 按照时间进行查询及统计
     * 粒度：日
     * 某年某月某日的电影数
     * cmp: 0 相等 1 after -1 before
     */
    public List<Record> queryTimeByDay(final String year, final String month, final String day){
        try(Session session = driver.session()){
            List<Record> cnt = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result;
                    result = transaction.run("match(t:Time) where t.year=$year and t.month=$month and t.day=day return sum(t.day_count) as count",
                            parameters("year", Integer.valueOf(year), "month", Integer.valueOf(month), "day", Integer.valueOf(day)));
                    List<Record> resultList = result.list();
                    return resultList;
//                    if(resultList.size()==0){
//                        return "";
//                    } else {
//                        return resultList.get(0).get(result.keys().get(0)).toString();
//                    }
                }
            });
            return cnt;
        }
    }

    public static void main(String[] args) throws Exception {
        try (TimeQuery query = new TimeQuery("bolt://localhost:7687", "neo4j", "your_password")){
            System.out.println(query.queryTimeByMonth("2000","12"));
        }
    };
}
