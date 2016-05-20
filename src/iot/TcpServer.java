/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author darwin
 */
public class TcpServer {
    final static String DISCONNECT = "DISCONNECTED";
    //setting the ServerSocket as t
    private ServerSocket serverSocket;
    private Socket clientSocket;
    /**
     * Difference between isConnected and initialized is isConnected only occur
     * to be used when the server is validated. initialized is true when the
     * initialization if constructor is done. initialized is false when there is
     * not initialization of the server is done.
     */
    private boolean initialised = false;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private final int portNumber;

    /**
     * Constructor for TcpServer require a port number which is in the range of
     * 1000 till 9999.
     * @param portNumber 
     */
    public TcpServer(int portNumber) {
        this.portNumber = portNumber;
            if (portNumberManager(portNumber)) {

                try {
                    serverSocket = new ServerSocket(portNumber);
                    try {
                        System.out.println("Waiting For Client in "
                                + getIPAddress() +" port : " + portNumber);

                        clientSocket = serverSocket.accept();

                        //Contructor is activated
                        initialised = true;
                    } catch (IOException e) {
                        Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, e);
                        System.out.println("Denied connection "
                                + "from a client");
                    }

                } catch (IOException e) {
                     Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, e);
                }

            } else {
                System.out.println("Error in Port number");

            }
        
    }
    
    public boolean isServerInitalized(){
        return initialised;
    }

    private void initClient() {
        if (!serverSocket.isClosed()) {
            if (clientSocket.isClosed()) {
                try {
                    clientSocket = serverSocket.accept();
                } catch (IOException ex) {
                    Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }System.out.println("A Client is connected");
        }
    }

    
    /**
     * NetWorkInterface is used here so it gets a Enumeration name number
     * example eth0 fe80:0:0:0:5d40:9c60:d4a5:8712%eth0 wlan0 192.168.1.105
     *
     * @return
     */
    public String getIPAddress() {

        String IPAddress = null;
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface
                    .getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (nic.getName().contains("wlan")) {
                        IPAddress = addr.getHostAddress();
                    }

                }
            }
        } catch (SocketException e) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, e);
        }
        return IPAddress;
    }

    /**
     * This check if the constructor is giving a valid port number
     *
     * @param port
     * @return
     */
    private boolean portNumberManager(int port) {
        boolean succes;

        //Check if the interger send is consist of 4 digit number
        if (port >= 1000 && port <= 9999) {
            succes = true;
        } else {
            System.out.println();
            System.out.println("Port Number should be consist of 4 digit number only");
            System.out.println();
            succes = false;
        }

        return succes;
    }

    /**
     * Data Send by client to server is Read here.
     *
     * @return String is return from the BufferReader.
     */
    public String readClient() {
        String dataReadable = " ";
        if (!serverSocket.isClosed()) {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));// in is a java.io.BufferReader Type
                dataReadable = in.readLine();

                /**
                 * This part enable the client to disconnect from the server the
                 * server gets the message from the client as "close",
                 *
                 */
                if (dataReadable.equals("close")) {
                    clientSocket.close();
                    return DISCONNECT;
                }

            } catch (IOException e) {
                 Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("BufferedReader getting error");
            }
        } else {
            System.out.println("Server socket is closed");
        }
        return dataReadable;
    }

    /**
     * Data is Sent to client from server
     *
     * @param data Use String type .
     */
    public void writeClient(String data) {
        if (!clientSocket.isClosed()) {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(data);
            } catch (IOException e) {
                 Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, e);
                System.out.println(" PrintWriter giving error");
            }
        } else {
            System.out.println("Client is not present");
        }
    }

    /**
     * checks if any client is connected to the server and return false if there
     * is problem while Constructing.
     *
     * @return
     */
    public boolean isClientConnected() {
        boolean isClientConnected;
        if (initialised) {
            isClientConnected = !clientSocket.isClosed();
        } else {
            isClientConnected = false;
        }
        return isClientConnected;
    }
    
    public int getPortNumber(){
        return portNumber;
    }

    public void waitClient() {
        System.out.println("Server is waiting for Client in " + getIPAddress() + " port number : " + getPortNumber() );
        initClient();
        
    }

    public void ShutDown() throws IOException {
        if (!clientSocket.isClosed()) {
            clientSocket.close();
            System.out.println("client is removed from the server");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server is Closing");
            serverSocket.close();
        }
    }
}
