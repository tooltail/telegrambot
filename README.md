#1
:shipit: \
Задача - создать БД(postgresql), где будут храниться название и адрес баров
+ возможность добавлять новые бары (т.к. используется БД - она может быть изменена - добавлены новые места, удалены старые и т.д.)
+ выводить список уже добавленных баров

Пример диалога:

>**/add** \
    Выберите категорию, в которую Вы хотите добавить заведение: \
        *Bar* \
    Укажите название заведения: \
        *Televisor* \
    Укажите адрес заведения: \
        *Radisheva, 4*

>**/add** <br />
Выберите категорию, в которую Вы хотите добавить заведение: <br />
    Bar <br />
    Укажите название заведения: <br />
        *Melodiya* <br />
    Укажите адрес заведения: <br />
        *Pervomayskaya, 36*

>**/bars** <br />
    Список баров: <br />
        Televisor (Radisheva, 4) <br />
        Melodiya (Pervomayskaya, 36)

#2 
:shipit: \
Задача - возможность ставить оценку заведению, в котором побывал пользователь

Пример диалога:

>**/rate** <br />
    Select the category in which you want to rate the establishment: <br />
        *Bar* <br />
    Enter the name of the establishment: <br /> 
        *Televisor* <br />
    Enter the address of the establishment: <br />
        *Radisheva,4* <br />
    Rate the establishment (between 1 and 5): <br />
        *100* <br />
    The rate must be between 1 and 5 <br />
        *(buttons from 1 to 5)* <br />
    Your rate was added ;)

#3
:shipit: \
Задача - возможность находить бары (использование api для нахождения координат места: https://dadata.ru/api/geocode/) в радиусе 2 км и сортировать по оценкам (места будут расположены в порядке уменьшения рейтинга) + деплой бота на Heroku \
Пример диалога:
>**/nearestBars** <br />
Please share your geolocation or enter your current address <br />
Stachek, 70 <br />
*(User uses button to send geo or just types his address in the chat)* <br />
Nearest bars: <br />
*Squirrel (Krasnoflotsev, 2) <br />
rate: 4,00/5 <br />
distance: 1,41 km*

Пример, если заведений в радиусе 2 километров нет:
>**/nearestBars** <br />
Please share your geolocation or enter your current address <br />
*(User uses button to send geo or just types his address in the chat)* <br />
Nearest bars: <br />
*There are no bars near you*
## Деплой бота на Heroku <br /> <br />
**1)** Регистрируемся на Heroku.com
![](images/img.png) <br /> <br />
**2)** Создаем новое приложение 
![](images/createapp.png) <br /> <br />
**3)** Переходим во вкладку Settings ![](images/settings.png) <br /> <br />
**4)** Указываем необходимые переменные, которые используются непосредственно в коде
![](images/конфиг.png)<br /> <br />
**5)** Переходим во вкладку Deploy 
![](images/deploy.png) <br /> <br />
**6)** С помощью GitHub привязываем наш аккаунт к Heroku
![](images/git.png) <br /> <br />
**7)** Находим необходимый для деплоя проект (telegrambot в нашем случае) и нажимаем *Connect*
![](images/project.png) <br /> <br />
**8)** Выбираем *Deploy Branch*
![](images/manualdeploy.png) <br /> <br />
**9)** ~~Потратив пару часов в попытках разобраться с остальными ошибками деплоя~~, Вы наконец-то сделали это! Поздравляю! Ваш первый деплой!




