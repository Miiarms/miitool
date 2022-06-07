package com.miiarms.miitool.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miiarms
 * @version 1.0
 * @date 2021/6/3
 */
public class QrCodeUtils {

    /**
     * <h3>生成二维码</h3>
     * @param width             宽度(像素)
     * @param height            高度(像素)
     * @param content           文本内容
     * @param charset           文本字符编码
     * @param path              存放二维码路径
     * @param imageFormat       图片格式(jpg/png...)
     * @throws Exception
     */
    public static void createQRCode(int width, int height, String content, String charset, String path,
                                    String imageFormat) throws Exception{
        // 设置二维码一些配置信息
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, charset);//设置二维码内容编码格式
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置二维码容错级别(L、M、Q、H)

        //将内容编码为指定的二维矩阵图像
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        //使用MatrixToImageWriter类的静态方法将二维矩阵写入到输出流或指定路径path
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, new FileOutputStream(path));
        //MatrixToImageWriter.writeToPath(bitMatrix, imageFormat, new File("").toPath());
    }

    /**
     * <h3>解析二维码内容</h3>
     *
     * @param imagePath         待解析的二维码图片存放路径
     * @return
     * @throws Exception
     */
    public static String decodeQRCode(String imagePath) throws Exception{
        // 其实下面这四步顺着推是很难推出来的,反着推倒比较简答,不过完全没必要记住,知道大概就行了
        BufferedImage bufferedImage = ImageIO.read(new File(imagePath));
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        //使用MultiFormatReader将二维码图片解析为内容对象
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        Result result = multiFormatReader.decode(binaryBitmap, hints);
        return result.getText();
    }

}
