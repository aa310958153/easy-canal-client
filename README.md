# easy-canal
canal客户端，用户不需要关心如何同canal交互,只需要定义对应的监听器,对相应的CRUD做业务逻辑处理
## 使用方式
#### 1.引入依赖
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
#### 2.spring配置
````
 <!-- spring启动时扫描项目路径下的properties文件,后续用${key }方式取出对应值,这样可以代码解耦和，后续只需修改properties文件即可 -->
    <bean id="propertyPlaceholderConfigurer"
          class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <list>
                <value>classpath:canal/canal.properties</value>
            </list>
        </property>
    </bean>
    <context:component-scan base-package="com.wine.easy.canal.core"></context:component-scan>
````    
#### 3.canal.properties配置
````
#canalserver地址
easy.canal.host=127.0.0.1
#canalserver端口
easy.canal.port=11111
#canalserver每个批次读取
easy.canal.batchSize=1200
````
#### 4.配置监听器
````
//[数据库名字].[表名]
@Table(name = "canal.act_activity")
@Component
public class ActivityCanalListener implements ProcessListener<Activity> {
    private static final Logger logger = LoggerFactory.getLogger(ActivityCanalListener.class);

    @Override
    public boolean update(Activity after, Activity before, List<String> updateFiled) {
        logger.info("-------------------------触发修改-------------------------");
        logger.info("修改一条数据修改前:{}", JSON.toJSON(after));
        logger.info("修改一条数据修改后:{}", JSON.toJSON(before));
        logger.info("修改一条数据修改字段:{}", JSON.toJSON(updateFiled));
        logger.info("-------------------------触发end-------------------------");
        return false;
    }

    @Override
    public boolean insert(Activity activity) {
        logger.info("新增一条数据{}", JSON.toJSON(activity));
        return false;
    }

    @Override
    public boolean delete(Activity activity) {
        logger.info("删除一条数据{}", JSON.toJSON(activity));
        return false;
    }
}
````

    
