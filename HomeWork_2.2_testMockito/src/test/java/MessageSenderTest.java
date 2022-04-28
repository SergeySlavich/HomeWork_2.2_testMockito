import Geo.Country;
import Geo.GeoService;
import Geo.GeoServiceImpl;
import Geo.Location;
import i18n.LocalizationService;
import i18n.LocalizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MessageSenderTest{
    @ParameterizedTest
    @MethodSource("testSource0")
    void sendTest(String ip, String expected, Country country) {
        GeoService geoService = Mockito.mock(GeoService.class);
        Location location = new Location(null, country, null, 0);
        Mockito.when(geoService.byIp(ip)).thenReturn(location);
        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
        Mockito.when(localizationService.locale(country)).thenReturn(expected);
        Assertions.assertEquals(expected, localizationService.locale(country));
    }
    private static Stream<Arguments> testSource0(){
        return Stream.of(
                Arguments.of("172.3.32.11", "Добро пожаловать", Country.RUSSIA),
                Arguments.of("96.55.183.149", "Welcome", Country.USA),
                Arguments.of("32.123.456.65", "Welcome", Country.BRAZIL)
        );
    }

    @ParameterizedTest
    @MethodSource("testSource1")
    void byIpTest(String ip, Country expected){
        GeoServiceImpl geoService = new GeoServiceImpl();
        Location location = geoService.byIp(ip);
        Country result = location.getCountry(ip);
        assertEquals(expected, result);
    }
    private static Stream<Arguments> testSource1(){
        return Stream.of(
                Arguments.of("172.3.32.11", Country.RUSSIA),
                Arguments.of("96.55.183.149", Country.USA)
        );
    }

    @ParameterizedTest
    @MethodSource("testSource2")
    void localeTest(Country country, String expected){
        LocalizationServiceImpl localizationService = new LocalizationServiceImpl();
        String result = localizationService.locale(country);
        assertEquals(expected, result);
    }
    private static Stream<Arguments> testSource2(){
        return Stream.of(
                Arguments.of(Country.RUSSIA, "Добро пожаловать"),
                Arguments.of(Country.USA, "Welcome"),
                Arguments.of(Country.GERMANY, "Welcome")
        );
    }

    @Test
    public void byCoordinatesTestThrowsExceptions(){
        GeoService geoService = new GeoServiceImpl();
        Class<RuntimeException> expected = RuntimeException.class;
        assertThrowsExactly(expected, ()-> geoService.byCoordinates());
    }
}