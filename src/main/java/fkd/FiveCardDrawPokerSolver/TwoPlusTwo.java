package fkd.FiveCardDrawPokerSolver;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



/**
 * For more information about this algorithm, see
 * http://archives1.twoplustwo.com/showflat.php?Cat=0&Number=8513906&page=0&fpart=1&vc=1
 * @author Chris Oei
 */
public class TwoPlusTwo {

    public static final String HandRankDataFilename = "HandRanks.dat";
    public static final int HandRankSize = 32487834;
    public static int HR[] = new int[HandRankSize];
    
    public static String[] HandRanks = {"BAD!!", "High Card", "Pair", "Two Pair", "Three of a Kind", "Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush"};

    public static int LookupHand7(int[] cards, int offset) {
        int pCards = offset;
        int p = HR[53 + cards[pCards++]];
        p = HR[p + cards[pCards++]];
        p = HR[p + cards[pCards++]];
        p = HR[p + cards[pCards++]];
        p = HR[p + cards[pCards++]];
        p = HR[p + cards[pCards++]];
        return HR[p + cards[pCards]];
    }

    public static int lookupHand5(int[] cards) {
    	int pCards = 0;
    	int p = HR[53 + cards[pCards++]];
    	p = HR[p + cards[pCards++]];
    	p = HR[p + cards[pCards++]];
    	p = HR[p + cards[pCards++]];
    	p = HR[p + cards[pCards]];
    	p = HR[p];
    	return p;
    	}
    
    protected static final int littleEndianByteArrayToInt(byte[] b, int offset) {
        return (b[offset + 3] << 24) + ((b[offset + 2] & 0xFF) << 16)
                + ((b[offset + 1] & 0xFF) << 8) + (b[offset] & 0xFF);
    }
    
    
    // Initialize static
    static {
        int tableSize = HandRankSize * 4;
        byte[] b = new byte[tableSize];
        try {
            FileInputStream fr = new FileInputStream(HandRankDataFilename);
            int bytesRead = fr.read(b, 0, tableSize);
            if (bytesRead != tableSize) {
                System.out.println("Read " + bytesRead + " bytes out of " + tableSize);
            }
            fr.close();
        } catch (FileNotFoundException e) {
        	System.out.println(e);
        } catch (IOException e) {
        	System.out.println(e);
        }
        for (int i = 0; i < HandRankSize; i++) {
            HR[i] = littleEndianByteArrayToInt(b, i * 4);
        }
        System.out.println("TwoPlusTwo initialized.");
    }
}