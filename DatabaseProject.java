package Assignment5Pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.ui.RefineryUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DatabaseProject {

	// variables used for database connection
	static String url = "jdbc:mysql://localhost:3306/weatherdb?useSSL=false";
	static String user = "root";
	static String password = "Lyf19871101";
	static Connection con;

	// set up mysql time zone and clear the previous data in the tabel
	// CurrentWeather and ForecastWeather
	public static void prepareDatabase() throws SQLException {

		con = DriverManager.getConnection(url, user, password);
		Statement stmt = con.createStatement();

		stmt.execute("SET @@global.time_zone = '+00:00';");
		stmt.execute("SET @@session.time_zone = '+00:00';");

		stmt.execute("TRUNCATE ForecastWeather;");
		stmt.execute("TRUNCATE CurrentWeather;");

		con.close();

	}

	// get latitude and longitude through google api based on the input of city and
	// state
	// populate lat and lng data into the table adrresses
	public static void doInput(String city, String state, int index) throws IOException, ParseException, SQLException {

		URL location_url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + city + "+" + state
				+ "&key=AIzaSyBS2nF9cSwnLi4aVYyHcYcwJoW6l3yG6i4");

		// https://maps.googleapis.com/maps/api/geocode/json?address=NewHaven+CT&key=AIzaSyBS2nF9cSwnLi4aVYyHcYcwJoW6l3yG6i4
		URLConnection conn = location_url.openConnection();

		// content of the resource/page
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println(
				"The content of the resource/page from Google Geolocation API given the city and state is as follows: ");
		System.out.println(
				"*****************************************************************************************************");
		String inputLine;
		// avoid parsing BufferedReader object in twice
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			System.out.println(inputLine);
			sb.append(inputLine);
		}

		// parse json file from server
		// get lat and lng
		JSONParser jsonParser = new JSONParser();
		// using sb.toString() instead of in is to avoid parsing BufferedReader object
		// in twice
		JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());

		// get an array from the JSON object
		JSONArray results = (JSONArray) jsonObject.get("results");

		Iterator i = results.iterator();

		// take each value from the json array separately
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			JSONObject structure = (JSONObject) innerObj.get("geometry");
			JSONObject structure2 = (JSONObject) structure.get("location");

			// database connection
			con = DriverManager.getConnection(url, user, password);

			// insert value to database
			String latlng_insert = "UPDATE addresses SET lat =" + (double) structure2.get("lat") + ", lng = "
					+ (double) structure2.get("lng") + " WHERE address_ID = " + index + ";";

			// UPDATE addresses SET lat = 41.308274, lng = -72.9278835 WHERE address_ID = 1;

			System.out.println(latlng_insert);

			showInsert(latlng_insert);

		}

		con.close();

		System.out.println();

		in.close();
	}

	// get lat string from the table addresses
	public static String getLatFromdb(int index) throws SQLException {

		int lat = 0;

		// database connection
		con = DriverManager.getConnection(url, user, password);

		String query = "SELECT lat FROM addresses WHERE address_ID =" + index + ";";
		PreparedStatement st = con.prepareStatement(query);
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			lat = rs.getInt("lat");
		}

		st.close();

		con.close();

		return Integer.toString(lat);
	}

	// get lng string from the table addresses
	public static String getLngFromdb(int index) throws SQLException {

		int lng = 0;

		// database connection
		con = DriverManager.getConnection(url, user, password);

		String query = "SELECT lng FROM addresses WHERE address_ID =" + index + ";";
		PreparedStatement st = con.prepareStatement(query);
		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {
			lng = rs.getInt("lng");
		}

		st.close();

		con.close();

		return Integer.toString(lng);
	}

	// get weather data from OpenWeatherMap apis and populate data into table
	// CurrentWeather and ForecastWeather
	public static void doOutput(String lat, String lng, int index) throws IOException, ParseException, SQLException {

		// get data of current weather
		// https://api.openweathermap.org/data/2.5/weather?lat=41.308274&lon=-72.9278835&APPID=5dd9a6a8292df45b90273c698a5929db
		URL cunnrentWeather_url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng
				+ "&APPID=5dd9a6a8292df45b90273c698a5929db");

		URLConnection conn2 = cunnrentWeather_url.openConnection();

		// content of the resource/page
		BufferedReader out = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
		System.out.println(
				"The content of the resource/page from OpenWeatherMap current weather API given the deolocation is as follows: ");
		System.out.println(
				"**************************************************************************************************************");
		String inputLine2;
		// avoid parsing BufferedReader object out twice
		StringBuilder sb2 = new StringBuilder();
		while ((inputLine2 = out.readLine()) != null) {
			System.out.println(inputLine2);
			sb2.append(inputLine2);
		}

		// parse the data
		JSONParser jsonParser2 = new JSONParser();
		// using sb2.toString() instead of out is to avoid parsing BufferedReader object
		// out twice
		JSONObject jsonObject2 = (JSONObject) jsonParser2.parse(sb2.toString());
		JSONObject jsonObject3 = (JSONObject) jsonObject2.get("main");

		System.out.println(
				"**************************************************************************************************************");

		// database connection
		con = DriverManager.getConnection(url, user, password);

		double curr_tempK = (double) jsonObject3.get("temp");
		double curr_tempF = (curr_tempK - 273.15) * 9 / 5 + 32;

		double curr_temp_minK = (double) jsonObject3.get("temp_min");
		double curr_temp_minF = (curr_temp_minK - 273.15) * 9 / 5 + 32;

		double curr_temp_maxK = (double) jsonObject3.get("temp_max");
		double curr_temp_maxF = (curr_temp_maxK - 273.15) * 9 / 5 + 32;

		// insert value to database
		String currW_insert = "INSERT INTO CurrentWeather (address_ID, temp, pressure, humidity, temp_min, temp_max) VALUES ("
				+ index + "," + curr_tempF + "," + jsonObject3.get("pressure") + "," + jsonObject3.get("humidity") + ","
				+ curr_temp_minF + "," + curr_temp_maxF + ");";

		System.out.println(currW_insert);

		showInsert(currW_insert);

		System.out.println();

		out.close();

		// get data of 5 days/3 hours forecast
		// https://api.openweathermap.org/data/2.5/forecast?lat=41.308274&lon=-72.9278835&APPID=5dd9a6a8292df45b90273c698a5929db
		URL fiveDaysWeather_url = new URL("https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lng
				+ "&APPID=5dd9a6a8292df45b90273c698a5929db");

		URLConnection conn3 = fiveDaysWeather_url.openConnection();

		// content of the resource/page
		BufferedReader out2 = new BufferedReader(new InputStreamReader(conn3.getInputStream()));
		System.out.println(
				"The content of the resource/page from OpenWeatherMap 5 days/3 hours weather API given the deolocation is as follows: ");
		System.out.println(
				"*********************************************************************************************************************");
		String inputLine3;
		// avoid parsing BufferedReader object out2 twice
		StringBuilder sb3 = new StringBuilder();
		while ((inputLine3 = out2.readLine()) != null) {
			System.out.println(inputLine3);
			sb3.append(inputLine3);
		}

		// parse the data
		JSONParser jsonParser4 = new JSONParser();
		// using sb3.toString() instead of out2 is to avoid parsing BufferedReader
		// object out2 twice
		JSONObject jsonObject4 = (JSONObject) jsonParser4.parse(sb3.toString());

		// get an array from the JSON object
		JSONArray list = (JSONArray) jsonObject4.get("list");

		Iterator i = list.iterator();

		String foreW_insert = "";

		String fore_tempKs = "";

		System.out.println(
				"*********************************************************************************************************************");

		// take each value from the json array separately
		while (i.hasNext()) {
			JSONObject innerObj = (JSONObject) i.next();
			// System.out.println(innerObj.get("main"));
			JSONObject structure3 = (JSONObject) innerObj.get("main");

			Object fore_tempKo = structure3.get("temp");
			double fore_tempK = Double.parseDouble(fore_tempKo.toString());
			double fore_tempF = (fore_tempK - 273.15) * 9 / 5 + 32;

			Object fore_temp_minKo = structure3.get("temp_min");
			double fore_temp_minK = Double.parseDouble(fore_temp_minKo.toString());
			double fore_temp_minF = (fore_temp_minK - 273.15) * 9 / 5 + 32;

			Object fore_temp_maxKo = structure3.get("temp_max");
			double fore_temp_maxK = Double.parseDouble(fore_temp_maxKo.toString());
			double fore_temp_maxF = (fore_temp_maxK - 273.15) * 9 / 5 + 32;

			// test result
			System.out.println("tem" + innerObj.get("dt_txt") + ": " + structure3.get("temp"));
			System.out.println("pressure" + innerObj.get("dt_txt") + ": " + structure3.get("pressure"));
			System.out.println("humidity" + innerObj.get("dt_txt") + ": " + structure3.get("humidity"));
			System.out.println("temp_min" + innerObj.get("dt_txt") + ": " + structure3.get("temp_min"));
			System.out.println("temp_max" + innerObj.get("dt_txt") + ": " + structure3.get("temp_max"));
			System.out.println("*********************************************************************");

			foreW_insert = "INSERT INTO ForecastWeather (address_ID, forecast_time, temp, pressure, humidity, temp_min, temp_max) VALUES ("
					+ index + ", '" + innerObj.get("dt_txt") + "'," + fore_tempF + "," + structure3.get("pressure")
					+ "," + structure3.get("humidity") + "," + fore_temp_minF + "," + fore_temp_maxF + ");";

		}

		System.out.println(foreW_insert);

		// insert value to database
		showInsert(foreW_insert);

		out2.close();
		con.close();
	}

	// execute sql statement
	// check execution result
	public static void showInsert(String query) throws SQLException {

		// database connection
		con = DriverManager.getConnection(url, user, password);

		Statement st = con.createStatement();
		int result = st.executeUpdate(query);
		System.out.println("The result of the update is " + result);
		st.close();
		con.close();
	}

	// show city list in our weather database system
	public static void showCityList() throws SQLException {

		// database connection
		con = DriverManager.getConnection(url, user, password);

		String query = "SELECT city FROM addresses;";
		PreparedStatement st = con.prepareStatement(query);
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			String cities = rs.getString("city");
			// print the results
			System.out.println(cities);
		}

		st.close();
		con.close();
	}

	// show current weather data based on the city chosen by the user
	// display data in GUI
	public static void showCurrWeather(String cityInterested) throws SQLException {

		// database connection
		con = DriverManager.getConnection(url, user, password);

		// SELECT * FROM CurrentWeather WHERE address_ID = (SELECT address_ID FROM
		// addresses WHERE city = 'Chicago');

		String query = "SELECT * FROM CurrentWeather WHERE address_ID = (SELECT address_ID FROM addresses WHERE city = '"
				+ cityInterested + "');";
		// System.out.println(query);
		PreparedStatement st = con.prepareStatement(query);
		ResultSet rs = st.executeQuery();

		String curr_temp = "";
		String curr_pressure = "";
		String curr_humidity = "";
		String curr_temp_min = "";
		String curr_temp_max = "";

		while (rs.next()) {

			curr_temp = rs.getString("temp");
			curr_pressure = rs.getString("pressure");
			curr_humidity = rs.getString("humidity");
			curr_temp_min = rs.getString("temp_min");
			curr_temp_max = rs.getString("temp_max");

			// print the results
			System.out.println(cityInterested + " current temperature: " + curr_temp + " current pressure: "
					+ curr_pressure + " current humidity:" + curr_humidity + " current minimum temperature:"
					+ curr_temp_min + " current maximun temperature: " + curr_temp_max);
		}

		st.close();
		con.close();

		// create the GUI for current weather result
		JFrame currframe = new JFrame(cityInterested);

		Font newFont = new Font("Courier New", Font.CENTER_BASELINE, 15);
		Font newFont2 = new Font("Courier New", Font.BOLD, 30);

		currframe.setLayout(new BorderLayout());
		currframe.setContentPane(new JLabel(new ImageIcon("BackgroundAss5.png")));
		currframe.setLayout(new FlowLayout());

		currframe.setVisible(true);
		currframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currframe.setSize(535, 300);
		currframe.setLocation(430, 100);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.PINK);
		currframe.add(panel);

		JLabel lbltitle = new JLabel("Current Weather");
		lbltitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltitle.setForeground(Color.WHITE);
		lbltitle.setFont(newFont2);
		panel.add(lbltitle);

		JLabel lbltemp = new JLabel("Temperature: " + curr_temp + "\260F");
		lbltemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltemp.setForeground(Color.GRAY);
		lbltemp.setFont(newFont);
		panel.add(lbltemp);

		JLabel lblpressure = new JLabel("Pressure: " + curr_pressure);
		lblpressure.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblpressure.setForeground(Color.GRAY);
		lblpressure.setFont(newFont);
		panel.add(lblpressure);

		JLabel lblhumidity = new JLabel("Humidity: " + curr_humidity);
		lblhumidity.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblhumidity.setForeground(Color.GRAY);
		lblhumidity.setFont(newFont);
		panel.add(lblhumidity);

		JLabel lbltempmin = new JLabel("Minimun Temperature: " + curr_temp_min + "\260F");
		lbltempmin.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltempmin.setForeground(Color.GRAY);
		lbltempmin.setFont(newFont);
		panel.add(lbltempmin);

		JLabel lbltempmax = new JLabel("Maximum Temperature: " + curr_temp_max + "\260F");
		lbltempmax.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltempmax.setForeground(Color.GRAY);
		lbltempmax.setFont(newFont);
		panel.add(lbltempmax);

		// Add go back button to let the user go back to the first page
		JButton gobackbtn = new JButton("GO BACK");

		gobackbtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		ActionListener gobackbtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				currframe.setVisible(false);
			}
		};

		gobackbtn.addActionListener(gobackbtnActionListener);
		panel.add(gobackbtn);

		currframe.setVisible(true);

	}

	// show forecast weather data based on the city chosen by the user
	// display data in GUI
	public static void showForecastWeather(String cityInterested) throws SQLException {

		// database connection
		con = DriverManager.getConnection(url, user, password);

		// SELECT * FROM ForecastWeather WHERE address_ID = (SELECT address_ID FROM
		// addresses WHERE city = 'Chicago');

		String query = "SELECT * FROM ForecastWeather WHERE address_ID = (SELECT address_ID FROM addresses WHERE city = '"
				+ cityInterested + "');";
		PreparedStatement st = con.prepareStatement(query);
		ResultSet rs = st.executeQuery();

		String fore_temp = "";
		String fore_pressure = "";
		String fore_humidity = "";
		String fore_temp_min = "";
		String fore_temp_max = "";

		while (rs.next()) {

			fore_temp = rs.getString("temp");
			fore_pressure = rs.getString("pressure");
			fore_humidity = rs.getString("humidity");
			fore_temp_min = rs.getString("temp_min");
			fore_temp_max = rs.getString("temp_max");

			// print the results
			System.out.println(cityInterested + " forecast temperature: " + fore_temp + " forecast pressure: "
					+ fore_pressure + " forecast humidity: " + fore_humidity + " forecast minimum temperature: "
					+ fore_temp_min + " forecast maximun temperature: " + fore_temp_max);
		}

		st.close();
		con.close();

		// create the GUI for forecast weather result
		JFrame foreframe = new JFrame(cityInterested);

		Font newFont = new Font("Courier New", Font.CENTER_BASELINE, 15);
		Font newFont2 = new Font("Courier New", Font.BOLD, 30);

		foreframe.setLayout(new BorderLayout());
		foreframe.setContentPane(new JLabel(new ImageIcon("BackgroundAss5.png")));
		foreframe.setLayout(new FlowLayout());

		foreframe.setVisible(true);
		foreframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		foreframe.setSize(535, 300);
		foreframe.setLocation(430, 100);

		JPanel panel = new JPanel();
		panel.setBackground(Color.PINK);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		foreframe.add(panel);

		JLabel lbltitle = new JLabel("Forecast Weather");
		lbltitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltitle.setForeground(Color.WHITE);
		lbltitle.setFont(newFont2);
		panel.add(lbltitle);

		JLabel lbltemp = new JLabel("Temperature: " + fore_temp + "\260F");
		lbltemp.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltemp.setForeground(Color.GRAY);
		lbltemp.setFont(newFont);
		panel.add(lbltemp);

		JLabel lblpressure = new JLabel("Pressure: " + fore_pressure);
		lblpressure.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblpressure.setForeground(Color.GRAY);
		lblpressure.setFont(newFont);
		panel.add(lblpressure);

		JLabel lblhumidity = new JLabel("Humidity: " + fore_humidity);
		lblhumidity.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblhumidity.setForeground(Color.GRAY);
		lblhumidity.setFont(newFont);
		panel.add(lblhumidity);

		JLabel lbltempmin = new JLabel("Minimun Temperature: " + fore_temp_min + "\260F");
		lbltempmin.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltempmin.setForeground(Color.GRAY);
		lbltempmin.setFont(newFont);
		panel.add(lbltempmin);

		JLabel lbltempmax = new JLabel("Maximum Temperature: " + fore_temp_max + "\260F");
		lbltempmax.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltempmax.setForeground(Color.GRAY);
		lbltempmax.setFont(newFont);
		panel.add(lbltempmax);

		// Add go back button to let the user go back to the first page
		JButton gobackbtn = new JButton("GO BACK");

		gobackbtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		ActionListener gobackbtnActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				foreframe.setVisible(false);
			}
		};

		gobackbtn.addActionListener(gobackbtnActionListener);
		panel.add(gobackbtn);

		foreframe.setVisible(true);
	}

	// show comparison of current and forecast weather data based on the city chosen
	// by the user
	// display comparison result in bar chart using GUI
	public void compareWeather(String cityInterested) throws SQLException {

		// database connection
		con = DriverManager.getConnection(url, user, password);

		// SELECT * FROM ForecastWeather WHERE address_ID = (SELECT address_ID FROM
		// addresses WHERE city = 'Chicago');

		String query2 = "SELECT * FROM CurrentWeather WHERE address_ID = (SELECT address_ID FROM addresses WHERE city = '"
				+ cityInterested + "');";
		String query3 = "SELECT * FROM ForecastWeather WHERE address_ID = (SELECT address_ID FROM addresses WHERE city = '"
				+ cityInterested + "');";
		PreparedStatement st = con.prepareStatement(query2);
		ResultSet rs = st.executeQuery();

		float curr_weather[] = new float[5];
		float fore_weather[] = new float[5];
		while (rs.next()) {

			curr_weather[0] = Float.parseFloat(rs.getString("temp"));
			curr_weather[1] = Float.parseFloat(rs.getString("pressure"));
			curr_weather[2] = Float.parseFloat(rs.getString("humidity"));
			curr_weather[3] = Float.parseFloat(rs.getString("temp_min"));
			curr_weather[4] = Float.parseFloat(rs.getString("temp_max"));

			// print the results
			System.out.println(cityInterested + " current temperature: " + curr_weather[0] + " current pressure: "
					+ curr_weather[1] + " current humidity:" + curr_weather[2] + " current minimum temperature:"
					+ curr_weather[3] + " current maximun temperature: " + curr_weather[4]);
		}

		PreparedStatement st1 = con.prepareStatement(query3);
		ResultSet rs1 = st1.executeQuery();

		while (rs1.next()) {

			fore_weather[0] = Float.parseFloat(rs1.getString("temp"));
			fore_weather[1] = Float.parseFloat(rs1.getString("pressure"));
			fore_weather[2] = Float.parseFloat(rs1.getString("humidity"));
			fore_weather[3] = Float.parseFloat(rs1.getString("temp_min"));
			fore_weather[4] = Float.parseFloat(rs1.getString("temp_max"));

			// print the results
			System.out.println(cityInterested + " forecast temperature: " + fore_weather[0] + " forecast pressure: "
					+ fore_weather[1] + " forecast humidity: " + fore_weather[2] + " forecast minimum temperature: "
					+ fore_weather[3] + " forecast maximun temperature: " + fore_weather[4]);
		}

		st.close();
		st1.close();
		con.close();

		CompWeather chart = new CompWeather("Weather Forecast", "Current vs Forecast Weather", curr_weather,
				fore_weather);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);

	}
}
