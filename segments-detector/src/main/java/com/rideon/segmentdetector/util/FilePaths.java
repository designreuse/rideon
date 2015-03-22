/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector.util;

/**
 *
 * @author fer
 */
public class FilePaths {

    private final static String FILE_OUTPUT = "C://Users//Fer//Desktop//CSV//Segmentos_";
//    private final static String FILE_OUTPUT = "/home/fer/Escritorio/output/Segmentos_";
    public static final String ROUTE_1 = "/GPX/route_1.gpx";
    public static final String ROUTE_2 = "/GPX/route_2.gpx";
    public static final String ROUTE_2_1 = "/GPX/route_2_1.gpx";
    public static final String ROUTE_3 = "/GPX/route_3.gpx";
    public static final String ROUTE_4 = "/GPX/route_4.gpx";
    public static final String ROUTE_4_1 = "/GPX/route_4_1.gpx";
    public static final String ROUTE_5 = "/GPX/route_5.gpx";
    public static final String ROUTE_6 = "/GPX/route_6.gpx";
    public static final String ROUTE_7 = "/GPX/route_7.gpx";
    public static final String ROUTE_8 = "/GPX/route_8.gpx";
    public static final String ROUTE_9 = "/GPX/route_9.gpx";
    public static final String ROUTE_VM_1 = "/GPX/route_vm_1.gpx";
    public static final String ROUTE_VM_2 = "/GPX/route_vm_2.gpx";
    public static final String ROUTE_VM_3 = "/GPX/route_vm_3.gpx";

    public static String getFileOutputUrl() {
        return FILE_OUTPUT + System.currentTimeMillis() + ".png";
    }
}
