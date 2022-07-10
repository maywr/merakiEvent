package xyz.maywr.example;

import xyz.maywr.meraki.event.CachedEventBus;
import xyz.maywr.meraki.event.Handle;
import xyz.maywr.meraki.event.IEventBus;

import java.util.Random;

/**
 * @author maywr
 * 10.07.2022 18:54
 */
public class Example
{
	public static final IEventBus EVENT_BUS = new CachedEventBus(); //cached event bus

	public Example()
	{
		EVENT_BUS.subscribe( this ); //subscribe the instance of Example class created in a main method
	}

	public static void main( String[] args )
	{
		//"inject" to something like a game
		new Example().inject();
	}

	public void inject()
	{
		new WalkEvent( new Random().nextInt( 600 ) ).post( EVENT_BUS ); //create and post event in one line if it's not cancelable
		SpeakEvent speakEvent = new SpeakEvent( "some bad word" ); //speak with bad word
		EVENT_BUS.post( speakEvent );
		if ( speakEvent.isCancelled() )
		{
			return; //perform handler and if it set the event to be cancelled - return
		}
		System.out.println( "player spoke" ); //event is not cancelled
	}

	public @Handle
	void onWalk( WalkEvent walkEvent )
	{
		//walk event
		System.out.println( "player has moved. distance: " + walkEvent.getDistance() );
	}

	public @Handle
	void onSpeak( SpeakEvent speakEvent )
	{
		//check if message player has sent to the chat is bad and cancel the event if true
		String[] badWords = { "bad", "word" };
		for ( String word : badWords )
		{
			if ( speakEvent.getText().contains( word ) )
				speakEvent.cancel();
		}
	}
}
