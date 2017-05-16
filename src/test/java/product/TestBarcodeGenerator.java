package product;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestBarcodeGenerator {

	private static int barcodeInt; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		String barcode = BarcodeGenerator.generate();
		barcodeInt = Integer.parseInt(barcode);
		
	}

	@Test
	public void testGenerate() {
	
		assertTrue(barcodeInt < 1000000);
		
	}

}
