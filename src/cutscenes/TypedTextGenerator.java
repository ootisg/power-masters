package cutscenes;

public class TypedTextGenerator extends ParamTextGenerator {

	public static final int DEFAULT_REVEAL_TIME = 300;
	
	@Override
	public String getText () {
		String fullText = getParams ().getString ("revealText");
		Integer charTime = getParams ().getInt ("revealTime");
		if (charTime == null) {
			charTime = DEFAULT_REVEAL_TIME;
		}
		int time = (int)this.getElapsedTime ();
		int numChars = time / charTime;
		if (numChars > fullText.length ()) {
			numChars = fullText.length ();
		}
		return fullText.substring (0, numChars);
	}
	
}