package boxalino.p13n.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.boxalino.p13n.api.thrift.P13nService;
import com.boxalino.p13n.api.thrift.P13nService.Iface;

public class DecoratorProxy implements InvocationHandler {
	
	private final P13nService.Iface delegate;
	
	private final HttpDecorator decorator;

	private DecoratorProxy(Iface delegate, HttpDecorator decorator) {
		this.delegate = delegate;
		this.decorator = decorator;
	}
	
	public static P13nService.Iface of(P13nService.Iface delegate, HttpDecorator decorator) {
		ClassLoader loader = delegate.getClass().getClassLoader();
		Class<?>[] interfaces = delegate.getClass().getInterfaces();
		Object proxy = Proxy.newProxyInstance(loader, interfaces, new DecoratorProxy(delegate, decorator));
		return (P13nService.Iface) proxy;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object o = args.length > 0 ? args[0] : null;
		decorator.beforeSend(o);
		return method.invoke(delegate, args);
	}

}
