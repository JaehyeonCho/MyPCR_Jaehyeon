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
		if(!HidClassLoader.LoadLibrary())		//load�ϴ� ���(���̺귯��)�� ȣ�⿡ �����������
		{
			JOptionPane.showMessageDialog(null, "This OS is not supported!");	//�޼��� ���
		}
	}
	
	
	private CallbackDeviceChange deviceChange = null;
	private HIDManager manager = null;	
	public Main()		//������ ȣ��
	{
		try
		{
			manager = HIDManager.getInstance();
		} catch (IOException e)
		{
			System.out.println("�������Ф�");
		}
		//���� callbackDeviceChange�� ��ġ�� ���� ���θ� "�ѹ���" Ȯ��
		deviceChange = CallbackDeviceChange.getInstance(manager, this);		//manager�� ���� ���Ῡ�� Ȯ��
		deviceChange.setSerialNumber("MyPCR333333");		//�ø���ѹ��� �´��� Ȯ���ϱ����� ����
		deviceChange.start();
	}
	
	private boolean statusFlag = true;
	//implements DeviceChange���� ���� ���� noti
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
	
	public static void main(String[] args)	//static�� �ڵ����� �Ҵ��.
	{
		/*
		Main m = new Main();		//��ü����
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
				System.out.println("�ùٸ� �������� ����.");
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
					
					//label�� GOTO���� Ȯ���ϴ� ���
					if(actions[i].label.equals("GOTO"))
						System.out.println("GOTO��");
					
					//���ڷ� �ٲٴ� ���
					System.out.println(Integer.parseInt(actions[i].time));
				}
				
				System.out.println(String.format("%02d:%02d", 10, 10));	//10�� 10��
				
				//Excercise
				//1. actions ������ �̿��Ͽ� ��ü �������� ������ �������� �ð��� ����Ͽ� ��:�� ���·� ����Ͻÿ� (eg. 05:05)
				//2. actions ������ �̿��Ͽ� ��ü �������� ������ ���� ������ label���� �־� ArrayList<String> list2�� �����Ͽ� ����ϼ���.(GOTO�� ���� �ʰ� label���� ���)
				//3. ���� �ֿ� ���� ������ ���� �ؿ�����.
			}
			else
			{
				System.out.println("�������� ������ �ƴ�.");
			}
		}
		catch(IOException e)
		{
			System.out.println("������ �����ϴ�.");
		}
	}
}
