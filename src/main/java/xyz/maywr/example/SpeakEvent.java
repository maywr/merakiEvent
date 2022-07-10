package xyz.maywr.example;

import xyz.maywr.meraki.event.Event;

/**
 * @author maywr
 * 10.07.2022 19:04
 */
public class SpeakEvent extends Event
{
	private final String text;

	public SpeakEvent( String text )
	{
		this.text = text;
	}

	public String getText()
	{
		return text;
	}
}
