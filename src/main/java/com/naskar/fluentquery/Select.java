package com.naskar.fluentquery;

import java.util.function.Function;

public interface Select {
	
	Select func(Function<String, String> action, String alias);

	Select groupBy();
	
	OrderBy<Select> orderBy();
}
