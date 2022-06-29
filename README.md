## UserManagementApp

SOAP backend для веб-приложения. Основная задача бекенда - управление пользователями и их ролями.

Запустить данный сервис можно при помощи java -jar UserManagementApp-0.0.1-SNAPSHOT.jar

База данных in-memory H2 в файле db.sql, также использовался Hibernate ORM + JPA Repository

Обратится к данной бд можно по http://localhost:9090/h2-console, для того, чтобы пройти успешную аутентификацию нужно заполнить поля из файла application.properties. А также изменить лишь spring.datasource.url на свой локальный путь к базе данных.


После авторизации будет бд с данными таблицами:

![image](https://user-images.githubusercontent.com/66429474/176429827-adef725f-90e1-49d8-bf00-c52e0caea17c.png)


Для теста сервиса можно использовать SOAP UI, для создания проекта необходимы такие Project Name - users и Initial wsdl - http://localhost:9090/usersService/users.wsdl

Добавление пользователя с несколькими ролями происходит следующим образом

![image](https://user-images.githubusercontent.com/66429474/176431203-7e7db366-79e8-4ba8-9f90-99edc74c67b0.png)
