package org.openweathermap.tests.forecast_daily

import com.ihg.middleware.test.OpenWeatherTestCase
import groovy.json.JsonSlurper
import org.openweathermap.utils.DateParser
import org.springframework.http.HttpMethod
import spock.lang.Unroll

import java.time.LocalDateTime

import static org.junit.Assert.assertNotNull

class TC2_16Day_dailyForecastXmlPositiveTest extends OpenWeatherTestCase {

    def response = null

    @Unroll
    def "Test api open weather : get weather 5 days: #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "forecast/daily")

        when: "send GET request to open weather and parse response"
        response = new XmlSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
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

        and: "parse date from response to LocalDateTime"

        LocalDateTime sunset = DateParser.parse(response.sun.@'set')
        LocalDateTime sunrise = DateParser.parse(response.sun.@'rise')

        then: "check that response contain expected data"
        assert response.location.name == expectedCity
        assert response.location.country == country
        assert sunset >= sunrise

        response.forecast[0].time.each {
            assertNotNull(it.symbol.@'number')
            assertNotNull(it.symbol.@'name')
            assertNotNull(it.symbol.@'var')
            assertNotNull(it.precipitation.@'value')
            assertNotNull(it.precipitation.@'type')
            assertNotNull(it.windDirection.@'deg')
            assertNotNull(it.windDirection.@'code')
            assertNotNull(it.humidity.@'value')
            assert it.clouds.@'unit' == "%"

        }

        where:
        q           | mode  | unit     | appid                              | expectedCity | country | cod
        "Brest,By"  | "xml" | "Metric" | "e887cf2c4eac937d0638a90d3b52147c" | "Brest"      | "BY"    | 200
        "Minsk,By"  | "xml" | "Kelvin" | "e887cf2c4eac937d0638a90d3b52147c" | "Minsk"      | "BY"    | 200
        "London,GB" | "xml" | "Kelvin" | "e887cf2c4eac937d0638a90d3b52147c" | "London"     | "GB"    | 200
    }
}