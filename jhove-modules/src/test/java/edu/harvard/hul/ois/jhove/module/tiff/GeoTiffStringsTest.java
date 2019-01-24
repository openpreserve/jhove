package edu.harvard.hul.ois.jhove.module.tiff;

import static org.junit.Assert.*;

import org.junit.Test;

/* 
 * placed in jhove/jhove-modules/src/test/java/edu/harvard/hul/ois/jhove/module/tiff/GeoTiffStringsTest.java 
 * run all tests by mvn [clean] test
 */

public class GeoTiffStringsTest {

    private GeoTiffStrings profile;

    @Test
    public void geoTiffStringsMapping() {
        assertEquals("Mapping MODELTYPE", profile.MODELTYPE_INDEX.length, profile.MODELTYPE.length);
        assertEquals("Mapping RASTERTYPE", profile.RASTERTYPE_INDEX.length, profile.RASTERTYPE.length);
        assertEquals("Mapping LINEARUNITS", profile.LINEARUNITS_INDEX.length, profile.LINEARUNITS.length);
        assertEquals("Mapping ANGULARUNITS", profile.ANGULARUNITS_INDEX.length, profile.ANGULARUNITS.length);
        assertEquals("Mapping GEOGRAPHICS", profile.GEOGRAPHICS_INDEX.length, profile.GEOGRAPHICS.length);
        assertEquals("Mapping GEODETICDATUM", profile.GEODETICDATUM_INDEX.length, profile.GEODETICDATUM.length);
        assertEquals("Mapping ELLIPSOID", profile.ELLIPSOID_INDEX.length, profile.ELLIPSOID.length);
        assertEquals("Mapping PRIMEMERIDIAN", profile.PRIMEMERIDIAN_INDEX.length, profile.PRIMEMERIDIAN.length);
        assertEquals("Mapping PROJECTEDCSTYPE", profile.PROJECTEDCSTYPE_INDEX.length, profile.PROJECTEDCSTYPE.length);
        assertEquals("Mapping PROJECTION", profile.PROJECTION_INDEX.length, profile.PROJECTION.length);
        assertEquals("Mapping COORDINATETRANSFORMATION", profile.COORDINATETRANSFORMATION_INDEX.length,
                profile.COORDINATETRANSFORMATION.length);
        assertEquals("Mapping VERTICALCSTYPE", profile.VERTICALCSTYPE_INDEX.length, profile.VERTICALCSTYPE.length);
        assertEquals("Mapping VERTICALCSDATUM", profile.VERTICALCSDATUM_INDEX.length, profile.VERTICALCSDATUM.length);
    }

    @Test /* MODELTYPE */
    public void pickMODELTYPE() {
        assertMappingMatch(profile.MODELTYPE_INDEX, profile.MODELTYPE, 1, "Projected");
        assertMappingMatch(profile.MODELTYPE_INDEX, profile.MODELTYPE, 3, "Geocentric");
    }
    
    @Test /* RASTERTYPE */
    public void pickRASTERTYPE() {
        assertMappingMatch(profile.RASTERTYPE_INDEX, profile.RASTERTYPE, 1, "PixelIsArea"); // could be 'Pixel Is Area'
        assertMappingMatch(profile.RASTERTYPE_INDEX, profile.RASTERTYPE, 2, "PixelIsPoint"); // could be 'Pixel Is Point'
    }
    
    @Test /* LINEARUNITS */
    public void pickLINEARUNITS() {
        assertMappingMatch(profile.LINEARUNITS_INDEX, profile.LINEARUNITS, 9001, "Meter");
        assertMappingMatch(profile.LINEARUNITS_INDEX, profile.LINEARUNITS, 9004, "Foot Modified American");
        assertMappingMatch(profile.LINEARUNITS_INDEX, profile.LINEARUNITS, 9015, "Mile International Nautical");
    }
    
    @Test /* ANGULARUNITS */
    public void pickANGULARUNITS() {
        assertMappingMatch(profile.ANGULARUNITS_INDEX, profile.ANGULARUNITS, 9101, "Radian");
        assertMappingMatch(profile.ANGULARUNITS_INDEX, profile.ANGULARUNITS, 9104, "Arc Second");
        assertMappingMatch(profile.ANGULARUNITS_INDEX, profile.ANGULARUNITS, 9108, "DMS Hemisphere");
    }

    @Test /* GEOGRAPHICS */
    public void pickGEOGRAPHICS() {
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4001, "Airy 1830");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4035, "Sphere");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4120, "Greek");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4201, "Adindan");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4209, "Arc 1950");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4255, "Herat North");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4324, "WGS 72BE");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4290, "RT38");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 4902, "NDG Paris");
        assertMappingMatch(profile.GEOGRAPHICS_INDEX, profile.GEOGRAPHICS, 32767, "User Defined");
    }

    @Test /* GEODETICDATUM */
    public void pickGEODETICDATUM() {
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6001, "Airy 1830");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6034, "Clarke 1880");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6215, "Reseau National Belge 1950");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6217, "Bern 1898");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6257, "Makassar");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6317, "Dealul Piscului 1970");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6314, "Deutsche Hauptdreiecksnetz");
        assertMappingMatch(profile.GEODETICDATUM_INDEX, profile.GEODETICDATUM, 6902, "Nord de Guerre");
    }

    @Test /* ELLIPSOID */
    public void pickELLIPSOID() {
        assertMappingMatch(profile.ELLIPSOID_INDEX, profile.ELLIPSOID, 7001, "Airy 1830");
        assertMappingMatch(profile.ELLIPSOID_INDEX, profile.ELLIPSOID, 7009, "Clarke 1866 Michigan");
        assertMappingMatch(profile.ELLIPSOID_INDEX, profile.ELLIPSOID, 7021, "Indonesian National Spheroid");
        assertMappingMatch(profile.ELLIPSOID_INDEX, profile.ELLIPSOID, 7035, "Sphere");
    }

    @Test /* PRIMEMERIDIAN */
    public void pickPRIMEMERIDIAN() {
        assertMappingMatch(profile.PRIMEMERIDIAN_INDEX, profile.PRIMEMERIDIAN, 8901, "Greenwich");
        assertMappingMatch(profile.PRIMEMERIDIAN_INDEX, profile.PRIMEMERIDIAN, 8911, "Stockholm");
        assertMappingMatch(profile.PRIMEMERIDIAN_INDEX, profile.PRIMEMERIDIAN, 32767, "User Defined");
    }

    @Test /* PROJECTEDCSTYPE */
    public void pickPROJECTEDCSTYPE() {
        //assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 2100, "GGRS87 Greek Grid");
        /* https://sno.phy.queensu.ca/~phil/exiftool/TagNames/GeoTiff.html 
         * keys 2100 - 3300 out of range, compare with http://geotiff.maptools.org/spec/geotiff6.html
         * Open for Discussion: should this data be collected at all?
         * Changed Decission from YES to NO, since there is a lot of new key-value-pairs from the latest source
         * that have no way of conformation
         * The only hint we currently have are the ranges defined in geotiff-spec 1.0
         */

        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 20137, "Adindan UTM zone 37N");
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 20255, "AGD66 AMG zone 55");
        // Test for the latest Key-Value-Pairs from csv-source of libgeotiff-database
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 25833, "ETRF89 / UTM zone 33N"); 
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 26730, "NAD27 Alabama West");
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 32009, "NAD27 Nevada West");
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 32548, "WGS72BE UTM zone 48S");
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 32757, "WGS84 UTM zone 57S");
        assertMappingMatch(profile.PROJECTEDCSTYPE_INDEX, profile.PROJECTEDCSTYPE, 32760, "WGS84 UTM zone 60S");
    }

    @Test /* PROJECTION */
    public void pickPROJECTION() {
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 10101, "Alabama CS27 East");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 10434, "California CS83 4");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 12733, "Nevada CS83 West");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 16125, "UTM zone 25S");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 16201, "Gauss Kruger zone 1");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 18037, "Argentina 7");
        assertMappingMatch(profile.PROJECTION_INDEX, profile.PROJECTION, 19926, "Stereo 70");
    }

    @Test /* COORDINATETRANSFORMATION */
    public void pickCOORDINATETRANSFORMATION() {
        assertMappingMatch(profile.COORDINATETRANSFORMATION_INDEX, profile.COORDINATETRANSFORMATION, 1, "Transverse Mercator");
        assertMappingMatch(profile.COORDINATETRANSFORMATION_INDEX, profile.COORDINATETRANSFORMATION, 8, "Lambert Conf Conic 2SP");
        assertMappingMatch(profile.COORDINATETRANSFORMATION_INDEX, profile.COORDINATETRANSFORMATION, 16, "Oblique Stereographic");
        assertMappingMatch(profile.COORDINATETRANSFORMATION_INDEX, profile.COORDINATETRANSFORMATION, 27, "Transverse Mercator South Orientated");
    }

    @Test /* VERTICALCSTYPE */
    public void pickVERTICALCSTYPE() {
        assertMappingMatch(profile.VERTICALCSTYPE_INDEX, profile.VERTICALCSTYPE, 5001, "Airy 1830 ellipsoid");
        assertMappingMatch(profile.VERTICALCSTYPE_INDEX, profile.VERTICALCSTYPE, 5011, "Clarke 1880 IGN ellipsoid");
        assertMappingMatch(profile.VERTICALCSTYPE_INDEX, profile.VERTICALCSTYPE, 5028, "Struve 1860 ellipsoid");
        assertMappingMatch(profile.VERTICALCSTYPE_INDEX, profile.VERTICALCSTYPE, 5106, "Caspian Sea");
    }

    @Test /* VERTICALCSDATUM */
    public void pickVERTICALCSDATUM() {
        assertMappingMatch(profile.VERTICALCSDATUM_INDEX, profile.VERTICALCSDATUM, 0, "Undefined");
        assertMappingMatch(profile.VERTICALCSDATUM_INDEX, profile.VERTICALCSDATUM, 32767, "User Defined");
    }

    private void assertMappingMatch(int[] key_array, String[] value_array, int key, String value) {
        int pickKeyIndex = getIndexOfArrayByValue(key_array, key);
        assert pickKeyIndex != -1 : "key: (" + key + ") not in key-array";
        int pickValueIndex = getIndexOfArrayByValue(value_array, value);
        assert pickValueIndex != -1 : "value: (" + value + ") not in value-array";
        assertEquals(key + " does not map to " + value, pickKeyIndex, pickValueIndex);
    }

    private int getIndexOfArrayByValue(int[] arr, int value) { 
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == value) return i;
        }
        return -1;
    }

    private int getIndexOfArrayByValue(String[] arr, String value) { 
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) return i;
        }
        return -1;
    }

}
