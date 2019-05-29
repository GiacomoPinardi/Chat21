package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import dominioPacchetto.Pacchetto;


public class ThreadServer implements Runnable{
	 
	 private ObjectInputStream inStream;
	 private Observer observer;

	 public ThreadServer(Observer observer, ObjectInputStream inStream){
	 	this.inStream=inStream;
	 	this.observer=observer;
	 }

	 public void run() {
	 	while(true){
	 		try{
	 			spacchetta((Pacchetto)inStream.readObject());
	 		}
	 		catch( IOException e){

	 		}
	 		catch(ClassNotFoundException e2){

	 		}
	 	}
	 }

	 public void spacchetta(Pacchetto pacchetto){

	 }
}