CRUD-приложение для управления книгами с БД Redis.
-
   В качестве базы данных и базы для хранения кеша 
   используется Redis.
   Для маппинга сущностей используется ModelMapper.
   Тесты выполнены на базе TestContainers.

Инструкция для запуска в Docker
-
     Соберите проект в Maven package;
     Запустите в терминале билдер командой:
     docker build -t book-service .

     Запустите контейнер командой:
     docker-compose up

     Выставленный порт 8080.