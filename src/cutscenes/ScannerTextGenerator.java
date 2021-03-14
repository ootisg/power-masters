package cutscenes;

public class ScannerTextGenerator extends RandomTextGenerator {

	public static int DEFAULT_REVEAL_TIME = 33;
	public static int DEFAULT_DURATION = 1000;
	
	@Override
	public String getText () {
		//Get random string
		String randomText = super.getText ();
		//Calculate reveal timing
		Integer len = getParams ().getInt ("length");
		if (len == null) {
			len = DEFAULT_LENGTH;
		}
		Integer rt = getParams ().getInt ("revealTime");
		if (rt == null) {
			rt = DEFAULT_REVEAL_TIME;
		}
		Integer randomTime = getParams ().getInt ("randomTime");
		if (randomTime == null) {
			randomTime = DEFAULT_DURATION;
		}
		String text = getParams ().getString ("revealText");
		int revLen = rt * randomText.length ();
		int revStart = randomTime - revLen;
		long currTime = System.currentTimeMillis () - getInitialTime ();
		int chars = 0;
		String out = "";
		//System.out.println (text);
		//System.out.println (randomText);
		System.out.println (currTime + ", " + revStart);
		while (text.length () < randomText.length ()) {
			text += " ";
		}
		if (currTime > randomTime) {
			chars = text.length ();
		} else if (currTime >= revStart) {
			chars = randomText.length () - ((int)(randomTime - currTime)) / rt;
		}
		System.out.println(chars);
		int startChar = text.length () - chars;
		for (int i = 0; i < randomText.length (); i++) {
			if (i < startChar) {
				out += randomText.charAt (i);
			} else {
				out += text.charAt (i);
			}
		}
		return out;
	}
	
}
