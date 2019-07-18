package classes_auxiliares;

import java.io.Serializable;

public class Data implements Serializable{
	
	@Override
	public String toString() {
		return "Data [size=" + size + ", color=" + color + ", posX=" + posX + ", posY=" + posY + ", framePos="
				+ framePos + "]";
	}
	private int size,color,posX,posY,framePos;
	public Data(int size,int color,int posX,int posY,int framePos)
	{
		this.color= color;
		this.size = size;
		this.posX=posX;
		this.posY=posY;
		this.framePos= framePos;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	public int getFramePos() {
		return framePos;
	}
	public void setFramePos(int framePos) {
		this.framePos = framePos;
	}
}
