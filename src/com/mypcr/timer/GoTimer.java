package com.mypcr.timer;

import java.io.IOException;
import java.util.TimerTask;

import com.codeminders.hidapi.HIDDevice;
import com.mypcr.beans.Action;
import com.mypcr.beans.RxAction;
import com.mypcr.beans.TxAction;
import com.mypcr.handler.Handler;
import com.mypcr.ui.MainUI;
import com.mypcr.ui.ProgressDialog;

public class GoTimer extends TimerTask
{
	public static final int TIMER_DURATION	=	50;
	public static final int TIMER_NUMBER	=	0x01;
	
	private HIDDevice 		m_Device = null;
	private MainUI 	  		m_Handler	= null;
	private TxAction		m_TxAction	= null;
	private RxAction		m_RxAction 	= null;
	private Action[]		m_Actions = null;
	private String			m_preheat = null;
	private int				m_index = 0;
	private int				m_protocol_length = 0;
	ProgressDialog 			m_dialog = null;
	
	public GoTimer(HIDDevice device, Action[] actions, String preheat, MainUI handler)
	{
		m_Device = device;
		m_TxAction = new TxAction();
		m_RxAction = new RxAction();
		m_Handler = handler;
		m_preheat = preheat;
		m_Actions = actions; 
		m_protocol_length = m_Actions.length;
		m_dialog = new ProgressDialog(m_Handler, "PCR Protocol Transmitting...", m_protocol_length);
		Thread TempThread = new Thread()
		{
			public void run()
			{
				m_dialog.setModal(true);
				m_dialog.setVisible(true);
			}
		};
		TempThread.start();
	}

	byte[] readBuffer = new byte[65];
	
	@Override
	public void run() 
	{
		m_dialog.setProgressValue(m_index);
		if( m_index < m_protocol_length )
		{
			try
			{
				//read하고 비교하고 맞으면 index증가하고 아니면 write
				m_Device.read(readBuffer);
				m_RxAction.set_Info(readBuffer);
				
				String time = (int)((m_RxAction.getTime_H()*256. + m_RxAction.getTime_L())) + "";
				System.out.println(m_Actions[m_index].getLabel() + "," + m_RxAction.getLabel());		//GOTO
				
				if(m_Actions[m_index].getLabel().equals(m_RxAction.getLabel()+"")
						&& m_Actions[m_index].getTemp().equals(m_RxAction.getTemp()+"")
						&& m_Actions[m_index].getTime().equals(time)
						&& m_index == m_RxAction.getReqLine())
				{
					m_index++;
				}
				else
					m_Device.write( m_TxAction.Tx_TaskWrite(m_Actions[m_index].getLabel(), m_Actions[m_index].getTemp(), m_Actions[m_index].getTime(), m_preheat, m_index) );
				
				
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				m_Device.write( m_TxAction.Tx_TaskEnd() );
				try
				{
					Thread.sleep(300);
				}catch(InterruptedException e)
				{
					e.printStackTrace();
				}
				m_Device.write( m_TxAction.Tx_Go() );
				m_Handler.OnHandleMessage(Handler.MESSAGE_TASK_WRITE_END, null);
				this.cancel();
				Thread TempThread = new Thread()
				{
					public void run()
					{
						try
						{
							Thread.sleep(1000);
						}catch(InterruptedException e)
						{
							e.printStackTrace();
						}
						m_dialog.setVisible(false);
					}
				};
				TempThread.start();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
