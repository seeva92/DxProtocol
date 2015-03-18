package com.android.dxbprotocol.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class AllEvents implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<Event> arrUpcomingEvents;
	public ArrayList<Event> arrPastEvents;
	public ArrayList<Event> arrSearchPastEvents;
	public ArrayList<Event> arrSearchUpcomingEvents;
}
