package com.arc90.xmlsanity.util;

import java.util.HashMap;
import java.util.Map;

/***
* Based on http://sourcemaking.com/design_patterns/object_pool/java
***/
public abstract class Pool<T>
{
	private long expirationTime;

	private final Map<T, Long> locked = new HashMap<T, Long>();
	private final Map<T, Long> unlocked = new HashMap<T, Long>();

	/**
	 * Constructor which uses the default expiration time of 5 minutes.
	 */
	public Pool()
	{
	    this(300);
	}
	
	public Pool(long expirationSeconds)
	{
		expirationTime = expirationSeconds * 1000;
	}

	protected abstract T create() throws PoolException;

	public abstract boolean validate(T o);

	public abstract void expire(T o);

	public synchronized T checkOut() throws PoolException
	{
		long now = System.currentTimeMillis();
		
		T t;
		
		if (unlocked.size() > 0)
		{
			for (T tt : unlocked.keySet())
			{
				t = tt;
				
				if ((now - unlocked.get(t)) > expirationTime)
				{
					// object has expired
					unlocked.remove(t);
					expire(t);
					t = null;
				}
				else
				{
					if (validate(t))
					{
						unlocked.remove(t);
						locked.put(t, now);
						return (t);
					}
					else
					{
						// object failed validation
						unlocked.remove(t);
						expire(t);
						t = null;
					}
				}
			}
		}
		
		// no objects available, create a new one
		t = create();
		locked.put(t, now);
		return (t);
	}

	public synchronized void checkIn(T t)
	{
		locked.remove(t);
		unlocked.put(t, System.currentTimeMillis());
	}
}