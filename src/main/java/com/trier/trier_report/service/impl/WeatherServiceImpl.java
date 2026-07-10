package com.trier.trier_report.service.impl;

import com.trier.trier_report.client.WeatherClient;
import com.trier.trier_report.client.impl.OpenWeatherClientImpl;
import com.trier.trier_report.dto.OpenWeatherResponse;
import com.trier.trier_report.service.WeatherService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherClient weatherClient;

    public WeatherServiceImpl(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    @Override
    @Cacheable(value = "lon", key = "#lon + ':' + #lat")
    public OpenWeatherResponse getTodayWeatherReport(String lon, String lat) {
        return weatherClient.fetchWeather(lon, lat);
    }
}
