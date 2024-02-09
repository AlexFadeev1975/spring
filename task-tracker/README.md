Микросервисное приложение Трекер Задач, реализованное на реактивном стеке WebFlux.
-
База данных MongoDB.

Тесты выполнены для UserHandler на базе TestContainers, для TaskHandler на базе SpringBootTest

Инструкция для запуска в Docker

     В task-tracker запустите Maven package.

     Запустите в терминале билдер командой:
     docker build -t task-tracker . 

     Запустите контейнер командой:
     з корневой папки docker-compose up -d

     Выставленный порт 8080.

     Для запуска тестов в протертис <skipTests> смените на false и запустите builder либо запустите отдельно.

     ДЛЯ ЗАПУСКА СЕРВИСА:

     Переменные окружения:
       - DB_URI - uri базы данной
       - DB_NAME - имя БД 
       