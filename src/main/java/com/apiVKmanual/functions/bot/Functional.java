package com.apiVKmanual.functions.bot;

import com.apiVKmanual.actions.Messages;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

@SuppressWarnings("ConstantConditions, deprecation")
public class Functional extends Messages{
    public Functional(com.apiVKmanual.client.BotApiClient client) {
        super(client);
    }

    private final String apiKeqOWM="5c202047bca9a767f476b06c746ab856";
    public String weather(String city)  {
        String pogoda="";
        // берем апи с OpenWeatherMap
        OWM owm = new OWM(apiKeqOWM);
        // выбираем город
        CurrentWeather cwd = null;
        try {
            cwd = owm.currentWeatherByCityName(city);
        } catch (APIException e) {
            e.printStackTrace();
        }
        // проверка ячейки на валидность
        if (cwd.hasRespCode() && cwd.getRespCode() == 200) {
            // проверка доступен ли город
            if (cwd.hasCityName()) {
                //printing city name from the retrieved data
                pogoda=pogoda+"Город: " + cwd.getCityName() + '\n';
                System.out.println("Город: " + cwd.getCityName());
            }
            // проверка на макс и мин температуру
            if (cwd.hasMainData() && cwd.getMainData().hasTempMax() && cwd.getMainData().hasTempMin()) {
                // printing the max./min. temperature
                System.out.println("Температура: " + cwd.getMainData().getTempMax()
                        + "/" + cwd.getMainData().getTempMin() + "\'F");
                //Градусы по Цельсию = (градусы по Фаренгейту - 32) / 1.8
                float celsiusmax = Math.round ((cwd.getMainData().getTempMax()-32)/1.8*100)/100;
                float celsiusmin = Math.round (((cwd.getMainData().getTempMin()-32)/1.8*100))/100;
                pogoda=pogoda+"Температура: от " + celsiusmin
                        + " до " + celsiusmax + "\'C"+"\n";
            }
            // проверка на среднюю температуру, влажность и давление, рассвет и закат
            if (cwd.getMainData().hasHumidity() && cwd.getMainData().hasPressure() &&
                    cwd.getMainData().hasTemp()&& cwd.getSystemData().hasSunriseDateTime()&& cwd.getSystemData().hasSunsetDateTime()) {
                float celsiussred = Math.round ((cwd.getMainData().getTemp()-32)/1.8*100)/100;
                pogoda = pogoda + "Средняя температура: "+ celsiussred + "\n"+
                        "Влажность: " + cwd.getMainData().getHumidity() + "%\n"
                        + "Давление: " + cwd.getMainData().getPressure() + " gPa \n";
                pogoda = pogoda + "Рассвет в: "+ cwd.getSystemData().getSunriseDateTime().getHours() + ":"+
                        cwd.getSystemData().getSunriseDateTime().getMinutes()+ ":" + cwd.getSystemData().getSunriseDateTime().getSeconds()+ "\n"+
                        "Закат в: " + cwd.getSystemData().getSunsetDateTime().getHours() + ":"+
                        cwd.getSystemData().getSunsetDateTime().getMinutes()+ ":" + cwd.getSystemData().getSunsetDateTime().getSeconds()+ "\n";
            }
            // проверка на скорость ветра, облачность и дождь
            if (cwd.getWindData().hasSpeed()) {
                pogoda = pogoda + "Скорость ветра: "+ cwd.getWindData().getSpeed() + " метров в сек  \n";
            }
            if (cwd.getCloudData().hasCloudiness() ) {
                pogoda = pogoda + "Облачность: "+ cwd.getCloudData().getCloudiness() + "%\n";
            }
            if (cwd.hasRainData() ) {
                pogoda = pogoda + "Дождь : "+ cwd.getRainData().getPrecipVol3h() + "\n";
            }
            /////////////////////////////////здесь возможны доп функции///////////////////
                      /* if (cwd.getWindData().getWindGust() != NaN && cwd.getWindData().hasWindGust()) {
                float por = cwd.getWindData().getWindGust();
                pogoda = pogoda + "Порывы ветра: "+ cwd.getWindData().getWindGust() + "\n";
            }
            if (cwd.getWindData().hasWindDegree()) {
                pogoda = pogoda + "Что-то там ветра: "+ cwd.getWindData().getWindDegree() + "\n";
            }*/
        }
        return pogoda;
    }
}
