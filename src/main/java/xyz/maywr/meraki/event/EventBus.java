package xyz.maywr.meraki.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maywr
 * 28.06.2022 22:07
 * a bus to post and handle events through
 */
public class EventBus implements IEventBus
{
	private final List< Object > subscribed = new ArrayList<>();

	/**
	 * subscribes an object to the EventBus so its handler methods
	 * will be executed on posting event it accepts as a parameter
	 *
	 * @param o object to be subscribed
	 */
	@Override
	public void subscribe( Object o )
	{
		//skip classes which does not have any methods
		for ( Method m : o.getClass().getMethods() )
		{
			if ( m.isAnnotationPresent( Handle.class ) )
			{
				//yup exceptions actually tells you what it does here
				if ( m.getParameters().length != 1 )
					throw new BusException( "the " + m + " method has @Handle annotation but parameters length is not equal 1" );
				if ( EventBus.isEvent( m.getParameters()[ 0 ].getType() ) )
					throw new BusException( "the " + m + " method has @Handle annotation but " + m.getParameters()[ 0 ]
							.getType().toString() + " is not event" );
			}
		}

		subscribed.add( o );
	}

	/**
	 * unsubscribes an object out of the EventBus so its handler methods will not be executed on post later
	 *
	 * @param o object to be unsubscribed
	 */
	@Override
	public void unsubscribe( Object o )
	{
		if ( !subscribed.contains( o ) )
			throw new BusException( "there is no such object subscribed" + o.toString() );
		subscribed.remove( o );
	}

	/**
	 * post an event so all handler methods with this type of event will be executed
	 *
	 * @param event event
	 */
	@Override
	public void post( Event event )
	{
		for ( Object ob : subscribed )
		{
			for ( Method m : ob.getClass().getMethods() )
			{
				if ( m.isAnnotationPresent( Handle.class ) && m.getParameters()[ 0 ].getType() == event.getClass() )
				{
					try
					{
						m.invoke( ob, event );
					} catch ( Exception e )
					{
						//actually should never happen but /shrug
						throw new BusException( e.getMessage() );
					}
				}
			}
		}
	}

	/**
	 * unsubscribes ALL subscribed objects in the EventBus
	 */
	@Override
	public void clear()
	{
		subscribed.clear();
	}

	/**
	 * checks if second superclass of a class given is xyz.maywr.meraki.event.Event class and not something other
	 *
	 * @param clazz class to check
	 * @return read up
	 */
	/*package-private*/ static boolean isEvent( Class< ? > clazz )
	{
		Class< ? > cl = clazz;
		while ( cl != Object.class )
		{
			if ( cl.getSuperclass() == Object.class )
			{
				break;
			}
			cl = cl.getSuperclass();
		}
		return cl != Event.class;
	}
}
