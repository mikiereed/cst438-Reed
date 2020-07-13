package cst438hw3.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import cst438hw3.domain.*;

@SpringBootTest
public class CityServiceTest {

  @MockBean
  private WeatherService weatherService;

  @Autowired
  private CityService cityService;

  @MockBean
  private CityRepository cityRepository;

  @MockBean
  private CountryRepository countryRepository;

  @Test
  public void contextLoads() {
  }

  @Test
  public void testCityFound() throws Exception {
    City testCity = new City(1, "Test City", "TST", "Test District", 1000);
    List<City> testCities = new ArrayList<City>();
    testCities.add(testCity);

    given(cityRepository.findByName(testCity.getName())).willReturn(testCities);

    given(countryRepository.findByCode(testCity.getCountryCode()))
        .willReturn(new Country("TST", "Test Country"));

    int timeZoneTest = 14400;
    TempAndTime testTempAndTime = new TempAndTime(300, 1000, timeZoneTest);
    given(weatherService.getTempAndTime(testCities.get(0).getName())).willReturn(testTempAndTime);

    CityInfo testData = cityService.getCityInfo(testCity.getName());

    Date currentTime = new Date(System.currentTimeMillis() + (timeZoneTest * 1000));
    DateFormat hourMinuteFormat = new SimpleDateFormat("h:mm a");
    hourMinuteFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    CityInfo expectedData = new CityInfo(testCity, "Test Country", 80.33,
        hourMinuteFormat.format(currentTime));

    assertEquals(testData, expectedData);
  }

  @Test
  public void testCityNotFound() {
    given(cityRepository.findByName("testCity")).willReturn(null);

    CityInfo testData = cityService.getCityInfo("testCity");
    CityInfo expectedData = null;

    assertEquals(testData, expectedData);
  }

  @Test
  public void testCityMultiple() {
    List<City> testCities = new ArrayList<City>();
    City testCity = new City(1, "Test City", "TST", "Test District", 1000);
    testCities.add(testCity);
    City testCity2 = new City(2, "Test City", "FAK", "Fake District", 2000);
    testCities.add(testCity2);

    given(cityRepository.findByName(testCity.getName())).willReturn(testCities);

    given(countryRepository.findByCode(testCity.getCountryCode()))
        .willReturn(new Country("TST", "Test Country"));

    int timeZoneTest = 14400;
    TempAndTime testTempAndTime = new TempAndTime(300, 1000, timeZoneTest);
    given(weatherService.getTempAndTime(testCities.get(0).getName())).willReturn(testTempAndTime);

    CityInfo testData = cityService.getCityInfo(testCity.getName());

    Date currentTime = new Date(System.currentTimeMillis() + (timeZoneTest * 1000));
    DateFormat hourMinuteFormat = new SimpleDateFormat("h:mm a");
    hourMinuteFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    CityInfo expectedData = new CityInfo(testCity, "Test Country", 80.33,
        hourMinuteFormat.format(currentTime));

    assertEquals(testData, expectedData);
  }

}
