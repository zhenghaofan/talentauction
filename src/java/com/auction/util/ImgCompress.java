package com.auction.util;

import java.io.*;  
import java.awt.*;  
import java.awt.image.*;  

import javax.imageio.ImageIO;  

import com.sun.image.codec.jpeg.*;  
/** 
 * 图片压缩处理 
 */  
public class ImgCompress {  
    private Image img;  
    private int width;  
    private int height;
    private String outUrl;
    
    public static void main(String[] args) throws Exception {  
    	
    	int start = (int) System.currentTimeMillis(); // 结束时间  
        ImgCompress imgCom = new ImgCompress("/Users/tobber/Pictures/test.jpg","/Users/tobber/Pictures/3.jpg");  
        imgCom.resizeByWidth(400);  
        
        int end = (int) System.currentTimeMillis(); // 结束时间   
        int re = end-start; // 但图片生成处理时间   
        System.out.println(re); 
    }  
    
    /**
     * 构造函数  
     * @param fileName 压缩图片路径
     * @param outUrl 压缩保存路径
     * @throws IOException
     */
    public ImgCompress(String fileName,String outUrl) throws IOException {  
        File file = new File(fileName);// 读入文件  
        img = ImageIO.read(file);      // 构造Image对象  
        width = img.getWidth(null);    // 得到源图宽  
        height = img.getHeight(null);  // 得到源图长
        this.outUrl = outUrl;
    }  
    /** 
     * 按照宽度还是高度进行压缩 
     * @param w int 最大宽度 
     * @param h int 最大高度 
     */  
    public void resizeFix(int w, int h) throws IOException {  
        if (width / height > w / h) {  
            resizeByWidth(w);  
        } else {  
            resizeByHeight(h);  
        }  
    }  
    /** 
     * 以宽度为基准，等比例放缩图片 
     * @param w int 新宽度 
     */  
    public void resizeByWidth(int w) throws IOException {  
        int h = (int) (height * w / width);  
        resize(w, h);  
    }  
    /** 
     * 以高度为基准，等比例缩放图片 
     * @param h int 新高度 
     */  
    public void resizeByHeight(int h) throws IOException {  
        int w = (int) (width * h / height);  
        resize(w, h);  
    }  
    /** 
     * 强制压缩/放大图片到固定的大小 
     * @param w int 新宽度 
     * @param h int 新高度 
     */  
    public void resize(int w, int h) throws IOException {  
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
       /**
    	BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );   
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图  
        File destFile = new File(outUrl);  
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
        // 可以正常实现bmp、png、gif转jpg  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
        encoder.encode(image); // JPEG编码  
        out.close();  
        */
        try {
            Image Itemp = img.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);
            BufferedImage thumbnail = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            thumbnail.getGraphics().drawImage(Itemp, 0, 0, null);
            // 缩略后的图片路径
            File newimg = new File(outUrl);
            FileOutputStream out = new FileOutputStream(newimg);
            // 绘图
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbnail);
            param.setQuality(1.0f, false);
            encoder.encode(thumbnail);
            out.close();
            img.flush();
            img = null;
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }  
} 
