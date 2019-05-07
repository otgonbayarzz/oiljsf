package model;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class Device implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ip;
	private int port;
	private String lastResponse;

	public Device(String _ip, int _port) {
		this.ip = _ip;
		this.port = _port;
	}

	public void giveCommand(String command) {
		this.lastResponse = "";

		try (Socket socket = new Socket(this.ip, this.port)) {

			OutputStream output = socket.getOutputStream();
			this.lastResponse = "Message";

		} catch (UnknownHostException ex) {

			System.out.println("Server not found: " + ex.getMessage());
			this.lastResponse = ex.getMessage();

		} catch (IOException ex) {

			System.out.println("I/O error: " + ex.getMessage());
			this.lastResponse = ex.getMessage();
		}

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;

	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLastResponse() {
		return lastResponse;
	}

	public void setLastResponse(String lastResponse) {
		this.lastResponse = lastResponse;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
