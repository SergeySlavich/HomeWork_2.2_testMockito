package Geo;

public interface GeoService {

    Location byIp(String ip);

    Location byCoordinates();
}