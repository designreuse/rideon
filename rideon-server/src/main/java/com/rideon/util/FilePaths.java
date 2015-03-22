/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.util;

/**
 *
 * @author fer
 */
public class FilePaths {
    public static final String USER_IMAGE = "/images/user.jpg";
    public static final String USER_2_IMAGE = "/images/user_2.jpg";
    public static final String USER_3_IMAGE = "/images/user_3.jpg";
    public static final String USER_4_IMAGE = "/images/user_4.jpg";
    public static final String BICYCLE_IMAGE = "/images/bicycle.jpg";
    public static final String GROUP_IMAGE = "/images/group.jpg";
    public static final String CHALLENGE_IMAGE = "/images/challenge.jpg";
    public static final String HANGOUT_IMAGE = "/images/hangout.jpg";
    private static final String FILE_OUTPUT = "C://Users//Fer//Desktop//CSV//Segmentos_";
    public static final String ROUTE_1 = "/GPX/route_1.gpx";
    public static final String ROUTE_2 = "/GPX/route_2.gpx";
    public static final String ROUTE_3 = "/GPX/route_3.gpx";
    public static final String ROUTE_3_1 = "/GPX/route_3_1.gpx";
    public static final String ROUTE_3_2 = "/GPX/route_3_2.gpx";
    public static final String ROUTE_3_3 = "/GPX/route_3_3.gpx";
    public static final String ROUTE_3_4 = "/GPX/route_3_4.gpx";
    public static final String ROUTE_4 = "/GPX/route_4.gpx";
    public static final String ROUTE_4_1 = "/GPX/route_4_1.gpx";
    public static final String ROUTE_4_2 = "/GPX/route_4_2.gpx";
    public static final String ROUTE_5 = "/GPX/route_5.gpx";
    public static final String ROUTE_6 = "/GPX/route_6.gpx";
    public static final String ROUTE_7 = "/GPX/route_7.gpx";
    public static final String ROUTE_8 = "/GPX/route_8.gpx";
    public static final String ROUTE_9 = "/GPX/route_9.gpx";
    public static final String ROUTE_10 = "/GPX/route_10.gpx";
    public static final String ROUTE_VM_1 = "/GPX/route_vm_1.gpx";
    public static final String ROUTE_VM_2 = "/GPX/route_vm_2.gpx";
    public static final String ROUTE_VM_3 = "/GPX/route_vm_3.gpx";
    public static final String ROUTE_EVENT = "/GPX/route_event.gpx";

    public static String getFileOutputUrl() {
        return FILE_OUTPUT + System.currentTimeMillis() + ".png";
    }
}
