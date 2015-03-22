/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.segmentdetector.util;

import com.rideon.common.enums.SegmentCategory;
import com.rideon.model.domain.Practice;
import static com.rideon.segmentdetector.util.SegmentUtils.getCategory;
import static com.rideon.segmentdetector.util.SegmentUtils.linestringDistance;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.Period;
import org.opengis.referencing.operation.TransformException;

/**
 *
 * @author Fer
 */
public class Helper {
       public static void printPoints(Practice pr, int from, int to) throws TransformException {
        Date[] times = pr.getTimes();
        LineString lineStr = pr.getRoute().getGeometry();
        Coordinate[] coors = lineStr.getCoordinates();
        double dist = 0;

        Date ini = times[0];
        for (int i = 0; i < lineStr.getNumPoints(); i++) {
            double parcial = JTS.orthodromicDistance(coors[i == 0 ? 0 : i - 1], coors[i], DefaultGeographicCRS.WGS84);
            dist += parcial;
            Long t = times[i].getTime() - ini.getTime();
            Period p = new Period(t.longValue());
            double inc = 0;
            if (i > 0 && parcial != 0) {
                double altura = coors[i].z - coors[i - 1].z;
                inc = ((altura / parcial) * 100);
            }
            DecimalFormat df = new DecimalFormat("#.##");
            if (i >= from && i < to) {
                System.out.println("Latitud: " + coors[i].x + "\t Longitud : " + coors[i].y
                        + "\t Altitud : " + coors[i].z + "\t Inclinacion : " + df.format(inc) + "%"
                        + "\t Dist : " + Math.round(dist) + "\t Tiempo -> " + p.getHours() + ":" + p.getMinutes() + ":" + p.getSeconds());
            }
        }
    }

    public static void printChart(Coordinate[] coors, List<int[]> segments, String chartName) throws TransformException, IOException {
        double dist = 0;

        List<XYSeries> series = new ArrayList<>();

        XYSeries fullDataset = new XYSeries("fullRoute");
//        series.add(fullDataset);

        for (int i = 0; i < coors.length; i++) {
            double parcial = JTS.orthodromicDistance(coors[i == 0 ? 0 : i - 1], coors[i], DefaultGeographicCRS.WGS84);
            dist += parcial;
            fullDataset.add(dist / 1000, Math.round(coors[i].z));
        }

        for (int j = 0; j < segments.size(); j++) {
            dist = 0;
            int[] c = segments.get(j);
            SegmentCategory category = getCategory(coors, c[0], c[1]);
            String name;
            DecimalFormat dc = new DecimalFormat("#.###");
            String alt = dc.format(coors[c[1]].z - coors[c[0]].z);
            String d = dc.format(linestringDistance(coors, c[0], c[1]));
            if (category != null) {
                name = "Segmento_" + j + "(" + category.ordinal() + "º Cat)";
                System.out.println(name + " : \t Altura " + alt + " m \t Distancia: " + d + " km " + "\t Categoria: " + category.ordinal() + "º Cat");
            } else {
                name = "Segmento_" + j + "(FLAT)";
                System.out.println(name + " : \t Altura " + alt + " m \t Distancia: " + d + " km " + "\t Categoria: FLAT");
            }
            XYSeries dataset = new XYSeries(name);

            for (int i = 0; i < coors.length; i++) {
                double parcial = JTS.orthodromicDistance(coors[i == 0 ? 0 : i - 1], coors[i], DefaultGeographicCRS.WGS84);
                dist += parcial;
                if (i >= c[0] && i < c[1]) {
                    dataset.add(dist / 1000, Math.round(coors[i].z));
                }
            }
            series.add(dataset);
        }

        XYSeriesCollection my_data_series = new XYSeriesCollection();
        for (XYSeries s : series) {
            my_data_series.addSeries(s);
        }
        my_data_series.addSeries(fullDataset);

        JFreeChart XYLineChart = ChartFactory.createXYStepAreaChart("Segmentos " + chartName, "Distancia", "Altura", my_data_series, PlotOrientation.VERTICAL, true, true, false);

        /* Step -3 : Write line chart to a file */
        int width = 1200; /* Width of the image */
        int height = 300; /* Height of the image */
        File XYlineChart = new File(FilePaths.getFileOutputUrl());
        ChartUtilities.saveChartAsPNG(XYlineChart, XYLineChart, width, height);

        Desktop.getDesktop().open(XYlineChart);
    }
 
}
