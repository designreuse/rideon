/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rideon.util;

import com.rideon.model.domain.Multimedia;
import com.rideon.model.dto.MultimediaDto;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Fer
 */
public class IOHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IOHelper.class);

    public static Multimedia resizeImage(Multimedia image, int maxHeight) throws IOException {

        ByteArrayInputStream input = new ByteArrayInputStream(image.getDataArray());
        BufferedImage bImg = null;

        try {
            bImg = ImageIO.read(input);
        } catch (IOException ex) {
            LOGGER.error("", ex);
        }


        double p = (double) maxHeight / (double) bImg.getHeight();
        int maxWidth = (int) Math.ceil((double) bImg.getWidth() * p);

        BufferedImage resizedImage = new BufferedImage(maxWidth, maxHeight, bImg.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(bImg, 0, 0, maxWidth, maxHeight, null);
        g.dispose();

        Multimedia resizedMultimedia = new Multimedia();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        byte[] dataArr = baos.toByteArray();

        resizedMultimedia.setDataArray(dataArr);
        resizedMultimedia.setFileSize(Long.valueOf(dataArr.length));
        resizedMultimedia.setFileType(image.getFileType());

        return resizedMultimedia;
    }

    public static Multimedia readMultimediaFromFile(String url) throws FileNotFoundException, IOException {
        Multimedia image = new Multimedia();
        File file = new File(url);
        byte[] bFile = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        //convert file into array of bytes
        fileInputStream.read(bFile);
        fileInputStream.close();
        image.setDataArray(bFile);
        image.setFileType(FilenameUtils.getExtension(url));
        image.setFileSize(Long.valueOf(bFile.length));
        return image;
    }

    public static MultimediaDto readMultimediaDtoFromFile(String url) throws FileNotFoundException, IOException {
        MultimediaDto image = new MultimediaDto();
        File file = new File(url);
        byte[] bFile = new byte[(int) file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        //convert file into array of bytes
        fileInputStream.read(bFile);
        fileInputStream.close();
        image.setDataArray(bFile);
        image.setFileType(FilenameUtils.getExtension(url));
        image.setFileSize(Long.valueOf(bFile.length));
        return image;
    }
}
