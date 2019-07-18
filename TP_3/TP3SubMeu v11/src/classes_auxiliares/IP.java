package classes_auxiliares;

import java.io.Serializable;

public class IP implements Serializable{
	private String ip;
	private boolean state;
	public IP(String ip,boolean state)
	{
		this.ip= ip;
		this.state=state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
}
