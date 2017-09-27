package Spear;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Calculate {
	static String[] handIndex = new String[]{"AA","A2s","A2o","A3s","A3o","A4s","A4o","A5s","A5o","A6s","A6o","A7s","A7o","A8s","A8o","A9s","A9o","ATs","ATo","AJs","AJo","AQs","AQo","AKs","AKo","22","32s","32o","42s","42o","52s","52o","62s","62o","72s","72o","82s","82o","92s","92o","T2s","T2o","J2s","J2o","Q2s","Q2o","K2s","K2o","33","43s","43o","53s","53o","63s","63o","73s","73o","83s","83o","93s","93o","T3s","T3o","J3s","J3o","Q3s","Q3o","K3s","K3o","44","54s","54o","64s","64o","74s","74o","84s","84o","94s","94o","T4s","T4o","J4s","J4o","Q4s","Q4o","K4s","K4o","55","65s","65o","75s","75o","85s","85o","95s","95o","T5s","T5o","J5s","J5o","Q5s","Q5o","K5s","K5o","66","76s","76o","86s","86o","96s","96o","T6s","T6o","J6s","J6o","Q6s","Q6o","K6s","K6o","77","87s","87o","97s","97o","T7s","T7o","J7s","J7o","Q7s","Q7o","K7s","K7o","88","98s","98o","T8s","T8o","J8s","J8o","Q8s","Q8o","K8s","K8o","99","T9s","T9o","J9s","J9o","Q9s","Q9o","K9s","K9o","TT","JTs","JTo","QTs","QTo","KTs","KTo","JJ","QJs","QJo","KJs","KJo","QQ","KQs","KQo","KK"};
	public static String indexToHand(int i) {
		return handIndex[i];
	}
	public static Object loadFile(String f) {
		try {
            ObjectInputStream obj = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
            Object ret = obj.readObject();
            obj.close();
            return ret;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Object loadFile(File f) {
		try {
            ObjectInputStream obj = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
            Object ret = obj.readObject();
            obj.close();
            return ret;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static Object loadFile(InputStream stream) {
		try {
            ObjectInputStream obj = new ObjectInputStream(new BufferedInputStream(stream));
            Object ret = obj.readObject();
            obj.close();
            return ret;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void saveFile(File f, Object o) {
		try {
		
			ObjectOutputStream obj = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f),2097152/16));
			obj.writeObject(o);
			obj.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
