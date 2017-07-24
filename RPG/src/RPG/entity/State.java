package RPG.entity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import RPG.component.Component;
import RPG.component.Components.CWalk;
import RPG.component.Components.CWalk.WalkType;

public class State
{
	public static HashMap < String , List<Component>> stateMap;
	
	public static void initStates()
	{
		stateMap = new HashMap < String , List<Component>>();
		
		List<Component> walkList = new LinkedList<Component>();
		walkList.add( new CWalk(WalkType.RIGHT));
		stateMap.put("Move", walkList );
	}
}
