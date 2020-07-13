package cst438hw3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cst438hw3.domain.City;
import cst438hw3.domain.CityInfo;
import cst438hw3.domain.CityRepository;
import cst438hw3.service.CityService;

@RestController
public class CityRestController {

  @Autowired
  private CityService cityService;

  @Autowired
  private CityRepository cityRepository;

  @GetMapping("/api/cities/{city}")
  public ResponseEntity<CityInfo> getWeather(@PathVariable("city") String cityName) {

    CityInfo cityInfo = cityService.getCityInfo(cityName);

    if (cityInfo == null) {

      return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);

    } else {

      // return 200 status code (OK) and city information in JSON format
      return new ResponseEntity<CityInfo>(cityInfo, HttpStatus.OK);
    }
  }

  @DeleteMapping("/api/cities/{city}")
  public ResponseEntity<CityInfo> deleteCity(@PathVariable("city") String name) {

    List<City> cities = cityRepository.findByName(name);

    if (cities.size() == 0) {
      // city name not found. Send 404 return code.
      return new ResponseEntity<CityInfo>(HttpStatus.NOT_FOUND);
    } else {

      for (City c : cities) {
        cityRepository.delete(c);
      }
      // return 204, request successful. no content returned.
      return new ResponseEntity<CityInfo>(HttpStatus.NO_CONTENT);

    }
  }

}
