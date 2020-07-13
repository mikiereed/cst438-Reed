package cst438hw3.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cst438hw3.domain.City;
import cst438hw3.domain.CityInfo;
import cst438hw3.domain.CityRepository;
import cst438hw3.domain.Country;
import cst438hw3.domain.CountryRepository;
import cst438hw3.domain.TempAndTime;

@Service
public class CityService {

  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private WeatherService weatherService;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private FanoutExchange fanout;

  public void requestReservation(String cityName, String level, String email) {
    String msg = "{\"cityName\": \"" + cityName + "\" \"level\": \"" + level + "\" \"email\": \""
        + email + "\"}";
    System.out.println("Sending message:" + msg);
    rabbitTemplate.convertSendAndReceive(fanout.getName(), "", // routing key none.
        msg);
  }

  public CityInfo getCityInfo(String cityName) {

    List<City> cities = cityRepository.findByName(cityName);

    if (cities == null || cities.isEmpty()) {
      return null;
    }

    City city = cities.get(0);
    Country country = countryRepository.findByCode(city.getCountryCode());
    TempAndTime tempAndTime = weatherService.getTempAndTime(city.getName());

    Date currentTime = new Date(System.currentTimeMillis() + (tempAndTime.timezone * 1000));
    DateFormat hourMinuteFormat = new SimpleDateFormat("h:mm a");
    hourMinuteFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // timezone based off GMT

    double tempFahrenheit = (((tempAndTime.temp - 273.15) * 9.0) / 5.0) + 32.0; // temp is in Kelvin
    tempFahrenheit = Math.round(tempFahrenheit * 100.0) / 100.0; // round to 2 decimal places

    CityInfo cityInfo = new CityInfo(city, country.getName(), tempFahrenheit,
        hourMinuteFormat.format(currentTime));

    return cityInfo;
  }

}
