package com.android.dxbprotocol.utility;

import java.io.Serializable;
import java.util.ArrayList;

public class EventDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<String> arrImages = new ArrayList<String>();
	public String eventTitleE;
	public String eventTitleA;
	public String eventDate;
	public String eventDay;
	public String eventTime;
	public String eventLocationE;
	public String eventLocationA;
	public String eventDressCodeE;
	public String eventDressCodeA;
	public String eventNoteE;
	public String eventNoteA;
	public String eventLongitude;
	public String eventLatitude;
	public String eventResponse;
}
