package com.database

import com.newapi.interfaces.DatabaseRequest
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.exceptions.ApiException
import com.vk.api.sdk.exceptions.ClientException
import java.sql.*


class DatabaseRequestImpl(private val statement: Statement,
                          private val connection: Connection) : DatabaseRequest {

    // --------тут красивые запросы--------

    //занесение данных в бд инфе о пользователях. userID, имя, фамилия
    @Throws(ClientException::class, ApiException::class, SQLException::class)
    fun addInfoUser(userID: Int, actor: UserActor, vk: VkApiClient) {
        val getInfoUserList =
                vk.users().get(actor).userIds(userID.toString()).execute()       //получает инфу о пользователях
        statement.execute(
                "INSERT  INTO 'UserVkInformation' ('userID', 'firstName', 'lastName') " +
                        "SELECT DISTINCT " + userID + ",'" + getInfoUserList[0].firstName + "','" +
                        getInfoUserList[0].lastName + "' FROM 'UserVkInformation'" +
                        "WHERE NOT EXISTS (SELECT 'userID' FROM 'UserVkInformation' WHERE userID=" + userID + ");"
        )        // проверка есть ли такая в бд. если нет, то добавить инфу.
    }

    //занесение данных в бд прав новых пользователей логин, userID, право - пользователь
    @Throws(SQLException::class)
    fun addInfoUserRights(userID: Int, actor: UserActor) {
        statement.execute(
                "INSERT  INTO 'UserRights' ('login', 'userID', 'nameRight') " +
                        "SELECT DISTINCT " + actor.id + "," + userID + ",'Пользователь' FROM 'UserRights'" +
                        "WHERE NOT EXISTS (SELECT 'login','userID' FROM 'UserRights' " +
                        "WHERE login=" + actor.id + " AND userID=" + userID + ");"
        )      // проверка если прав у пользователя нет, то добавить права пользователя
    }

    // инициализация одной таблицы  REQUEST RESPONSE
    @Throws(SQLException::class)
    fun findUserRights(userID: Int, actor: UserActor): String {
        val resSet: ResultSet
        resSet =
                statement.executeQuery("SELECT nameRight FROM UserRights WHERE login=" + actor.id + " AND userID=" + userID)
        var nameRight = ""
        while (resSet.next()) {
            nameRight = resSet.getString("nameRight")
        }
        resSet.close()
        return nameRight
    }

    // --------Создание первичных таблиц--------
    @Throws(SQLException::class)
    fun CreateDB() {

        //statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        // NameRights
        statement.execute("CREATE TABLE if not exists 'NameRights' ('nameRight' TEXT UNIQUE" + "NOT NULL PRIMARY KEY);")
        // RandomBazaBot
        statement.execute("CREATE TABLE if not exists 'RandomBazaBot' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "'requesttextbot'  TEXT (100), 'responsetextbot' TEXT (100));")

        // RandomMessages
        statement.execute("CREATE TABLE if not exists 'RandomMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "'request' TEXT    NOT NULL UNIQUE ON CONFLICT IGNORE);")
        // UserProgramInformation
        statement.execute("CREATE TABLE if not exists 'UserProgramInformation' ('Login' TEXT NOT NULL PRIMARY KEY, " + "'UserID' INT, 'LastName' CHAR, 'FirstName' CHAR, 'accessToken' TEXT);")
        // UserVkInformation
        statement.execute("CREATE TABLE if not exists 'UserVkInformation' ('userID' INTEGER NOT NULL PRIMARY KEY ON CONFLICT FAIL, " + "'firstName' TEXT, 'lastName' TEXT, 'countMessages' INTEGER DEFAULT (0), 'countError' INTEGER DEFAULT (0));")
        // UserRights
        statement.execute(
                "CREATE TABLE if not exists 'UserRights' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'login' TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, " +
                        "'userID' TEXT    REFERENCES 'UserVkInformation' ('userID') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, " +
                        "'nameRight' TEXT    REFERENCES 'NameRights' ('nameRight') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);"
        )
        // StatisticsBot
        statement.execute(
                "CREATE TABLE if not exists 'StatisticsBot' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "'Login'      TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL," +
                        "'Error' TEXT, 'countError' INTEGER, 'date' DATE);"
        )
        // BotMessages
        statement.execute(
                "CREATE TABLE if not exists 'BotMessages' ('id' INTEGER NOT NULL UNIQUE PRIMARY KEY ASC AUTOINCREMENT, " +
                        "'requesttextbot' CHAR NOT NULL, 'responsetextbot' CHAR  NOT NULL,  " +
                        "'Login' TEXT REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);"
        )

        println("Таблица создана или уже существует.")
    }

    // --------Создание вторичных таблиц--------
    @Throws(SQLException::class)
    fun CreateSecondaryDB() {

        //statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        // AforismMessages
        statement.execute("CREATE TABLE if not exists 'AforismMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT," + "'request' TEXT (500) NOT NULL);")
        // AnekdotMessages
        statement.execute("CREATE TABLE if not exists 'AnekdotMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "'request'  TEXT NOT NULL);")


        // NorkinForewer
        statement.execute(
                "CREATE TABLE if not exists 'NorkinForewer' ('id' INTEGER UNIQUE PRIMARY KEY ASC AUTOINCREMENT NOT NULL, " +
                        "'requesttextbot' CHAR    NOT NULL, 'responsetextbot' CHAR    NOT NULL, " +
                        "'Login' TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL DEFAULT (294987132));"
        )

        // StatusMessages
        statement.execute("CREATE TABLE if not exists 'StatusMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "'request'  TEXT NOT NULL);")
        // StihMessages
        statement.execute("CREATE TABLE if not exists 'StihMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + "'request'  TEXT NOT NULL);")

        println("Вторичные таблицы созданы или уже существуют.")

    }

    // --------Создание первичной записи в таблице--------
    fun InsertIntoTablePrimary() {
        //statmt.execute("INSERT INTO 'RandomMessages' ('request')  VALUES  ('Это тестовая запись'); ");
    }

    @Throws(SQLException::class)
    fun addElementinDialog(request: String, response: String, actorId: Int) {
        println(request)
        println(response)

        val requestSQL = ("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login')  VALUES  (?, ?, ?); ")
        val statement = connection.prepareStatement(requestSQL)
        statement.setString(1, request)
        statement.setString(2, response)
        statement.setInt(3, actorId)
        statement.executeUpdate()
        // statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('"+request+"', '"+response+"',  '"+ids+"');");
        println("Успешно занесено в БД")
        DatabaseEntity.database.InitDB()
        //  System.out.println("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot') VALUES ("+zapros.getText()+", "+ otvet.getText()+ ")");
    }   // добавить новый элемент в таблицу


    @Throws(SQLException::class)
    override fun addRandomMessage(text: String) {
        println(text)

        try {

            statement.execute("INSERT INTO 'RandomMessages' ('request')  VALUES  ('$text'); ")
        } catch (e: Exception) {
            println("wtf")
        }
        // statmt.execute("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot', 'Login') VALUES ('"+request+"', '"+response+"',  '"+ids+"');");
        println("Успешно занесено в БД")
        //InitDB()
        //  System.out.println("INSERT INTO 'BotMessages' ('requesttextbot', 'responsetextbot') VALUES ("+zapros.getText()+", "+ otvet.getText()+ ")");
    }   // добавить новый элемент в таблицу

    // парсер добавления в таблицу через вк
    @Throws(SQLException::class)
    fun addToDB(word: String, actorId: Int): String {

        var firstWord = ""
        var secondWord = ""
        val chars = CharArray(50)
        var first = false
        var second = false
        var boolfirst = true
        var i = 0
        while (!second) {

            if (word.length - 1 < i) return "Неправильный формат"
            if (word[i] == '(') {
                while (word[i] != ')') {
                    i++
                    if (word.length - 1 < i) return "Неправильный формат"
                    if (word[i] != ')')
                        chars[i] = word[i]
                }
                if (first) {

                    second = true
                }
                first = true
            }
            if (first && boolfirst) {
                firstWord = String(chars).trim { it <= ' ' }
                boolfirst = false
            }
            if (second) {
                secondWord = String(chars).trim { it <= ' ' }
            }
            i++
        }
        secondWord = secondWord.replace(firstWord, "")
        secondWord = secondWord.replace("\\u0000".toRegex(), "")
        println("firstWord $firstWord")
        println("secondWord $secondWord")
        addElementinDialog(firstWord, secondWord, actorId)
        return "Сделано!"
    }

    /* пример написания запроса, чтоб исключал повторяющиеся
     * INSERT INTO UserRights(login, userID, nameRight)
     * SELECT DISTINCT 294987132, 30562433, 'Пользователь' FROM UserRights
     * WHERE NOT EXISTS (SELECT userID FROM UserRights WHERE userID = '30562433')
     */
}
