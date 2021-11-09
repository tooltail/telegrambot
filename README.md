1 Задача - создать БД(postgresql), где будут храниться название и адрес баров
+ возможность добавлять новые бары (т.к. используется БД - она может быть изменена - добавлены новые места, удалены старые и т.д.)
+ выводить список уже добавленных баров

Пример диалога:

/add
    Выберите категорию, в которую Вы хотите добавить заведение:
        Bar
    Укажите название заведения:
        Televisor
    Укажите адрес заведения:
        Radisheva, 4

/add
    Выберите категорию, в которую Вы хотите добавить заведение:
        Bar
    Укажите название заведения:
        Melodiya
    Укажите адрес заведения:
        Pervomayskaya, 36

/bars
    Список баров:
        Televisor (Radisheva, 4)
        Melodiya (Pervomayskaya, 36)