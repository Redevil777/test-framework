package org.openweathermap.tests.forecast_daily

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import org.springframework.http.HttpMethod
import spock.lang.Unroll

import static org.junit.Assert.assertNotNull

class TC1_16Day_dailyForecastJsonPositiveTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : get weather 5 days: #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "forecast/daily")

        when: "send GET request to open weather and parse response"
        response = new JsonSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: cod,
                REQUEST_METHOD: HttpMethod.GET,
                REQUEST_PARAMS_STRING: "q={q}&mode={mode}&units={units}&cnt={cnt}&appid={appid}",
                REQUEST_PARAMS_VARIABLES: [
                        q    : q,
                        mode : mode,
                        units: unit,
                        cnt  : 5,
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
            assert it.temp.temp_max >= it.temp.temp_min
            assertNotNull(it.temp.day)
            assertNotNull(it.temp.night)
            assertNotNull(it.temp.eve)
            assertNotNull(it.temp.morn)
            assertNotNull(it.pressure)
            assertNotNull(it.humidity)
            assertNotNull(it.weather)
            assertNotNull(it.speed)
            assertNotNull(it.deg)
        }


        where:
        q           | mode      | unit          |   appid                                   |  expectedCity  | country | cod
        "Brest,By"  | "json"    | "Metric"      |   "e887cf2c4eac937d0638a90d3b52147c"      |  "Brest"       | "BY"    | 200
        "Minsk,By"  | "json"    | "Kelvin"      | "e887cf2c4eac937d0638a90d3b52147c"        |  "Minsk"       | "BY"    | 200
        "London,GB" | "json"    | "Kelvin"      | "e887cf2c4eac937d0638a90d3b52147c"        |  "London"      | "GB"    | 200
    }
}