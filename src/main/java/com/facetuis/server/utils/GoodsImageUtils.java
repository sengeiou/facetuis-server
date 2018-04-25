package com.facetuis.server.utils;

import com.alipay.api.domain.GoodsDetail;
import com.facetuis.server.service.pinduoduo.response.GoodsDetails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GoodsImageUtils {

    public static byte[] createImage(String backgroundImgPath, GoodsDetails goodsDetail){
        String goodsImageUrl = goodsDetail.getGoods_image_url();
        BufferedImage goodsImage = null;
        try {
            goodsImage = ImageIO.read(new URL(goodsImageUrl));

        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage resultImage = null;
        try {
            resultImage = ImageIO.read(new File(backgroundImgPath));
            Graphics graphics = resultImage.createGraphics();
            graphics.drawImage(goodsImage,0,0,750,760,null);
            graphics.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            boolean flag = ImageIO.write(resultImage, "jpg", out);
            if(flag){
                return out.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
