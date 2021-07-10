package org.acme.controllers;

import org.acme.beans.caching.CachedService;
import org.acme.beans.caching.WeatherForecast;
import org.acme.beans.caching.WeatherForecastService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Path("/weather")
@Tag(name = "WeatherForecastResource",
  description = "Controller for testing caches")
public class WeatherForecastResource {
    @Inject
    WeatherForecastService service;
    @Inject
    CachedService cachedService;

    @GET
    @Operation(summary = "summary: gets weather forecast",
      description = "description: uses cashing for 60s")
    @APIResponses({@APIResponse(
      name = "name: Forecast Response", responseCode = "200",
      description = "description: the cashed (60s) forecast")})
    public WeatherForecast getForecast(
      @QueryParam("city") String city, @QueryParam("days") long days) {
        long executionStart = System.currentTimeMillis();
        List<String> dailyForecasts = Arrays.asList(
          service.gtDailyForecast(LocalDate.now().plusDays(days), city),
          service.gtDailyForecast(LocalDate.now().plusDays(days + 1L), city),
          service.gtDailyForecast(LocalDate.now().plusDays(days + 2L), city)
        );
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(
          dailyForecasts, executionEnd - executionStart);
    }

    @GET
    @Path("/inv1")
    public void invalidate1(
      @QueryParam("city") String city, @QueryParam("days") long days) {
        service.invalidate1(city, LocalDate.now().plusDays(days));
        service.invalidate1(city, LocalDate.now().plusDays(days + 1L));
        service.invalidate1(city, LocalDate.now().plusDays(days + 2L));
    }

    @GET
    @Path("/inv2")
    public void invalidate2(
      @QueryParam("city") String city, @QueryParam("days") long days) {
        service.invalidate2(city, city);
        service.invalidate2(city, city);
        service.invalidate2(city, city);
    }

    @GET
    @Path("/inv3")
    public void invalidate3(
      @QueryParam("city") String city, @QueryParam("days") long days) {
        service.invalidate3(
          "V1", LocalDate.now().plusDays(days), city);
        service.invalidate3(
          "V1", LocalDate.now().plusDays(days + 1L), city);
        service.invalidate3(
          "V1", LocalDate.now().plusDays(days + 2L), city);
    }

    @GET
    @Path("/inv4")
    public void invalidate4(
      @QueryParam("city") String city, @QueryParam("days") long days) {
        service.invalidate4(LocalDate.now().plusDays(days), city);
        service.invalidate4(LocalDate.now().plusDays(days + 1L), city);
        service.invalidate4(LocalDate.now().plusDays(days + 2L), city);
    }

    @GET
    @Path("/invAll")
    public void invalidateAll() {
        service.invalidate5();
    }

    @GET
    @Path("/cached")
    public String cached() {
        return cachedService.load();
    }

    @GET
    @Path("/cached/inv")
    public void cachedInv() {
        cachedService.invalidate();
    }

    @GET
    @Path("/cached/all")
    public void cachedAll() {
        cachedService.invalidateAllCached();
    }

    @GET
    @Path("/cached/multiple")
    public void multipleInvalidateAll() {
        cachedService.multipleInvalidateAll();
    }

    @GET
    @Path("/cached/{val}")
    public String cacheNull(@PathParam("val") int val) {
        return cachedService.caller(val);
    }

    @GET
    @Path("/cached/throws/{val}")
    public String cacheThrow(@PathParam("val") int val) {
        return cachedService.caller2(val);
    }
}
