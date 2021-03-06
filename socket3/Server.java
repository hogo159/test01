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
import java.util.List;
import java.util.Vector;

public class Server {
	public static void main(String args[]) {
		ServerSocket server = null;
		//List<EchoThread> list2 = new Vector<>();
		List<Socket> list = new Vector<>();
		
		try {
			server = new ServerSocket(9009);
			System.out.println("클라이언트의 접속을 대기중");

			while (true) {
				Socket socket = server.accept();
				list.add(socket);
				new EchoThread(socket,list).start();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (server != null)
					server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class EchoThread extends Thread {
	List<Socket> list;
	Socket socket;

	public EchoThread() {
	}

	public EchoThread(Socket socket,List<Socket> list) {
		this.socket = socket;
		this.list = list;
	}

	@Override
	public void run() {

		InetAddress address = socket.getInetAddress();
		System.out.println(address.getHostAddress() + " 로부터 접속했습니다.");
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));

			String message = null;
			while ((message = br.readLine()) != null) {
				
				//System.out.println("클라이언트 --> 서버로 : " + message);
				broadcast(message);
				
			}
			br.close();
			pw.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());

		}finally{
			list.remove(socket);
			System.out.println(socket+"정속해제");
			System.out.println(list);
			try{if(socket !=null)socket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	public synchronized void broadcast(String msg) throws IOException{
		for(Socket socket:list){
			
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			pw.println(msg);
			pw.flush();
		}
	}
}