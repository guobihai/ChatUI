package smt.qrcode.zxing.qrcode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class CreateBitmapUtils {
	public static Bitmap createImage(Context mContext, String text, boolean hasLogo, Bitmap logoBitmap){
		Bitmap bitmap_QRCode = null;
		try {
			
			DisplayMetrics dm = new DisplayMetrics();
			((Activity) mContext).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			int QR_WIDTH = dm.widthPixels - 100;
			int QR_HEIGHT = QR_WIDTH;
			
			QRCodeWriter writer = new QRCodeWriter();
			BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
			QRCodeMultiReader dd = new QRCodeMultiReader();
			Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//���ñ���
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); //�ݴ���
			BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for(int y = 0; y < QR_HEIGHT; y++){
				for(int x = 0; x < QR_WIDTH; x++){
					if(bitMatrix.get(x, y)){
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}else{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
		    bitmap_QRCode = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap_QRCode.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			
			
			if(hasLogo){
				bitmap_QRCode = setQRLogo(bitmap_QRCode, logoBitmap);
			}
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap_QRCode;
	}
	
	public static Bitmap createQRCode(Context mContext, String text){
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		int QR_WIDTH = dm.widthPixels - 100;
		int QR_HEIGHT = QR_WIDTH;
		
		Bitmap bitmap = null;
		
		MultiFormatWriter write = new MultiFormatWriter();
		try {
			write.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		
		return bitmap;
	}
	
	private static Bitmap setQRLogo(Bitmap src, Bitmap logo){
		
		if(src == null){
			return null;
		}
		if(logo == null){
			return src;
		}
		
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();
		if(srcWidth == 0 || srcHeight == 0){
			return null;
		}
		if(logoWidth == 0 || logoHeight == 0){
			return src;
		}
		float scale = srcWidth*1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(src, 0, 0, null);
		canvas.scale(scale, scale, srcWidth/2, srcHeight/2);
		canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
		return bitmap;
	}
}
