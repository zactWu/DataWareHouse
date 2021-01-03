package com.example.dw_backend.dao;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class SimpleQuery implements AutoCloseable{

    private final Driver driver;

    public SimpleQuery(String uri, String user, String password){
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    /**
     * ② 按照电影名称进行查询电影
     */
    public List<Record> queryByTitle(final String title, final int limit){
        try(Session session = driver.session()){
            List<Record> ans = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result = transaction.run("match (a:Movie) where a.title=$title return " +
                                    "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                    "a.title as title, a.versionCount as versionCount "+
                                    "limit 5;",
                            parameters("title", title));
                    List<Record> resultList = result.list();
                    System.out.println(resultList);
                    return resultList;
                }
            });
            return ans;
        }
    }

    /**
     * ③ 按照导演进行查询电影
     */
    public List<Record>queryMovieByDirector (final String director, final int limit){
        try(Session session = driver.session()){
            List<Record> ans = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result = transaction.run("match (d:Director)-[]-(a:Movie) where d.director=$director return " +
                                    "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                    "a.title as title, a.versionCount as versionCount;",
                            parameters("director", director));
                    List<Record> resultList = result.list();
                    System.out.println(resultList);
                    return resultList;
                }
            });
            return ans;
        }
    }

    /**
     * ④ 按照演员进行查询电影
     */
    public List<Record>queryMovieByActor (final String actor, final int limit){
        try(Session session = driver.session()){
            List<Record> ans = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result = transaction.run("match (d:Actor)-[]-(a:Movie) where d.actor=$actor return " +
                                    "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                    "a.title as title, a.versionCount as versionCount;",
                            parameters("actor", actor));
                    List<Record> resultList = result.list();
                    System.out.println(resultList);
                    return resultList;
                }
            });
            return ans;
        }
    }

    /**
     * ⑤ 按照类别进行查询电影
     */
    public List<Record>queryMovieByLabel (final String label, final int limit){
        try(Session session = driver.session()){
            List<Record> ans = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result = transaction.run("match (d:Label)-[]-(a:Movie) where d.label=$label return " +
                                    "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                    "a.title as title, a.versionCount as versionCount;",
                            parameters("label", label));
                    List<Record> resultList = result.list();
                    System.out.println(resultList);
                    return resultList;
                }
            });
            return ans;
        }
    }

    /**
     * ⑥ 按照评价分数进行查询电影
     * ⑦ 按照情感分数进行查询电影
     * scoreType: score 评价分数 emotion_score 情感分数
     * cmp: 0 相等 1 greater -1 less
     */
    public List<Record>queryMovieByScore (final Integer score, final int limit, final String scoreType, final String cmp){
        try(Session session = driver.session()){
            List<Record> ans = session.readTransaction(new TransactionWork<List<Record>>() {
                @Override
                public List<Record> execute(Transaction transaction) {
                    Result result;
                    switch (cmp) {
                        case "equal":
                            result = transaction.run("match (a:Movie) where a." + scoreType + "=$score return " +
                                            "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                            "a.title as title, a.versionCount as versionCount;",
                                    parameters("score", Integer.valueOf(score.toString())));
                            break;
                        case "greater":
                            result = transaction.run("match (a:Movie) where a." + scoreType + ">"+score+" return " +
                                            "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                            "a.title as title, a.versionCount as versionCount;",
                                    parameters("score", Integer.valueOf(score.toString())));
                            break;
                        case "less":
                            String cmd = "match (a:Movie) where a." + scoreType + "<"+score+" return " +
                                    "a.product_id as product_id, a.score as score, a.emotion_score as emotion_score, " +
                                    "a.title as title, a.versionCount as versionCount;";
                            result = transaction.run(cmd);
                            break;
                        default:
                            return new ArrayList<Record>();
                    }
                    List<Record> resultList = result.list();
                    System.out.println(resultList);
                    return resultList;
                }
            });
            return ans;
        }
    }

    public static void main(String[] args) throws Exception {
        try (SimpleQuery query = new SimpleQuery("bolt://localhost:7687", "neo4j", "123")){
            List<Record> ans = query.queryByTitle("Dr. Seuss - The Cat in the Hat",5);
            System.out.println(ans);
        }
    };
}
