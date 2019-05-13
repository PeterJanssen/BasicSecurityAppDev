package be.pxl.basicSecurity.appDev.steganography;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

// Many thanks and credits to Yoga-Priya @ GitHub: https://github.com/Yoga-Priya/Image-Steganography-using-LSB

public class LSB_decode {

    public static String DecodeTheMessage(Path imagePath) throws Exception {
        String b_msg = "";
        int len = 0;

        BufferedImage image = ImageIO.read(imagePath.toFile());
        int j = 0;
        int currentBitEntry = 0;
        String bx_msg = "";
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (x == 0 && y < 8) {
                    int currentPixel = image.getRGB(x, y);
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    bx_msg += x_s.charAt(x_s.length() - 1);
                    len = Integer.parseInt(bx_msg, 2);

                } else if (currentBitEntry < len * 8) {
                    int currentPixel = image.getRGB(x, y);
                    int red = currentPixel >> 16;
                    red = red & 255;
                    int green = currentPixel >> 8;
                    green = green & 255;
                    int blue = currentPixel;
                    blue = blue & 255;
                    String x_s = Integer.toBinaryString(blue);
                    b_msg += x_s.charAt(x_s.length() - 1);


                    currentBitEntry++;
                }
            }
        }
        String msg = "";
        for (int i = 0; i < len * 8; i = i + 8) {
            String sub = b_msg.substring(i, i + 8);
            int m = Integer.parseInt(sub, 2);
            char ch = (char) m;
            msg += ch;
        }

        return msg;
    }
}