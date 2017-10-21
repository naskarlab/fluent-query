package com.naskar.fluentquery;

import com.naskar.fluentquery.impl.DeleteConverter;

public interface Delete<T> extends Whereable<T, Delete<T>> {
	
	Class<T> getClazz();
	
	<E> E to(DeleteConverter<E> converter);

}
