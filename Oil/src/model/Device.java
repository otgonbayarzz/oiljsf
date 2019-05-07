package model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

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
		byte[] buff = new byte[10000];
		buff[0] = 0x02;

		try (Socket socket = new Socket(this.ip, this.port)) {
			System.out.println("I AM HERE WITH " + this.ip);
			OutputStream output = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(output);

			byte[] b = command.getBytes(StandardCharsets.UTF_8);
			for (int i = 0; i < b.length; i++) {
				buff[i + 1] = b[i];
			}
			buff[b.length + 1] = 0x03;
			dos.write(buff, 0, b.length + 2);
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			String time = reader.readLine();

			this.lastResponse = time;

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
