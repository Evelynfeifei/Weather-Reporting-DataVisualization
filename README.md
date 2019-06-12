# Weather-Reporting-DataVisualization
<h3>Introduction</h3>
This is a project of my graduate course named Advanced Java Programming.<br>
This is an implementation of retrieving data from web server API, save them in database and visualize them through GUI.<br>
System allows users to run statistics over the stored data.

<h3>Features</h3>
An end user should be able to interact with your application in the following way:<br>
*view the list of cities available<br> 
*choose an address (e.g. city) that is already available from your database<br>
*run (at least) 3 statistics (e.g. avg temp, avg. rain, “avg” weather) about a specific city<br>
*compare the current weather with the corresponding forecasted one<br>
*Note: 5 day / 3 hours forecast API is free (and this is good!). OpenWeather charges for all the other APIs providing historical data.

<h3>Software Environment & Design Method</h3>
1.Use a MySQL database to store weather forecast data<br>
2.Use the OpenWeather 5 day / 3 hour forecast APIs<br>
3.OOD is used throughout the project development

<h3>Structure</h3>
This project is designed and realized from both front end and back end.<br>
This project contains four java files. The DatabaseProject.java contains all the methods related to extracting data from APIs and saving data into database system. The UpdateWeatherDatabase.java is used for calling the methods in DatabaseProject.java so that the data will be updated. The CompWeather.java is used for creating a bar chart to visualize the data comparison. These three files composite the back end of our project. The InteractWithUser.java is used for providing the user an GUI to play with all the functionalities easily and intuitively. The GUI is the front end of our project.<br> 
The reason why I chose this structure of classes (put main() into the InteractWithUser class) is that I want to separate the main method from other methods for any future changes from the perspectives of object oriented design. Also, I want to separate the process of data extracting (from APIs) and data inserting(to database) from the functionality of just querying updated weather data from database. According to java's nature, object oriented programming, the code such a static method may execute is more concerned with setting up the program for execution, and delegating to our business logic to run the application. In this project, main()'s concern is with running our program. As such, it should be declared away from our business objects, in a module of the project concerned with application launch or execution.<br> 
From the GUI, the user can update weather database to the latest data by clicking the update button. The user could also choose city from the drop down menu. Through the drop down menu, the user would be able to know the city options in the database system. Through the drop down menu of weather type, the user could choose current, forecast or comparison weather to view the data respectively. After making selection, clicking GO button will lead the user to the result of his/her choice. The go back button on each JFrame will lead the user back to the main menu. The exit button will let the user quit the system.<br>
As for the database design, I create three tables in the weatherdb schema. The table addresses contains the city information. The table CurrentWeather contains the current weather information whereas the ForecastWeather contains the forecast weather information.<br> 
Before the user run the GUI, the administror should run the weather_db.sql firstly in MySQL workBench so that the three tables are created and the name of the five cities are preset. The user should also start up the MySQL workBench first and then play with the GUI by clicking update button at the beginning.<br>
