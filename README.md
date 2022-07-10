<div align="center">
    <h2>merakiEvent</h2>
    <h5>lightweight java event system</h5>
    <h6>(designed for meraki.xyz minecraft client)</h6>
</div>

## some examples:

### handle simple event with cached event bus:

```java
public static final IEventBus EVENT_BUS = new CachedEventBus();

public void main()
{
		EVENT_BUS.subscribe( this );
		EVENT_BUS.post( new Event() );
}

public @Handle void onEvent( Event event )
{
		System.out.println( "event triggered!" );
		System.out.println( event );
}
```

### posting user-defined event

```java

public static final IEventBus EVENT_BUS = new CachedEventBus();

public void main()
{
		EVENT_BUS.subscribe( this );
		EVENT_BUS.post( new TextEvent( "Hello World!" ) );
}

public @Handle void onEvent( TextEvent event )
{
		System.out.println( "event triggered!" );
		System.out.println( event.getText() );
}

public static final class TextEvent extends Event
{
	private final String text;
	
	public TextEvent( String text )
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
}
```

### posting and cancelling

```java

public static final IEventBus EVENT_BUS = new CachedEventBus();

public void main()
{
		EVENT_BUS.subscribe( this );
		TextEvent event = new TextEvent( "Some Text" );
		EVENT_BUS.post( event );
		if ( event.isCancelled )
		{
			System.out.println( "Event has been cancelled" );
		}
}

public @Handle void onEvent( TextEvent event )
{
		event.cancel();
}

public static final class TextEvent extends Event
{
	private final String text;
	
	public TextEvent( String text )
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
}
```