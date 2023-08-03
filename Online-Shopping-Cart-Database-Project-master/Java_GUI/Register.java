package comp421;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends JFrame {
	JTextField name = new JTextField();
	JTextField phonenum = new JTextField();
	JTextField cardnum = new JTextField();
	JTextField expirydate = new JTextField();
	JTextField bank = new JTextField();
	JTextField organization = new JTextField();
	JButton submit = new JButton("Submit");
	MainFrame mainFrame = null;
	Register frame = this;

	SQL sql;
	ResultSet rs;
	int userid;
	String sqlCode;

	public Register(SQL sql, MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.sql = sql;
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));
		panel.add(new JLabel("Name:"));
		panel.add(name);
		panel.add(new JLabel("Phone Number:"));
		panel.add(phonenum);
		panel.add(new JLabel("Card Number:"));
		panel.add(cardnum);
		panel.add(new JLabel("Expiry Date:"));
		panel.add(expirydate);
		panel.add(new JLabel("Bank:"));
		panel.add(bank);
		panel.add(new JLabel("Card Issue Organization:"));
		panel.add(organization);
		this.add(panel, BorderLayout.CENTER);
		submit.addActionListener(new RegisterListener());
		submit.setPreferredSize(new Dimension(20, 40));
		this.add(submit, BorderLayout.SOUTH);
	}

	public static void invoke(SQL sql, MainFrame mainFrame) {
		Register register = new Register(sql, mainFrame);
		register.setVisible(true);
		register.setSize(400, 200);
		register.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		register.setLocationRelativeTo(null);
		register.setTitle("Register a new User");
	}

	class RegisterListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			sqlCode = "SELECT MAX(userid) FROM users;"; // Get the max userid from the user table
			rs = sql.QueryExchte(sqlCode);
			try {
				rs.next();
				userid = rs.getInt(1) + 1;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

			String nameValue = name.getText();
			String phonenumValue = phonenum.getText();
			String cardnumValue = cardnum.getText();
			String expirydateValue = expirydate.getText();
			String bankValue = bank.getText();
			String organizationValue = organization.getText();
			if (nameValue.trim().isEmpty() || phonenumValue.trim().isEmpty() || cardnumValue.trim().isEmpty()
					|| expirydateValue.trim().isEmpty() || bankValue.trim().isEmpty() || organizationValue.trim().isEmpty()) {
				JOptionPane.showMessageDialog(null, "It is required to fill in every blank", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (!isValidCard(cardnumValue)) {
				JOptionPane.showMessageDialog(null, "The card number is not correct", "Error", JOptionPane.ERROR_MESSAGE);
			} else if (!isValidDate(expirydateValue)) {
				JOptionPane.showMessageDialog(null, "The format of expiry date is not correct", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				sqlCode = "INSERT INTO users VALUES (" + userid + ", '" + nameValue + "', '" + phonenumValue + "');";
				sql.WriteExcute(sqlCode);
				sqlCode = "INSERT INTO bankcard VALUES ('" + cardnumValue + "', '" + expirydateValue + "', '" + bankValue + "');";
				sql.WriteExcute(sqlCode);
				sqlCode = "INSERT INTO creditcard VALUES ('" + cardnumValue + "'," + userid + ", '" + organizationValue + "');";
				sql.WriteExcute(sqlCode);
				sqlCode = "INSERT INTO buyer VALUES (" + userid + ");";
				sql.WriteExcute(sqlCode);

				JOptionPane.showMessageDialog(null, "You have successfully registered. Your unique userid is " + userid +
						". Please keep it for login next time.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);

				try {
					mainFrame.setUserid(userid);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				mainFrame.setVisible(true);
				mainFrame.setAddAddressButtonEnable(true);
				frame.dispose();
			}
		}
	}

	public boolean isValidCard(String cardnum) {
		String cardPattern = "\\d{4} \\d{4} \\d{4} \\d{4}";
		Pattern pattern = Pattern.compile(cardPattern);
		Matcher matcher = pattern.matcher(cardnum);
		return matcher.matches();
	}

	public boolean isValidDate(String date) {
		String datePattern = "\\d{4}-\\d{1,2}-\\d{1,2}";
		Pattern pattern = Pattern.compile(datePattern);
		Matcher matcher = pattern.matcher(date);
		if (matcher.matches()) {
			if (date.length() == 8) {
				if (date.charAt(5) == '0' || date.charAt(7) == '0')
					return false;
			}
			if (date.length() == 10) {
				if (date.charAt(5) == '0' && date.charAt(6) == '0')
					return false;
				if (date.charAt(8) == '0' && date.charAt(9) == '0')
					return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
