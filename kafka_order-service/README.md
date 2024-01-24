Микросервисное приложение, использующее для обмена сообщениями Apache Kafka.
-
База данных не используется.

 - kafka-service - библиотека конфигурации kafka.

 - order-service - REST контроллер с одним ендпоинтом (POST), принимающим сущность Product  и отправляющим сообщение в топик для Product одновременно слушающее топик для сообщений о статусе StatusMessage.

 - order-status-service - принимает сообщения с топика для Product и отправляет сообщение со статусом CREATED в топик для StatusMessage.


Тесты выполнены на базе TestContainers.

Инструкция для запуска в Docker
     
     В kafka-service запустите maven install для установки библиотеки в других сервисах.

     Соберите проекты order-service и order-status-service в Maven package.

     Запустите в терминале билдер командой:
     из корневой папки cd order-status-service
     docker build -t order-status-service . 

     из корневой папки cd order-service
     docker build -t order-service .

     Запустите контейнер командой:
     з корневой папки docker-compose up -d

     Выставленный порт 8080.

     Для запуска тестов в помнике order-service протерти <skipTests> смените на false и запустите builder либо запустите отдельно.

     ДЛЯ ЗАПУСКА СЕРВИСОВ ОТДЕЛЬНО:

     Переменные окружения:
       - BS_SERVERS - kafka bootstrap-servers (выставлен localhost:9092)
       - GROUP_ID - kafka group-id (любое значение)
       - MESSAGE_TOPIC - kafka топик для StatusMessage (любое значение)
       - ORDER_TOPIC - kafka топик для OrderEvent (любое значение)