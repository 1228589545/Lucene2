import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Created by 自由翱翔峰 on 2018/11/23 00:20
 */
public class Search {
//    查询索引，必须知道索引目录
    public static void search(String indexDir,String q) throws Exception{
//        获取索引目录
        Directory directory =FSDirectory.open ( Paths.get (indexDir ) );
        IndexReader indexReader =DirectoryReader.open (directory);//读索引
        //索引查询器
        IndexSearcher indexSearcher = new IndexSearcher ( indexReader );
        Analyzer analyzer = new StandardAnalyzer (  );//标准分词器
        QueryParser queryParser = new QueryParser ( "contents",analyzer );//查询解析器
//        直接查询返回query对象
        Query query = queryParser.parse ( q );

        long start = System.currentTimeMillis ();
        //真正实现查询
        TopDocs ts = indexSearcher.search ( query,10 );//前十条数据（文档）
        long end = System.currentTimeMillis ();
        System.out.println("匹配"+q+"耗时"+(end-start)+"查询到"+ts.totalHits+"个记录");
        for(ScoreDoc scoreDoc:ts.scoreDocs){//得分的文档（查询到的文档）//ScoreDoc是一个集合

            Document document = indexSearcher.doc ( scoreDoc.doc );//根据文档id（主键）来获取文档
            System.out.println(document.get("Path"));//输出查询到的文档路径
        }
        indexReader.close ();

    }
    public static void main(String[] args) throws Exception{
         String indexDir = "D:\\Lucene";
         String query = "world";
         search ( indexDir,query );
    }
}
