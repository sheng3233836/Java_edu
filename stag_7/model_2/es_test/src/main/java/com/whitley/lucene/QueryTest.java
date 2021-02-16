package com.lagou.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.*;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;

public class QueryTest {
     public  void  doSearch(Query query)throws  Exception{
         // 创建Directory 流对象
         Directory directory = FSDirectory.open(Paths.get("D:/lucene/index"));
         // 创建IndexReader
         IndexReader indexReader = DirectoryReader.open(directory);
         IndexSearcher searcher = new IndexSearcher(indexReader);
         // 获取TopDocs
         TopDocs  topDocs = searcher.search(query,10);
         System.out.println("查询索引总条数:" + topDocs.totalHits);
         ScoreDoc[] docs = topDocs.scoreDocs;
         // 解析结果集
         for (ScoreDoc scoreDoc : docs){
             int  docID = scoreDoc.doc;
             Document  document = searcher.doc(docID);

             System.out.println("docID:"+docID);
             System.out.println("bookId:"+document.get("id"));
             System.out.println("name:"+document.get("name"));
             System.out.println("price:"+document.get("price"));
             System.out.println("desc:"+document.get("desc"));
         }
         indexReader.close();
     }
     @Test
     public  void testSearchTermQuery()throws  Exception{
         // 创建TermQuery 搜索对象
         Query  query = new TermQuery(new Term("name","lucene"));

         doSearch(query);
     }

    @Test
    public void testSearchBooleanQuery() throws Exception {
        // 创建两个 TermQuery搜索对象
        Query query1 = new TermQuery(new Term("name", "lucene"));
        Query query2 = new TermQuery(new Term("desc", "java"));
        // 创建BooleanQuery搜索对象,组合查询条件
        BooleanQuery.Builder  boolQuery = new BooleanQuery.Builder();
        // 组合条件,
        // 第一个参数，查询条件，第二个参数，组合方式
        boolQuery.add(query1, BooleanClause.Occur.MUST);
        boolQuery.add(query2, BooleanClause.Occur.MUST);

        doSearch(boolQuery.build());
    }

    @Test
    public  void testSearchPhraseQuery()throws  Exception{
       // PhraseQuery query = new PhraseQuery("name","lucene");
        PhraseQuery query = new PhraseQuery(3,"desc","lucene","java");
        doSearch(query);
    }

    @Test
    public  void testSearchSpanNearQuery()throws  Exception{
        SpanTermQuery tq1 = new SpanTermQuery(new Term("desc", "lucene"));
        SpanTermQuery tq2 = new SpanTermQuery(new Term("desc", "java"));
        SpanNearQuery spanNearQuery = new SpanNearQuery(new SpanQuery[] { tq1, tq2 },2,true);
        doSearch(spanNearQuery);
    }

    @Test
    public  void testSearchLikeQuery()throws  Exception{
       // WildcardQuery wildcardQuery = new WildcardQuery( new Term("name", "so??"));
        //doSearch(wildcardQuery);
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("name", "slorss"), 2);
        doSearch(fuzzyQuery);
    }

    @Test
    public  void testSearchNumQuery()throws  Exception{
        Query query = IntPoint.newRangeQuery("id",1,10);
        doSearch(query);
    }

    // QueryParser
    @Test
    public  void testQueryPasrser() throws  Exception{
        Analyzer  analyzer  = new StandardAnalyzer();
        QueryParser  queryParser  = new QueryParser("desc",analyzer);
        // 构建搜索对象
        Query query = queryParser.parse("desc:java AND name:lucene");
        doSearch(query);
    }

    @Test
    public void testSearchMultiFieldQueryParser() throws Exception {
        // 创建分词器
        Analyzer analyzer = new IKAnalyzer();
        // 1. 创建MultiFieldQueryParser搜索对象
        String[] fields = { "name", "desc" };
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(fields, analyzer);
        // 创建搜索对象
        Query query = multiFieldQueryParser.parse("lucene");
        // 打印生成的搜索语句
        System.out.println(query);
        // 执行搜索
        doSearch(query);
    }
    @Test
    public  void   testStandardQueryParser() throws  Exception{
         Analyzer analyzer = new StandardAnalyzer();
        StandardQueryParser  standardQueryParser = new StandardQueryParser(analyzer);
        //Query query = standardQueryParser.parse("desc:java AND name:lucene","desc");
        //Query query = standardQueryParser.parse("+desc:java + name:lucene","desc");
       // Query query = standardQueryParser.parse("desc:java  name:lucene","desc");
        //Query query = standardQueryParser.parse("name:L*","desc");
        //Query query = standardQueryParser.parse("lucensess~","desc");
        //Query query = standardQueryParser.parse("name:[Ha TO mock]","desc");
        Query query = standardQueryParser.parse("\"lucene java\"~3","desc");
        doSearch(query);
    }
}
