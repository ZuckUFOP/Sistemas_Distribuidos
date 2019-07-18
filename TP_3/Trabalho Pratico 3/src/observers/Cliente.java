package observers;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import classes_auxiliares.Data;
import classes_auxiliares.TPanel;

import java.awt.Component;

public class Cliente 
{
	private int port = 5050;
	private ServerSocket observer;
	private JFrame frame;
	//private int numSubjects=3;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cliente window = new Cliente();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Cliente() {
		initialize();		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 650, 410);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		/*JButton btnStatusView = new JButton("Status view");
		btnStatusView.setBounds(520, 335, 100, 25);
		btnStatusView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String action = e.getActionCommand();
				if (action.equals("Status view")) {
					// point(3,5,5,4);
					// point(1,3,6,1);
					// point(2,155,142,3);
					// point(4,15,50,2);
				}
			} 
		});
		frame.getContentPane().add(btnStatusView);*/

		frame.getContentPane().add(new TPanel(10, 10, 300, 150));
		frame.getContentPane().add(new TPanel(10, 171, 300, 150));
		frame.getContentPane().add(new TPanel(320, 171, 300, 150));
		frame.getContentPane().add(new TPanel(320, 10, 300, 150));
		
		try 
		{
			observer = new ServerSocket(port);
		} catch (IOException e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//for (int i=0; i < numSubjects;i++)
			new Thread()
			{
				@SuppressWarnings("unchecked")
				public void run()
				{
					//String ipCliente;
					//int porta;
					Socket cliente = null;
					ArrayList<Data> dataReceived;
					long initialTime = System.currentTimeMillis();
					while(!observer.isClosed())
					{
						try
						{
							System.out.println("esperando atualizacao do Subject: ");
							cliente = observer.accept();
							System.out.println("Observer conectado: "+cliente.getInetAddress().getHostAddress());
							ObjectInputStream input = new ObjectInputStream(cliente.getInputStream());
							try 
							{
								dataReceived= (ArrayList<Data>) input.readObject();
								System.out.println("Tempo decorrido desde a ultima "
										+ "atualizacao " + (System.currentTimeMillis()-initialTime));
								initialTime = System.currentTimeMillis();
								input.close();
								cliente.close();
								for(Data it:dataReceived) {
									Cliente.this.point(it.getFramePos(), it.getPosX(), it.getPosY(),
											it.getColor(), it.getSize());
								}
								
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) 
						{
							// TODO Auto-generated catch block
							try {
								cliente.close();
								this.run();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							e1.printStackTrace();
						}
					}
				}
			}.start();	

	}
	void point(int thread, int x, int y, int team, int size) {
		Component[] a = frame.getContentPane().getComponents();
		TPanel aux = (TPanel) a[thread];
		//System.out.println("Frame " + thread);
		for(int i = 0; i < size;i++)
		{
			if (aux.isEditavel()) {
				if (x+i<=299)
					aux.createPoint((x+i), y, team);
				else
					aux.createPoint(0 + (300 - (x+i)), y, team);
			}
			else {
				i--;
			}
		}
	}
}
