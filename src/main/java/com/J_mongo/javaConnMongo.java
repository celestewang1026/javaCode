package com.J_mongo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.text.ParseException;

public class javaConnMongo{
    public static void main( String args[] ){
        try{
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "localhost", 27001 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mldn");
            System.out.println("Connect to database successfully");

                MongoCollection<Document> collection = mongoDatabase.getCollection("student_accuracy");
                System.out.println("集合 student_accuracy 选择成功");
                /**
                 * 1. 创建文档 org.bson.Document 参数为key-value的格式
                 * 2. 创建文档集合List<Document>
                 * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
                 * */
                String sub_set[]={"ENGLISH","MATH","CHINESE"};
                Random random=new Random();
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String createtime = format.format(date);
                List<Document> documents = new ArrayList<Document>();
                for (int i=0;i < 1000000;i++) {
                    int index=random.nextInt(sub_set.length);
                    Document document = new Document("student_id",(int)(Math.random()*1000000000)).
                            append("subject", sub_set[index]).
                            append("create_at",format.parse(createtime)).
                            append("accuracy",((int)(Math.random()*1000))/1000.0);
                //isodate时间比本地时间早8hour
                    documents.add(document);
                }
                //插入文档,一次性插入
                collection.insertMany(documents);

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("done");
    }

    /**
     * 根据本地时间获取ISODate时间
     * @param time:字符串格式的本地时间
     * @return
     */
    public static Date formatDate(String time){
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
