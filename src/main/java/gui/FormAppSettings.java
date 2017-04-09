package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import management.AppSetting;
import management.Manager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

@SuppressWarnings("serial")
public class FormAppSettings extends JFrame {

	private FormAppSettingsPanel formAppSettingsPanel;

	private AppSetting countConsumersAppSetting;
	private AppSetting maxCountEnterStoreAppSetting;
	private AppSetting timeWorkStoreAppSetting;

	private JButton SaveAndCloseButton = new JButton("Save and close");
	private ActionListener SaveAndCloseAL = new ActionListener() {
		public void actionPerformed(ActionEvent e) {

			boolean failure = false;

			Long countConsumers = (Long) formAppSettingsPanel
					.getCountConsumersFieldValue();
			if (countConsumers <= 0 || countConsumers > 20) {
				System.out.println("count consumers must be 1 - 20");
				failure = true;
			}

			Long maxCountEnterStore = (Long) formAppSettingsPanel
					.getMaxCountEnterStoreFieldValue();
			if (maxCountEnterStore <= 0 || maxCountEnterStore > 4) {
				System.out.println("max count enter store must be 1 - 4");
				failure = true;
			}

			Long timeWorkStore = (Long) formAppSettingsPanel
					.getTimeWorkStoreFieldValue();
			if (timeWorkStore < 1 || timeWorkStore > 60) {
				System.out.println("count consumers must be 1 min - 60 min");
				failure = true;
			}

			if (failure) {
				return;
			}

			if (countConsumersAppSetting == null) {
				countConsumersAppSetting = new AppSetting("countConsumers",
						countConsumers);
			}

			countConsumersAppSetting.setValue(countConsumers);

			if (maxCountEnterStoreAppSetting == null) {
				maxCountEnterStoreAppSetting = new AppSetting(
						"maxCountEnterStore", maxCountEnterStore);
			}

			maxCountEnterStoreAppSetting.setValue(maxCountEnterStore);

			if (timeWorkStoreAppSetting == null) {
				timeWorkStoreAppSetting = new AppSetting("timeWorkStore",
						timeWorkStore);
			}

			timeWorkStoreAppSetting.setValue(timeWorkStore);

			Session session = Manager.getSession();
			Transaction tx = session.beginTransaction();

			try {

				session.saveOrUpdate(countConsumersAppSetting);
				session.saveOrUpdate(maxCountEnterStoreAppSetting);
				session.saveOrUpdate(timeWorkStoreAppSetting);

				tx.commit();

			} catch (Exception e2) {

				System.out.println("unable to save values");
				e2.printStackTrace();

				return;

			} finally {

				try {
					session.close();
				} catch (Exception e2) {
					System.out.println("failed to close the session");
					e2.printStackTrace();
				}

			}

			dispose();

		}

	};

	public FormAppSettings() {

		super("please enter application parametrs");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new GridLayout());

		Session session = Manager.getSession();

		try {

			Transaction tx = session.beginTransaction();

			countConsumersAppSetting = (AppSetting) session.get(
					AppSetting.class, "countConsumers");
			maxCountEnterStoreAppSetting = (AppSetting) session.get(
					AppSetting.class, "maxCountEnterStore");
			timeWorkStoreAppSetting = (AppSetting) session.get(
					AppSetting.class, "timeWorkStore");

			tx.commit();

		} catch (Exception e) {

			System.out.println("failed to retrieve settings from the database");
			e.printStackTrace();

			return;

		} finally {
			try {
				session.close();
			} catch (Exception e2) {
				System.out.println("failed to close the session");
				e2.printStackTrace();
			}
		}

		// Add contents to the window.
		formAppSettingsPanel = new FormAppSettingsPanel();
		add(formAppSettingsPanel);

		SaveAndCloseButton.addActionListener(SaveAndCloseAL);
		add(SaveAndCloseButton);

		// Display the window.
		pack();
		setVisible(true);

	}

	private class FormAppSettingsPanel extends JPanel {

		// Labels to identify the fields
		private JLabel countConsumersLabel;
		private JLabel maxCountEnterStoreLabel;
		private JLabel timeWorkStoreLabel;

		// Strings for the labels
		private String countConsumersString = "Count consumers: ";
		private String maxCountEnterStoreString = "Maximum count enter store: ";
		private String timeWorkStoreString = "Time work store: ";

		// Fields for data entry
		private JFormattedTextField countConsumersField;
		private JFormattedTextField maxCountEnterStoreField;
		private JFormattedTextField timeWorkStoreField;

		// Formats to format and parse numbers
		private NumberFormat countDisplayFormat;
		private NumberFormat countEditFormat;

		public FormAppSettingsPanel() {
			super(new BorderLayout());
			setUpFormats();

			// Create the labels.
			countConsumersLabel = new JLabel(countConsumersString);
			maxCountEnterStoreLabel = new JLabel(maxCountEnterStoreString);
			timeWorkStoreLabel = new JLabel(timeWorkStoreString);

			// Create the text fields and set them up.

			countConsumersField = createTextField(countConsumersAppSetting);
			maxCountEnterStoreField = createTextField(maxCountEnterStoreAppSetting);
			timeWorkStoreField = createTextField(timeWorkStoreAppSetting);

			// Tell accessibility tools about label/textfield pairs.
			countConsumersLabel.setLabelFor(countConsumersField);
			maxCountEnterStoreLabel.setLabelFor(maxCountEnterStoreField);
			timeWorkStoreLabel.setLabelFor(timeWorkStoreField);

			// Lay out the labels in a panel.
			JPanel labelPane = new JPanel(new GridLayout(0, 1));
			labelPane.add(countConsumersLabel);
			labelPane.add(maxCountEnterStoreLabel);
			labelPane.add(timeWorkStoreLabel);

			// Layout the text fields in a panel.
			JPanel fieldPane = new JPanel(new GridLayout(0, 1));
			fieldPane.add(countConsumersField);
			fieldPane.add(maxCountEnterStoreField);
			fieldPane.add(timeWorkStoreField);

			// Put the panels in this panel, labels on left,
			// text fields on right.
			setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			add(labelPane, BorderLayout.CENTER);
			add(fieldPane, BorderLayout.LINE_END);
		}

		// Create and set up number formats. These objects also
		// parse numbers input by user.
		private void setUpFormats() {

			countDisplayFormat = NumberFormat.getInstance();
			countDisplayFormat.setMinimumFractionDigits(0);
			countEditFormat = NumberFormat.getInstance();

		}

		private JFormattedTextField createTextField(AppSetting appSetting) {

			JFormattedTextField field = new JFormattedTextField(
					new DefaultFormatterFactory(new NumberFormatter(
							countDisplayFormat), new NumberFormatter(
							countDisplayFormat), new NumberFormatter(
							countEditFormat)));

			Long value = new Long(0);
			if (appSetting != null) {
				value = appSetting.getValue();
			}

			field.setValue(value);
			field.setColumns(10);

			return field;

		}

		public Object getCountConsumersFieldValue() {
			return countConsumersField.getValue();
		}

		public Object getMaxCountEnterStoreFieldValue() {
			return maxCountEnterStoreField.getValue();
		}

		public Object getTimeWorkStoreFieldValue() {
			return timeWorkStoreField.getValue();
		}
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				new FormAppSettings();

			}

		});

	}

}
