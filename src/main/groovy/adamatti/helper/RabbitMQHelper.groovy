package adamatti.helper

abstract class RabbitMQHelper {
    // load the config once and reuse it
    private static final ConfigObject cfg = ConfigHelper.cfg

    static String buildQueueEndpoint(String queueName) {
        // from cfg, get rabbitmq host and port, with defaults if not set
        def rabbitHost = cfg.rabbitmq?.host ?: "localhost"
        def rabbitPort = cfg.rabbitmq?.port ?: "5672"

        return "rabbitmq://${rabbitHost}:${rabbitPort}/amq.topic?" +
                "queue=${queueName}&" +
                "exchangeType=topic&" +
                "durable=true&" +
                "routingKey=*&" +
                "autoDelete=false&"+
                "connectionFactory=#customConnectionFactory"
    }
}