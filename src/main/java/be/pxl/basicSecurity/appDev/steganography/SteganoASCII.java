package be.pxl.basicSecurity.appDev.steganography;

public class SteganoASCII {
    public static boolean[] encode(String message) {
        if (message == null) {
            return null;
        }

        byte[] bytes = message.getBytes();
        boolean[] bits = new boolean[7 * message.length()];
        int counter = 0;

        for (byte b :
                bytes) {
            String binaryString = String.format("%7s", Integer.toBinaryString(b).replace(' ', '0'));
            for (int i = 0; i < 7; i++) {
                bits[counter] = binaryString.charAt(i) == '1';
                counter++;
            }
        }

        return bits;
    }

    public static String decode(boolean[] bits) {
        byte[] bytes = new byte[bits.length / 7];
        int counter = 0;

            for (int j = 0; j < bytes.length; j++) {
                StringBuilder binaryStringBuilder = new StringBuilder();
                for (int k = 0; k < 7; k++) {
                    binaryStringBuilder.append(bits[counter] ? 1 : 0);
                    counter++;
                }

                bytes[j] = Byte.parseByte(binaryStringBuilder.toString(), 2);
            }

        return new String(bytes);
    }
}
