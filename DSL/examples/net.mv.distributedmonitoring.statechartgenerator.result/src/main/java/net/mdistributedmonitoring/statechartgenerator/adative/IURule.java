package net.mdistributedmonitoring.statechartgenerator.adative;

import java.util.List;

public interface IURule {

	public void execute();

	List<String> getConditions();

	public void setSalience(int i);

	public void setName(String string); 

	public int getSalience();

	public void executeRule();

	public String getName();

}
