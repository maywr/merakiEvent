package xyz.maywr.meraki.event;

/**
 * @author maywr
 * 29.06.2022 12:32
 */
public class Event
{
	private boolean canceled = false;

	/**
	 * basically allows you to post event in one line
	 * {@code Example: <code>new SomeEvent( param1, param2 ).post( EVENT_BUS );</code>}
	 *
	 * @param eventBus event bus event should be posted to
	 */
	public void post( IEventBus eventBus )
	{
		eventBus.post( this );
	}

	public boolean isCancelled()
	{
		return canceled;
	}

	public void setCancelled( boolean cancel )
	{
		if ( !this.isCancelable() )
			throw new BusException( "tried to cancel a not cancelable event" );
		this.canceled = cancel;
	}

	public void cancel()
	{
		this.setCancelled( true );
	}

	protected boolean isCancelable()
	{
		return true; //by default
	}
}
