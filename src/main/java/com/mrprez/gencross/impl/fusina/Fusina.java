package com.mrprez.gencross.impl.fusina;

import java.util.HashMap;
import java.util.Map;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.history.ConstantHistoryFactory;
import com.mrprez.gencross.history.HistoryItem;
import com.mrprez.gencross.listener.custom.CustomAfterChangeValueListener;
import com.mrprez.gencross.value.IntValue;
import com.mrprez.gencross.value.StringValue;
import com.mrprez.gencross.value.Value;

public class Fusina extends Personnage {

	public void calculate(){
		super.calculate();
		if(phase.equals("Création")){
			int nbFaiblesses = getProperty("Faiblesses").getSubProperties().size();
			if(nbFaiblesses<1 || nbFaiblesses>2){
				errors.add("Vous devez avoir 1 ou 2 faiblesses");
			}
			
		}
		
		
	}
	
	public void endHeroismePhase(){
		getProperty("Héroïsme").setEditable(false);
		int heroisme = getProperty("Héroïsme").getValue().getInt();
		pointPools.get("Traits").add(heroisme);
		pointPools.get("Traits").setToEmpty(true);
		pointPools.get("Compétences").setToEmpty(true);
	}
	
	public void endCreationPhase() throws SecurityException, NoSuchMethodException{
		Property defaultTrait = getProperty("Traits").getSubProperties().getDefaultProperty();
		defaultTrait.getOptions().add(new StringValue(""));
		defaultTrait.setValue(new StringValue(""));
		getProperty("Traits").setHistoryFactory(new ConstantHistoryFactory("Evolution", 5));
		addAfterChangeValueListener(new CustomAfterChangeValueListener(this, "changeTraitLink", "Traits#[^#]*"));
		
		getProperty("Faiblesses").setHistoryFactory(new ConstantHistoryFactory("Evolution", -10));
		getProperty("Compétences").getHistoryFactory().setPointPool("Evolution");
	}
	
	public void changeTraitLink(Property property, Value oldValue){
		if(oldValue.getString().isEmpty() && !property.getValue().getString().isEmpty()){
			HistoryItem historyItem = history.get(history.size()-1);
			historyItem.setCost(5);
			pointPools.get("Evolution").spend(historyItem.getCost());
			property.getOptions().remove(new StringValue(""));
		}
	}
	
	public void changeTrait(Property property, Value oldValue){
		calculateTrait();
	}
	
	public void addTrait(Property competence){
		calculateTrait();
	}
	
	private void calculateTrait(){
		Map<String, Integer> caracMap = new HashMap<String, Integer>();
		for(Property trait : getProperty("Traits").getSubProperties()){
			String name = trait.getValue().getString();
			if(!caracMap.containsKey(name)){
				caracMap.put(name, 2);
			}
			caracMap.put(name, caracMap.get(name)+1);
		}
		for(Property carac : getProperty("Caractéristiques").getSubProperties()){
			if(caracMap.containsKey(carac.getName())){
				carac.setValue(new IntValue(caracMap.get(carac.getName())));
			}else{
				carac.setValue(new IntValue(2));
			}
		}
	}

}
