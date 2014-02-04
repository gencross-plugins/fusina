package com.mrprez.gencross.impl.fusina;

import com.mrprez.gencross.Property;
import com.mrprez.gencross.renderer.Renderer;
import com.mrprez.gencross.value.Value;

public class FusinaRenderer extends Renderer {

	@Override
	public String displayValue(Property property) {
		if(property.getValue()==null){
			return "";
		}
		return displayValue(property.getValue());
	}

	@Override
	public String displayValue(Value value) {
		int v = value.getInt();
		if(v<=6){
			return "d"+(v*2);
		}else{
			return "d12+"+(v-6);
		}
	}
	
	

}
