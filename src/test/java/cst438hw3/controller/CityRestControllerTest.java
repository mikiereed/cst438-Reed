package cst438hw3.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import cst438hw3.domain.City;
import cst438hw3.domain.CityInfo;
import cst438hw3.domain.CityRepository;
import cst438hw3.domain.Country;
import cst438hw3.service.CityService;

@RunWith(SpringRunner.class)
@WebMvcTest(CityRestController.class)
public class CityRestControllerTest {

  @MockBean
  private CityService cityService;

  @MockBean
  private CityRepository cityRepository;

  @Autowired
  private MockMvc mvc;

  // This object will be magically initialized by the initFields method below.
  private JacksonTester<CityInfo> json;

  @BeforeEach
  public void setUpEach() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  public void contextLoads() {
  }

  @Test
  public void getCityInfo() throws Exception {

    CityInfo testData = new CityInfo(1, "testCity", "TST", "Test Country", "Test District", 1000,
        75.0, "11:03 AM");

    // cityService stub
    given(cityService.getCityInfo("testCity")).willReturn(testData);

    MockHttpServletResponse response = mvc.perform(get("/api/cities/" + testData.getName()))
        .andReturn().getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

    String resultJSON = response.getContentAsString();
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonTestData = objectMapper.writeValueAsString(testData);

    assertThat(resultJSON).isEqualTo(jsonTestData);

    /**
     * ERRORED OUT ON TESTS (use of JacksonTester<CityInfo> json caused the error)
     * Alternate approach using JacksonTester JSON object CityInfo testData = new
     * CityInfo(1, "testCity", "TST", "Test Country", "Test District", 1000, 300.0,
     * "101"); CityInfo expData = new CityInfo( 1, "testCity", "TST", "Test
     * Country", "Test District", 1000, 80.0, "4:00:00 PM");
     * 
     * // cityService stub
     * given(cityService.getCityInfo("testCity")).willReturn(expData);
     * 
     * // execute query against mock inp_data MockHttpServletResponse response =
     * mvc.perform(
     * get("/api/cities/testCity").contentType(MediaType.APPLICATION_JSON)
     * .content(json.write(testData).getJson()) ).andReturn().getResponse();
     * assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
     * 
     * String cityResult = response.getContentAsString(); String cityExpected =
     * json.write(expData).getJson();
     * 
     * assertThat(cityResult).isEqualTo(cityExpected);
     **/
  }

  @Test
  public void getNullCityInfo() throws Exception {

    String testName = "testCity";
    CityInfo testData = null;

    // cityService stub
    given(cityService.getCityInfo(testName)).willReturn(testData);

    MockHttpServletResponse response = mvc.perform(get("/api/cities/" + testName)).andReturn()
        .getResponse();

    assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}
