package sit707_week7;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {
	
	// Mock TemperatureSensor
	TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
	// Mock NotificationSender
	NotificationSender notificationSender = Mockito.mock(NotificationSender.class);
	// Mock CloudService
	CloudService cloudService = Mockito.mock(CloudService.class);

	@Test
	public void testStudentIdentity() {
		String studentId = "s222435189";
		Assert.assertNotNull("Student ID is null", studentId);
	}

	@Test
	public void testStudentName() {
		String studentName = "Samadhi Jayasinghe";
		Assert.assertNotNull("Student name is null", studentName);
	}
	
	@Test
	public void testReadTemperatureNegative() {
		// Set up a dummy negative temperature reading
        double temperatureValue = -1.0;
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);
        
        // Create BodyTemperatureMonitor instance with mocked TemperatureSensor
		BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor,null,null);
		
		// Test readTemperature() function for negative temperature
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}
	
	@Test
	public void testReadTemperatureZero() {
		// Set up a dummy temperature reading
        double temperatureValue = 0.0;
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);
        
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
		
        // Test readTemperature() function for zero temperature
		double actualTemperature = temperatureMonitor.readTemperature();
		Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}
	
	@Test
	public void testReadTemperatureNormal() {
		// Set up a dummy temperature reading
        double temperatureValue = 37.0;
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);

        // Create BodyTemperatureMonitor instance with mocked TemperatureSensor
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Test readTemperature() function for normal temperature
        double actualTemperature = temperatureMonitor.readTemperature();
        Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}

	@Test
	public void testReadTemperatureAbnormallyHigh() {
		// Set up a dummy temperature reading
        double temperatureValue = 40.0;
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(temperatureValue);

        // Create BodyTemperatureMonitor instance with mocked TemperatureSensor
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);

        // Test readTemperature() function for abnormally high temperature
        double actualTemperature = temperatureMonitor.readTemperature();
        Assert.assertEquals(temperatureValue, actualTemperature, 0.01);
	}

    @Test
    public void testReportTemperatureReadingToCloud() {
        // Create BodyTemperatureMonitor instance with mocked CloudService
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, null);

        // Test reportTemperatureReadingToCloud() function
        TemperatureReading temperatureReading = new TemperatureReading();
        temperatureMonitor.reportTemperatureReadingToCloud(temperatureReading);

        // Verify that sendTemperatureToCloud() is called with the correct temperature reading
        Mockito.verify(cloudService, Mockito.times(1)).sendTemperatureToCloud(temperatureReading);
    }

	
    @Test
    public void testInquireBodyStatusNormalNotification() {
        // Stub queryCustomerBodyStatus() to return "NORMAL"
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any())).thenReturn("NORMAL");

        // Create BodyTemperatureMonitor instance with mocked CloudService and NotificationSender
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);

        // Test inquireBodyStatus() function
        temperatureMonitor.inquireBodyStatus();

        // Verify that sendEmailNotification() is called with the correct parameters
        Mockito.verify(notificationSender, Mockito.times(1)).sendEmailNotification(temperatureMonitor.getFixedCustomer(), "Thumbs Up!");
    }
	
    @Test
    public void testInquireBodyStatusAbnormalNotification() {

        // Stub queryCustomerBodyStatus() to return "ABNORMAL"
    	Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any())).thenReturn("ABNORMAL");

        // Create BodyTemperatureMonitor instance with mocked CloudService and NotificationSender
        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);

        // Test inquireBodyStatus() function
        temperatureMonitor.inquireBodyStatus();

        // Verify that sendEmailNotification() is called with the correct parameters
        Mockito.verify(notificationSender, Mockito.times(1)).sendEmailNotification(temperatureMonitor.getFamilyDoctor(),"Emergency!");
    }

}
