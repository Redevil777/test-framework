package org.openweathermap.tests.current_weather_data

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j
import org.springframework.http.HttpMethod
import spock.lang.Unroll

import static org.junit.Assert.assertNotNull

@Log4j
class TC1_CurrentWeatherDataJsonPositiveTest extends OpenWeatherTestCase {

    def response = null

    @Unroll
    def "Test api open weather : get current weather by city name : #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "weather")

        when: "send GET request to open weather and parse response"
        response = new JsonSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: 200,
                REQUEST_METHOD : HttpMethod.GET,
                REQUEST_PARAMS_STRING : "q={q}&mode={mode}&appid={appid}",
                REQUEST_PARAMS_VARIABLES    : [
                        q : q,
                        mode : mode,
                        appid : appid
                ]
        ))

        then: "check that response contain expected data"
        assertNotNull(response)
        assertNotNull(response.coord.lon)
        assertNotNull(response.coord.lat)
        assertNotNull(response.weather)
        assertNotNull(response.weather)
        assertNotNull(response.main.temp)
        assertNotNull(response.main.pressure)
        assertNotNull(response.main.humidity)
        assertNotNull(response.main.temp_min)
        assertNotNull(response.main.temp_max)
        assertNotNull(response.visibility)
        assertNotNull(response.wind.speed)
        assert response.name == expectedCity
        assert response.sys.country == country
        assert response.sys.sunset > response.sys.sunrise
        assert response.cod == cod

        where:
        q           | mode      | appid                                 |  expectedCity  | country | cod
        "Brest,By"  | "json"    | "e887cf2c4eac937d0638a90d3b52147c"    |  "Brest"       | "BY"    | 200
        "Minsk,By"  | "json"    | "e887cf2c4eac937d0638a90d3b52147c"    |  "Minsk"       | "BY"    | 200
    }
}
