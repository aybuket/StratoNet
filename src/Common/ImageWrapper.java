package Common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageWrapper implements Serializable {
    protected byte[] imgBytes;

    public ImageWrapper(Image img) throws IOException {
        try{
        BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bimg, "jpg", baos);
        imgBytes = baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedImage getImg() {
        try {

            InputStream is = new ByteArrayInputStream(imgBytes);
            return ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setImg(Image img) {
        try{
            BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bimg, "jpg", baos);
            imgBytes = baos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
