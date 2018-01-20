package com.apiVKmanual.functions.botdatabase;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import static com.fomenko.vkbot.controller.menuprogram.DataBaseWindowController.addElementinDialog;
import static com.fomenko.vkbot.controller.menuprogram.DataBaseWindowController.statmt;

@SuppressWarnings("SqlDialectInspection")
public class DatabaseRequest {


    // --------тут красивые запросы--------


    //занесение данных в бд инфе о пользователях. userID, имя, фамилия
    public static void addInfoUser(int userID, UserActor actor, VkApiClient vk) throws ClientException, ApiException, SQLException {
        List<UserXtrCounters> getInfoUserList = vk.users().get(actor).userIds(String.valueOf(userID)).execute();       //получает инфу о пользователях
        statmt.execute("INSERT  INTO 'UserVkInformation' ('userID', 'firstName', 'lastName') " +
                "SELECT DISTINCT " +userID +",'"+getInfoUserList.get(0).getFirstName()+"','"+
                getInfoUserList.get(0).getLastName()+ "' FROM 'UserVkInformation'" +
                "WHERE NOT EXISTS (SELECT 'userID' FROM 'UserVkInformation' WHERE userID="+userID+");");        // проверка есть ли такая в бд. если нет, то добавить инфу.
    }

    //занесение данных в бд прав новых пользователей логин, userID, право - пользователь
    public static void addInfoUserRights(int userID, UserActor actor) throws SQLException {
        statmt.execute("INSERT  INTO 'UserRights' ('login', 'userID', 'nameRight') " +
                "SELECT DISTINCT "+actor.getId()+","+userID+",'Пользователь' FROM 'UserRights'" +
                "WHERE NOT EXISTS (SELECT 'login','userID' FROM 'UserRights' " +
                "WHERE login="+actor.getId()+" AND userID="+userID+");");      // проверка если прав у пользователя нет, то добавить права пользователя
    }

    // инициализация одной таблицы  REQUEST RESPONSE
    public static String findUserRights(int userID, UserActor actor) throws SQLException{
        ResultSet resSet;
        resSet = statmt.executeQuery("SELECT nameRight FROM UserRights WHERE login="+actor.getId()+" AND userID="+userID);
        String  nameRight="";
        while(resSet.next())
        {
            nameRight = resSet.getString("nameRight");
        }
        resSet.close();
        return nameRight;
    }


    // --------Создание первичных таблиц--------
    public static void CreateDB() throws SQLException {

        // statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        // NameRights
        statmt.execute("CREATE TABLE if not exists 'NameRights' ('nameRight' TEXT UNIQUE" +
                "NOT NULL PRIMARY KEY);");
        // RandomBazaBot
        statmt.execute("CREATE TABLE if not exists 'RandomBazaBot' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'requesttextbot'  TEXT (100), 'responsetextbot' TEXT (100));");

        // RandomMessages
        statmt.execute("CREATE TABLE if not exists 'RandomMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'request' TEXT    NOT NULL UNIQUE ON CONFLICT IGNORE);");
        // UserProgramInformation
        statmt.execute("CREATE TABLE if not exists 'UserProgramInformation' ('Login' TEXT NOT NULL PRIMARY KEY, " +
                "'UserID' INT, 'LastName' CHAR, 'FirstName' CHAR, 'accessToken' TEXT);");
        // UserVkInformation
        statmt.execute("CREATE TABLE if not exists 'UserVkInformation' ('userID' INTEGER NOT NULL PRIMARY KEY ON CONFLICT FAIL, " +
                "'firstName' TEXT, 'lastName' TEXT, 'countMessages' INTEGER DEFAULT (0), 'countError' INTEGER DEFAULT (0));");
        // UserRights
        statmt.execute("CREATE TABLE if not exists 'UserRights' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'login' TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, " +
                "'userID' TEXT    REFERENCES 'UserVkInformation' ('userID') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, " +
                "'nameRight' TEXT    REFERENCES 'NameRights' ('nameRight') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);");
        // StatisticsBot
        statmt.execute("CREATE TABLE if not exists 'StatisticsBot' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'Login'      TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL," +
                "'Error' TEXT, 'countError' INTEGER, 'date' DATE);");
        // BotMessages
        statmt.execute("CREATE TABLE if not exists 'BotMessages' ('id' INTEGER NOT NULL UNIQUE PRIMARY KEY ASC AUTOINCREMENT, " +
                "'requesttextbot' CHAR NOT NULL, 'responsetextbot' CHAR  NOT NULL,  " +
                "'Login' TEXT REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL);");

        System.out.println("Таблица создана или уже существует.");

    }

    // --------Создание вторичных таблиц--------
    public static void CreateSecondaryDB() throws SQLException {

        // statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'phone' INT);");

        // AforismMessages
        statmt.execute("CREATE TABLE if not exists 'AforismMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
                "'request' TEXT (500) NOT NULL);");
        // AnekdotMessages
        statmt.execute("CREATE TABLE if not exists 'AnekdotMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'request'  TEXT NOT NULL);");


        // NorkinForewer
        statmt.execute("CREATE TABLE if not exists 'NorkinForewer' ('id' INTEGER UNIQUE PRIMARY KEY ASC AUTOINCREMENT NOT NULL, " +
                "'requesttextbot' CHAR    NOT NULL, 'responsetextbot' CHAR    NOT NULL, " +
                "'Login' TEXT    REFERENCES 'UserProgramInformation' ('Login') ON DELETE CASCADE ON UPDATE CASCADE NOT NULL DEFAULT (294987132));");

        // StatusMessages
        statmt.execute("CREATE TABLE if not exists 'StatusMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'request'  TEXT NOT NULL);");
        // StihMessages
        statmt.execute("CREATE TABLE if not exists 'StihMessages' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "'request'  TEXT NOT NULL);");

        System.out.println("Вторичные таблицы созданы или уже существуют.");

    }
    // --------Создание первичной записи в таблице--------
    public static void InsertIntoTablePrimary() throws SQLException {
        statmt.execute("INSERT INTO 'RandomMessages' ('request')  VALUES  ('Это тестовая запись'); ");
    }



    // парсер добавления в таблицу через вк

    public static String addToDB(String word) throws SQLException, ClassNotFoundException {

        String firstWord="", secondWord="";
        char[] chars = new char[50];
        boolean first=false,second=false,boolfirst=true;
        int i=0;
        while (!second){

            if (word.length()-1<i) return "Неправильный формат";
            if (word.charAt(i)=='('){
                while (word.charAt(i)!=')'){
                    i++;
                    if (word.length()-1<i) return "Неправильный формат";
                    if (word.charAt(i)!=')')
                        chars[i]=word.charAt(i);
                }
                if (first){

                    second=true;
                }
                first=true;
            }
            if (first&&boolfirst){
                firstWord=new String(chars).trim();
                boolfirst=false;
            }
            if (second){
                secondWord=new String(chars).trim();
            }
            i++;
        }
        secondWord=secondWord.replace(firstWord, "");
        secondWord=secondWord.replaceAll("\\u0000", "");
        System.out.println("firstWord " + firstWord);
        System.out.println("secondWord " + secondWord);
        addElementinDialog(firstWord,secondWord);
        return "Сделано!";
    }

    /* пример написания запроса, чтоб исключал повторяющиеся
 * INSERT INTO UserRights(login, userID, nameRight)
 * SELECT DISTINCT 294987132, 30562433, 'Пользователь' FROM UserRights
 * WHERE NOT EXISTS (SELECT userID FROM UserRights WHERE userID = '30562433')
 */
}
