package com.lagou.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.nio.file.Paths;

public class LuceneIndexManager {
    @Test
    public  void  indexCreate()throws  Exception{
        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建Directory 流对象
        Directory directory = FSDirectory.open(Paths.get("D:/lucene/index3"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 创建索引写入对象
        IndexWriter  indexWriter = new IndexWriter(directory,config);
        // 创建Document
        Document  document = new Document();
        document.add(new TextField("id","1001", Field.Store.YES));
        document.add(new TextField("name","game", Field.Store.YES));
        document.add(new TextField("desc","one world one dream", Field.Store.YES));
        // 添加文档 创建索引
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    @Test
    public  void  indexDelete()throws  Exception{
        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建Directory 流对象
        Directory directory = FSDirectory.open(Paths.get("D:/lucene/index3"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 创建索引写入对象
        IndexWriter  indexWriter = new IndexWriter(directory,config);
        indexWriter.deleteDocuments(new Term("name","game"));
        indexWriter.close();
    }

    @Test
    public  void  indexUpdate()throws  Exception{
        // 创建分词器
        Analyzer analyzer = new StandardAnalyzer();
        // 创建Directory 流对象
        Directory directory = FSDirectory.open(Paths.get("D:/lucene/index3"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 创建索引写入对象
        IndexWriter  indexWriter = new IndexWriter(directory,config);
        // 创建Document
        Document  document = new Document();

        document.add(new TextField("id","1001", Field.Store.YES));
        document.add(new TextField("name","好好学习", Field.Store.YES));
        document.add(new TextField("desc","游戏结束该干啥干啥去", Field.Store.YES));
        // 更新
        indexWriter.updateDocument(new Term("name","game"),document);
        indexWriter.close();

    }


}
