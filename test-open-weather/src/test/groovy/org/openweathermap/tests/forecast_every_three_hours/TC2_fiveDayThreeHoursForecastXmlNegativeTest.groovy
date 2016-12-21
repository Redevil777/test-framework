package org.openweathermap.tests.forecast_every_three_hours

import com.ihg.middleware.test.OpenWeatherTestCase
import org.springframework.http.HttpMethod
import spock.lang.Unroll
import static org.junit.Assert.assertNotNull


class TC2_fiveDayThreeHoursForecastXmlNegativeTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : get weather by city name for the next 5 days every 3 hours : #q"() {
        setup: "Configuration url"
        openWeatherRestClient.setHostUrl(openWeatherRestClient.getHostUrl() + "forecast")

        when: "send GET request to open weather and parse response"
        response = new XmlSlurper().parseText(openWeatherRestClient.sendAndAssertResponseStatus(
                EXPECTED_STATUS: 200,
                REQUEST_METHOD: HttpMethod.GET,
                REQUEST_PARAMS_STRING: "q={q}&mode={mode}&appid={appid}",
                REQUEST_PARAMS_VARIABLES: [
                        q    : q,
                        mode : mode,
                        appid: appid
                ]
        ))

        then: "check that response contain expected data"
        assert response.location.name == expectedCity
        assert response.location.country == country

        response.forecast[0].time.each {

            assertNotNull(it.symbol.@'number')
            assertNotNull(it.symbol.@'name')
            assertNotNull(it.symbol.@'var')
            assertNotNull(it.precipitation.@'unit')
            assertNotNull(it.precipitation.@'value')
            assertNotNull(it.precipitation.@'type')
            assertNotNull(it.windDirection.@'deg')
            assertNotNull(it.windDirection.@'code')
            assertNotNull(it.windDirection.@'name')
            assertNotNull(it.windSpeed.@'mps')
            assertNotNull(it.windSpeed.@'name')
            assertNotNull(it.humidity.@'value')
            assert it.humidity.@'unit' == "%"
            assertNotNull(it.clouds.@'value')
            assert it.clouds.@'unit' == "%"
            assert it.temperature.@'unit' == "celsius"
        }

        where:
        q          | mode  | appid                              | expectedCity | country
        "Brest,By" | "xml" | "e887cf2c4eac937d0638a90d3b52147c" | "Brest"      | "BY"
        "Minsk,By" | "xml" | "e887cf2c4eac937d0638a90d3b52147c" | "Minsk"      | "BY"
    }
}