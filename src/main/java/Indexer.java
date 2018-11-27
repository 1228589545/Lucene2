import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

/**
 * Created by 自由翱翔峰 on 2018/11/22 20:14
 */
public class Indexer {
//    写索引的实例（文件）
    private IndexWriter writer;

    /**
     * 构造方法实例化indexwriter
     * @param indexDir
     * @throws Exception
     */
    public Indexer(String indexDir) throws  Exception{
//        将索引写到那个目录
        Directory dir =FSDirectory.open ( Paths.get ( indexDir ) );
        Analyzer analyer = new StandardAnalyzer ( );//标准解析器（分词器）（只支持英文）
        //    配置解析器
        IndexWriterConfig iwc = new IndexWriterConfig ( analyer );
        writer = new IndexWriter(dir,iwc);

    }

    /**
     * 关闭写索引
     * @throws Exception
     */
    private void close() throws  Exception{
     writer.close ();
    }

    /**
     * 索引指定目录的所有文件
     * @param datadir 索引的数据目录
     * @throws Exception
     */
    private  int index(String datadir) throws  Exception{
        File[] files = new File( datadir). listFiles ();
        for(File fi:files){
            indexFile(fi);
        }
        return writer.numDocs ();//返回索引了多少个文件
    }

    /**
     * 索引指定文件（一个一个的文件）
     * @param f
     */
    private void indexFile(File f )throws  Exception{
//        相当于数据库中的行和列，一行就是一个docement（文档）,一列就是一个filed
         System.out.println("索引文件："+f.getCanonicalPath ());//文件真是路径（规范化路径）
         Document doc = getDocument(f);
         writer.addDocument ( doc );//添加到索引里面
    }

    /**
     * 获取文档，文档里在设置每个字段（相当于数据库中的一条记录）
     * @param f
     * @throws Exception
     */
    private Document getDocument(File f)throws  Exception{
        Document document = new Document ();
        document.add ( new TextField("contents",new FileReader (f ) ) );//添加字段
        document.add ( new TextField ( "fileName" , f.getName (),Field.Store.YES ));//Field.Store.YES把文件名存到索引文件里文件(存储索引，建议字段不太长时，效率高，用空间换时间)
        document.add ( new TextField ( "Path" ,f.getCanonicalPath () ,Field.Store.YES ));
        return document;
    }
public static void main(String[] args) throws  Exception{
    String indexDir="d:\\Lucene";
    String dataDir="d:\\Lucene\\data";
    Indexer indexer =null;
    int numIndexed = 0;
    long start = System.currentTimeMillis ();
    indexer=new Indexer ( indexDir );
    numIndexed = indexer.index ( dataDir );
    indexer.close ();
    long end = System.currentTimeMillis ();
    System.out.println("索引了"+numIndexed+"文件。花费了"+(end-start)+"毫秒");
}
}
