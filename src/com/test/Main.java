package com.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.codeminders.hidapi.HIDManager;
import com.hidapi.CallbackDeviceChange;
import com.hidapi.DeviceChange;
import com.hidapi.HidClassLoader;

public class Main implements DeviceChange
{
	static
	{
		if(!HidClassLoader.LoadLibrary())		//load하는 기능(라이브러리)을 호출에 실패했을경우
		{
			JOptionPane.showMessageDialog(null, "This OS is not supported!");	//메세지 출력
		}
	}
	
	
	private CallbackDeviceChange deviceChange = null;
	private HIDManager manager = null;	
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
	
	private boolean statusFlag = true;
	//implements DeviceChange에서 통지 받을 noti
	public void OnMessage(int MessageType, Object data, int firmwareVersion)
	{
		String count = (String)data;
		
		switch(MessageType)
		{
		case CONNECTED:
			if(count.equals("1"))
			{
				System.out.println("connected");
				statusFlag = true;
			}
			break;
		case DISCONNECTED:
			if(statusFlag)
			{
				System.out.println("disconnected");
				statusFlag = !statusFlag;
			}
			break;
		}
	}
	
	public static void main(String[] args)	//static은 자동으로 할당됨.
	{
		/*
		Main m = new Main();		//객체생성
		while(true);
		*/
		Action[] actions = new Action[20];
		int lines = 0;
		
		String path = "test.txt";
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new FileReader(path));
			
			String line = null;
			ArrayList<String> list = new ArrayList<>();
			
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
				
				for(int i=0; i<lines; i++)
				{
					System.out.println(String.format("label: %s, temp: %s, time: %s",
							actions[i].label, actions[i].temp, actions[i].time));
					
					//label이 GOTO인지 확인하는 방법
					if(actions[i].label.equals("GOTO"))
						System.out.println("GOTO임");
					
					//숫자로 바꾸는 방법
					System.out.println(Integer.parseInt(actions[i].time));
				}
				
				System.out.println(String.format("%02d:%02d", 10, 10));	//10분 10초
				
				//Excercise
				//1. actions 변수를 이용하여 전체 프로토콜 파일의 프로토콜 시간을 계산하여 분:초 형태로 출력하시오 (eg. 05:05)
				//2. actions 변수를 이용하여 전체 프로토콜 파일의 실행 순서를 label값을 넣어 ArrayList<String> list2에 저장하여 출력하세요.(GOTO는 넣지 않고 label값만 출력)
				//3. 지난 주에 못한 숙제를 마저 해오세요.
			}
			else
			{
				System.out.println("프로토콜 파일이 아님.");
			}
		}
		catch(IOException e)
		{
			System.out.println("파일이 없습니다.");
		}
	}
}
