### 卡夫卡使用
#### 引入jar
```
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```
#### 配置文件
~~~
# 指定kafka 代理地址，可以多个
spring.kafka.bootstrap-servers=127.0.0.1:9092
# 指定默认消费者group id
spring.kafka.consumer.group-id=dangqp
# 指定默认topic id
spring.kafka.template.default-topic= dangqp
# 指定listener 容器中的线程数，用于提高并发量
spring.kafka.listener.concurrency= 3
# 每次批量发送消息的数量
spring.kafka.producer.batch-size= 1000

#key-value序列化反序列化
#spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
#spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
#spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
#spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
~~~

#### 生产者

~~~
@Component
@EnableScheduling
public class KafaProduce {
    @Autowired
    private KafkaTemplate kafkaTemplate;
    private Gson gson = new GsonBuilder().create();
    static  int i = 0;
    /**
     * 定时任务
     */
    @Scheduled(cron = "00/1 * * * * ?")
    public void send(){
        String message = UUID.randomUUID().toString();

        Message message1 = Message.builder().build();
        message1.setId(message);
        message1.setMsgName("name"+i++);
        message1.setMsg(message);
        ListenableFuture future = kafkaTemplate.send("dangqp", gson.toJson(message1));
        future.addCallback(o -> System.out.println("send-消息发送成功：" + message1), throwable -> System.out.println("消息发送失败：" + message1));
    }
}
~~~

#### 消费者
~~~
@Component
public class KafkaReceiver {

    private static final Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    private Gson gson = new GsonBuilder().create();

    @KafkaListener(topics = {"dangqp"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            Message obj = gson.fromJson(message.toString(),Message.class);
            log.info("----------------- record =" + record);
            log.info("------------------ message =" + message);
            log.info("------------------ message =" + obj.toString());
            log.info("------------------ message =" + obj.getMsgName());
        }

    }
}

~~~

