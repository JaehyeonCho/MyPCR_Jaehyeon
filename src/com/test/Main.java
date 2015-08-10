package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;
import com.hidapi.CallbackDeviceChange;
import com.hidapi.DeviceChange;
import com.hidapi.HidClassLoader;
import com.mypcr.beans.RxAction;
import com.mypcr.beans.State;
import com.mypcr.beans.TxAction;
import com.mypcr.ui.MainUI;

public class Main
{
	static
	{
		HidClassLoader.LoadLibrary();
	}
	/*
	static
	{
		if(!HidClassLoader.LoadLibrary())		//load하는 기능(라이브러리)을 호출에 실패했을경우
		{
			JOptionPane.showMessageDialog(null, "This OS is not supported!");	//메세지 출력
		}
	}
	
	
	private CallbackDeviceChange deviceChange = null;
	private HIDManager manager = null;
	private HIDDevice device;
	private String SerialNumber; 	//시리얼 변수 
	
	private RxAction rx = new RxAction();
	private TxAction tx = new TxAction();
	
	public Main()		//생성자 호출
	{
		try
		{
			manager = HIDManager.getInstance();
		} catch (IOException e)
		{
			System.out.println("에러남ㅠㅠ");
		}
		//먼저 callbackDeviceChange로 장치의 연결 여부를 "한번만" 확인
		deviceChange = CallbackDeviceChange.getInstance(manager, this);		//manager를 통해 연결여부 확인
		deviceChange.setSerialNumber("MyPCR333333");		//시리얼넘버가 맞는지 확인하기위해 설정
		deviceChange.start();
	}
	
	private void connectToDevice(int firmwareVersion)
	{
		try
		{
			if( device != null )		//이미 연결이 있는 장치가 있는 경우 연결을 끊음
			{
				device.close();
				device = null;
			}
			
			device = manager.openById(0x04D8, 0xFB76, null);		//디바이스 새로 오픈
			if( device != null )		//연결됨
			{
				device.disableBlocking();		//blocking: input할때까지 기다리는것.
				SerialNumber = device.getSerialNumberString();
				
				//자바는 write할때 65byte. 0번지에 0이 들어감. read할 떄에는 64byte
				//byte[] senddata = new byte[65];
				byte[] readdata = new byte[64];
				
				device.write(tx.Tx_NOP());
				
				try
				{
					Thread.sleep(100);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
				
				//byte(unsigned char): 8bit
				//자바에서는 unsigned가 없는데 저쪽은 unsigned가 있음.
				
				if( device.read(readdata) != 0 )
				{
					rx.set_Info(readdata);
					System.out.println("chamber temp: " + rx.getChamber_TempH() + "." + rx.getChamber_TempL());
					
					switch(rx.getState())
					{
					case 1:
						System.out.println("READY");
						break;
					case 2:
						System.out.println("RUN");
						break;
					case 3:
						System.out.println("PCREND");
						break;
					case 4:
						System.out.println("STOP");
						break;
					case 5:
						System.out.println("TASK_WRITE");
						break;
					case 6:
						System.out.println("TASK_READ");
						break;
					case 7:
						System.out.println("ERROR");
						break;
					case 8:
						System.out.println("REFRIGERATION");
						break;
					}
				}
				
			}
			else
			{
				//연결 에러 처리
				System.out.println("Fetal Error!");
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//implements DeviceChange에서 통지 받을 noti
	//인식 된 것이 여기로 옴.
	public void OnMessage(int MessageType, Object data, int firmwareVersion)
	{
		String count = (String)data;
		
		switch(MessageType)
		{
		case CONNECTED:
			if(count.equals("1"))
			{
				System.out.println("connected");
				connectToDevice(firmwareVersion);
			}
			break;
		case DISCONNECTED:
				System.out.println("disconnected");
			break;
		}
	}
	*/
	
	public static void main(String[] args)	//static은 자동으로 할당됨.
	{
		
		//Main m = new Main();		//객체생성
		//while(true);
		String selectNumber = "MyPCR333333";
		
		MainUI main = new MainUI();
		main.setSerialNumber(selectNumber);
		main.Run();
		
		/*
		Action[] actions = new Action[20];
		int lines = 0;
		int start = 0;
		String path = "test.txt";
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new FileReader(path));
			
			String line = null;
			ArrayList<String> list = new ArrayList<>();
			ArrayList<String> list2 = new ArrayList<>();	//프로토콜 순서 저장 list
			
			while( (line = in.readLine()) != null )
			{
				//System.out.println(line);
				list.add(line);
			}
			in.close();
			
			
			String first = list.get(0);
			String last = list.get(list.size()-1);
			if(first.contains("%PCR%") && last.contains("%END%"))
			{
				System.out.println("올바른 프로토콜 파일.");
				for(int i=1; i<=list.size()-2; i++)
				{
					String[] datas = list.get(i).split("\t");
					actions[lines] = new Action(datas[0], datas[1], datas[2]);
					lines++;
				}
				int time = 0;
				for(int i=0; i<lines; i++)
				{
					if(actions[i].label.equals("GOTO"))		//label이 GOTO일 경우에
					{
						String goto_label = actions[i].temp;		//몇번째 레이블인지 변수에 넣어줌.
						int count = Integer.parseInt(actions[i].time);	//몇번 돌건지 값(20)을 가져오고 integer로 변환.
						for(int k=0; k<i; k++)		//label 처음부터 goto index까지 증가시킬동안
						{
							if((actions[k].label).equals(goto_label))	//goto_label(2)와 label이 같으면
							{
								start = k;		//그 index의 값을 저장
								break;		//for문 나가기
							}
						}
						for(int j=0; j<count; j++)		//20번 돌림. (2,3,4)x20
						{
							for(int c=start; c<i; c++)
							{
								list2.add(actions[c].label);
								time += Integer.parseInt(actions[c].time);	//time
							}
						}
					}
					else
					{
						list2.add(actions[i].label);	//list2에 label을 차곡차곡 넣는다.
						time += Integer.parseInt(actions[i].time);	//time
					}
				}
				
				for(int i=0; i<lines; i++)
				{
					System.out.println(String.format("label: %s,	temp: %s,	time: %s",
							actions[i].label, actions[i].temp, actions[i].time));
					
					//label이 GOTO인지 확인하는 방법
					//if(actions[i].label.equals("GOTO"))
					//{
					//	System.out.println("GOTO임");					//}
					
					//숫자로 바꾸는 방법
					//System.out.println(Integer.parseInt(actions[i].time));
				}
				
				//System.out.println(String.format("%02d:%02d", 10, 10));	//10분 10초
				
				//Excercise
				//1. actions 변수를 이용하여 전체 프로토콜 파일의 프로토콜 시간을 계산하여 분:초 형태로 출력하시오 (eg. 05:05)
				//2. actions 변수를 이용하여 전체 프로토콜 파일의 실행 순서를 label값을 넣어 ArrayList<String> list2에 저장하여 출력하세요.(GOTO는 넣지 않고 label값만 출력)
				//3. 지난 주에 못한 숙제를 마저 해오세요.
				for(int i=0; i<list2.size(); i++)
				{
					System.out.print(list2.get(i) + " ");
				}
				//- time ---------------------------
				//System.out.println("\nTime: "+time);
				System.out.println(String.format("\n%02d:%02d", time/60, time%60));
			}
			else
			{
				System.out.println("프로토콜 파일이 아님.");
			}
			in.close();
		}
		catch(IOException e)
		{
			System.out.println("파일이 없습니다.");
		}
		*/
	}
	
}
