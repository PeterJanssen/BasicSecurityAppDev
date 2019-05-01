package be.pxl.basicSecurity.appDev.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

// Many thanks and credits to Yoga-Priya @ GitHub: https://github.com/Yoga-Priya/Image-Steganography-using-LSB

public class LSB_encode {

    public static int[] getMessageBits(String msg) {
        int j = 0;
        int[] b_msg = new int[msg.length() * 8];
        for (int i = 0; i < msg.length(); i++) {
            int x = msg.charAt(i);
            String binaryString = Integer.toBinaryString(x);
            while (binaryString.length() != 8) {
                binaryString = '0' + binaryString;
            }

            for (int i1 = 0; i1 < 8; i1++) {
                b_msg[j] = Integer.parseInt(String.valueOf(binaryString.charAt(i1)));
                j++;
            }
        }

        return b_msg;
    }

    public static BufferedImage readImageFile(String COVERIMAGEFILE) {
        BufferedImage theImage = null;
        File p = new File(COVERIMAGEFILE);
        try {
            theImage = ImageIO.read(p);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return theImage;
    }


    public static void hideTheMessage(int[] bits, BufferedImage theImage, Path textImagePath) throws Exception {
        File f = textImagePath.toFile();
        BufferedImage sten_img = null;
        int bit_l = bits.length / 8;
        int[] bl_msg = new int[8];
        String bl_s = Integer.toBinaryString(bit_l);
        while (bl_s.length() != 8) {
            bl_s = '0' + bl_s;
        }
        for (int i1 = 0; i1 < 8; i1++) {
            bl_msg[i1] = Integer.parseInt(String.valueOf(bl_s.charAt(i1)));
        }
        ;
        int j = 0;
        int b = 0;
        int currentBitEntry = 8;

        for (int x = 0; x < theImage.getWidth(); x++) {
            for (int y = 0; y < theImage.getHeight(); y++) {
                if (x == 0 && y < 8) {
                    int currentPixel = theImage.getRGB(x, y);
                    int ori = currentPixel;
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    String sten_s = x_s.substring(0, x_s.length() - 1);
                    sten_s = sten_s + Integer.toString(bl_msg[b]);

                    int temp = Integer.parseInt(sten_s, 2);
                    int s_pixel = Integer.parseInt(sten_s, 2);
                    int a = 255;
                    int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
                    theImage.setRGB(x, y, rgb);
                    ImageIO.write(theImage, "png", f);
                    b++;

                } else if (currentBitEntry < bits.length + 8) {

                    int currentPixel = theImage.getRGB(x, y);
                    int ori = currentPixel;
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    String sten_s = x_s.substring(0, x_s.length() - 1);
                    sten_s = sten_s + Integer.toString(bits[j]);
                    j++;
                    int temp = Integer.parseInt(sten_s, 2);
                    int s_pixel = Integer.parseInt(sten_s, 2);

                    int a = 255;
                    int rgb = (a << 24) | (red << 16) | (green << 8) | s_pixel;
                    theImage.setRGB(x, y, rgb);
                    ImageIO.write(theImage, "png", f);

                    currentBitEntry++;
                }
            }
        }
    }
}