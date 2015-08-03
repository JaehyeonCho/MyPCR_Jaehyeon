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
		if(!HidClassLoader.LoadLibrary())		//load�ϴ� ���(���̺귯��)�� ȣ�⿡ �����������
		{
			JOptionPane.showMessageDialog(null, "This OS is not supported!");	//�޼��� ���
		}
	}
	
	
	private CallbackDeviceChange deviceChange = null;
	private HIDManager manager = null;
	private HIDDevice device;
	private String SerialNumber; 	//�ø��� ���� 
	
	private RxAction rx = new RxAction();
	private TxAction tx = new TxAction();
	
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
	
	private void connectToDevice(int firmwareVersion)
	{
		try
		{
			if( device != null )		//�̹� ������ �ִ� ��ġ�� �ִ� ��� ������ ����
			{
				device.close();
				device = null;
			}
			
			device = manager.openById(0x04D8, 0xFB76, null);		//����̽� ���� ����
			if( device != null )		//�����
			{
				device.disableBlocking();		//blocking: input�Ҷ����� ��ٸ��°�.
				SerialNumber = device.getSerialNumberString();
				
				//�ڹٴ� write�Ҷ� 65byte. 0������ 0�� ��. read�� ������ 64byte
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
				//�ڹٿ����� unsigned�� ���µ� ������ unsigned�� ����.
				
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
				//���� ���� ó��
				System.out.println("Fetal Error!");
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//implements DeviceChange���� ���� ���� noti
	//�ν� �� ���� ����� ��.
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
	
	public static void main(String[] args)	//static�� �ڵ����� �Ҵ��.
	{
		
		//Main m = new Main();		//��ü����
		//while(true);
		
		MainUI main = new MainUI();
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
			ArrayList<String> list2 = new ArrayList<>();	//�������� ���� ���� list
			
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
				int time = 0;
				for(int i=0; i<lines; i++)
				{
					if(actions[i].label.equals("GOTO"))		//label�� GOTO�� ��쿡
					{
						String goto_label = actions[i].temp;		//���° ���̺����� ������ �־���.
						int count = Integer.parseInt(actions[i].time);	//��� ������ ��(20)�� �������� integer�� ��ȯ.
						for(int k=0; k<i; k++)		//label ó������ goto index���� ������ų����
						{
							if((actions[k].label).equals(goto_label))	//goto_label(2)�� label�� ������
							{
								start = k;		//�� index�� ���� ����
								break;		//for�� ������
							}
						}
						for(int j=0; j<count; j++)		//20�� ����. (2,3,4)x20
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
						list2.add(actions[i].label);	//list2�� label�� �������� �ִ´�.
						time += Integer.parseInt(actions[i].time);	//time
					}
				}
				
				for(int i=0; i<lines; i++)
				{
					System.out.println(String.format("label: %s,	temp: %s,	time: %s",
							actions[i].label, actions[i].temp, actions[i].time));
					
					//label�� GOTO���� Ȯ���ϴ� ���
					//if(actions[i].label.equals("GOTO"))
					//{
					//	System.out.println("GOTO��");					//}
					
					//���ڷ� �ٲٴ� ���
					//System.out.println(Integer.parseInt(actions[i].time));
				}
				
				//System.out.println(String.format("%02d:%02d", 10, 10));	//10�� 10��
				
				//Excercise
				//1. actions ������ �̿��Ͽ� ��ü �������� ������ �������� �ð��� ����Ͽ� ��:�� ���·� ����Ͻÿ� (eg. 05:05)
				//2. actions ������ �̿��Ͽ� ��ü �������� ������ ���� ������ label���� �־� ArrayList<String> list2�� �����Ͽ� ����ϼ���.(GOTO�� ���� �ʰ� label���� ���)
				//3. ���� �ֿ� ���� ������ ���� �ؿ�����.
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
				System.out.println("�������� ������ �ƴ�.");
			}
			in.close();
		}
		catch(IOException e)
		{
			System.out.println("������ �����ϴ�.");
		}
		*/
	}
	
}
