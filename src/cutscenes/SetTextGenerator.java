package cutscenes;

import json.JSONObject;

public class SetTextGenerator extends ParamTextGenerator {

	private String text;
	
	@Override
	public String getText () {
		return text;
	}
	
	/**
	 * Sets the text for this SetTextGenerator
	 * @param t
	 */
	public void setText (String t) {
		text = t;
	}

}
