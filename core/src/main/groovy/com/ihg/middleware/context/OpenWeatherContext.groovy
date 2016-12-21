package com.ihg.middleware.context

import com.ihg.middleware.client.HttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
class OpenWeatherContext extends AbstractContext {

    @Bean
    PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(){
        def propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer()
        def resources = [
                new ClassPathResource("conf/environments/local/openweather.properties")
        ]
        propertyPlaceholderConfigurer.locations = resources
        propertyPlaceholderConfigurer.ignoreResourceNotFound = true
        propertyPlaceholderConfigurer
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Value('${openweather.endpoint}')
    HttpClient openWeatherRestClient(String url){
        new HttpClient(url)
    }
}
