package jp.crudefox.tunacan.chikara.util;


import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.text.TextPaint;
import android.text.TextUtils;

public class TextDraw {

	public enum HAlign{
		Left,
		Center,
		Right,
	}
	
	public enum VAlign{
		Top,
		Center,
		Bottom,
	}
	
	public static void drawText(Canvas cv,TextPaint paint, FontMetrics fm,String text,float x,float y,HAlign ha,VAlign va){
		//Point Pt = new Point(0,0);
		float PtX=0,PtY=0;
		
		switch(ha){
		case Left:
			paint.setTextAlign(Align.LEFT); 
			PtX = 0 + x;
			break;
		case Center:	
			paint.setTextAlign(Align.CENTER);
			PtX = 0 + x;
			break;
		case Right:	
			paint.setTextAlign(Align.RIGHT); 
			PtX = 0 + x;
			break;
		}		
		
		//FontMetrics fm = paint.getFontMetrics();
		switch(va){
		case Top:
			PtY = (int)( 0+y - fm.ascent );
			break;
		case Center:	
			PtY = (int)( 0+y - (fm.ascent+fm.descent)/2.0f );
			break;
		case Bottom:	
			PtY = (int)( 0+y - fm.descent );
			break;
		}
		
		cv.drawText(text, PtX, PtY, paint);
	}	
	
	public static int calcTextWidth(String text,TextPaint paint){
		
		if(TextUtils.isEmpty(text)) return 0;
		
		return (int) Math.ceil( paint.measureText(text) );
	}
	public static int calcTextHeight(FontMetrics fm){

//		AppUtility.Log("fm.top = "+fm.top);
//		AppUtility.Log("fm.bottom = "+fm.bottom);
		
		return (int) Math.ceil( fm.bottom - fm.top );
	}
	
}
