package socket3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	public static void main(String args[]){
		try {
			ServerSocket server = new ServerSocket(9001);
			System.out.println("클라이언트의 접속을 대기중");
			
			Socket socket = server.accept();
			InetAddress address = socket.getInetAddress();
			
			System.out.println(address.getHostAddress()+" 로부터 접속했습니다.");
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			
			String message = null;
			while((message = br.readLine())!= null){
				System.out.println("클라이언트 --> 서버로 : "+message);
				pw.println(message);
				pw.flush();
			}
			br.close();
			pw.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}