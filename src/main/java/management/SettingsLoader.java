package management;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;


/**
 * Created by Aschur on 15.01.2017
 */



/**
 * @author Yulay
 *
 */
public class SettingsLoader {


	/**
	 * Settings - Map<String, String>:
	 * modeDataStorage - "fileApp", "dataBase"
	 * usageMode - "handMode", "automaticMode"
	 * fileNameProcurementPlan
	 * fileNameProducts
	*/
	public static Map<String, String> getPrimarySettings(){

		Map<String, String> settings = new HashMap<String, String>();
		settings.put("modeDataStorage", "");
		settings.put("usageMode", "");
		settings.put("fileNameProcurementPlan", "");
		settings.put("fileNameProducts", "");

		
		String modeDataStorage = "";
		String usageMode = "";
		String fileNameProcurementPlan = "";
		String fileNameProducts = "";
		
		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();

		try {

			ClassLoader myCL = SettingsLoader.class.getClassLoader();
			inputStream = myCL.getResourceAsStream("PrimarySettings.txt");

			try {

				int c;
				while ((c = inputStream.read()) != -1) {

					sb.append(Character.toString((char) c));

				}

			} finally {

				inputStream.close();
			}

		} catch (Exception x) {
			
			x.printStackTrace();
			
			return settings;
			
		}

		String allString = sb.toString().trim();

		String[] primarySettings = allString.split("\r\n");

	
		
		if (primarySettings.length >= 1) {
			
			modeDataStorage = primarySettings[0];
			settings.put("modeDataStorage", modeDataStorage);
			
		}
		if (primarySettings.length >= 2) {
			
			usageMode = primarySettings[1];
			settings.put("usageMode", usageMode);
			
		}
		if (primarySettings.length >= 3) {
			
			fileNameProcurementPlan = primarySettings[2];
			settings.put("fileNameProcurementPlan", fileNameProcurementPlan);
			
		}
		if (primarySettings.length >= 4) {
			
			fileNameProducts = primarySettings[3];
			settings.put("fileNameProducts", fileNameProducts);
			
		}
		
		
		return settings;

	}

	/**
	 * Settings - Map<String, String>:
	 * countConsumers
	 * maxCountEnterStore
	 * timeWorkStore
	*/
	public static Map<String, String> getSettings() {

		Map<String, String> settings = new HashMap<String, String>();
		settings.put("countConsumers", "");
		settings.put("maxCountEnterStore", "");
		settings.put("timeWorkStore", "");
		
		String modeDataStorage = Manager.getModeDataStorage();
		if (modeDataStorage.equals("fileApp")) {

			settings = getFileAppSettings(settings);
			
		} else {

			settings = getDataBaseSettings(settings);
			
		}
		
	
		return settings;

	}

	private static Map<String, String> getFileAppSettings(Map<String, String> settings) {

		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();

		try {

			ClassLoader myCL = SettingsLoader.class.getClassLoader();
			inputStream = myCL.getResourceAsStream("FileAppSettings.txt");

			try {

				int c;
				while ((c = inputStream.read()) != -1) {

					sb.append(Character.toString((char) c));

				}

			} finally {

				inputStream.close();
			}

		} catch (Exception x) {
			
			x.printStackTrace();
			
			return settings;
			
		}

		String allString = sb.toString().trim();

		String[] fileAppSettings = allString.split("\n");

		if (fileAppSettings.length == 3) {
			
			settings.put("countConsumers", fileAppSettings[0]);
			settings.put("maxCountEnterStore", fileAppSettings[1]);
			
			String timeWorkStoreString = fileAppSettings[2];
			Integer timeWorkStoreInteger = null;
			
			try {
				timeWorkStoreInteger = new Integer(timeWorkStoreString) * 1000 * 60;
			} catch (Exception e) {
				
			}
			
			if (timeWorkStoreInteger != null) {
				settings.put("timeWorkStore", timeWorkStoreInteger.toString());
			}
			
		}
		
		return settings;

	}

	@SuppressWarnings("unchecked")
	private static  Map<String, String> getDataBaseSettings(Map<String, String> settings) {
		
		Session session = Manager.getSession();
		String query = "select p from " + AppSetting.class.getSimpleName() + " p";
	
		
		AppSetting countConsumersAppSetting = null;
		AppSetting maxCountEnterStoreAppSetting = null;
		AppSetting timeWorkStoreAppSetting = null;
		
		try {
			countConsumersAppSetting = (AppSetting) session.get(
					AppSetting.class, "countConsumers");
			maxCountEnterStoreAppSetting = (AppSetting) session.get(
					AppSetting.class, "maxCountEnterStore");
			timeWorkStoreAppSetting = (AppSetting) session.get(
					AppSetting.class, "timeWorkStore");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				session.close();
			} catch (Exception e2) {
				
				System.out.println("failed to close the session");
				e2.printStackTrace();
				
				return settings;
				
			}
			
		}

		if (countConsumersAppSetting != null) {
			settings.put("countConsumers", Long.toString(countConsumersAppSetting.getValue()));
		}
		if (maxCountEnterStoreAppSetting != null) {
			settings.put("maxCountEnterStore", Long.toString(maxCountEnterStoreAppSetting.getValue()));
		}
		if (timeWorkStoreAppSetting != null) {
			settings.put("timeWorkStore", Long.toString(timeWorkStoreAppSetting.getValue() * 1000 * 60));
		}
		
        
		return settings;
		
	}
	
}	