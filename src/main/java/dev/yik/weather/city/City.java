package dev.yik.weather.city;

/** A preset city; latitude/longitude are used only for a trusted public weather API. */
public record City(String id, String name, String province,
                   double latitude, double longitude) {}
