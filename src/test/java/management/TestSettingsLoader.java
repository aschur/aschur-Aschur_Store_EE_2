package management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestSettingsLoader {

	@Test
	public void testGetPrimarySettings() {

		List<String> list = new ArrayList<String>();
		
		list.add("modeDataStorage");
		list.add("usageMode");
		list.add("fileNameProcurementPlan");
		list.add("fileNameProducts");
		
		Map<String, String> map = SettingsLoader.getPrimarySettings();
		
		assertTrue(map.size() == list.size());
		
		for (String key : list) {
			
			String value = map.get(key);
			assertNotNull(value);
			assertNotEquals(value, "");
			
		}
		
		
		
	}

}
