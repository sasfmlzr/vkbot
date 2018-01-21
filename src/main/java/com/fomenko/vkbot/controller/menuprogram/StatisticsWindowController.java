package com.fomenko.vkbot.controller.menuprogram;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import static com.apiVKmanual.object.StatisticsVariable.*;
import static com.fomenko.vkbot.StaticModel.botWork;

public class StatisticsWindowController  implements Initializable

{
	public final static String resourcePath = "com.fomenko.vkbot.resourcebundle.StatisticsWindow.messages";
	public final static String fxmlPath = "StatisticsWindow.fxml";

	@FXML private TextArea textLog;
	@FXML public StackedAreaChart timeZaprosVk;
	@FXML public NumberAxis timeZaprosVkX;
	@FXML public NumberAxis timeZaprosVkY;
	@FXML public StackedAreaChart timeThread;
	@FXML public NumberAxis timeThreadX;
	@FXML public NumberAxis timeThreadY;
	@FXML public StackedAreaChart timeBigBD;
	@FXML public NumberAxis timeBigBDX;
	@FXML public NumberAxis timeBigBDY;

	@FXML private Button button;

	@FXML private RadioButton setTen,setFiveteen,setHungry,setThousand,setAuto;

	public void initialize(URL location, ResourceBundle resources)	{	}
	public void initWindow() {
		textLog.setText("");
		if (botWork) {
			/*BufferedReader bReader = new BufferedReader(new FileReader("src/resources/locale/StatisticsWindow/Log.txt"));
			System.out.println(bReader);
			String s;
			while ((s = bReader.readLine()) != null) {
				textLog.appendText(s + "\n");
			}*/
			textLog.appendText("Статистика бота" + "\n");
			seriesZaprosVk.setName("Запрос в вк");
			seriesZaprosVk.setName("Запрос с отправкой");



		//	timeZaprosVk;
			timeZaprosVk.getData() .setAll(seriesZaprosVk,seriesItogVk);
		//	timeZaprosVk.getData() .setAll( seriesItogVk);

			timeThread.getData() .setAll(seriesThread);
//			timeBigBD.getData() .setAll(seriesBigBD);
		}
		else{
			textLog.appendText("Сначала запусти бота" + "\n");
		}



	//		System.out.println(s);
		}
//////////////при нажатии на кнопку
		public void blabla () {

			System.out.print("setUpperBound "+ timeZaprosVkX.getUpperBound() + "\n");
			punchRadioButton ();
				System.out.print("выполнено \n");
}


	public static XYChart.Series seriesItogVk = new XYChart.Series();
	public static XYChart.Series seriesZaprosVk = new XYChart.Series();
	public static XYChart.Series<Integer,Integer> seriesThread = new XYChart.Series<>();
	public static XYChart.Series<Integer,Integer> seriesBigBD = new XYChart.Series<>();


	//////////////при нажатии на кнопку выбора кратности статистики
public void punchRadioButton (){
	//setTen,setFiveteen,setHungry,setThousand;
	if (setTen.isFocused()){
		//System.out.print("время прохода минус запросвк= "+ "\n");
		//setFiveteen.;
		setTen.setSelected (true);
		setFiveteen.setSelected (false);
		setHungry.setSelected (false);
		setThousand.setSelected (false);
		setAuto.setSelected (false);
		setTen.requestFocus ();
		final Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() { //ПЕРЕЗАГРУЖАЕМ МЕТОД RUN В КОТОРОМ ДЕЛАЕТЕ ТО ЧТО ВАМ НАДО
				if(!setTen.isSelected()){
					time.cancel();
				}
					if (countSendMessageUser -10>1) {
						timeZaprosVkX.setAutoRanging(false);
						timeZaprosVkX.setLowerBound(countSendMessageUser - 10);
						timeZaprosVkX.setUpperBound(5 + countSendMessageUser);
					//	timeZaprosVkY.setAutoRanging(false);
						if (timeItogoMsMinusVK >timeZaprosFinishItogo){
							timeZaprosVkX.setUpperBound(100 + countSendMessageUser);
						}
						timeThreadX.setAutoRanging(false);
						timeThreadX.setLowerBound(countSendMessageUser - 10);
						timeThreadX.setUpperBound(5 + countSendMessageUser);
					}else setTen.setSelected (false);
					if(!setTen.isSelected()){
						System.out.println("Таймер завершил свою работу");
						time.cancel();
					}

			}
		}, 0, 3000); //(выполнять каждые 3 сек))
	}
	if (setFiveteen.isFocused()){
		setFiveteen.setSelected (true);
		setTen.setSelected (false);
		setHungry.setSelected (false);
		setThousand.setSelected (false);
		setAuto.setSelected (false);
		setFiveteen.requestFocus ();
		final Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() { //запускаем таймер
				if(!setFiveteen.isSelected()){
					time.cancel();
				}
				if (countSendMessageUser -50>1) {
					timeZaprosVkX.setAutoRanging(false);
					timeZaprosVkX.setLowerBound(countSendMessageUser - 50);
					timeZaprosVkX.setUpperBound(20 + countSendMessageUser);
					timeThreadX.setAutoRanging(false);
					timeThreadX.setLowerBound(countSendMessageUser - 50);
					timeThreadX.setUpperBound(20 + countSendMessageUser);
				}else setFiveteen.setSelected (false);
				if(!setFiveteen.isSelected()){
					System.out.println("Таймер завершил свою работу");
					time.cancel();
				}

			}
		}, 0, 5000); //(выполнять каждые 3 сек))
	}
	if (setHungry.isFocused()){
		setHungry.setSelected (true);
		setTen.setSelected (false);
		setFiveteen.setSelected (false);
		setThousand.setSelected (false);
		setAuto.setSelected (false);
		setHungry.requestFocus ();
		final Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() { //запускаем таймер
				if(!setHungry.isSelected()){
					time.cancel();
				}
				if (countSendMessageUser -100>1) {
					timeZaprosVkX.setAutoRanging(false);
					timeZaprosVkX.setLowerBound(countSendMessageUser - 100);
					timeZaprosVkX.setUpperBound(30 + countSendMessageUser);
					timeThreadX.setAutoRanging(false);
					timeThreadX.setLowerBound(countSendMessageUser - 100);
					timeThreadX.setUpperBound(30 + countSendMessageUser);
				}else setHungry.setSelected (false);
				if(!setHungry.isSelected()){
					System.out.println("Таймер завершил свою работу");
					time.cancel();
				}

			}
		}, 0, 7000); //(выполнять каждые 3 сек))
	}
	if (setThousand.isFocused()){
		setThousand.setSelected (true);
		setTen.setSelected (false);
		setFiveteen.setSelected (false);
		setHungry.setSelected (false);
		setAuto.setSelected (false);
		setThousand.requestFocus ();
		final Timer time = new Timer();
		time.schedule(new TimerTask() {
			@Override
			public void run() { //запускаем таймер
				if(!setThousand.isSelected()){
					time.cancel();
				}
				if (countSendMessageUser -1000>1) {
					timeZaprosVkX.setAutoRanging(false);
					timeZaprosVkX.setLowerBound(countSendMessageUser - 1000);
					timeZaprosVkX.setUpperBound(200 + countSendMessageUser);
					timeThreadX.setAutoRanging(false);
					timeThreadX.setLowerBound(countSendMessageUser - 1000);
					timeThreadX.setUpperBound(200 + countSendMessageUser);
				}else setThousand.setSelected (false);
				if(!setThousand.isSelected()){
					System.out.println("Таймер завершил свою работу");
					time.cancel();
				}

			}
		}, 0, 10000); //(выполнять каждые 3 сек))

	}
	if (setAuto.isFocused()){
		setAuto.setSelected (true);
		setTen.setSelected (false);
		setFiveteen.setSelected (false);
		setHungry.setSelected (false);
		setThousand.setSelected (false);
		setAuto.requestFocus ();
		timeZaprosVkX.setAutoRanging(true);
		timeThreadX.setAutoRanging(true);
	}
}





}




