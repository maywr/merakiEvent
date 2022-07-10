package xyz.maywr.meraki.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author maywr
 * 10.07.2022 15:51
 * caches all methods that should be invoked at post method unlike basic EventBus
 */
public class CachedEventBus implements IEventBus
{
	/**
	 * map to contain: <br>
	 * subscribed object -> its methods with event type
	 */
	private final HashMap< Object, HashMap< Method, Class< ? extends Event > > > eventsCache = new HashMap<>();


	/**
	 * subscribes an object to the EventBus so its handler methods
	 * will be executed on posting event it accepts as a parameter
	 *
	 * @param o object to be subscribed
	 */
	@Override @SuppressWarnings( "unchecked" )
	public void subscribe( Object o )
	{
		HashMap< Method, Class< ? extends Event > > cache = new HashMap<>();
		int methodCount = 0;
		//skip classes which does not have any methods
		if ( o.getClass().getMethods().length == 0 )
			throw new BusException( "no methods in subscribing class" );
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
				cache.put( m, ( Class< ? extends Event > ) m.getParameters()[ 0 ].getType() );
				++methodCount;
			}
		}
		if ( methodCount == 0 )
		{
			throw new BusException( "the object to subscribe class should contain at least one handler method" );
		}
		eventsCache.put( o, cache );
	}

	/**
	 * unsubscribes an object out of the EventBus so its handler methods will not be executed on post later
	 *
	 * @param o object to be unsubscribed
	 */
	@Override
	public void unsubscribe( Object o )
	{
		if ( !eventsCache.containsKey( o ) )
			throw new BusException( "there is no such object subscribed" + o.toString() );
		eventsCache.remove( o );
	}

	/**
	 * post an event so all handler methods with this type of event will be executed
	 *
	 * @param event event
	 */
	@Override
	public void post( Event event )
	{
		for ( Map.Entry< Object, HashMap< Method, Class< ? extends Event > > > entry : eventsCache.entrySet() )
		{
			for ( Map.Entry< Method, Class< ? extends Event > > methodEntry : entry.getValue().entrySet() )
			{
				if ( methodEntry.getValue().equals( event.getClass() ) )
				{
					try
					{
						methodEntry.getKey().invoke( entry.getKey(), event );
					} catch ( Exception e )
					{
						throw new BusException( String.format( "method %s has thrown exception during its invoking: %s %s", methodEntry
								.getKey().toGenericString(), e, e.getMessage() ) );
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
		eventsCache.clear();
	}
}
