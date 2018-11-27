# arena
Testing Skillz WebApp
Description:
При первом входе в игру предлагается авторизоваться используя имя и пароль.
•   Если игрок с таким именем и паролем есть, то игрока авторизует;
•   Если игрока нет, то он создается с параметрами урон 10, жизни 100;
•   Если игрок есть, но пароль не совпал – выдать ошибку.

После авторизации игрок попадает на главную страницу. На главной есть меню:
•   Дуэли – переход на страницу с дуэлями
•   Выход – закрытие сессии и переход на страницу входа

Страница дуэлей

При первом входе выводим мой рейтинг и кнопку “Начать дуэль”.

После нажатия на кнопку, игрок помечается, как готовый к дуэли и ему в противники подбирается другой игрок с таким же статусом “готовый к дуэли”.

После того как оба игрока найдены, каждый видит своего врага и таймер 30 сек до начала битвы.

По истечении таймера начинается битва. Здесь игрок видит параметры своего противника – имя, жизни (прогресс бар), урон и кнопку Атаковать, а так же свои параметры в отдельном блоке.

Внизу страницы лог боя в виде:
Васька ударил вас на 10 урона
Вы ударили Петюня на 5 урона
Вы убили Васька

По окончании дуэли победитель получает +1 рейтинга, проигравший получает -1 рейтинга. Независимо от победы или проигрыша каждый игрок получает +1 к параметру урон и +1 к параметру жизни.

База данных MySQL (обязательное условие)

Выбор движка таблиц и какие индексы делать – на ваше усмотрение.

Важно! При выполнении задания учитывайте возможные высокие нагрузки. Подумайте, что вынести в оперативную память, а что в базу данных.

Внизу каждой страницы выводить техническую информацию:
•   Время генерации страницы, в милисекундах
•   Количество запросов в MySQL и время их выполнения
Пример формата: page:100 ms, db: 5req (20ms)
