
                                          КОНТАКТЫ                                         
    Консольная программа для управления контактами, позволяющая в простой форме создавать,
    просматривать, удалять, производить поиск контактов.
    Формат контакта: ФИО; телефон; email;
    Преимущества:
    - простая программа для удобного хранения контактов;
    - возможность хранения контактов в файле, позволяет переносить контакты в текстовом формате;
    - подсказки для правильного ввода данных и использования программы.

                                        ГЛАВНОЕ МЕНЮ

    1 - Посмотреть все контакты
    2 - Найти контакты
    3 - Создать новый контакт
    4 - Удалить контакт
    5 - Выход

       По умолчанию (профиль default) программа сохраняет контакты в память компьютера.
       Запуск программы с профилем init позволит использовть в качестве хранилища текстовый файл
    delault-contacts.txt, находящийся в директории src/main/resources. При необходимости можно изменить
    расположение файла с контактами через настройки в файле application-file.properties, указав иной 
    путь и\или файла.
     
     Для чтения и записи в файл используется стандарные средства InputStreamReader и BufferedWriter.
     Для оперативного хранения выбран Set как набор уникальных элементов, исключющий дублирование
     контактов.
     Поиск выполняется по любому реквизиту контакта с любой точностью.
     Запуск программы производится через Main класс.
    