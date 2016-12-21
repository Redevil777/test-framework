package org.openweathermap.tests.forecast_every_three_hours

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import org.springframework.http.HttpMethod
import spock.lang.Unroll
import static org.junit.Assert.assertNotNull

class TC1_fiveDayThreeHoursForecastJsonPositiveTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : get weather by city name for the next 5 days every 3 hours : #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "forecast")

        when: "send GET request to open weather and parse response"
        response = new JsonSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: cod,
                REQUEST_METHOD: HttpMethod.GET,
                REQUEST_PARAMS_STRING: "q={q}&mode={mode}&appid={appid}",
                REQUEST_PARAMS_VARIABLES: [
                        q    : q,
                        mode : mode,
                        appid: appid
                ]
        ))


        then: "check that response contain expected data"
        assert response.city.name == expectedCity
        assert response.city.country == country
        assert response.cod == cod.toString()
        assert response.list.size() == response.cnt

        response.list.each {
            assertNotNull(it.clouds)
            assertNotNull(it.dt)
            assertNotNull(it.dt_txt)
            assertNotNull(it.main.humidity)
            assertNotNull(it.main.pressure)
            assertNotNull(it.main.sea_level)
            assertNotNull(it.main.temp)
            assert it.main.temp_max >= it.main.temp_min
            assertNotNull(it.weather)
            assertNotNull(it.wind)
        }


        where:
        q           | mode      | appid                                 |  expectedCity  | country | cod
        "Brest,By"  | "json"    | "e887cf2c4eac937d0638a90d3b52147c"    |  "Brest"       | "BY"    | 200
        "Minsk,By"  | "json"    | "e887cf2c4eac937d0638a90d3b52147c"    |  "Minsk"       | "BY"    | 200
    }
}