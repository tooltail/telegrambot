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