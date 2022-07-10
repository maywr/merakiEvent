package xyz.maywr.meraki.event;

/**
 * @author maywr
 * 10.07.2022 16:07
 */
public interface IEventBus
{
	/**
	 * subscribes an object to the EventBus so its handler methods
	 * will be executed on posting event it accepts as a parameter
	 *
	 * @param o object to be subscribed
	 */
	void subscribe( Object o );

	/**
	 * unsubscribes an object out of the EventBus so its handler methods will not be executed on post later
	 *
	 * @param o object to be unsubscribed
	 */
	void unsubscribe( Object o );

	/**
	 * post an event so all handler methods with this type of event will be executed
	 *
	 * @param event event
	 */
	void post( Event event );

	/**
	 * unsubscribes ALL subscribed objects in the EventBus
	 */
	void clear();

}
