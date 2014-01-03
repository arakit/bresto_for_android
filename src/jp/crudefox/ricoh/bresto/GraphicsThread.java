package jp.crudefox.ricoh.bresto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import jp.co.ricoh.pjs.pcscreen.RNCBHeader;
import jp.crudefox.tunacan.chikara.util.CFUtil;

import org.apache.commons.httpclient.ChunkedOutputStream;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import android.graphics.Bitmap;


/**
 * 	@author Chikara Funabashi.
 */

public class GraphicsThread extends Thread{

	private static final String PORT = "80";
	private static final String SCHEME_HTTP = "http://";


	private boolean mIsCanceld = false;;


//	private BufferedImage mImgBuf;
//	private Graphics2D mGraphic;

	private int mWidth;
	private int mheight;
	//private byte[] mData;
	//private ByteArrayOutputStream mDataStream;
	//private SynchronousQueue<ByteArrayOutputStream> mDataQue = new SynchronousQueue<ByteArrayOutputStream>();
	private ByteArrayOutputStream mData;//, mWaitData;


	public GraphicsThread(int width, int heght){
//		BufferedImage img = new BufferedImage(width, heght, BufferedImage.TYPE_3BYTE_BGR);
//		Graphics2D g = img.createGraphics();
//		mImgBuf = img;
//		mGraphic = g;



		mWidth = width;
		mheight = heght;
	}


	public void updateImage(Bitmap bmp){
		//mGraphic = g;
		//mImgBuf = imgbuf;
		//mGraphic.drawImage(imgbuf, 0,0, null);

		//if(mData!=null) return ;

		for(int i=0;mData!=null;i++){
			if(i>5){
				log("time over...");
				return ;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
		}

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 80, os);
		offerData(os);
		//mDataQue.offer(os);

		//synchronized(GraphicsThread.this){
		//	mDataStream = os;
		//}

		log("update_imege");

		GraphicsThread.this.interrupt();
		//Thread.interrupted();
	}


	@Override
	public void run(){

		String projector_ip;

//		DiscoveryAgent discover = new DiscoverAgentImpl();
//		List<String> device_ip_list = discover.getDevice("hoge");
//		String projector_ip =device_ip_list.get(0);
//		System.out.println(projector_ip);

		//projector_ip = "192.168.1.102";
		//projector_ip = "172.16.32.115";
		//projector_ip = "172.16.32.115";
		projector_ip = "192.168.1.141";


		HttpClient httpclient = new HttpClient();
		HttpMethod http = null;

		/** 1. 投影のための投影リソースを作る **/
		// POST /service/projection
		http = new PostMethod(SCHEME_HTTP + projector_ip + ":" +PORT + "/service/projection");

		// ヘッダも特に注意は無い
		http.setRequestHeader("Content-Type", "application/json");
		// entity(body) は無くても良い
		RequestEntity entity = null;
		try
		{
			entity = new StringRequestEntity("{\"exclusive\":\"off\"}", "application/json", null);

		}
		catch (UnsupportedEncodingException e)
		{
			return;
		}
		if (entity != null)
		{
			((EntityEnclosingMethod)http).setRequestEntity(entity);
		}

		//
		log("接続を実行します。");
        try {
        	// 実行！
			httpclient.executeMethod(http);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		log("Status Line: " + http.getStatusLine());

		// Response
		// 201 Createdが返って来ており、Locationヘッダが取れればOK
		Header[] headerList = http.getResponseHeaders();

		String targetUrl = "";

		for (Header header : headerList)
		{
			if (header.getName().equals("Location"))
			{
				// Location value.
				targetUrl = header.getValue();
				break;
			}
		}
		http.releaseConnection();

		/** 2. projection(image) **/
		//HttpConnectionを生成
		HttpConnection conn = new HttpConnection(projector_ip, Integer.parseInt(PORT));

		try{
			conn.open();//コネクションを張る

			//PUTメソッドの実行
			String projector_url = SCHEME_HTTP + projector_ip;
			int index = targetUrl.indexOf(projector_url);
			index += projector_url.length();
			String projector_resource = targetUrl.substring(index);

			conn.printLine("PUT "+ projector_resource + " " + "HTTP/1.1");
			conn.printLine("Host:"+ "hoge");
			conn.printLine("Content-Type:"+ "video/x-rncb");
			conn.printLine("Transfer-Encoding:"+ "chunked");
			conn.printLine();
			conn.flushRequestOutputStream();

			//Chunked用のストリームを生成する
			OutputStream os = conn.getRequestOutputStream();
			ChunkedOutputStream cos = new ChunkedOutputStream(os);


			//PC画面をキャプチャし、ストリームに流す
			//ScreenCapture sc_cap = new ScreenCapture("./tmp/");//workspaceを相対パスで指定。

//			BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
//			Graphics2D g = img.createGraphics();

			ByteArrayOutputStream drawing = null;

			RNCBHeader rncb = new RNCBHeader();
			while(!mIsCanceld){

				//System.out.println("描画中"+System.currentTimeMillis());
				//ByteArrayOutputStream byte_array_stream = sc_cap.capture();//画面キャプチャをJPEGのバイト配列で出力する

//				g.setBackground(Color.WHITE);
//				g.clearRect(0, 0, 200, 200);
//
////				g.setColor(Color.WHITE);
////				g.drawRect(0, 0, 200, 200);
//
//				g.rotate(Math.PI / 16, 50, 50);
//
//				g.setColor(Color.BLACK);
//				g.drawRect(0, 0, 100, 100);
//
//				g.setColor(Color.BLACK);
//				g.drawString("みかん！", 25, 25);

				//ByteArrayOutputStream byte_array_stream = new ByteArrayOutputStream();
				//ImageIO.write(mImgBuf, "jpg", byte_array_stream);
				//ByteArrayOutputStream byte_array_stream = null;
				ByteArrayOutputStream byte_array_stream = null;
				byte_array_stream = waitDataToData();
				if(byte_array_stream!=null){
					drawing = byte_array_stream;
				}
				//drawing = byte_array_stream;
//				try{
//					byte_array_stream = mDataQue.poll(1, TimeUnit.MILLISECONDS);
//				}catch(Exception e){
//				}
				//mDataStream = null;
				//log("byte_array_outputstream = "+byte_array_stream);



				if(drawing!=null){
					log("byte_array_stream is not null.");

					long sendst = System.currentTimeMillis();
					byte[] out_array = rncb.createRNCBHeader(drawing); //JPEGをPC画面投影用のコンテナに詰める
					cos.write(out_array);
					cos.flush();
					long sendpass = System.currentTimeMillis() - sendst;

					log("使用時間="+sendpass+"ms.");

				}else{
					log("byte_array_stream is null.");
				}

				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					log("", e);
				}
			}

		}catch(IOException e){
			e.printStackTrace();
		}

		http.releaseConnection();
	}

	private synchronized boolean offerData(ByteArrayOutputStream os){
//		if( mWaitData ==null && mData==null ){
//			mData = os;
//		}else if(mWaitData==null){
//			mWaitData = os;
//		}else{
//			return false;
//		}
		if(mData != null) return false;
		mData = os;
		return true;
	}

	private synchronized ByteArrayOutputStream waitDataToData(){
		ByteArrayOutputStream byte_array_stream = null;
//		if(mData!=null){
//			byte_array_stream = mData;
//			mData = null;
//		}else{
//			if(mWaitData!=null){
//				mData = mWaitData;
//				mWaitData = null;
//			}
//		}
//		if(mWaitData!=null){
//			mData = mWaitData;
//			mWaitData = null;
//		}
		if(mData!=null){
			byte_array_stream = mData;
			mData = null;
		}
		return byte_array_stream;
	}


	public void cancel(){
		if(mIsCanceld) return ;
		mIsCanceld = true;
		GraphicsThread.this.interrupt();
		try {
			GraphicsThread.this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	private void log(String str){
		CFUtil.Log(str);
	}
	private void log(String str, Throwable t){
		CFUtil.Log(str, t);
	}

}
