package prac;

import javax.swing.*;
import javax.swing.text.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadixConv {
	
	private JFrame frame;
	private JPanel mainPanel;
	
	// To Decim
	private JTextField numberfield;
	private JComboBox<String> baseList;
	private JTextField resultfield;
	private Pattern regex = Pattern.compile("[0-1]");
	
	// From Decim
	private JTextField numberfield2;
	private JTextField basefield2;
	private JTextField resultfield2;

	public static void main(String[] args) {
		RadixConv converter = new RadixConv();
		converter.setUpGui();

	}
	
	public int val (char c) {
		if (c >= '0' && c <= '9') {
			return (int) c - '0';
		} 
		else {
			return (int) c - 'A' + 10;
		}
	}
	
	public int convertToDecim(String s, int base) {
		int power = 1;
		int result = 0;
		
		for (int i = s.length() - 1; i >= 0; i--) {
			result += val(s.charAt(i)) * power;
			power = power * base;
		}
		return result;
	}
	
	public String convertFromDecim(String s, int base) {
		String str = "";
		int number = Integer.parseInt(s);
		
		while (number > 0) {
			int remainder = number % base;
			char charNum;
			if (remainder >= 0 && remainder <= 9) {
				charNum = (char) (remainder + 48);
			}
			else {
				charNum = (char) (remainder - 10 + 65);
			}
			str += charNum;
			number /= base;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(str);
		return new String(sb.reverse());
	}
	
	public void setUpGui() {
		frame = new JFrame("Radix Converter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		Font font = new Font("Times New Roman", Font.PLAIN, 16);
		
		Box contentBox = new Box(BoxLayout.Y_AXIS);
		Box toDecimalBox = new Box(BoxLayout.X_AXIS);
		Box fromDecimalBox = new Box(BoxLayout.X_AXIS);
		
		// To Decim
		numberfield = new JTextField(10);
		numberfield.setFont(font);
		((AbstractDocument)numberfield.getDocument()).setDocumentFilter(new ToDecimFilter());
		
		String[] bases = new String[] {"2","8","16"};
		baseList = new JComboBox<>(bases);
		baseList.setFont(font);
		baseList.setPreferredSize(new Dimension(113,0));
		baseList.addActionListener(new BaseListListener());
		
		resultfield = new JTextField("Result",10);
		resultfield.setFont(font);
		resultfield.setEditable(false);
		
		JButton convertButton = new JButton("Convert");
		convertButton.addActionListener(new ConvertButtonListener());
		
		// From Decim
		numberfield2 = new JTextField(10);
		numberfield2.setFont(font);
		((AbstractDocument)numberfield2.getDocument()).setDocumentFilter(new FromDecimFilter());
		
		basefield2 = new JTextField(10);
		basefield2.setFont(font);
		
		resultfield2 = new JTextField("Result", 10);
		resultfield2.setFont(font);
		resultfield2.setEditable(false);
		
		JButton convertButton2 = new JButton("Convert");
		convertButton2.addActionListener(new ConvertButton2Listener());
		
		toDecimalBox.add(new JLabel("Number:"));
		toDecimalBox.add(Box.createHorizontalStrut(3));
		toDecimalBox.add(numberfield);
		toDecimalBox.add(Box.createHorizontalStrut(6));
		toDecimalBox.add(new JLabel("Base:"));
		toDecimalBox.add(Box.createHorizontalStrut(3));
		toDecimalBox.add(baseList);
		toDecimalBox.add(Box.createHorizontalStrut(6));
		toDecimalBox.add(convertButton);
		toDecimalBox.add(Box.createHorizontalStrut(6));
		toDecimalBox.add(resultfield);
		
		fromDecimalBox.add(new JLabel("Number:"));
		fromDecimalBox.add(Box.createHorizontalStrut(3));
		fromDecimalBox.add(numberfield2);
		fromDecimalBox.add(Box.createHorizontalStrut(6));
		fromDecimalBox.add(new JLabel("Base:"));
		fromDecimalBox.add(Box.createHorizontalStrut(3));
		fromDecimalBox.add(basefield2);
		fromDecimalBox.add(Box.createHorizontalStrut(6));
		fromDecimalBox.add(convertButton2);
		fromDecimalBox.add(Box.createHorizontalStrut(6));
		fromDecimalBox.add(resultfield2);
		
		contentBox.add(new JLabel("Convert from chosen base to decimal"));
		contentBox.add(Box.createRigidArea(new Dimension(0, 10)));
		contentBox.add(toDecimalBox);
		contentBox.add(Box.createRigidArea(new Dimension(0, 30)));
		contentBox.add(new JLabel("Convert from decimal to chosen base"));
		contentBox.add(Box.createRigidArea(new Dimension(0, 10)));
		contentBox.add(fromDecimalBox);
		mainPanel.add(contentBox);
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(600, 400);
		frame.setVisible(true);
	}
	
	// To Decim
	class ConvertButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String number = numberfield.getText();
			int base = Integer.parseInt((String) baseList.getSelectedItem());
			int result = convertToDecim(number, base);
			resultfield.setText(Integer.toString(result));
		}
	}
	// From Decim
	class ConvertButton2Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String number = numberfield2.getText();
			int base = Integer.parseInt((String) basefield2.getText());
			String result = convertFromDecim(number, base);
			resultfield2.setText(result);
		}
	}
	
	class BaseListListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String base = (String) baseList.getSelectedItem();
			
			switch (base) {
			case "2": 
				regex = Pattern.compile("[0-1]");
				break;
			case "8":
				regex = Pattern.compile("[0-7]");
				break;
			case "16":
				regex = Pattern.compile("[0-9A-F]");
				break;
			}
		}
	}
	
	class ToDecimFilter extends DocumentFilter {
		
		public void insertString (DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) 
		throws BadLocationException {
			Matcher matcher = regex.matcher(str);
			if (!matcher.matches()) {
				return;
			}
			super.insertString(fb, offset, str, attr);
		}
		
		public void replace(DocumentFilter.FilterBypass fb,
	            int offset, int length, String string, AttributeSet attr) throws BadLocationException {
	        if (length > 0) {
	        fb.remove(offset, length);
	        }
	        insertString(fb, offset, string, attr);
	    }

	}
	
	class FromDecimFilter extends DocumentFilter {
		
		Pattern regex2 = Pattern.compile("[0-9]");
		
		public void insertString (DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attr) 
		throws BadLocationException {
			Matcher matcher = regex2.matcher(str);
			if (!matcher.matches()) {
				return;
			}
			super.insertString(fb, offset, str, attr);
		}
		
		public void replace(DocumentFilter.FilterBypass fb,
	            int offset, int length, String string, AttributeSet attr) throws BadLocationException {
	        if (length > 0) {
	        fb.remove(offset, length);
	        }
	        insertString(fb, offset, string, attr);
	    }

	}

}
