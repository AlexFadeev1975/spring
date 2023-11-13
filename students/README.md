Консольное приложение "Учет студентов"

Сервис для учета студентов, позволяющая просматривать, создавать,
изменять или удалять записи о студентах.

КОМАНДЫ КОНСОЛИ

init главное меню с описанием команд;

create [имя] [фамилия] [возраст] создание записи о студенте;

delete [id] удаление записи о студенте по его ID;

delete * очистка списка студентов;

find [любая информация о студенте] поиск записи о студенте(ах);

find * вывод списка всех записей о студентах;

help подсказки по использованию программы;

exit выход из программы.


ИНФОРМАЦИЯ О ПРОГРАММЕ

Хранение записей производится в простом списке для упрощения добавления и поиска данных.

По умолчанию список заполняется произвольным количеством студентов из дефолтного 
спиcка data.json.
Для запуска программы без заполнения списка необходимо установить значение переменной окружения
LOAD_FROM_FILE равное false.

Консоль и собственно сервис взаимодействуют через интерфейс EventListener, через который настроен
флажок автоматического заполнения списка. 

Приложение содержит Dockerfile для контейнеризации приложения.

Инструкция по созданию Docker-контейнера

    1. Создайте jar-файл проекта c использованием Maven 
    (запустите билдер с командой package )
    2. Установите Docker локально, если еще не установлен,
    и запустите его. (офоициальный образ на сайте https.docs.docker.com)
    3. В командной строке введите команду: docker build -t students .
    4. Запуск контейнера без генерации списка: 
           docker run -i -t -e LOAD_FROM_FILE=false students
    5. Запуск контейнера со списком:
           docker run -i -t -e LOAD_FROM_FILE=true students
           либо коротко docker run -i -t students







