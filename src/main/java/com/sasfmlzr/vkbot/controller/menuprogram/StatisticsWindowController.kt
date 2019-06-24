package com.sasfmlzr.vkbot.controller.menuprogram

import com.sasfmlzr.apivk.`object`.StatisticsVariable
import com.sasfmlzr.vkbot.StaticModel
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.StackedAreaChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.RadioButton
import javafx.scene.control.TextArea
import java.net.URL
import java.util.*

class StatisticsWindowController : Initializable {

    @FXML
    private lateinit var textLog: TextArea
    @FXML
    lateinit var timeZaprosVk: StackedAreaChart<Int, Long>
    @FXML
    lateinit var timeZaprosVkX: NumberAxis
    @FXML
    lateinit var timeZaprosVkY: NumberAxis
    @FXML
    lateinit var timeThread: StackedAreaChart<Int, Int>
    @FXML
    lateinit var timeThreadX: NumberAxis
    @FXML
    lateinit var timeThreadY: NumberAxis
    @FXML
    lateinit var timeBigBD: StackedAreaChart<*, *>
    @FXML
    lateinit var timeBigBDX: NumberAxis
    @FXML
    lateinit var timeBigBDY: NumberAxis

    @FXML
    private lateinit var button: Button

    @FXML
    private lateinit var setTen: RadioButton
    @FXML
    private lateinit var setFiveteen: RadioButton
    @FXML
    private lateinit var setHungry: RadioButton
    @FXML
    private lateinit var setThousand: RadioButton
    @FXML
    private lateinit var setAuto: RadioButton

    override fun initialize(location: URL, resources: ResourceBundle) {}

    fun initWindow() {
        textLog.text = ""
        if (StaticModel.userBot.botApiClient().stateBot.botWork) {
            /*BufferedReader bReader = new BufferedReader(new FileReader("src/resources/locale/StatisticsWindow/Log.txt"));
			System.out.println(bReader);
			String s;
			while ((s = bReader.readLine()) != null) {
				textLog.appendText(s + "\n");
			}*/
            textLog.appendText("Статистика бота" + "\n")
            seriesZaprosVk.setName("Запрос в вк")
            seriesZaprosVk.setName("Запрос с отправкой")


            //	timeZaprosVk;
            timeZaprosVk.data.setAll(seriesZaprosVk, seriesItogVk)
            //	timeZaprosVk.getData() .setAll( seriesItogVk);

            timeThread.data.setAll(seriesThread)
            //			timeBigBD.getData() .setAll(seriesBigBD);
        } else {
            textLog.appendText("Сначала запусти бота" + "\n")
        }
        //		System.out.println(s);
    }

    //////////////при нажатии на кнопку
    fun blabla() {

        print("setUpperBound " + timeZaprosVkX.upperBound + "\n")
        punchRadioButton()
        print("выполнено \n")
    }


    //////////////при нажатии на кнопку выбора кратности статистики
    fun punchRadioButton() {
        //setTen,setFiveteen,setHungry,setThousand;
        if (setTen.isFocused) {
            //System.out.print("время прохода минус запросвк= "+ "\n");
            //setFiveteen.;
            setTen.isSelected = true
            setFiveteen.isSelected = false
            setHungry.isSelected = false
            setThousand.isSelected = false
            setAuto.isSelected = false
            setTen.requestFocus()
            val time = Timer()
            time.schedule(object : TimerTask() {
                override fun run() { //ПЕРЕЗАГРУЖАЕМ МЕТОД RUN В КОТОРОМ ДЕЛАЕТЕ ТО ЧТО ВАМ НАДО
                    if (!setTen.isSelected) {
                        time.cancel()
                    }
                    if (StatisticsVariable.countSendMessageUser - 10 > 1) {
                        timeZaprosVkX.isAutoRanging = false
                        timeZaprosVkX.lowerBound = (StatisticsVariable.countSendMessageUser - 10).toDouble()
                        timeZaprosVkX.upperBound = (5 + StatisticsVariable.countSendMessageUser).toDouble()
                        //	timeZaprosVkY.setAutoRanging(false);
                        if (StatisticsVariable.timeItogoMsMinusVK > StatisticsVariable.timeZaprosFinishItogo) {
                            timeZaprosVkX.upperBound = (100 + StatisticsVariable.countSendMessageUser).toDouble()
                        }
                        timeThreadX.isAutoRanging = false
                        timeThreadX.lowerBound = (StatisticsVariable.countSendMessageUser - 10).toDouble()
                        timeThreadX.upperBound = (5 + StatisticsVariable.countSendMessageUser).toDouble()
                    } else
                        setTen.isSelected = false
                    if (!setTen.isSelected) {
                        println("Таймер завершил свою работу")
                        time.cancel()
                    }

                }
            }, 0, 3000) //(выполнять каждые 3 сек))
        }
        if (setFiveteen.isFocused) {
            setFiveteen.isSelected = true
            setTen.isSelected = false
            setHungry.isSelected = false
            setThousand.isSelected = false
            setAuto.isSelected = false
            setFiveteen.requestFocus()
            val time = Timer()
            time.schedule(object : TimerTask() {
                override fun run() { //запускаем таймер
                    if (!setFiveteen.isSelected) {
                        time.cancel()
                    }
                    if (StatisticsVariable.countSendMessageUser - 50 > 1) {
                        timeZaprosVkX.isAutoRanging = false
                        timeZaprosVkX.lowerBound = (StatisticsVariable.countSendMessageUser - 50).toDouble()
                        timeZaprosVkX.upperBound = (20 + StatisticsVariable.countSendMessageUser).toDouble()
                        timeThreadX.isAutoRanging = false
                        timeThreadX.lowerBound = (StatisticsVariable.countSendMessageUser - 50).toDouble()
                        timeThreadX.upperBound = (20 + StatisticsVariable.countSendMessageUser).toDouble()
                    } else
                        setFiveteen.isSelected = false
                    if (!setFiveteen.isSelected) {
                        println("Таймер завершил свою работу")
                        time.cancel()
                    }

                }
            }, 0, 5000) //(выполнять каждые 3 сек))
        }
        if (setHungry.isFocused) {
            setHungry.isSelected = true
            setTen.isSelected = false
            setFiveteen.isSelected = false
            setThousand.isSelected = false
            setAuto.isSelected = false
            setHungry.requestFocus()
            val time = Timer()
            time.schedule(object : TimerTask() {
                override fun run() { //запускаем таймер
                    if (!setHungry.isSelected) {
                        time.cancel()
                    }
                    if (StatisticsVariable.countSendMessageUser - 100 > 1) {
                        timeZaprosVkX.isAutoRanging = false
                        timeZaprosVkX.lowerBound = (StatisticsVariable.countSendMessageUser - 100).toDouble()
                        timeZaprosVkX.upperBound = (30 + StatisticsVariable.countSendMessageUser).toDouble()
                        timeThreadX.isAutoRanging = false
                        timeThreadX.lowerBound = (StatisticsVariable.countSendMessageUser - 100).toDouble()
                        timeThreadX.upperBound = (30 + StatisticsVariable.countSendMessageUser).toDouble()
                    } else
                        setHungry.isSelected = false
                    if (!setHungry.isSelected) {
                        println("Таймер завершил свою работу")
                        time.cancel()
                    }

                }
            }, 0, 7000) //(выполнять каждые 3 сек))
        }
        if (setThousand.isFocused) {
            setThousand.isSelected = true
            setTen.isSelected = false
            setFiveteen.isSelected = false
            setHungry.isSelected = false
            setAuto.isSelected = false
            setThousand.requestFocus()
            val time = Timer()
            time.schedule(object : TimerTask() {
                override fun run() { //запускаем таймер
                    if (!setThousand.isSelected) {
                        time.cancel()
                    }
                    if (StatisticsVariable.countSendMessageUser - 1000 > 1) {
                        timeZaprosVkX.isAutoRanging = false
                        timeZaprosVkX.lowerBound = (StatisticsVariable.countSendMessageUser - 1000).toDouble()
                        timeZaprosVkX.upperBound = (200 + StatisticsVariable.countSendMessageUser).toDouble()
                        timeThreadX.isAutoRanging = false
                        timeThreadX.lowerBound = (StatisticsVariable.countSendMessageUser - 1000).toDouble()
                        timeThreadX.upperBound = (200 + StatisticsVariable.countSendMessageUser).toDouble()
                    } else
                        setThousand.isSelected = false
                    if (!setThousand.isSelected) {
                        println("Таймер завершил свою работу")
                        time.cancel()
                    }

                }
            }, 0, 10000) //(выполнять каждые 3 сек))

        }
        if (setAuto.isFocused) {
            setAuto.isSelected = true
            setTen.isSelected = false
            setFiveteen.isSelected = false
            setHungry.isSelected = false
            setThousand.isSelected = false
            setAuto.requestFocus()
            timeZaprosVkX.isAutoRanging = true
            timeThreadX.isAutoRanging = true
        }
    }

    companion object {
        val resourcePath = "com.sasfmlzr.vkbot.resourcebundle.StatisticsWindow.messages"
        val fxmlPath = "StatisticsWindow.fxml"


        var seriesItogVk = XYChart.Series<Int, Long>()
        var seriesZaprosVk = XYChart.Series<Int, Long>()
        var seriesThread = XYChart.Series<Int, Int>()
        var seriesBigBD = XYChart.Series<Int, Int>()
    }
    
}




