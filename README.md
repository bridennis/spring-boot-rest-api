[![Build Status](https://travis-ci.org/bridennis/spring-boot-rest-api.svg?branch=master)](https://travis-ci.org/bridennis/spring-boot-rest-api)
[![codecov](https://codecov.io/gh/bridennis/spring-boot-rest-api/branch/master/graph/badge.svg)](https://codecov.io/gh/bridennis/spring-boot-rest-api)

Проект голосования: restaurant lunch menu of the day
====================================================

Используемый стек технологий: Java8, Spring Framework, Spring Boot, Spring Security, Hibernate

REST API
--------
<table>
    <tr>
        <th>Ресурс</th>
        <th>GET</th>
        <th>POST</th>
        <th>PUT</th>
        <th>DELETE</th>
    </tr>
    <tr>
        <td><b>все пользователи</b></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>/restaurants/menus</td>
        <td>просмотр текущего меню ресторанов</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/restaurants/votes/results</td>
        <td>просмотр текущих результатов голосования</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/users/reg</td>
        <td>-</td>
        <td>добавить пользователя</td>
        <td>-</td>
        <td>-</td>
    </tr>
    <tr>
        <td><b>аутентифицированные</b></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>/restaurants/1/vote</td>
        <td>-</td>
        <td>добавить голос за ресторан с ID=1</td>
        <td>-</td>
        <td>-</td>
    </tr>
    <tr>
        <td><b>администраторы</b></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>/restaurants/votes</td>
        <td>-</td>
        <td>просмотр голосов</td>
        <td>-</td>
        <td>удалить текущие результаты голосования</td>
    </tr>
    <tr>
        <td>/restaurants/menus/all</td>
        <td>-</td>
        <td>-</td>
        <td>-</td>
        <td>удалить текущее меню ресторанов</td>
    </tr>
    <tr>
        <td>/restaurants/1/dish/1</td>
        <td>-</td>
        <td>добавить в меню ресторана ID=1 блюдо ID=1</td>
        <td>-</td>
        <td>удалить из меню ресторана ID=1 блюдо ID=1</td>
    </tr>
    <tr>
        <td>/users</td>
        <td>список пользователей</td>
        <td>добавить пользователя</td>
        <td>-</td>
        <td>-</td>
    </tr>
    <tr>
        <td>/users/1</td>
        <td>вывести пользователя с ID=1</td>
        <td>-</td>
        <td>обновить пользователя с ID=1</td>
        <td>удалить пользователя с ID=1</td>
    </tr>
    <tr>
        <td>/dishes</td>
        <td>список доступных блюд</td>
        <td>добавить блюдо</td>
        <td>-</td>
        <td>удалить все блюда</td>
    </tr>
    <tr>
        <td>/dishes/1</td>
        <td>вывести блюдо с ID=1</td>
        <td>-</td>
        <td>обновить блюдо с ID=1</td>
        <td>удалить блюдо с ID=1</td>
    </tr>
    <tr>
        <td>/restaurants</td>
        <td>список доступных ресторанов</td>
        <td>добавить ресторан</td>
        <td>-</td>
        <td>удалить все рестораны</td>
    </tr>
    <tr>
        <td>/restaurants/1</td>
        <td>вывести ресторан с ID=1</td>
        <td>-</td>
        <td>обновить ресторан с ID=1</td>
        <td>удалить ресторан с ID=1</td>
    </tr>
    <!--<tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
    </tr>-->
</table>


Доступ к праву голосования имеют **только зарегистрированные** пользователи (базовая HTTP авторизация).

По умолчанию создаются:

- три ресторана
- десять блюд (цены в случайном порядке)
- два пользователя:
    - Пользователь (login: **user**, password: **user**)
    - Администратор (login: **admin**, password: **admin**)
 
**Процесс голосования:**
- Шаг 1: Если вы не зарегистрированы, необходимо зарегистрировать нового пользователя (к примеру: пользователь "test" c паролем "password")
```code
curl -v -d "{\"login\":\"test\",\"password\":\"password\"}" -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/users/reg
```

- Шаг 2: Просмотреть текущее меню всех ресторанов
```code
curl -v http://127.0.01:8080/restaurants/menus
```

- Шаг 3: Голосовать за выбранный ресторан пользователем test (к примеру: ресторан с id = 1)
```code
curl -v -u test:password -X POST http://127.0.01:8080/restaurants/1/vote
```

(примечание: пользователь может голосовать только до 11:00 текущего дня, учитывается только последний голос)

- Шаг 4: Просмотреть общие результаты голосования
```code
curl -v http://127.0.01:8080/restaurants/votes/results
```

Действия администратора
-----------------------
**Подготовка к голосованию:**

- Шаг 0: Очистить данные предыдущего голосования

очистить текущие результаты голосования<sup>1</sup>
```code
curl -v -u admin:admin -X DELETE http://127.0.0.1:8080/restaurants/votes
```

очистить текущее меню ресторанов<sup>2</sup> (если необходимо)
```code
curl -v -u admin:admin -X DELETE http://127.0.0.1:8080/restaurants/menus/all
```

- Шаг 1: Сформировать новое меню ресторана

(добавить в меню ресторана с id=1, блюдо с id=1)
```code
curl -v -u admin:admin -X POST http://127.0.0.1:8080/restaurants/1/dish/1
```

(удалить из меню ресторана с id=1, блюдо с id=1)
```code
curl -v -u admin:admin -X DELETE http://127.0.0.1:8080/restaurants/1/dish/1
```

**Другие действия администратора**

Список ресторанов:
```code
curl -v -u admin:admin http://127.0.0.1:8080/restaurants
```

Добавить ресторан:
```code
curl -v -u admin:admin -d "{\"name\":\"newRestaurant\"}" -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/restaurants
```

Удалить ресторан c id=1:
```code
curl -v -u admin:admin -X DELETE http://127.0.0.1:8080/restaurants/1
```

Список блюд:
```code
curl -v -u admin:admin http://127.0.0.1:8080/dishes
```

Добавить блюдо:
```code
curl -v -u admin:admin -d "{\"name\":\"newDish\",\"price\":\"500\"}" -H "Content-Type: application/json" -X POST http://127.0.0.1:8080/dishes
```

Удалить блюдо c id=1: 
```code
curl -v -u admin:admin -X DELETE http://127.0.0.1:8080/dishes/1
```

Замечания (на будущее):
-------
1. В процедуре очистки голосования предусмотреть возможность переноса данных в архивную БД
2. В процедуре очистки ресторанного меню предусмотреть возможность переноса данных в архивную БД
