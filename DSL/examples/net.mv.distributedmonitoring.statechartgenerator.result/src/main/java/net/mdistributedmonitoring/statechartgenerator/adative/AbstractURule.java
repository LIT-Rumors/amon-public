package net.mdistributedmonitoring.statechartgenerator.adative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbstractURule implements IURule {

	private List<String> conditionList = new ArrayList<>();
	private int salience;
	private String name;

	public AbstractURule(String conditions) {
		String cd = conditions.replace('[', ',');
		cd = cd.replace(']', ' ');
		String[] split = cd.split(",");
		for (String s : split) {
			if (!(s.trim().isBlank() || s.trim().isEmpty())) {
				conditionList.add(s.trim());
			}

		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getConditions() {
		return conditionList;
	}

	public int getSalience() {
		return salience;
	}

	public void setSalience(int salience) {
		this.salience = salience;
	}

	@Override
	public void setName(String name) {
		this.name = name;

	}

	@Override
	public void executeRule() {
		System.out.println("Executing rule: " + name);
		this.execute();
	}

	@Override
	public String getName() {
		return name;
	}

}
