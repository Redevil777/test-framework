package org.openweathermap.tests.forecast_daily

import com.ihg.middleware.test.OpenWeatherTestCase
import org.springframework.http.HttpMethod
import spock.lang.Unroll

class TC8_16Day_dailyForecastCheckMaxDaysXmlNegativeTest extends OpenWeatherTestCase {
    def response = null

    @Unroll
    def "Test api open weather : #q"() {
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
                        cnt  : cnt,
                        appid: appid
                ]
        ))


        then: "check that response contain expected data"

        assert cnt == response.forecast[0].time.size()


        where:

        q = "Brest,By"
        mode = "xml"
        unit = "Kelvin"
        appid = "e887cf2c4eac937d0638a90d3b52147c"
        expectedCity = "Brest"
        country = "BY"
        cod = 200

        cnt << [
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16
        ]
    }
}