package com.whksoft.mymapplugin.QRcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class QRCode {

    String str;

    public QRCode(String str) {
        this.str = str;
    }

    private BitMatrix encode(String contents) throws WriterException {
        final Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN,1);
        //这块不知道怎么改尺寸才合适
        return new QRCodeWriter().encode(contents, BarcodeFormat.QR_CODE, 128, 128, hints);
    }

    /**
     * @return 返回一个二维码图片实例
     */
    public BufferedImage getImg(){
        BufferedImage bufferedImage=null;
        try {
            bufferedImage = MatrixToImageWriter.toBufferedImage(encode(str), new MatrixToImageConfig(Color.BLACK.getRGB(), Color.WHITE.getRGB()));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}
