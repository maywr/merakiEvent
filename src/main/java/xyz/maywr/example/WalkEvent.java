package xyz.maywr.example;

import xyz.maywr.meraki.event.Event;

/**
 * @author maywr
 * 10.07.2022 18:57
 */
public class WalkEvent extends Event
{
	private final int distance;

	public WalkEvent( int distance )
	{
		this.distance = distance;
	}

	public int getDistance()
	{
		return distance;
	}

	@Override
	protected boolean isCancelable()
	{
		return false; // <- this is true by default
	}
}
