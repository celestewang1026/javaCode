package com.J_mongo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.mongodb.client.model.Filters;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.text.ParseException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * java连接mongo，并写入数据
 * created by yu.wang.a
 */
public class javaConnMongo{
    public static void main( String args[] ){
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient( "localhost", 27001 );

        try{
            // 连接到数据库
            String DatebaseName="mldn";//数据库名称
            MongoDatabase mongoDatabase = mongoClient.getDatabase(DatebaseName);
            System.out.println(DatebaseName+"连接数据库成功");
            String CollectName="student_accuracy";//集合名称
             MongoCollection<Document> collection = mongoDatabase.getCollection(CollectName);
             System.out.println(CollectName+"选择成功");
             String sub_set[]={"ENGLISH","MATH","CHINESE"};
             Random random=new Random();
             Date date = new Date();
             DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
             String createtime = format.format(date);
             List<Document> documents = new ArrayList<Document>();
             for (int i=0;i < 100000;i++) {
                 int index=random.nextInt(sub_set.length);
                 Integer userid= (int)(Math.random()*1000000000);
                 Document document = new Document("student_id",userid).
                         append("subject", sub_set[index]).
                         append("create_at",format.parse(createtime)).
                         append("update_at",format.parse(createtime)).
                         append("accuracy",((int)(Math.random()*1000))/1000.0);
                        //isodate时间比本地时间早8hour
                 documents.add(document);
            }
        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }finally {
            /**
             * 关闭数据库连接
             */
            mongoClient.close();
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

    /**
     *
     * @param collection 待插入的表
     * @param document 插入的文档
     *  1. 创建文档 org.bson.Document 参数为key-value的格式
     * 2. 创建文档集合List<Document>
     * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用
     *    mongoCollection.insertOne(Document)
     */
    public static void MongoInsertOne(MongoCollection<Document> collection,Document document)
    {
        collection.insertOne(document);
    }
    public static void MongoInsert(MongoCollection<Document> collection,List<Document> documents){
        collection.insertMany(documents);
    }

    /**
     * 检索查看
     */
    public static void MongoSelect(MongoCollection<Document> collection,BasicDBObject query) {
        //BasicDBObject query = new BasicDBObject("student_id", 457249268);
        FindIterable<Document> findIterable = collection.find(query);
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()) {
            System.out.println(mongoCursor.next());
        }
    }
    
    /**
     *
     * @param collection
     * @param query  查询符合此条件的
     * @param newContent  替换的新内容
     * @param flag 1:替换1条，2：替换全部
     */
    public  static void MongoUpdate(MongoCollection<Document> collection,BasicDBObject query,BasicDBObject newContent,int flag) {
//           BasicDBObject query = new BasicDBObject("subject","MATH");
//           BasicDBObject newContent = new BasicDBObject("subject","MUSIC");
            BasicDBObject update = new BasicDBObject("$set",newContent);
            switch (flag) {
                case 1:collection.updateOne(query, update);break;
                case 2:collection.updateMany(query, update);break;
                default:collection.updateMany(query, update);break;
            }

//           BasicDBObject where = new BasicDBObject("student_id",457249268);
//           BasicDBObject newContent = new BasicDBObject("delete_at","").append("update_at",format.parse(createtime));
    }

    /**
     * 删除数
     * @param collection
     * @param query 条件
     */
    public  static  void MongoDrop(MongoCollection<Document> collection,BasicDBObject query){
//            collection.drop();
//            collection.deleteMany(Filters.eq("subject","ENGLISH"));
            collection.deleteMany(query);
    }


}
