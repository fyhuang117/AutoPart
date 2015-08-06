package com.autoparts.sellers.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class LineGrildView extends MyGridView{

	public LineGrildView(Context context) {
		super(context);
		
	}
	public LineGrildView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public LineGrildView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	private int lineColor=Color.LTGRAY;

	

	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
		if (getChildCount()==0) {
			
		}else {
			View localView1 = getChildAt(0);
			int column = getWidth() / localView1.getWidth();
			int childCount = getChildCount();
			Paint localPaint;
			localPaint = new Paint();
			localPaint.setStyle(Paint.Style.STROKE);
			localPaint.setColor(lineColor);
			for (int i = 0; i < childCount; i++) {
				View cellView = getChildAt(i);
				if ((i + 1) % column == 0) {
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
				} else if ((i + 1) > (childCount - (childCount % column))) {
					canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
				} else {
					canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
					canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
				}
			}
			if (childCount % column != 0) {
				for (int j = 0; j < (column - childCount % column); j++) {
					View lastView = getChildAt(childCount - 1);
					canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth() * j,
							lastView.getBottom(), localPaint);
				}
			}
		}
		

	}

	
	/**
	 * @return the lineColor
	 */
	public int getLineColor() {
		return lineColor;
	}

	
	/**
	 * @param lineColor the lineColor to set
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

}
