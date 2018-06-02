package com.mbd.jarmo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.mbd.jarmo.R;

import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.LinearLayout;

class TryWifiNeighbourhood extends AsyncTask<Integer, Integer, Integer> {

	private int f_total=80;
	private Smalltext f_currentText, f_currentText2;
	private LinearLayout f_egysorgomb1;

	public void onProgressUpdate (Integer... txt) {
		if (isCancelled()) {
			JarmoService.JarmoLog  ("onPRogressUpdateben, CANCEL pressed!!!");
			return;
		}

		JarmoService.JarmoLog  ("WIFI progressUpdate");
		try {
			f_currentText=((Smalltext)((LinearLayout)(JarmoSetting.menuoldal.getChildAt(0))).getChildAt(1));
			f_currentText2=((Smalltext)((LinearLayout)(JarmoSetting.menuoldal.getChildAt(0))).getChildAt(2));
			f_egysorgomb1=((LinearLayout)((LinearLayout)(JarmoSetting.menuoldal.getChildAt(0))).getChildAt(6));
			f_currentText.setText(JarmoSetting.js.getString(R.string.WifiFailed)+": "+JarmoSetting.wifiSearch_nok+" - "+JarmoSetting.js.getString(R.string.WifiSuccess)+": "+JarmoSetting.wifiSearch_ok);
		} catch (Exception e) {e.printStackTrace();}
	}

	public void onPostUpdate (Integer... res) {
		if (JarmoSetting.wifiSearch_ok==0) {
			f_currentText.setText(JarmoSetting.js.getString(R.string.NoWifiEnemyFound));
			f_egysorgomb1.removeAllViews();
			EgyMenu m1 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuAgain1), JarmoSetting.js.getString(R.string.MenuAgain2), "retry.png", 9, false);
			EgyMenu m2 = new EgyMenu (JarmoSetting.main.getContext(), JarmoSetting.js.getString(R.string.MenuBack), "", "back.png", 15, true);
			f_egysorgomb1.addView (m1, 0);
			f_egysorgomb1.addView (m2,1);
			f_egysorgomb1.setHorizontalGravity (Gravity.CENTER_HORIZONTAL);

			f_egysorgomb1.postInvalidate();
			//      menuoldal.layout (JarmoSetting.MENUWIDTH, JarmoSetting.MENUHEIGHT, JarmoSetting.MENUWIDTH*2, JarmoSetting.MENUHEIGHT*2);

			// 	 menuoldal.postInvalidate();
			//menuNyit ();
		} else {
			f_currentText2.setText(JarmoSetting.js.getString(R.string.WaitingForResp));		        		        	 
		}

	}

	@Override
	public Integer doInBackground (Integer... ip) {
		JarmoService.JarmoLog  ("WifiSearch oinbg start, ip"+ip);
		//JarmoService.mySocketListener.isStopped=1;
		int f_ip=ip[0].intValue();

		for (int t=2;t<=f_total;t++) {// max 10 szomszedos ip
			JarmoService.JarmoLog  ("BMO search, t="+t);
			int from=(f_ip >> 24 & 0xff);
	
			int tt=0;
			if (t % 2==0) tt=(int) (from-Math.floor(t/2));
			if (t % 2==1) tt=(int) (from+Math.floor (t/2));

			if (tt>255 && tt<0) continue;
		//	tt=102;
			final int f_index=tt;
			final String targetIp=(f_ip >> 0 & 0xff)+"."+(f_ip >> 8 & 0xff)+"."+(f_ip >> 16 & 0xff)+"."+tt;

			JarmoService.JarmoLog (" bmo t "+t);

			try {
				JarmoService.JarmoLog  ("trying: "+f_index);
				if (isCancelled()) {
//GDPR					JarmoService.mySocketListener.isStopped=0;
					JarmoService.JarmoLog  ("Asynctaskban, CANCEL pressed!!!");
					return 0;
				}

				Socket socket = new Socket ();
				JarmoService.JarmoLog  ("3");
				socket.setSoTimeout(2000);
			//	JarmoService.remoteWifiIP=targetIp;
			//	int res = JarmoService.sendToSocket("ARE YOU JARMO?");
			//	JarmoService.remoteWifiIP="";
			//	JarmoService.resetWifiConnectionData();

				
				socket.connect(new InetSocketAddress(targetIp, JarmoSetting.DATAPORT), 4000);
				JarmoService.JarmoLog  ("4");
				PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				JarmoService.JarmoLog  ("Kapcsolodott! "+targetIp);
				pw.write("ARE YOU JARMO? "+String.format("%03d", JarmoService.JARMOVERSION)+"\r\n");
				pw.flush();
				String inLine=null;
				while (inLine==null) {
					inLine = br.readLine(); 
					JarmoService.JarmoLog  ("null jott"); 
				}
				JarmoService.JarmoLog  ("areyoujarmo valasz="+inLine);
				if (inLine.indexOf("1 YES I AM JARMO")>-1) {
					JarmoService.JarmoLog  ("SIKER "+targetIp);
					JarmoSetting.wifiSearch_done++;
					JarmoSetting.wifiSearch_ok++;
//					JarmoService.resetWifiConnectionData();
					socket.close();
					synchronized (socket) {
						socket.wait(200);
					}
//GDPR					JarmoService.remoteWifiIP=targetIp;
//GDPR					JarmoService.socket=null;
//GDPR					JarmoService.JarmoLog  ("Sending CONNECT WIFI to" +targetIp);
//GDPR					int res=JarmoService.sendToSocket("CONNECT WIFI "+JarmoSetting.getUsername());//JarmoService.getPhoneNumber()
//GDPR					JarmoService.mySocketListener.isClientSocket=1;
//DGPR					JarmoService.JarmoLog  ("Sending result "+res);	
					try {
		//			if (!JarmoService.pinger.isAlive()) JarmoService.pinger.start ();		
					} catch (Exception e) {e.printStackTrace();}
					break;
				} else {
					JarmoSetting.wifiSearch_done++;
					JarmoSetting.wifiSearch_nok++;
				}
				
			} catch (Exception e) {
				JarmoService.JarmoLog  ("senki sem figyel."+targetIp);
				e.printStackTrace();
				JarmoSetting.wifiSearch_done++;
				JarmoSetting.wifiSearch_nok++;
			}
			JarmoService.JarmoLog  ("total"+JarmoSetting.wifiSearch_done+" okk"+JarmoSetting.wifiSearch_ok);

			publishProgress (0);



		} //for
//GDPR		JarmoService.mySocketListener.isStopped=0;
		return 1;
	}



}