package Assignment5Pro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.simple.parser.ParseException;

public class InteractWithUser extends JFrame {

	// variables used for the result of drop down menu selection
	static String cityOption = "";
	static String functionOption = "";

	// instantiation
	DatabaseProject db = new DatabaseProject();

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		// instantiation
		InteractWithUser interact_User = new InteractWithUser();

		// create the GUI for options
		JFrame frame = new JFrame("Weather Checking System");

		Font newFont = new Font("Courier New", Font.CENTER_BASELINE, 15);
		Font newFont2 = new Font("Courier New", Font.BOLD, 40);

		frame.setLayout(new BorderLayout());
		frame.setContentPane(new JLabel(new ImageIcon("BackgroundAss5.png")));
		frame.setLayout(new FlowLayout());

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(535, 300);
		frame.setLocation(430, 100);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.PINK);
		frame.add(panel);

		JLabel lbltitle = new JLabel("SmartWeather");
		lbltitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbltitle.setForeground(Color.WHITE);
		lbltitle.setFont(newFont2);
		panel.add(lbltitle);

		JLabel lblcity = new JLabel("Choose a city: ");
		lblcity.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblcity.setForeground(Color.GRAY);
		lblcity.setFont(newFont);
		panel.add(lblcity);

		// drop down menu for city
		String[] cityChoices = { " ", "NewHaven", "Chicago", "Seattle", "Phoenix", "Charlotte" };

		final JComboBox<String> cb = new JComboBox<String>(cityChoices);
		cb.setMaximumSize(cb.getPreferredSize());
		cb.setAlignmentX(Component.CENTER_ALIGNMENT);

		// add actionlistner to listen for change
		ActionListener cbActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// get the selected item
				cityOption = String.valueOf(cb.getSelectedItem());
				;
			}
		};

		cb.addActionListener(cbActionListener);
		panel.add(cb);

		// drop down menu for weather data type
		String[] curr_foreChoices = { " ", "Current Weather", "Forecast Weather",
				"Compare Current & Forecast Weather" };

		JLabel lblweather = new JLabel("  Choose weather type and click GO: ");
		lblweather.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblweather.setForeground(Color.GRAY);
		lblweather.setFont(newFont);
		panel.add(lblweather);

		final JComboBox<String> cb1 = new JComboBox<String>(curr_foreChoices);
		cb1.setMaximumSize(cb1.getPreferredSize());
		cb1.setAlignmentX(Component.CENTER_ALIGNMENT);

		// add actionlistner to listen for change
		ActionListener cb1ActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// get the selected item
				functionOption = String.valueOf(cb1.getSelectedItem());
				;
			}
		};

		cb1.addActionListener(cb1ActionListener);
		panel.add(cb1);

		JButton gobtn = new JButton("GO");
		gobtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		GoBtnListener objGoBtnListener = interact_User.new GoBtnListener();
		gobtn.addActionListener(objGoBtnListener);
		panel.add(gobtn);

		JButton updatebtn = new JButton("Update Weather Database");
		updatebtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		UpdateBtnListener objUpdateBtnListener = interact_User.new UpdateBtnListener();
		updatebtn.addActionListener(objUpdateBtnListener);
		panel.add(updatebtn);

		JButton exitbtn = new JButton("Exit");
		exitbtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		ExitBtnListener objExitBtnListener = interact_User.new ExitBtnListener();
		exitbtn.addActionListener(objExitBtnListener);
		panel.add(exitbtn);

		frame.setVisible(true);
	}

	// once the button is clicked the method will execute
	private class GoBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if (functionOption == "Current Weather") {

				try {
					db.showCurrWeather(cityOption);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			if (functionOption == "Forecast Weather") {

				try {
					db.showForecastWeather(cityOption);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			if (functionOption == "Compare Current & Forecast Weather") {

				try {
					db.compareWeather(cityOption);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}
	}

	// once the button is clicked the method will execute
	private class UpdateBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			try {
				new UpdateWeatherDatabase();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	// once the button is clicked the method will execute
	private class ExitBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			System.exit(0);

		}
	}
}
