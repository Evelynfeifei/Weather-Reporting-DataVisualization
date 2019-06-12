package Assignment5Pro;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class CompWeather extends JFrame {

	public CompWeather(String applicationTitle, String chartTitle, float[] cw, float[] fw) {
		super(applicationTitle);
		Image icon = null;
		try {
			icon = ImageIO.read(new File("BackgroundAss5.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JFreeChart barChart = ChartFactory.createBarChart(chartTitle, "Category", "Value", createDataset(cw, fw),
				PlotOrientation.VERTICAL, true, true, false);

		barChart.setBackgroundImage(icon);
		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);

		// setDefaultCloseOperation(ApplicationFrame.DO_NOTHING_ON_CLOSE);

	}

	private CategoryDataset createDataset(float[] cw, float[] fw) {
		final String temp = "Temp";
		final String pressure = "Pressure";
		final String humidity = "Humidity";
		final String temp_min = "Temp_Min";
		final String temp_max = "Temp_Max";
		final String current = "Current";
		final String forecast = "Forecast";

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset.addValue(cw[0], current, temp);
		dataset.addValue(fw[0], forecast, temp);

		dataset.addValue(cw[1], current, pressure);
		dataset.addValue(fw[1], forecast, pressure);

		dataset.addValue(cw[2], current, humidity);
		dataset.addValue(fw[2], forecast, humidity);

		dataset.addValue(cw[3], current, temp_min);
		dataset.addValue(fw[3], forecast, temp_min);

		dataset.addValue(cw[4], current, temp_max);
		dataset.addValue(fw[4], forecast, temp_max);

		return dataset;
	}

}