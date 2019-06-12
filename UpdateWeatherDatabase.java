package Assignment5Pro;

import java.io.IOException;
import java.sql.SQLException;

import org.json.simple.parser.ParseException;

public class UpdateWeatherDatabase {

	public UpdateWeatherDatabase() throws SQLException, IOException, ParseException {

		// hard code the specified city list
		String[] cityArray = new String[5];
		cityArray[0] = "NewHaven";
		cityArray[1] = "Chicago";
		cityArray[2] = "Seattle";
		cityArray[3] = "Phoenix";
		cityArray[4] = "Charlotte";

		// hard code the specified state list
		String[] stateArray = new String[5];
		stateArray[0] = "CT";
		stateArray[1] = "IL";
		stateArray[2] = "WA";
		stateArray[3] = "AZ";
		stateArray[4] = "NC";

		// instantiation
		DatabaseProject db = new DatabaseProject();

		// set up mysql time zone and clear the previous data in the tabel
		// CurrentWeather and ForecastWeather
		db.prepareDatabase();

		// store lat & lng data into weather database
		for (int i = 0; i < 5; i++) {

			// get latitude and longitude through google api based on the input of city and
			// state
			// populate lat and lng data into the table adrresses
			db.doInput(cityArray[i], stateArray[i], i + 1);

		}

		// store weather data into weather database
		for (int i = 0; i < 5; i++) {

			// get weather data from OpenWeatherMap apis and populate data into table
			// CurrentWeather and ForecastWeather
			db.doOutput(db.getLatFromdb(i + 1), db.getLngFromdb(i + 1), i + 1);
		}

		System.out.println("*********************************************************************");
		System.out.println("The new data has loaded to weather database.");
	}

}
