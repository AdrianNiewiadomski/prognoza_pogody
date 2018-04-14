package application;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {
	@FXML LineChart<String,Number> lineChart1;
	@FXML BarChart<String,Number>  barChart2;
	@FXML AreaChart<String,Number> areaChart3;
	@FXML AreaChart<String,Number> areaChart4;
	
	@FXML    private CategoryAxis x3;
    @FXML    private NumberAxis y3;
    
    @FXML 	 private TextField poleMiasta;
    @FXML	 private TextField poleKraju;
    
    @FXML	private Label uwagi;
    
    private List<Prognoza> prognozy = new ArrayList<Prognoza>();
	
	public void pobierz(ActionEvent event){
		lineChart1.getData().clear();
		barChart2.getData().clear();
		areaChart3.getData().clear();
		areaChart4.getData().clear();
		
		
		XYChart.Series<String,Number> series1=new XYChart.Series<String,Number>();
		XYChart.Series<String,Number> series2a=new XYChart.Series<String,Number>();
		XYChart.Series<String,Number> series2b=new XYChart.Series<String,Number>();
		XYChart.Series<String,Number> series3=new XYChart.Series<String,Number>();
		XYChart.Series<String,Number> series4=new XYChart.Series<String,Number>();
		
		String Miasto=poleMiasta.getText(),	Kraj=poleKraju.getText();
		poleMiasta.clear();poleKraju.clear();
		
		String linijka="";
		Prognoza prognoza;
		if((Miasto!="")&&(Kraj!="")&&(Miasto.matches("[A-Z]*[a-z]*"))&&(Kraj.matches("[A-Z]*[a-z]*"))){
			
			if(!prognozy.isEmpty()){
				for (int i=0;i<prognozy.size();i++) {
					prognoza=prognozy.get(i);
					
					
					if((Miasto.equals(prognoza.podajMiasto()))&&(prognoza.podajPanstwo().equals(Kraj))){
						linijka=prognoza.podajJson();
						System.out.println("Pobrano ju¿ wczesniej prognoze dla: "+prognoza.podajMiasto());
					}
				}
			}
			
				// read JSON
			if(linijka==""){
				try{
					linijka=pobierzDaneZSieci(Miasto,Kraj);
				}catch(Exception e){
					/*System.out.println("Niepowodzenie pobrania danych");
					e.printStackTrace();
					*/
					uwagi.setText("Niepowodzenie pobrania prognozy. \nCzy wprowadzono poprawne dane?");
					
				}
				prognozy.add(new Prognoza(Miasto,Kraj,linijka));
				System.out.println("dodano el do listy");
			}
				
			try {	
				//FileReader reader = new FileReader(filePath);
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(linijka);
				
				// --------------------------------------------- handle a structure into the json object
				JSONObject structure = (JSONObject) jsonObject.get("city");
				System.out.println("Prognoza dla miasta: " + structure.get("name")+"\n");
							
				
				// ---------------------------------------------------- get an array from the JSON object
				JSONArray wszystkieParametry = (JSONArray) jsonObject.get("list");
	
																	// take the elements of the json array
				for (int i = 0; i < wszystkieParametry.size(); i++) {
					//System.out.println("The " + i + " element of the array: " + wszystkieParametry.get(i));
				}
				
				// take each value from the json array separately
				int j=1;
				double maxcisnienie=0,mincisnienie=0;
				Iterator i = wszystkieParametry.iterator();
				while (i.hasNext()) {
					
					String data="";
					double deszcz=0,snieg=0,temperatura=0,predkoscWiatru=0,cisnienie=0;
					
					JSONObject objektWewnetrzny = (JSONObject) i.next();
					try{
						JSONObject deszczJSON = (JSONObject) objektWewnetrzny.get("rain");
						deszcz=(double)deszczJSON.get("3h");
					}catch(NullPointerException e){
						deszcz=0;
					}
					
					try{
						JSONObject sniegJSON = (JSONObject) objektWewnetrzny.get("snow");
						snieg=(double)sniegJSON.get("3h");
					}catch(NullPointerException e){
						snieg=0;
					}
					
					JSONObject glowneParametry = (JSONObject) objektWewnetrzny.get("main");
					
					JSONObject wiatr = (JSONObject) objektWewnetrzny.get("wind");
					predkoscWiatru=(double)wiatr.get("speed");
					temperatura=(double)glowneParametry.get("temp")-273;
					cisnienie=(double)glowneParametry.get("pressure");
					
					if(j==1){
						maxcisnienie=cisnienie; mincisnienie=cisnienie;
					}
					else{
						if(cisnienie>maxcisnienie){
							maxcisnienie=cisnienie;
						}
						if(cisnienie<mincisnienie){
							mincisnienie=cisnienie;
						}
					}
					j++;
					
					data=objektWewnetrzny.get("dt_txt").toString().replaceAll("[0-9][0-9][0-9][0-9][-]", "");
					data=data.replaceAll("00:00", "00");
					
					
					//System.out.println("Prognoza dla miasta: " + structure.get("name")+"\n");
					/*System.out.println(
						"Data: " + data
						+", deszcz: "+deszcz
						+", œnieg: "+snieg
						+", temperatura: "+temperatura
						+", ciœnienie: "+cisnienie
						+", wiatr: "+predkoscWiatru
					);*/
					
					
					series1.getData().add(new XYChart.Data<String,Number>(data, temperatura));
					series2a.getData().add(new XYChart.Data<String,Number>(data, deszcz));
					series2b.getData().add(new XYChart.Data<String,Number>(data, snieg));
					series3.getData().add(new XYChart.Data<String,Number>(data, cisnienie));
					series4.getData().add(new XYChart.Data<String,Number>(data, predkoscWiatru));
		
				}
				
				lineChart1.getXAxis().setTickLabelsVisible(false);
				lineChart1.getXAxis().setOpacity(0);
				lineChart1.getData().add(series1);
				
				barChart2.getXAxis().setTickLabelsVisible(false);
				barChart2.getXAxis().setOpacity(0);
				
				barChart2.setBarGap(0);
				barChart2.setCategoryGap(1.0);
				
				barChart2.getData().add(series2a);
				barChart2.getData().add(series2b);
				
				areaChart3.getXAxis().setTickLabelsVisible(false);
				areaChart3.getXAxis().setOpacity(0);
				
				y3.setAutoRanging(false);
			    y3.setLowerBound(mincisnienie-5);
			    y3.setUpperBound(maxcisnienie+5);
			    y3.setTickLabelFormatter(new NumberAxis.DefaultFormatter(y3) {
			    	@Override public String toString(final Number object) {        
			    		return String.format("%6.1f", object);
			        }
			    });
			    
				areaChart3.getData().add(series3);
				
				
				areaChart4.getData().add(series4);
				
	
			} catch (ParseException ex) {
	
				ex.printStackTrace();
	
			} catch (NullPointerException ex) {
	
				ex.printStackTrace();
	
			}
		}
		else{
			uwagi.setText("Niepowodzenie pobrania prognozy. \nCzy wprowadzono poprawne dane?");
		}
	}
	
	public static String pobierzDaneZSieci(String miasto, String kraj)throws Exception{
		String stringURL="http://api.openweathermap.org/data/2.5/forecast?q=Miasto,Kraj&APPID=XXX";
		
		stringURL=stringURL.replace("Miasto", miasto);
		stringURL=stringURL.replace("Kraj", kraj);
		
		//try{
			URL url=new URL(
					stringURL
					);
			
			URLConnection urlConnection = url.openConnection();
	        HttpURLConnection connection = null;
	        if(urlConnection instanceof HttpURLConnection) {
	            connection = (HttpURLConnection) urlConnection;
	        }else {
	            System.out.println("Please enter an HTTP URL.");
	            return "Please enter an HTTP URL.";
	        }
	         
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(connection.getInputStream()));
	        String urlString = "";
	        String current;
	         
	        while((current = in.readLine()) != null) {
	            urlString += current;
	        }
	        //System.out.println(urlString);
	        return urlString;
		//}
	}
}
