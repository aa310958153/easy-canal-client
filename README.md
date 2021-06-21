# easy-canal
canal客户端，用户不需要关心如何同canal服务交互,只需要定义对应的监听器,对相应的CRUD做业务逻辑处理。

elk支持全量以及根据条件全量增量 。

博客:https://www.cnblogs.com/LQBlog/tag/Canal/
## 使用方式
### 1.引入依赖
```
 <dependencies>
        <dependency>
            <groupId>org.example</groupId>
            <artifactId>easy-canal</artifactId>
            <version>1.0-SNAPSHOT</version>
            <exclusions>
                <exclusion>
                    <artifactId>spring</artifactId>
                    <groupId>org.springframework</groupId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
```    
### 2.spring配置
#### 2.1spring传统项目配置
````
 <!-- 使用spring生命周期InitializingBean进行初始化 所以配置扫描此包下的类 -->
 
    <context:component-scan base-package="com.wine.easy.canal.core"></context:component-scan>
````    
#### 2.2spring-boot项目配置
````
在启动类加上@ComponentScan(basePackages = {"cn.wine.ms.promotion","com.wine.easy.canal.core"})
注意 此注解会覆盖默认扫描，需要将默认启动类目录加上，我这里是"cn.wine.ms.promotion"
````    
### 3.canal配置
#### 3.1固定Ip配置,resources新增/canal/canal.yml
````
#基于固定canal server的地址，建立链接，其中一台server发生crash，可以支持failover多个,号隔开
host: 127.0.0.1:11111
batchSize: 100
groups:
  g2:
    destination: example
properties: #多环境配置@Table name属性占位符${database}.tableName
  database: promotion_test
````
#### 3.2基于zookeeper配置resources新增/canal/canal.yml
````
#基于固定zookeeper的地址，建立链接，其中一台server发生crash，可以支持failover多个,号隔开
zkHosts: 127.0.0.1:2181
batchSize: 100
groups:
  g2:
    destination: example
properties: #多环境配置@Table name属性占位符${database}.tableName
  database: promotion_test

````
###4.配置监听器
````
//[数据库名字].[表名]
@Table(name = "${database}.ord_pay_way",group = "g1")
@Component
public class PaymentWayAndRelationListener implements ProcessListener<PaymentWayAndRelation> {
    private static final Logger logger = LoggerFactory.getLogger(PaymentWayAndRelationListener.class);
    @Override
    public void update(PaymentWayAndRelation after, PaymentWayAndRelation before, Set<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改后:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改前:{}", JSON.toJSON(before));
        logger.info("修改一条数据修改字段:{}", JSON.toJSON(updateFiled));
        logger.info("-------------------------触发end-------------------------");
    }

    @Override
    public void insert(PaymentWayAndRelation paymentWayAndRelation) {
        logger.info("新增一条数据{}", JSON.toJSON(paymentWayAndRelation));
    }
    @Override
    public void delete(PaymentWayAndRelation paymentWayAndRelation) {
        logger.info("删除一条数据{}", JSON.toJSON(paymentWayAndRelation));
    }
   
    /**
     * 处理失败回调。不阻塞后续返回true则跳过,返回false会不断重试 但是会阻塞后续binlog 直到成功
     * @param entry
     */
    @Override
    public boolean errorCallback(Dml entry) {
       //记录日志后续补偿
        return true;
    }
}
````

## ELK使用方式
### 1.elk配置 resources新增/canal/elk.yml
````
port: 8099 #ELK端口
jdbc: #默认的elk数据源jdbc信息 可以在group里面配置每个group单独的
  url: jdbc:mysql://127.0.0.1:3306/merge_test?useSSL=false&useUnicode=true&characterEncoding=utf8&autoReconnect=true
  username: kuaihe
  password: Kuaihe0910Mysql
  driverClassName: com.mysql.jdbc.Driver
dbMappings:
  commitBatch: 1000 #默认的分批次处理
  g1: #elk task的值
    table: merge_test.ord_pay_way #对应的表
    group: g1  #表名字+group 则对应处理器
    commitBatch: 100 #分批次处理
````
### 2.使用
````
注:如果condition多个{}占位符,params:";"隔开 根据顺序设置参数

post http://192.168.11.19:8079/canalClient/elk

body:

     {"task":"g1","condition":"where id={}","params":"839248604024786944"}

response:

{"resultMessage":"导入 数据：1 条","succeeded":true}
````